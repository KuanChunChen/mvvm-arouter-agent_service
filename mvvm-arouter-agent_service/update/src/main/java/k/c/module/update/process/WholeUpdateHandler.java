package k.c.module.update.process;

import android.text.TextUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import k.c.common.lib.CommonLib;
import k.c.common.lib.DialogActivity;
import k.c.common.lib.Util.AndroidUtil;
import k.c.common.lib.Util.BatteryUtil;
import k.c.common.lib.Util.ShareUtil;
import k.c.common.lib.dialogUtil.DialogListener;
import k.c.common.lib.logTool.LogTool;
import k.c.module.commonbusiness.CommonSingle;
import k.c.module.commonbusiness.common.CommonConst;
import k.c.module.commonbusiness.common.CommonFileTool;
import k.c.module.commonbusiness.common.LifeManager;
import k.c.module.commonbusiness.model.GetInfoDataModel;
import k.c.module.commonbusiness.model.UpdateDataModel;
import k.c.module.update.constants.Constants;
import k.c.module.update.process.base.BaseUpdateHandler;
import k.c.module.update.process.base.UpdateHandleListener;
import k.c.module.update.process.base.UpdateHandlerInterface;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WholeUpdateHandler extends BaseUpdateHandler implements UpdateHandlerInterface {

    private Disposable dialogDisposable;
    private List<UpdateDataModel> dataModelList;
    private int handlerStatus = Constants.PROCESS_HANDLER_IDLE;// 0 idle, 1 busy
    private Disposable specifyTaskDisposable;
    private String sblVersion;
    private boolean isAllInstallSuccess = true;
    private boolean isSblInstallSuccess = false;


    private volatile boolean isUpdateRunning = false;

    public WholeUpdateHandler(UpdateHandleListener handleListener) {
        super(handleListener);
    }

    @Override
    public void initData(List<UpdateDataModel> updateDataModelList) {
        dataModelList = updateDataModelList;
    }

    @Override
    public List<UpdateDataModel> getUpdateDataList() {
        return dataModelList;
    }

    @Override
    public boolean isHandle() {
        return isUpdateRunning;
    }

    @Override
    public void execute(GetInfoDataModel infoDataModel) {
        ShareUtil.save(CommonLib.getAppContext(), CommonConst.SHARE_KEY_HAS_REBOOT_INSTALL, false);
        switch (infoDataModel.activeMode){
            case CommonConst.Mode.ACTIVE_MODE_IMMEDIATELY:
                if(!isBatteryEnough()){
                    LogTool.d("immediately install battery is not enough");
                    handleListener.batteryNotEnough();
                    return;
                }
                executeUpdateData();
                break;
            case CommonConst.Mode.ACTIVE_MODE_SPECIFIED:
                scheduleSpecifyTask((infoDataModel.activeTime * 1000) - System.currentTimeMillis());
                break;
            case CommonConst.Mode.ACTIVE_MODE_AP_CONTROL:
                if(handleListener.isApControlInstallMode()){
                    if(!isBatteryEnough()){
                        LogTool.d("AP control install battery is not enough");
                        handleListener.batteryNotEnough();
                        return;
                    }
                    executeUpdateData();
                }
                break;
            case CommonConst.Mode.ACTIVE_MODE_REBOOT:
                LogTool.d("reboot install mode = %s", handleListener.isRebootInstallMode());
                if(handleListener.isRebootInstallMode()){
                    if(!isBatteryEnough()){
                        LogTool.d("AP control install battery is not enough");
                        handleListener.batteryNotEnough();
                        return;
                    }
                    executeUpdateData();
                }else{
                    ShareUtil.save(CommonLib.getAppContext(), CommonConst.SHARE_KEY_HAS_REBOOT_INSTALL, true);
                    LifeManager.getInstance().refreshWakeLockUpdate();
                }
                break;
            default:
                break;
        }
    }

    private void scheduleSpecifyTask(long delayTime) {
        LogTool.d("scheduleSpecifyTask, delayTime time = %s", delayTime);
        if(delayTime < 0){
            delayTime = 0;
        }
        CommonSingle.getInstance().setRegisterInstallAlarm(false);
        specifyTaskDisposable = Flowable.timer(delayTime, TimeUnit.MILLISECONDS)
                .onBackpressureDrop()
                .subscribe(aLong -> {
                    LogTool.d("trigger specify task, delayTime = %s, updateDataList = %s", aLong, dataModelList);
                    LifeManager.getInstance().wakeLockUpdate();
                    if(!isBatteryEnough()){
                        scheduleSpecifyTask(60 * 1000);
                        return;
                    }

                    if (dialogDisposable != null && !dialogDisposable.isDisposed()){
                        dialogDisposable.dispose();
                    }
                    DialogActivity.startChooseTimeDialog(new DialogListener() {
                        @Override
                        public void onSure() { }

                        @Override
                        public void onCancel() { }

                        @Override
                        public void onTimeout() {
                            DialogActivity.sendFinishDialog();
                            executeUpdateData();
                        }

                        @Override
                        public void onSelect(int time) {
                            LogTool.d("select time index is = %s", time);
                            DialogActivity.sendFinishDialog();
                            switch (time){
                                case k.c.common.lib.constants.Constants.DIALOG_TIME_NOW:
                                    executeUpdateData();
                                    break;
                                case k.c.common.lib.constants.Constants.DIALOG_TIME_2_MIN:
                                    scheduleSpecifyTask(2 * 60 * 1000);
                                    break;
                                case k.c.common.lib.constants.Constants.DIALOG_TIME_15_MIN:
                                    scheduleSpecifyTask(15 * 60 * 1000);
                                    break;
                                case k.c.common.lib.constants.Constants.DIALOG_TIME_30_MIN:
                                    scheduleSpecifyTask(30 * 60 * 1000);
                                    break;
                                default:
                                    executeUpdateData();
                                    break;
                            }
                        }
                    });
                });
        CommonSingle.getInstance().setRegisterInstallAlarm(true);
        LifeManager.getInstance().refreshWakeLockProcess();
        LifeManager.getInstance().refreshWakeLockDownload();
        LifeManager.getInstance().refreshWakeLockUpdate();
    }

    private void executeUpdateData(){
        if(dialogDisposable != null && !dialogDisposable.isDisposed()){
            dialogDisposable.dispose();
        }
        isUpdateRunning = true;
        isAllInstallSuccess = true;
        dialogDisposable = Observable.create(e -> e.onNext(1))
                .subscribeOn(Schedulers.newThread())
                .subscribe(o -> {
                    LifeManager.getInstance().wakeLockUpdate();
                    GetInfoDataModel infoDataModel = CommonFileTool.getInfoData();
                    if(infoDataModel == null){
                        LogTool.d("infoData is null");
                        LifeManager.getInstance().refreshWakeLockUpdate();
                        return;
                    }

                    if(specifyTaskDisposable != null){
                        specifyTaskDisposable.dispose();
                        LogTool.d("dispose the specify execute task");
                    }

                    LogTool.d("execute update data = %s", dataModelList);
                    for (int i = 0;i < dataModelList.size();i++){
                        UpdateDataModel updateDataModel = dataModelList.get(i);
                        LogTool.d("updateDataModel is ==== %s", updateDataModel);

                        if(updateDataModel.size <= 0){
                            LogTool.d("updateDataModel size is 0");
                            continue;
                        }

                        if(TextUtils.isEmpty(updateDataModel.path)){
                            LogTool.d("updateDataModel path is empty");
                            continue;
                        }

                        if (AndroidUtil.isApRunning(updateDataModel.packageName)){
                            LogTool.d("AP is running,do not install");
                            continue;
                        }

                        long timeOut = Constants.INSTALL_DEFAULT_TIMEOUT;
                        switch (updateDataModel.type){
                            case CommonConst.FileType.FILE_TYPE_APK:
                                installAPK(updateDataModel, infoDataModel);
                                timeOut = getTimeOut(updateDataModel.size, updateDataModel.type);
                                LogTool.d("apk size = %s, timeOut time = %s", updateDataModel.size, timeOut);
                                break;
                            case CommonConst.FileType.FILE_TYPE_PRM:
                                installPRM(updateDataModel, infoDataModel);
                                timeOut = getTimeOut(updateDataModel.size, updateDataModel.type);
                                LogTool.d("prm size = %s, timeOut time = %s", updateDataModel.size, timeOut);
                                break;
                            case CommonConst.FileType.FILE_TYPE_OTA:
                                installAPK(updateDataModel, infoDataModel);
                                timeOut = getTimeOut(updateDataModel.size, updateDataModel.type);
                                LogTool.d("ota size = %s, timeOut time = %s", updateDataModel.size, timeOut);
                                break;
                            case CommonConst.FileType.FILE_TYPE_SMF:
                                installAPK(updateDataModel, infoDataModel);
                                timeOut = getTimeOut(updateDataModel.size, updateDataModel.type);
                                LogTool.d("smf size = %s, timeOut time = %s", updateDataModel.size, timeOut);
                                break;
                            case CommonConst.FileType.FILE_TYPE_CMF:
                                installAPK(updateDataModel, infoDataModel);
                                timeOut = getTimeOut(updateDataModel.size, updateDataModel.type);
                                LogTool.d("cmf size = %s, timeOut time = %s", updateDataModel.size, timeOut);
                                break;
                            case CommonConst.FileType.FILE_TYPE_SBL:
                                sblVersion = updateDataModel.versionName;
                                installAPK(updateDataModel, infoDataModel);
                                timeOut = getTimeOut(updateDataModel.size, updateDataModel.type);
                                LogTool.d("sbl size = %s, timeOut time = %s", updateDataModel.size, timeOut);
                                break;
                            case CommonConst.FileType.FILE_TYPE_SME:
                                installAPK(updateDataModel, infoDataModel);
                                timeOut = getTimeOut(updateDataModel.size, updateDataModel.type);
                                LogTool.d("sme size = %s, timeOut time = %s", updateDataModel.size, timeOut);
                                break;
                            case CommonConst.FileType.FILE_TYPE_AME:
                                installAPK(updateDataModel, infoDataModel);
                                timeOut = getTimeOut(updateDataModel.size, updateDataModel.type);
                                LogTool.d("ame size = %s, timeOut time = %s", updateDataModel.size, timeOut);
                                break;
                            default:
                                break;
                        }

                        long startLoopTime = System.currentTimeMillis();
                        LogTool.d("handlerStatus loop wait start");
                        for (;;){
                            if(handlerStatus != Constants.PROCESS_HANDLER_IDLE){
                                if((System.currentTimeMillis() - startLoopTime) < timeOut){
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    continue;
                                }else{
                                    LogTool.d("vendor result timeout, timeout = %s", timeOut);
                                    getVendorResult().fail();
                                    getVendorResult().complete();
                                    break;
                                }
                            }
                            LogTool.d("vendor result complete");
                            break;
                        }
                        LogTool.d("handlerStatus loop wait end");
                    }
                    allExecuted();
                });
    }

    private boolean isBatteryEnough(){
        boolean isEnough = true;
        for(UpdateDataModel updateDataModel : dataModelList){
            LogTool.d("updateDataModel = %s", updateDataModel);
            if (updateDataModel.type == CommonConst.FileType.FILE_TYPE_OTA
                    || updateDataModel.type == CommonConst.FileType.FILE_TYPE_SMF) {
                if (BatteryUtil.getBatteryLevel(CommonLib.getAppContext()) < CommonConst.BATTERY_LOW_LEVEL_OTA) {
                    LogTool.d("SpecifyTask OTA/SMF battery level is not enough");
                    LogTool.i(CommonConst.ModuleName.UPDATE_MODULE_NAME, "Install", CommonConst.returnCode.CTMS_BATTERY_LOW);
                    isEnough = false;
                    break;
                }
            }

            if(updateDataModel.type == CommonConst.FileType.FILE_TYPE_APK){
                if (!BatteryUtil.isCharging(CommonLib.getAppContext())
                        && BatteryUtil.getBatteryLevel(CommonLib.getAppContext()) < CommonConst.BATTERY_LOW_LEVEL_APK) {
                    LogTool.d("SpecifyTask APK battery level is not enough");
                    LogTool.i(CommonConst.ModuleName.UPDATE_MODULE_NAME, "Install", CommonConst.returnCode.CTMS_BATTERY_LOW);
                    isEnough = false;
                    break;
                }
            }
        }
        return isEnough;
    }

    private void allExecuted(){
        ShareUtil.save(CommonLib.getAppContext(), CommonConst.SHARE_KEY_HAS_REBOOT_INSTALL, false);
        isUpdateRunning = false;
        if(handleListener != null){
            handleListener.allExecuted();
        }
        CommonSingle.getInstance().setRegisterInstallAlarm(false);
        LifeManager.getInstance().refreshWakeLockUpdate();
        LifeManager.getInstance().refreshWakeLockDownload();
        LifeManager.getInstance().refreshWakeLockProcess();
        if (isAllInstallSuccess){
            LogTool.i(CommonConst.ModuleName.UPDATE_MODULE_NAME, "Install", CommonConst.returnCode.CTMS_SUCCESS_STRING);
        }else {
            LogTool.i(CommonConst.ModuleName.UPDATE_MODULE_NAME, "Install", CommonConst.returnCode.CTMS_UPDATE_FAIL);
        }
        if (isSblInstallSuccess){
            DialogActivity.startChooseDialog(new DialogListener() {
                @Override
                public void onSure() {
                    ctSystem.shutdown((byte) 0);
                }

                @Override
                public void onCancel() {
                    DialogActivity.sendFinishDialog();
                    ShareUtil.save(CommonLib.getAppContext(), CommonConst.SBL_INSTALLED_BUT_NOT_REBOOT, sblVersion);
                    CommonSingle.getInstance().getPollingService().updateNow();
                }

                @Override
                public void onTimeout() {
                    ctSystem.shutdown((byte) 0);
                }

                @Override
                public void onSelect(int time) {

                }
            });
        }else {
            CommonSingle.getInstance().getPollingService().updateNow();
        }
    }


    private void installAPK(UpdateDataModel updateData, GetInfoDataModel getInfoData) {
        openNotification(updateData);
        handlerStatus = Constants.PROCESS_HANDLER_BUSY;
        executeInstall(updateData, getInfoData);
    }

    private void installPRM(UpdateDataModel updateData, GetInfoDataModel getInfoData) {
        openNotification(updateData);
        handlerStatus = Constants.PROCESS_HANDLER_BUSY;
        executeInstallPrm(updateData, getInfoData);
    }

    @Override
    protected void installSuccess(UpdateDataModel updateData) {
        if(handleListener != null){
            handleListener.onceExecuteSuccess(updateData);
        }
        if (updateData.type == CommonConst.FileType.FILE_TYPE_SBL){
            isSblInstallSuccess = true;
        }
        handlerStatus = Constants.PROCESS_HANDLER_IDLE;
    }

    @Override
    protected void installFail(UpdateDataModel updateData) {
        if(handleListener != null){
            handleListener.onceExecuteFail(updateData);
        }
        isAllInstallSuccess = false;
        handlerStatus = Constants.PROCESS_HANDLER_IDLE;
    }
}
