package k.c.module.polling.process;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.Objects;

import k.c.common.lib.AppSingle;
import k.c.common.lib.CommonLib;
import k.c.common.lib.Util.AndroidUtil;
import k.c.common.lib.Util.CommonUtil;
import k.c.common.lib.Util.CradleUtil;
import k.c.common.lib.Util.FileFastUtil;
import k.c.common.lib.logTool.LogTool;
import k.c.common.lib.proxy.ProxyData;
import k.c.module.commonbusiness.CommonSingle;
import k.c.module.commonbusiness.common.CommonConst;
import k.c.module.commonbusiness.common.CommonFileTool;
import k.c.module.commonbusiness.common.LifeManager;
import k.c.module.commonbusiness.common.UpdateListManager;
import k.c.module.commonbusiness.listener.DownloadListener;
import k.c.module.commonbusiness.listener.GetInfoListener;
import k.c.module.commonbusiness.model.CTMSProcessInfo;
import k.c.module.commonbusiness.model.GetInfoDataModel;
import k.c.module.commonbusiness.model.GetInfoRequestModel;
import k.c.module.commonbusiness.model.UpdateDataModel;
import k.c.module.commonbusiness.po.BaseConfig;
import k.c.module.http.DownloadConsumer;
import k.c.module.http.RetrofitClient;
import k.c.module.http.model.HttpConfig;
import k.c.module.polling.constants.Constants;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public abstract class CtmsMainProcessImpl implements MainProcessInterface {

    private int needDownloadSize = 0;
    private int completeDownloadSize = 0;
    private Thread thread;

//    private boolean downloadAllSuccess = false;

    @Override
    public void startProcess(@NonNull CTMSProcessInfo processInfo) {
        initialize(processInfo);
        CommonSingle.getInstance().getConfigService().changeSystemPanelPassword();
        getInfoData();
    }

    @Override
    public void initialize(@NonNull CTMSProcessInfo processInfo) {
        if(thread != null){
            thread.interrupt();
        }
        needDownloadSize = 0;
        completeDownloadSize = 0;
        HttpConfig httpConfig = new HttpConfig();
        BaseConfig baseConfig = CommonSingle.getInstance().getBaseConfig();
        LogTool.d("reset http config by communicationMode = %s", baseConfig.communicationMode);
        if(baseConfig.communicationMode == CommonConst.COMMUNICATION_MODE_TCP){
            httpConfig.baseUrl = CommonUtil.hostFormat(baseConfig.tcpHostIp, baseConfig.tcpHostPort);
            httpConfig.rxTimeout = baseConfig.tcpRxTimeout;
            httpConfig.txTimeout = baseConfig.tcpTxTimeout;
            httpConfig.connectTimeout = baseConfig.tcpConnectTimeout;
            LogTool.d("http config = %s", httpConfig);
        }else{
            httpConfig.baseUrl = CommonUtil.hostFormat(baseConfig.tlsHostIp, baseConfig.tlsHostPort);
            httpConfig.rxTimeout = baseConfig.tcpRxTimeout;
            httpConfig.txTimeout = baseConfig.tcpTxTimeout;
            httpConfig.connectTimeout = baseConfig.tcpConnectTimeout;
            LogTool.d("https config = %s", httpConfig);
        }
        ProxyData proxyData = processInfo.proxyData;
        if(processInfo.proxyData != null){
            LogTool.d("proxy data is not null, proxy = %s", proxyData);
            httpConfig.proxy = new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(proxyData.proxyIp, proxyData.proxyPort));
        }
        RetrofitClient.resetAllHostName(httpConfig);
        LogTool.d("try cancel alarm");
        LifeManager.cancelAlarm(LifeManager.getStartCTMSInstallIntent());
    }

    @Override
    public void getInfoData() {

        GetInfoRequestModel getInfoRequestModel = new GetInfoRequestModel();
        CommonSingle.getInstance().getGetInfoService().getInfo(getInfoRequestModel, new GetInfoListener() {
            @Override
            public void getInfoSuccess(int fileId) {
                LogTool.d("getinfo success, fileId = %s", fileId);

                if (Objects.requireNonNull(CommonFileTool.getInfoData()).diagnosticEnable == CommonConst.Mode.DIAGNOSTIC_ENABLE
                        && AppSingle.getInstance().getOperationPermission() == 0) {

                    new Handler().post(() -> CommonSingle.getInstance().getDiagnosticService().getDiagnosticData());
                }
                CommonSingle.getInstance().getPollingService().startSchedule();
                downloadUpdateList(CommonFileTool.getInfoData());
            }

            @Override
            public void getInfoFail(int code) {
                LogTool.d("getInfoFail code = %d", code);
                LogTool.d("try restart update now");
                endProcess(Constants.MAIN_PROCESS_STATUS_GET_INFO_FAIL);
            }

            @Override
            public void error() {
                LogTool.d("getInfoFail error");
                LogTool.d("try restart update now");
                endProcess(Constants.MAIN_PROCESS_STATUS_GET_INFO_FAIL);
            }

            @Override
            public void loginFail() {
                LogTool.d("getInfoData loginFail");
                LogTool.d("try restart update now");
                endProcess(Constants.MAIN_PROCESS_STATUS_LOGIN_FAIL);
            }
        });
    }

    @Override
    public void downloadUpdateList(GetInfoDataModel getInfoDataModel) {
        if(getInfoDataModel == null){
            endProcess(Constants.MAIN_PROCESS_STATUS_GET_INFO_FAIL);
            return;
        }
        CommonSingle.getInstance().getDownloadService().downloadUpdateList(getInfoDataModel.updateListId, new DownloadListener() {
            @Override
            public void onStart(int id) {
                LogTool.d("download onStart");
            }

            @Override
            public void onProgress(int currentLength) {
                LogTool.d("download onProgress, currentLength", currentLength);
            }

            @Override
            public void onFinish(String path, long size, int id) {
                executeUpdateList(id);
            }

            @Override
            public void onFailure(int statusCode) {
                LogTool.d("download onFailure, statusCode = %d", statusCode);
                endProcess(Constants.MAIN_PROCESS_STATUS_DOWNLOAD_UPDATE_LIST_FAIL);
            }
        });
    }

    @Override
    public void executeUpdateList(int updateListId) {
        List<UpdateDataModel> updateDataModelList = CommonSingle.getInstance().getDownloadService().convertUpdateInfo();
        List<UpdateDataModel> mergeList;
        LogTool.d("new update list = %s", updateDataModelList);
        GetInfoDataModel getInfoDataModel = CommonFileTool.getInfoData();
        if(getInfoDataModel != null){
            LogTool.d("Compare the update list");
            List<UpdateDataModel> oldUpdateDataList = CommonFileTool.getUpdateList(getInfoDataModel.updateListId);
            LogTool.d("old update list = %s", oldUpdateDataList);

            mergeList = UpdateListManager.getMergeCommandList(oldUpdateDataList, updateDataModelList);
            LogTool.d("merge list = %s", mergeList);

            mergeList = UpdateListManager.updateResultList(mergeList);
            LogTool.d("update merge list = %s", mergeList);

            List<UpdateDataModel> differentUpdateDataList = UpdateListManager.getDiffCommandList(oldUpdateDataList, updateDataModelList);
            LogTool.d("differentUpdateDataList = %s", differentUpdateDataList);

            if (differentUpdateDataList != null){
                for (UpdateDataModel differentUpdateData : differentUpdateDataList) {
                    if(differentUpdateData.status == CommonConst.UPDATE_STATUS_EXECUTED
                            && differentUpdateData.updateResult == CommonConst.UPDATE_RESULT_NO_RESULT){
                        File capFile = new File(differentUpdateData.path);
                        String rootFile = capFile.getParent();
                        LogTool.d("capFile = %s, capFile.getParent() = %s", capFile.getAbsoluteFile(), rootFile);
                        FileFastUtil.delete(rootFile);
                        FileFastUtil.delete(rootFile + ".zip");
                    }
                }
            }
        }else{
            endProcess(Constants.MAIN_PROCESS_STATUS_EXECUTE_UPDATE_LIST_FAIL);
            return;
        }

        if(mergeList == null){
            LogTool.d("merge list is null");
            endProcess(Constants.MAIN_PROCESS_STATUS_EXECUTE_UPDATE_LIST_FAIL);
            return;
        }

        if(mergeList.size() == 0){
            LogTool.d("merge list is empty");
            endProcess(Constants.MAIN_PROCESS_STATUS_EXECUTE_UPDATE_LIST_FAIL);
            return;
        }

        for (UpdateDataModel updateDataModel : mergeList) {
            if (updateDataModel.type == CommonConst.FileType.FILE_TYPE_CMF){
                LogTool.d("CMF versionName = %s", new CradleUtil(CommonLib.getAppContext()).getCradleVersion());
                String cmfVersion = AndroidUtil.getCmfVersion();
                if (cmfVersion == null){
                    LogTool.d("Do not connect cradle.");
                    endProcess(Constants.MAIN_PROCESS_STATUS_EXECUTE_UPDATE_LIST_FAIL);
                    return;
                }
            }
        }

        CommonFileTool.saveUpdateDataList(mergeList, updateListId);
        needDownloadSize = mergeList.size();
        completeDownloadSize = 0;
//        downloadAllSuccess = true;
        LifeManager.getInstance().wakeLockDownload();
        startWaitDownloadFinish(updateListId);
        Flowable.fromIterable(mergeList)
                .filter(updateDataModel -> {
                    switch (updateDataModel.status){
                        case CommonConst.UPDATE_STATUS_UNEXECUTE:
                        case CommonConst.UPDATE_STATUS_EXECUTING:
                            return true;
                        case CommonConst.UPDATE_STATUS_EXECUTED:
                            completeDownloadSize += 1;
                            return false;
                        default:
                            completeDownloadSize += 1;
                            return false;
                    }
                })
                .flatMap(updateDataModel -> CommonSingle.getInstance().getDownloadService().download(CommonConst.DownloadConst.DOWNLOAD_MODE_SLOW, updateDataModel.id, updateDataModel.type))
                .doAfterNext(downloadResultModel -> LogTool.d("download afterNext === %s", downloadResultModel))
                .subscribeOn(Schedulers.newThread())
                .subscribe(new DownloadConsumer() {

                    @Override
                    public void onStart(int id) {
                        LogTool.d("start download");
                        CommonFileTool.changeUpdateDataStatusById(updateListId, id, CommonConst.UPDATE_STATUS_EXECUTING);
                    }

                    @Override
                    public void onProgress(int currentLength) {
                        LogTool.d("onProgress = %d", currentLength);
                    }

                    @Override
                    public void onFinish(int id, String path, long size) {
                        LogTool.d("finish download, path = %s, size = %s", path, size);
                        CommonFileTool.changeUpdateDataStatusById(updateListId, id, path, size, CommonConst.UPDATE_STATUS_EXECUTED);
                        completeDownloadSize += 1;
                    }

                    @Override
                    public void onFailure(int statusCode) {
                        LogTool.d("onFailure download, statusCode = %s", statusCode);
                        completeDownloadSize += 1;
//                        downloadAllSuccess = false;
                    }
                });

        LogTool.d("start wait the downloads completed");
    }

    private void startWaitDownloadFinish(int updateListId){
        LogTool.d("start wait the downloads completed");
        thread = new Thread(() -> {
            try {
                for(;;){
                    if(completeDownloadSize == needDownloadSize){
//                        if(downloadAllSuccess){
                        LogTool.i(CommonConst.ModuleName.DOWNLOAD_MODULE_NAME, "Download", CommonConst.returnCode.CTMS_SUCCESS_STRING);
                        LogTool.d("all download success, end process");
                        endProcess(Constants.MAIN_PROCESS_STATUS_MAIN_PROCESS_FINISH);
                        break;
//                        }
//                        else{
//                            LogTool.d("");
//                            executeUpdateList(updateListId);
//                        }
                    }
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        LogTool.d("download onFinish");
    }

    @Override
    public void callVendorExecute() {
        LogTool.d("call vendor module");
        LifeManager.getInstance().refreshWakeLockDownload();
        CommonSingle.getInstance().getUpdateService().initUpdateProcess();
    }
}
