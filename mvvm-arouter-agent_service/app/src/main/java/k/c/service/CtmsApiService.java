package k.c.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
import java.util.List;

import k.c.common.lib.CommonLib;
import k.c.common.lib.DialogActivity;
import k.c.common.lib.Util.AndroidUtil;
import k.c.common.lib.logTool.LogTool;
import k.c.module.commonbusiness.CommonSingle;
import k.c.module.commonbusiness.common.CommonConst;
import k.c.module.commonbusiness.common.CommonFileTool;
import k.c.module.commonbusiness.common.LifeManager;
import k.c.module.commonbusiness.model.UpdateDataModel;
import k.c.module.commonbusiness.po.BaseConfig;
import k.c.module.commonbusiness.po.EnableConfig;
import k.c.service.constants.Constants;
import k.c.service.model.API_ConfigData;
import k.c.service.model.API_KernelInfoData;
import k.c.service.model.API_UpdateListData;

public class CtmsApiService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogTool.d("Ctms API Service onBind.");
        LogTool.d("onBind init install service.");
        CommonLib.getAppContext().startService(LifeManager.getStartCTMSInstallIntent());
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogTool.d("Ctms API Service onUnbind.");
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DialogActivity.startAskPermission();
        LogTool.d("Ctms API Service init.");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null){
            LogTool.d("intent is null return");
            return super.onStartCommand(intent, flags, startId);
        }

        if(!CommonSingle.getInstance().getEnableConfig().ctmsEnable){
            LogTool.d("service onStartCommand, Ctms is not enable");
            return super.onStartCommand(intent, flags, startId);
        }

        int actionType = intent.getIntExtra(CommonConst.ACTION_TYPE_KEY, -1);
        LogTool.d("onStartCommand actionType = %s", actionType);
        if(actionType == CommonConst.SERVICE_ACTION_TYPE_START_POLLING_PROCESS){
            LogTool.d("reStart execute the main process");
            CommonSingle.getInstance().getPollingService().updateNow();
        }else if(actionType == CommonConst.SERVICE_ACTION_TYPE_START_INSTALL_PROCESS){
            LogTool.d("reStart execute the update process");
            CommonSingle.getInstance().getUpdateService().initUpdateProcess();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private final ACTMSAPI.Stub binder = new ACTMSAPI.Stub() {

        @Override
        public String getAllConfig() throws RemoteException {
            BaseConfig baseConfig = CommonSingle.getInstance().getBaseConfig();
            API_ConfigData api_configData = new API_ConfigData();
            api_configData.serialNumber = baseConfig.serialNumber;
            api_configData.compatibleFlag = baseConfig.compatibleFlag;
            api_configData.retryCount = baseConfig.retryCount;
            api_configData.downloadConfiguration = baseConfig.downloadConfiguration;
            api_configData.cmMode = baseConfig.communicationMode;
            api_configData.tcp.hostIp = baseConfig.tcpHostIp;
            api_configData.tcp.hostPort = baseConfig.tcpHostPort;
            api_configData.tcp.transmissionSize = baseConfig.tcpTransmissionSize;
            api_configData.tcp.connectTimeout = baseConfig.tcpConnectTimeout;
            api_configData.tcp.txTimeout = baseConfig.tcpTxTimeout;
            api_configData.tcp.rxTimeout = baseConfig.tcpRxTimeout;
            api_configData.tls.hostIp = baseConfig.tlsHostIp;
            api_configData.tls.hostPort = baseConfig.tlsHostPort;
            api_configData.tls.verifyPeer = baseConfig.tlsVerifyPeer;
            api_configData.nac.protocol = baseConfig.nacProtocol;
            api_configData.nac.blockSize = baseConfig.nacBlockSize;
            api_configData.nac.sourceAddr = baseConfig.nacSourceAddr;
            api_configData.nac.destAddr = baseConfig.nacDestAddr;
            api_configData.nac.lenType = baseConfig.nacLenType;
            api_configData.nac.addLenFlag = baseConfig.nacAddLenFlag;
            api_configData.usb.hostIp = baseConfig.usbHostIp;
            api_configData.usb.hostPort = baseConfig.usbHostPort;
            LogTool.d("getAllConfig : " + "\r\n"+
                    baseConfig.toString() + "\r\n"
                    + api_configData.toString());
            return new Gson().toJson(api_configData);
        }

        @Override
        public int setAllConfig(String strConfigContent) throws RemoteException {
            BaseConfig baseConfig = CommonSingle.getInstance().getBaseConfig();
            API_ConfigData api_configData = new Gson().fromJson(strConfigContent, API_ConfigData.class);
            baseConfig.serialNumber = api_configData.serialNumber;
            baseConfig.compatibleFlag = api_configData.compatibleFlag;
            baseConfig.retryCount = api_configData.retryCount;
            baseConfig.downloadConfiguration = api_configData.downloadConfiguration;
            baseConfig.communicationMode = api_configData.cmMode;
            baseConfig.tcpHostIp = api_configData.tcp.hostIp;
            baseConfig.tcpHostPort = api_configData.tcp.hostPort;
            baseConfig.tcpTransmissionSize = api_configData.tcp.transmissionSize;
            baseConfig.tcpConnectTimeout = api_configData.tcp.connectTimeout;
            baseConfig.tcpTxTimeout = api_configData.tcp.txTimeout;
            baseConfig.tcpRxTimeout = api_configData.tcp.rxTimeout;
            baseConfig.tlsHostIp = api_configData.tls.hostIp;
            baseConfig.tlsHostPort = api_configData.tls.hostPort;
            baseConfig.tlsVerifyPeer = api_configData.tls.verifyPeer;
            baseConfig.nacProtocol = api_configData.nac.protocol;
            baseConfig.nacBlockSize = api_configData.nac.blockSize;
            baseConfig.nacSourceAddr = api_configData.nac.sourceAddr;
            baseConfig.nacDestAddr = api_configData.nac.destAddr;
            baseConfig.nacLenType = api_configData.nac.lenType;
            baseConfig.nacAddLenFlag = api_configData.nac.addLenFlag;
            baseConfig.usbHostIp = api_configData.usb.hostIp;
            baseConfig.usbHostPort = api_configData.usb.hostPort;
            CommonSingle.getInstance().setBaseConfig(baseConfig);
            LogTool.d("setAllConfig : " + "\r\n" +
                    "Input value : " + strConfigContent + "\r\n" +
                    baseConfig.toString());
            return 0;
        }

        @Override
        public String getConfig(byte bConfigType) throws RemoteException {
            String returnValue;
            BaseConfig baseConfig = CommonSingle.getInstance().getBaseConfig();
            switch (bConfigType) {
                case Constants.CONFIG.CONFIG_TERMINAL_SN:
                    returnValue = baseConfig.serialNumber;
                    break;
                case Constants.CONFIG.CONFIG_SERVER_COMPATIBLE:
                    returnValue = String.valueOf(baseConfig.compatibleFlag);
                    break;
                case Constants.CONFIG.CONFIG_RETRY_COUNT:
                    returnValue = String.valueOf(baseConfig.retryCount);
                    break;
                case Constants.CONFIG.CONFIG_DOWNLOAD_CONFIG:
                    returnValue = String.valueOf(baseConfig.downloadConfiguration);
                    break;
                case Constants.CONFIG.CONFIG_COMMUNICATE_TCP:
                    returnValue = baseConfig.tcpHostIp;
                    break;
                case Constants.CONFIG.CONFIG_COMMUNICATE_TLS:
                    returnValue = baseConfig.tlsHostIp;
                    break;
                case Constants.CONFIG.CONFIG_COMMUNICATE_USB:
                    returnValue = baseConfig.usbHostIp;
                    break;
                case Constants.CONFIG.CONFIG_COMMUNICATE_NAC:
                    returnValue = baseConfig.nacDestAddr;
                    break;
                case Constants.CONFIG.CONFIG_KERNEL_VER:
                    //GET KERNEL VERSION
                    API_KernelInfoData api_kernelInfoData = new API_KernelInfoData();
                    api_kernelInfoData.version = AndroidUtil.getVersionName(CommonLib.getAppContext()).versionName;
                    returnValue = new Gson().toJson(api_kernelInfoData);
                    break;
                case Constants.CONFIG.CONFIG_MERCHANT_ID:
                    //GET MERCHANT ID
                    returnValue = baseConfig.merchantID;
                    break;
                default:
                    returnValue = null;
                    break;

            }
            LogTool.d("getConfig : " + "\r\n" +
                    "Get Type : " + bConfigType + "\r\n" +
                    "returnValue : " + returnValue);
            return returnValue;
        }

        @Override
        public int setConfig(byte bConfigType, String strConfigContent) throws RemoteException {
            int isSuccess = 1;
            BaseConfig baseConfig = CommonSingle.getInstance().getBaseConfig();
            switch (bConfigType) {
                case Constants.CONFIG.CONFIG_TERMINAL_SN:
                    baseConfig.serialNumber = strConfigContent;
                    CommonSingle.getInstance().setBaseConfig(baseConfig);
                    break;
                case Constants.CONFIG.CONFIG_SERVER_COMPATIBLE:
                    baseConfig.compatibleFlag = Integer.parseInt(strConfigContent);
                    CommonSingle.getInstance().setBaseConfig(baseConfig);
                    break;
                case Constants.CONFIG.CONFIG_RETRY_COUNT:
                    baseConfig.retryCount = Integer.parseInt(strConfigContent);
                    CommonSingle.getInstance().setBaseConfig(baseConfig);
                    break;
                case Constants.CONFIG.CONFIG_DOWNLOAD_CONFIG:
                    baseConfig.downloadConfiguration = Integer.parseInt(strConfigContent);
                    CommonSingle.getInstance().setBaseConfig(baseConfig);
                    break;
                case Constants.CONFIG.CONFIG_COMMUNICATE_TCP:
                    baseConfig.tcpHostIp = strConfigContent;
                    CommonSingle.getInstance().setBaseConfig(baseConfig);
                    break;
                case Constants.CONFIG.CONFIG_COMMUNICATE_TLS:
                    baseConfig.tlsHostIp = strConfigContent;
                    CommonSingle.getInstance().setBaseConfig(baseConfig);
                    break;
                case Constants.CONFIG.CONFIG_COMMUNICATE_USB:
                    baseConfig.usbHostIp = strConfigContent;
                    CommonSingle.getInstance().setBaseConfig(baseConfig);
                    break;
                case Constants.CONFIG.CONFIG_COMMUNICATE_NAC:
                    baseConfig.nacDestAddr = strConfigContent;
                    CommonSingle.getInstance().setBaseConfig(baseConfig);
                    break;
//                case Constants.CONFIG.CONFIG_KERNEL_VER:
                    //GET KERNEL VERSION
                case Constants.CONFIG.CONFIG_MERCHANT_ID:
                    //SET MERCHANT ID
                    baseConfig.merchantID = strConfigContent;
                    CommonSingle.getInstance().setBaseConfig(baseConfig);
                default:
                    isSuccess = 0;
                    break;

            }
            LogTool.d("setConfig : " + "\r\n" +
                    "Get Type : " + bConfigType + "\r\n" +
                    "Input Value : " + strConfigContent);
            return isSuccess;
        }

        @Override
        public long getTrigger() throws RemoteException {
            long triggerTime = CommonSingle.getInstance().getTriggerConfig().scheduleUnixtime;
            LogTool.d("getTrigger : " + "\r\n" +
                    "triggerTime : " + triggerTime + "\r\n");
            return triggerTime;
        }

        @Override
        public long getActiveTime() throws RemoteException {
            long activeTime = CommonSingle.getInstance().getActiveConfig().activeUnixtime;
            LogTool.d("getActiveTime : " + "\r\n" +
                    "activeTime : " + activeTime + "\r\n");
            return activeTime;
        }

        @Override
        public int setBootConnectEnable(byte openType) throws RemoteException {
            EnableConfig enableConfig = CommonSingle.getInstance().getEnableConfig();
            enableConfig.bootConnectEnable = openType == CommonConst.ENABLE_CONFIG_BOOT_CONNECT_ENABLE_VALUE;
            LogTool.d("setBootConnectEnable : " + "\r\n" +
                    "openType : " + openType + "\r\n" +
                    "After Set :" + enableConfig.bootConnectEnable);
            return 0;
        }

        @Override
        public int getBootConnectStatus() throws RemoteException {
            EnableConfig enableConfig = CommonSingle.getInstance().getEnableConfig();
            int bootConnectStatus = 0;
            if (enableConfig.bootConnectEnable) {
                bootConnectStatus = 1;
            }
            LogTool.d("getBootConnectStatus : " + "\r\n" +
                    "bootConnectStatus : " + bootConnectStatus + "\r\n");
            return bootConnectStatus;
        }


        @Override
        public int setCTMSEnable(byte openType) throws RemoteException {
            EnableConfig enableConfig = CommonSingle.getInstance().getEnableConfig();
            enableConfig.ctmsEnable = openType == CommonConst.ENABLE_CONFIG_CTMS_ENABLE_VALUE;
            CommonSingle.getInstance().setEnableConfig(enableConfig);

            if(!enableConfig.ctmsEnable){
                CommonSingle.getInstance().getPollingService().stopSchedule();
            }
            LogTool.d("setCTMSEnable : " + "\r\n" +
                    "openType : " + openType + "\r\n" +
                    "After Set :" + enableConfig.ctmsEnable);
            return 0;
        }


        @Override
        public int setCM_Mode(byte openType) throws RemoteException {
            BaseConfig baseConfig = CommonSingle.getInstance().getBaseConfig();
            baseConfig.communicationMode = openType;
            CommonSingle.getInstance().setBaseConfig(baseConfig);
            LogTool.d("setCM_Mode : " + "\r\n" +
                    "openType : " + openType + "\r\n" +
                    "After Set :" + baseConfig.communicationMode);
            return 0;
        }

        @Override
        public String getUpdateList() throws RemoteException {

            String updateList = "";
            API_UpdateListData api_updateListData;
            // Check fild Id
            if (CommonFileTool.getInfoData() != null) {
                // Get Update List
                List<UpdateDataModel> updateListData = CommonSingle.getInstance().getUpdateService().getWaitUpdateList();
                // Check Content
                if (updateListData != null) {
                    api_updateListData = new API_UpdateListData();
                    // Prepare new update list for CTMS APP
                    for (UpdateDataModel updateDataModel : updateListData) {
                        api_updateListData.updateListForCTMS.add(
                                new API_UpdateListData.UpdateListForCTMS(
                                        updateDataModel.status,
                                        updateDataModel.packageName,
                                        "",
                                        1,
                                        updateDataModel.id, updateDataModel.type,
                                        0,
                                        updateDataModel.versionName)
                        );
                    }
                    updateList = new Gson().toJson(api_updateListData);
                }
            }
            LogTool.d("getUpdateList : " + "\r\n" +
                    "updateList : " + updateList + "\r\n");
            return updateList;
        }

        @Override
        public int getCTMSStatus() throws RemoteException {
            EnableConfig enableConfig = CommonSingle.getInstance().getEnableConfig();
            int returnValue = enableConfig.ctmsEnable ? CommonConst.ENABLE_CONFIG_CTMS_ENABLE_VALUE : CommonConst.ENABLE_CONFIG_CTMS_NOT_ENABLE_VALUE;
            LogTool.d("getCTMSStatus : " + "\r\n" +
                    "getCTMSStatus : " + returnValue + "\r\n");
            return returnValue;
        }

        @Override
        public long getConnectTime() throws RemoteException {
            long connectTime = CommonSingle.getInstance().getEnableConfig().connectTime;
            LogTool.d("getConnectTime : " + "\r\n" +
                    "connectTime : " + connectTime + "\r\n");
            return connectTime;
        }

        @Override
        public long getLeaveTime() throws RemoteException {
            long leaveTime = CommonSingle.getInstance().getEnableConfig().leaveTime;
            LogTool.d("getLeaveTime : " + "\r\n" +
                    "leaveTime : " + leaveTime + "\r\n");
            return leaveTime;
        }

        @Override
        public int UpdateActive() throws RemoteException {
            // Ap Control
            CommonSingle.getInstance().getUpdateService().setAPControlInstallMode();
            CommonSingle.getInstance().getUpdateService().initUpdateProcess();
            LogTool.d("Start UpdateActive.");
            return 1;
        }

        @Override
        public int ResetFolder() throws RemoteException {
            // Clear Download
            CommonSingle.getInstance().getDownloadService().clearDownload();
            CommonSingle.getInstance().getUpdateService().clearWaitUpdateList();
            CommonSingle.getInstance().getGetInfoService().clearGetInfoFile();
            LogTool.d("Start ResetFolder.");
            return 1;
        }

        @Override
        public int UpdateImmediately() throws RemoteException {
            CommonSingle.getInstance().getPollingService().updateNow();
            LogTool.d("Start UpdateImmediately.");
            return 1;
        }

        @Override
        public int resetSetting() throws RemoteException {
            CommonSingle.getInstance().getDownloadService().clearDownload();
            CommonSingle.getInstance().getUpdateService().clearWaitUpdateList();
            CommonSingle.getInstance().getGetInfoService().clearGetInfoFile();
            CommonSingle.getInstance().getConfigService().clearLocalLog();
            LogTool.d("Start resetSetting.");
            return 1;
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogTool.d("Ctms API Service onDestroy.");
    }
}
