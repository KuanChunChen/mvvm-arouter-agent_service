package k.c.module.update.process;

import java.util.ArrayList;
import java.util.List;
import k.c.common.lib.logTool.LogTool;
import k.c.module.commonbusiness.common.CommonConst;
import k.c.module.commonbusiness.common.CommonFileTool;
import k.c.module.commonbusiness.common.LifeManager;
import k.c.module.commonbusiness.model.GetInfoDataModel;
import k.c.module.commonbusiness.model.UpdateDataModel;
import k.c.module.update.battery.CommonBatteryManager;
import k.c.module.update.process.base.UpdateHandleListener;
import k.c.module.update.process.base.UpdateHandlerInterface;

public class UpdateProcessManager implements UpdateHandleListener {
    private CommonBatteryManager commonBatteryManager = new CommonBatteryManager();
    private UpdateHandlerInterface updateHandler;
    private List<UpdateDataModel> waitingUpdateList = new ArrayList<>();

    private boolean rebootInstallMode = false;
    private boolean apControlInstallMode = false;

    public UpdateProcessManager() {
        updateHandler = new WholeUpdateHandler(this);
    }

    public synchronized void initUpdateProcess(){
        LifeManager.getInstance().wakeLockUpdate();
        if(updateHandler.isHandle()){
            LogTool.d("is update running");
            return;
        }
        GetInfoDataModel infoDataModel = CommonFileTool.getInfoData();
        if(infoDataModel == null){
            LogTool.d("infoData is null");
            LifeManager.getInstance().refreshWakeLockUpdate();
            return;
        }
        LogTool.d("infoData is %s", infoDataModel);
        if(!CommonFileTool.checkFreeSpace(30)){
            LogTool.d("free space is not enough");
            LifeManager.getInstance().refreshWakeLockUpdate();
            return;
        }
        List<UpdateDataModel> updateDataList = filterUpdateList(CommonFileTool.getUpdateList(infoDataModel.updateListId));
        LogTool.d("update list is %s", updateDataList);

        if(updateDataList.size() == 0){
            LogTool.d("waitingUpdateList list is empty");
            LifeManager.getInstance().refreshWakeLockUpdate();
            return;
        }
        LogTool.d("waitingUpdateList = %s", updateDataList);
        waitingUpdateList = updateDataList;
        updateHandler.initData(updateDataList);
        commonBatteryManager.closeBatteryListener();
        updateHandler.execute(infoDataModel);
    }

    public void clearWaitUpdateList(){
        waitingUpdateList.clear();
    }

    private static List<UpdateDataModel> filterUpdateList(List<UpdateDataModel> updateDataList){
        List<UpdateDataModel> filterUpdateList = new ArrayList<>();

        if (updateDataList == null) {
            LogTool.d("update list is null");
            return filterUpdateList;
        }

        for (UpdateDataModel updateDataModel: updateDataList) {
            if(updateDataModel.updateResult == CommonConst.UPDATE_RESULT_SUCCESS
                || updateDataModel.updateResult == CommonConst.UPDATE_RESULT_FAIL){
                LogTool.d("updateResult is ==== %s", updateDataModel.updateResult);
                continue;
            }
            filterUpdateList.add(updateDataModel);
        }
        return filterUpdateList;
    }

    private static List<UpdateDataModel> orderUpdateList(List<UpdateDataModel> updateDataList) {
//        SMF->OTA->SME->CMF->AP->SBL
        int[] bInstallOrder = {CommonConst.FileType.FILE_TYPE_SMF, CommonConst.FileType.FILE_TYPE_OTA,
                CommonConst.FileType.FILE_TYPE_SME, CommonConst.FileType.FILE_TYPE_CMF,
                CommonConst.FileType.FILE_TYPE_APK, CommonConst.FileType.FILE_TYPE_SBL,
                CommonConst.FileType.FILE_TYPE_AME};
        List<UpdateDataModel> tempUpdateList = new ArrayList<>();
        if (updateDataList == null) {
            LogTool.d("update list is null");
            return tempUpdateList;
        }
        for (int bOrderType : bInstallOrder) {
            for (int j = 0; j < updateDataList.size(); j++) {
                UpdateDataModel updateData = updateDataList.get(j);
                int bType = updateData.type;
                if (bOrderType == bType) {
                    tempUpdateList.add(updateData);
                }
            }
        }

        return tempUpdateList;
    }

    public List<UpdateDataModel> getWaitingUpdateList() {
            return updateHandler.getUpdateDataList();
    }

    public void setRebootInstallMode() {
        this.rebootInstallMode = true;
        this.apControlInstallMode = false;
    }

    public void setApControlInstallMode() {
        this.apControlInstallMode = true;
        this.rebootInstallMode = false;
    }

    @Override
    public void onceExecuteFail(UpdateDataModel updateData) {

    }

    @Override
    public void onceExecuteSuccess(UpdateDataModel updateData) {

    }

    @Override
    public void allExecuted() {
        clearWaitUpdateList();
        rebootInstallMode = false;
        apControlInstallMode = false;
    }

    @Override
    public void batteryNotEnough() {
        int batteryLimit = CommonConst.Common.BATTERY_AP_INSTALL_LIMIT;
        for (UpdateDataModel updateDataModel : updateHandler.getUpdateDataList()) {
            if (updateDataModel.type != CommonConst.FileType.FILE_TYPE_APK){
                batteryLimit = CommonConst.Common.BATTERY_FW_INSTALL_LIMIT;
                break;
            }
        }
        commonBatteryManager.openBatteryListener(batteryLimit);
        LifeManager.getInstance().refreshWakeLockUpdate();
    }

    @Override
    public boolean isApControlInstallMode() {
        return apControlInstallMode;
    }

    @Override
    public boolean isRebootInstallMode() {
        return rebootInstallMode;
    }
}
