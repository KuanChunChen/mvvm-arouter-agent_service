package k.c.module.update.process.base;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Handler;

import androidx.core.app.NotificationCompat;

import java.io.File;

import CTOS.CtSystem;
import k.c.common.lib.CommonLib;
import k.c.common.lib.Util.AndroidUtil;
import k.c.common.lib.Util.FileFastUtil;
import k.c.common.lib.logTool.LogTool;
import k.c.module.commonbusiness.common.CommonConst;
import k.c.module.commonbusiness.common.CommonFileTool;
import k.c.module.commonbusiness.model.GetInfoDataModel;
import k.c.module.commonbusiness.model.UpdateDataModel;
import k.c.module.update.constants.Constants;
import k.c.module.update.vendor.common.VendorApiResult;
import k.c.module.update.vendor.common.VendorPackageAgent;
import k.c.module.update.vendor.common.VendorPackageInterface;
import k.c.module_update.R;

public abstract class BaseUpdateHandler {

    private VendorPackageInterface vendorPackageInterface;
    private VendorApiResult vendorResult;
    private NotificationManager notificationManager = (NotificationManager) CommonLib.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
    private Handler handler;
    private static class MainHandler extends Handler{}
    protected CtSystem ctSystem = new CtSystem();
    protected UpdateHandleListener handleListener;

    public BaseUpdateHandler(UpdateHandleListener handleListener) {
        handler = new MainHandler();
        vendorPackageInterface = VendorPackageAgent.selectVendorPackage();
        this.handleListener = handleListener;
    }

    protected void executeInstall(UpdateDataModel updateData, GetInfoDataModel getInfoData){
        vendorResult = new VendorProcessResult(updateData, getInfoData);
        vendorPackageInterface.setCallback(vendorResult);
        vendorPackageInterface.installStart(updateData.path);
    }

    protected void executeInstallPrm(UpdateDataModel updateData, GetInfoDataModel getInfoData){
        vendorResult = new VendorProcessResult(updateData, getInfoData);
        vendorPackageInterface.setPrmCallback(vendorResult);
        vendorPackageInterface.installPrmStart(updateData.path);
    }

    protected void openNotification(UpdateDataModel updateData){
        handler.post(() -> {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(CommonLib.getAppContext());
            notificationBuilder.setContentTitle(updateData.packageName + "." + updateData.versionName);
            notificationBuilder.setContentText(updateData.type + " Update");
            notificationBuilder.setSmallIcon(R.drawable.castles_logo);
            notificationBuilder.setProgress(0, 0, true);
            notificationBuilder.setAutoCancel(true);
            notificationManager.notify(updateData.id, notificationBuilder.build());
        });
    }

    protected void closeNotification(UpdateDataModel updateData){
        handler.post(() -> notificationManager.cancel(updateData.id));
    }

    protected long getTimeOut(long size, int type) {
        long timeOut;
        if (type == CommonConst.FileType.FILE_TYPE_OTA || type == CommonConst.FileType.FILE_TYPE_SMF || type == CommonConst.FileType.FILE_TYPE_CMF){
            timeOut = Constants.PACKAGE_RESULT_TIME_OUT_SMF;
        }else if(size <= Constants.PACKAGE_SIZE_RANGE_2M){
            timeOut = Constants.PACKAGE_RESULT_TIME_OUT_2M;
        }else if(size <= Constants.PACKAGE_SIZE_RANGE_50M){
            timeOut = Constants.PACKAGE_RESULT_TIME_OUT_50M;
        }else if(size <= Constants.PACKAGE_SIZE_RANGE_200M){
            timeOut = Constants.PACKAGE_RESULT_TIME_OUT_200M;
        }else{
            timeOut = Constants.PACKAGE_RESULT_TIME_OUT_MAX_M;
        }
        return timeOut;
    }

    protected abstract void installSuccess(UpdateDataModel updateData);
    protected abstract void installFail(UpdateDataModel updateData);

    protected VendorApiResult getVendorResult(){
        return vendorResult;
    }

    private class VendorProcessResult implements VendorApiResult {

        private UpdateDataModel updateData;
        private GetInfoDataModel getInfoDataModel;
        private int status = -1;

        public VendorProcessResult(UpdateDataModel updateData, GetInfoDataModel getInfoDataModel) {
            this.updateData = updateData;
            this.getInfoDataModel = getInfoDataModel;
        }

        @Override
        public void applyPrepare() {
            closeNotification(updateData);
        }

        @Override
        public void success() {
            LogTool.d("install success");
            status = Constants.UPDATE_STATUS_INFO_INSTALL_END;
        }

        @Override
        public void fail() {
            LogTool.d("install fail");
            status = Constants.UPDATE_STATUS_INFO_INSTALL_FAILD;
        }

        @Override
        public void complete() {
            LogTool.d("install complete, status = %s", status);
            closeNotification(updateData);
            if(status >= 0){
                File capFile = new File(updateData.path);
                String rootFile = capFile.getParent();
                LogTool.d("capFile = %s, capFile.getParent() = %s", capFile.getAbsoluteFile(), rootFile);
                FileFastUtil.delete(rootFile);
                FileFastUtil.delete(rootFile + ".zip");

                if(status == Constants.UPDATE_STATUS_INFO_INSTALL_END){
                    LogTool.d("Change the result to success of the update list file ");
                    if (updateData.type == CommonConst.FileType.FILE_TYPE_CMF){
                        if (updateData.versionName.equals(AndroidUtil.getCmfVersion())){
                            CommonFileTool.changeUpdateDataResultById(getInfoDataModel.updateListId, updateData.id, CommonConst.UPDATE_RESULT_SUCCESS);
                        }else {
                            CommonFileTool.changeUpdateDataResultById(getInfoDataModel.updateListId, updateData.id, CommonConst.UPDATE_RESULT_FAIL);
                        }
                    }else {
                        CommonFileTool.changeUpdateDataResultById(getInfoDataModel.updateListId, updateData.id, CommonConst.UPDATE_RESULT_SUCCESS);
                    }
                    installSuccess(updateData);
                }else{
                    LogTool.d("Change the result to fail of the update list file ");
                    CommonFileTool.changeUpdateDataResultById(getInfoDataModel.updateListId, updateData.id, CommonConst.UPDATE_RESULT_FAIL);
                    installFail(updateData);
                }

                LogTool.d("polling immediately");
//                sendStartPollingBroadcast();
            }

        }
    }
}
