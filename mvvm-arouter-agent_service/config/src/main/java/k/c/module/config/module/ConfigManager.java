package k.c.module.config.module;


import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;

import k.c.common.lib.CommonLib;
import k.c.common.lib.Util.FileFastUtil;
import k.c.common.lib.logTool.LogTool;
import k.c.module.commonbusiness.CommonSingle;
import k.c.module.commonbusiness.common.CommonConst;
import k.c.module.commonbusiness.model.CtmsConfig;
import k.c.module.commonbusiness.po.ActiveConfig;
import k.c.module.commonbusiness.po.BaseConfig;
import k.c.module.commonbusiness.po.EnableConfig;
import k.c.module.commonbusiness.po.TriggerConfig;
import k.c.module.config.model.CtmsActive;
import k.c.module.config.model.CtmsEnable;
import k.c.module.config.model.CtmsTrigger;

public class ConfigManager {

    public static void makeDefaultCtmsConfig(){
        boolean isExist = FileFastUtil.fileIsExists(CommonConst.FileConst.PATH_WITH_PUBLIC_BASE_CONFIG_REAL_NAME);
        LogTool.d("base config file isExist = %s, path = %s", isExist, CommonConst.FileConst.PATH_WITH_PUBLIC_BASE_CONFIG_REAL_NAME);
        if(!isExist){
            LogTool.d("base config file is not exist, then get the default config file");
            InputStream defInputStream;
            try {
                defInputStream = CommonLib.getAppContext().getAssets().open(CommonConst.FileConst.CONFIG_NAME_WITH_BASE_SETTING);
                LogTool.d("write the base config");
                FileFastUtil.fileWrite(CommonConst.FileConst.PATH_WITH_PUBLIC_BASE_CONFIG_REAL_NAME, defInputStream);
            } catch (IOException e) {
                e.printStackTrace();
                LogTool.e("write the base config fail by the assets");
            }
        }else{
            LogTool.d("File is exist");
        }
    }

    public static void makeDefaultCtmsEnable(){
        boolean isExist = FileFastUtil.fileIsExists(CommonConst.FileConst.PATH_WITH_PUBLIC_ENABLE_CONFIG_REAL_NAME);
        LogTool.d("enable config file isExist = %s, path = %s", isExist, CommonConst.FileConst.PATH_WITH_PUBLIC_ENABLE_CONFIG_REAL_NAME);
        if(!isExist){
            LogTool.d("enable config file is not exist, then get the default enable config file");
            InputStream defInputStream;
            try {
                defInputStream = CommonLib.getAppContext().getAssets().open(CommonConst.FileConst.CONFIG_NAME_WITH_ENABLE_SETTING);
                LogTool.d("write the enable config");
                FileFastUtil.fileWrite(CommonConst.FileConst.PATH_WITH_PUBLIC_ENABLE_CONFIG_REAL_NAME, defInputStream);
            } catch (IOException e) {
                e.printStackTrace();
                LogTool.e("write the enable config fail by the assets");
            }
        }else{
            LogTool.d("File is exist");
        }
    }

    public static void makeDefaultCtmsTrigger(){
        boolean isExist = FileFastUtil.fileIsExists(CommonConst.FileConst.PATH_WITH_PUBLIC_TRIGGER_CONFIG_REAL_NAME);
        LogTool.d("Trigger config file isExist = %s, path = %s", isExist, CommonConst.FileConst.PATH_WITH_PUBLIC_TRIGGER_CONFIG_REAL_NAME);
        if(!isExist){
            LogTool.d("Trigger config file is not exist, then get the default trigger config file");
            InputStream defInputStream;
            try {
                defInputStream = CommonLib.getAppContext().getAssets().open(CommonConst.FileConst.CONFIG_NAME_WITH_TRIGGER_SETTING);
                LogTool.d("write the Trigger config");
                FileFastUtil.fileWrite(CommonConst.FileConst.PATH_WITH_PUBLIC_TRIGGER_CONFIG_REAL_NAME, defInputStream);
            } catch (IOException e) {
                e.printStackTrace();
                LogTool.e("write the trigger config fail by the assets");
            }
        }else{
            LogTool.d("File is exist");
        }
    }

    public static void makeDefaultCtmsActive() {
        boolean isExist = FileFastUtil.fileIsExists(CommonConst.FileConst.PATH_WITH_PUBLIC_ACTIVE_CONFIG_REAL_NAME);
        LogTool.d("Active config file isExist = %s, path = %s", isExist, CommonConst.FileConst.PATH_WITH_PUBLIC_ACTIVE_CONFIG_REAL_NAME);
        if(!isExist){
            LogTool.d("Active config file is not exist, then get the default Active config file");
            InputStream defInputStream;
            try {
                defInputStream = CommonLib.getAppContext().getAssets().open(CommonConst.FileConst.CONFIG_NAME_WITH_ACTIVE_SETTING);
                LogTool.d("write the Active config");
                FileFastUtil.fileWrite(CommonConst.FileConst.PATH_WITH_PUBLIC_ACTIVE_CONFIG_REAL_NAME, defInputStream);
            } catch (IOException e) {
                e.printStackTrace();
                LogTool.e("write the Active config fail by the assets");
            }
        }else{
            LogTool.d("File is exist");
        }
    }

    /**
     * Save the content by path
     * @param content content info
     * @param path save path
     */
    private static <T> boolean saveLatestContent(T content, String path){
        String jsonString = new Gson().toJson(content);
        try {
            FileFastUtil.fileWrite(path, jsonString);
        } catch (IOException e) {
            LogTool.e(e.getMessage());
            return false;
        }
        return true;
    }

    public static String getBaseConfigString() {
        try {
            return FileFastUtil.fileReader(CommonConst.FileConst.PATH_WITH_PUBLIC_BASE_CONFIG_REAL_NAME);
        }catch (IOException e) {
            LogTool.e(e.getMessage());
            return null;
        }
    }

    public static CtmsConfig getBaseConfig() {
        try {
            String jsonContent = FileFastUtil.fileReader(CommonConst.FileConst.PATH_WITH_PUBLIC_BASE_CONFIG_REAL_NAME);
            return new Gson().fromJson(jsonContent, CtmsConfig.class);
        }catch (IOException e) {
            LogTool.e(e.getMessage());
            return null;
        }
    }

    public static CtmsEnable getEnableConfig() {
        try {
            String jsonContent = FileFastUtil.fileReader(CommonConst.FileConst.PATH_WITH_PUBLIC_ENABLE_CONFIG_REAL_NAME);
            return new Gson().fromJson(jsonContent, CtmsEnable.class);
        }catch (IOException e) {
            LogTool.e(e.getMessage());
            return null;
        }
    }

    public static CtmsTrigger getTriggerConfig() {
        try {
            String jsonContent = FileFastUtil.fileReader(CommonConst.FileConst.PATH_WITH_PUBLIC_TRIGGER_CONFIG_REAL_NAME);
            return new Gson().fromJson(jsonContent, CtmsTrigger.class);
        }catch (IOException e) {
            LogTool.e(e.getMessage());
            return null;
        }
    }

    public static CtmsActive getActiveConfig() {
        try {
            String jsonContent = FileFastUtil.fileReader(CommonConst.FileConst.PATH_WITH_PUBLIC_ACTIVE_CONFIG_REAL_NAME);
            return new Gson().fromJson(jsonContent, CtmsActive.class);
        }catch (IOException e) {
            LogTool.e(e.getMessage());
            return null;
        }
    }

    public static void saveBaseConfig() {
        BaseConfig baseConfig = CommonSingle.getInstance().getBaseConfig();
        if(baseConfig != null){
            CtmsConfig ctmsConfig = new CtmsConfig();
            ctmsConfig.serialNumber = baseConfig.serialNumber;
            ctmsConfig.compatibleFlag = baseConfig.compatibleFlag;
            ctmsConfig.retryCount = baseConfig.retryCount;
            ctmsConfig.downloadConfiguration = baseConfig.downloadConfiguration;
            ctmsConfig.cmMode = baseConfig.communicationMode;
            ctmsConfig.tcp.hostIp = baseConfig.tcpHostIp;
            ctmsConfig.tcp.hostPort = baseConfig.tcpHostPort;
            ctmsConfig.tcp.transmissionSize = baseConfig.tcpTransmissionSize;
            ctmsConfig.tcp.connectTimeout = baseConfig.tcpConnectTimeout;
            ctmsConfig.tcp.txTimeout = baseConfig.tcpTxTimeout;
            ctmsConfig.tcp.rxTimeout = baseConfig.tcpRxTimeout;
            ctmsConfig.tls.hostIp = baseConfig.tlsHostIp;
            ctmsConfig.tls.hostPort = baseConfig.tlsHostPort;
            ctmsConfig.tls.verifyPeer = baseConfig.tlsVerifyPeer;
            ctmsConfig.nac.protocol = baseConfig.nacProtocol;
            ctmsConfig.nac.blockSize = baseConfig.nacBlockSize;
            ctmsConfig.nac.sourceAddr = baseConfig.nacSourceAddr;
            ctmsConfig.nac.destAddr = baseConfig.nacDestAddr;
            ctmsConfig.nac.lenType = baseConfig.nacLenType;
            ctmsConfig.nac.addLenFlag = baseConfig.nacAddLenFlag;
            ctmsConfig.usb.hostIp = baseConfig.usbHostIp;
            ctmsConfig.usb.hostPort = baseConfig.usbHostPort;
            LogTool.d("start saveCTMSConfig = %s", ctmsConfig);
            saveLatestContent(ctmsConfig, CommonConst.FileConst.PATH_WITH_PUBLIC_BASE_CONFIG_REAL_NAME);
        }
    }

    public static void saveEnableConfig() {
        EnableConfig enableConfig = CommonSingle.getInstance().getEnableConfig();
        LogTool.d("start saveEnableConfig = %s", enableConfig);
        if(enableConfig != null){
            CtmsEnable ctmsEnable = new CtmsEnable();
            ctmsEnable.mCtms.enable = enableConfig.ctmsEnable ? CommonConst.ENABLE_CONFIG_CTMS_ENABLE_VALUE : CommonConst.ENABLE_CONFIG_CTMS_NOT_ENABLE_VALUE;
            ctmsEnable.mBootConnect.enable = enableConfig.bootConnectEnable ? CommonConst.ENABLE_CONFIG_BOOT_CONNECT_ENABLE_VALUE : CommonConst.ENABLE_CONFIG_BOOT_CONNECT_NOT_ENABLE_VALUE;
            ctmsEnable.mConnect.time = enableConfig.connectTime;
            ctmsEnable.mLeave.time = enableConfig.leaveTime;
            saveLatestContent(ctmsEnable, CommonConst.FileConst.PATH_WITH_PUBLIC_ENABLE_CONFIG_REAL_NAME);
        }
    }

    public static void saveTriggerConfig() {
        TriggerConfig triggerConfig = CommonSingle.getInstance().getTriggerConfig();
        LogTool.d("start saveTriggerConfig = %s", triggerConfig);
        if(triggerConfig != null){
            CtmsTrigger ctmsTrigger = new CtmsTrigger();
            ctmsTrigger.schedule.year = triggerConfig.scheduleYear;
            ctmsTrigger.schedule.month = triggerConfig.scheduleMonth;
            ctmsTrigger.schedule.day = triggerConfig.scheduleDay;
            ctmsTrigger.schedule.hour = triggerConfig.scheduleHour;
            ctmsTrigger.schedule.minute = triggerConfig.scheduleMinute;
            ctmsTrigger.schedule.second = triggerConfig.scheduleSecond;
            ctmsTrigger.schedule.timezone = triggerConfig.scheduleTimezone;
            ctmsTrigger.schedule.unixtime = triggerConfig.scheduleUnixtime;
            ctmsTrigger.mSwitch.trigger = triggerConfig.switchTrigger;
            ctmsTrigger.immediately.execute = triggerConfig.immediateExecute;
            ctmsTrigger.polling.enable = triggerConfig.pollingEnable ? CommonConst.TRIGGER_CONFIG_POLLING_ENABLE_VALUE : CommonConst.TRIGGER_CONFIG_POLLING_NOT_ENABLE_VALUE;
            ctmsTrigger.polling.cycle = triggerConfig.pollingCycle;
            saveLatestContent(ctmsTrigger, CommonConst.FileConst.PATH_WITH_PUBLIC_TRIGGER_CONFIG_REAL_NAME);
        }
    }

    public static void saveActiveConfig() {
        ActiveConfig activeConfig = CommonSingle.getInstance().getActiveConfig();
        LogTool.d("start saveActiveConfig = %s", activeConfig);
        if(activeConfig != null){
            CtmsActive ctmsActive = new CtmsActive();
            ctmsActive.active.year = activeConfig.activeYear;
            ctmsActive.active.month = activeConfig.activeMonth;
            ctmsActive.active.day = activeConfig.activeDay;
            ctmsActive.active.hour = activeConfig.activeHour;
            ctmsActive.active.minute = activeConfig.activeMinute;
            ctmsActive.active.second = activeConfig.activeSecond;
            ctmsActive.active.timezone = activeConfig.activeTimezone;
            ctmsActive.active.unixtime = activeConfig.activeUnixtime;
            ctmsActive.mSwitch.action = activeConfig.switchAction;
            ctmsActive.install.lock = activeConfig.installLock;
            ctmsActive.immediately.execute = activeConfig.immediatelyExecute;
            ctmsActive.postpone.minute = activeConfig.postponeMinute;
            saveLatestContent(ctmsActive, CommonConst.FileConst.PATH_WITH_PUBLIC_ACTIVE_CONFIG_REAL_NAME);
        }
    }

//    public static int getCtmeEnable() {
//        String strDefaultPath = AppSingle.getInstance().getDefalutPath();
//        int iReturn;
//        if (FileUtil.isDirectoryExist(strDefaultPath + ENABLE_CONFIG_NAME)) {
//            String strRead = FileUtil.readFile(strDefaultPath + ENABLE_CONFIG_NAME);
//            CtmsEnable mCtmsEnable = new Gson().fromJson(strRead, CtmsEnable.class);
//            iReturn = mCtmsEnable.mCtms.getEnable();
//        } else {
//            iReturn = 0;
//        }
//        return iReturn;
//    }
//
//    public static void setCtmeEnable(int iCtmsEnable) {
//        String strDefaultPath = AppSingle.getInstance().getDefalutPath();
//        if (FileUtil.isDirectoryExist(strDefaultPath + ENABLE_CONFIG_NAME)) {
//            String strRead = FileUtil.readFile(strDefaultPath + ENABLE_CONFIG_NAME);
//            CtmsEnable mCtmsEnable = new Gson().fromJson(strRead, CtmsEnable.class);
//            mCtmsEnable.mCtms.setEnable(iCtmsEnable);
//            String strCtmsEnable= new Gson().toJson(mCtmsEnable);
//            FileUtil.writeFile(strDefaultPath + ENABLE_CONFIG_NAME, strCtmsEnable);
//        }
//    }
//
//
//    public static int getBootConnect() {
//        String strDefaultPath = AppSingle.getInstance().getDefalutPath();
//        int iReturn = -1;
//
//        if (FileUtil.isDirectoryExist(strDefaultPath + ENABLE_CONFIG_NAME)) {
//            String strRead = FileUtil.readFile(strDefaultPath + ENABLE_CONFIG_NAME);
//            CtmsEnable mCtmsEnable = new Gson().fromJson(strRead, CtmsEnable.class);
//            iReturn = mCtmsEnable.mBootConnect.getEnable();
//        }
//        return iReturn;
//    }
//
//    public static void setBootConnect(int iBootConnect) {
//        String strDefaultPath = AppSingle.getInstance().getDefalutPath();
//        if (FileUtil.isDirectoryExist(strDefaultPath + ENABLE_CONFIG_NAME)) {
//            String strRead = FileUtil.readFile(strDefaultPath + ENABLE_CONFIG_NAME);
//            CtmsEnable mCtmsEnable = new Gson().fromJson(strRead, CtmsEnable.class);
//            mCtmsEnable.mBootConnect.setEnable(iBootConnect);
//            String strCtmsEnable= new Gson().toJson(mCtmsEnable);
//            FileUtil.writeFile(strDefaultPath + ENABLE_CONFIG_NAME, strCtmsEnable);
//        }
//    }
//
//    public static int getCM_Mode() {
//        String strDefaultPath = AppSingle.getInstance().getDefalutPath();
//        int iReturn;
//        if (FileUtil.isDirectoryExist(strDefaultPath + CTMS_CONFIG_NAME)) {
//            String strRead = FileUtil.readFile(strDefaultPath + CTMS_CONFIG_NAME);
//            CtmsConfig mCtmsConfig = new Gson().fromJson(strRead, CtmsConfig.class);
//            iReturn = mCtmsConfig.getCmMode();
//        } else {
//            iReturn = 0;
//        }
//        return iReturn;
//    }
//
//    public static int getTriggerMode() {
//        String strDefaultPath = AppSingle.getInstance().getDefalutPath();
//        int iReturn;
//        if (FileUtil.isDirectoryExist(strDefaultPath + TRIGGER_CONFIG_NAME)) {
//            String strRead = FileUtil.readFile(strDefaultPath + TRIGGER_CONFIG_NAME);
//            CtmsTrigger mCtmsTrigger = new Gson().fromJson(strRead, CtmsTrigger.class);
//            iReturn = mCtmsTrigger.mSwitch.getTrigger();
//        } else {
//            iReturn =-1;
//
//        }
//        return iReturn;
//    }
//
//    public static int getTriggerUnixTime() {
//
//        String strDefaultPath = AppSingle.getInstance().getDefalutPath();
//        int iReturn;
//        if (FileUtil.isDirectoryExist(strDefaultPath + TRIGGER_CONFIG_NAME)) {
//            String strRead = FileUtil.readFile(strDefaultPath + TRIGGER_CONFIG_NAME);
//            CtmsTrigger mCtmsTrigger = new Gson().fromJson(strRead, CtmsTrigger.class);
//            iReturn = mCtmsTrigger.mSchedule.getUnixtime();
//        } else {
//            iReturn = -1;
//
//        }
//        return iReturn;
//    }
//
//    public static int CheckTriggerTime(int iTriggerUnixTime) {
//        String strDefaultPath = AppSingle.getInstance().getDefalutPath();
//        int iReturn = -1;
//        if (iTriggerUnixTime != -1) {
//            if (Integer.parseInt(TimeUtil.getCurrentSysMillisTime()) > iTriggerUnixTime) {
//                iReturn = 1;
//
//            } else {
//                iReturn = 0;
//
//            }
//        }
//
//        return iReturn;
//    }
//    public static void setCM_Mode(int iCM_mode) {
//        String strDefaultPath = AppSingle.getInstance().getDefalutPath();
//        if (FileUtil.isDirectoryExist(strDefaultPath + CTMS_CONFIG_NAME)) {
//            String strRead = FileUtil.readFile(strDefaultPath + CTMS_CONFIG_NAME);
//            CtmsConfig mCtmsConfig = new Gson().fromJson(strRead, CtmsConfig.class);
//            mCtmsConfig.setCmMode(iCM_mode);
//            String strCtmsConfig= new Gson().toJson(mCtmsConfig);
//            FileUtil.writeFile(strDefaultPath + CTMS_CONFIG_NAME, strCtmsConfig);
//        }
//    }
//
//    public static String getSerialNumber() {
//        String strDefaultPath = AppSingle.getInstance().getDefalutPath();
//        String strReturn;
//        if (FileUtil.isDirectoryExist(strDefaultPath + CTMS_CONFIG_NAME)) {
//            String strRead = FileUtil.readFile(strDefaultPath + CTMS_CONFIG_NAME);
//            CtmsConfig mCtmsConfig = new Gson().fromJson(strRead, CtmsConfig.class);
//            strReturn = mCtmsConfig.getSerialNumber();
//        } else {
//            strReturn = "FileNotExist";
//        }
//        return strReturn;
//    }
//    public static CtmsConfig getConfigGSON() {
//        String strDefaultPath = AppSingle.getInstance().getDefalutPath();
//        return new Gson().fromJson(FileUtil.readFile(strDefaultPath + CTMS_CONFIG_NAME), CtmsConfig.class);
//    }
//
//    public static String getConfig(byte bCommand) {
//
//        String strDefaultPath = AppSingle.getInstance().getDefalutPath();
//        String strReturn;
//        if (FileUtil.isDirectoryExist(strDefaultPath + CTMS_CONFIG_NAME)) {
//            String strRead = FileUtil.readFile(strDefaultPath + CTMS_CONFIG_NAME);
//            CtmsConfig mCtmsConfig = new Gson().fromJson(strRead, CtmsConfig.class);
//            switch (bCommand) {
//                case ConfigDefine.CONFIG_TERMINAL_SN:
//                    strReturn = mCtmsConfig.getSerialNumber();
//                    break;
//                case ConfigDefine.CONFIG_SERVER_COMPATIBLE:
//                    strReturn = String.valueOf(mCtmsConfig.getCompatibleFlag());
//                    break;
//                case ConfigDefine.CONFIG_RETRY_COUNT:
//                    strReturn = String.valueOf(mCtmsConfig.getRetryCount());
//                    break;
//                case ConfigDefine.CONFIG_DOWNLOAD_CONFIG:
//                    strReturn = String.valueOf(mCtmsConfig.getDownloadConfiguration());
//                    break;
//                case ConfigDefine.CONFIG_COMMUNICATE_TCP_IP:
//                    strReturn = mCtmsConfig.tcp.getHostIp();
//                    break;
//                case ConfigDefine.CONFIG_COMMUNICATE_TCP_PORT:
//                    strReturn = mCtmsConfig.tcp.getHostIp();
//                    break;
//                case ConfigDefine.CONFIG_COMMUNICATE_TLS_IP:
//                    strReturn = mCtmsConfig.tls.getHostIp();
//                    break;
//                case ConfigDefine.CONFIG_COMMUNICATE_TLS_PORT:
//                    strReturn = String.valueOf(mCtmsConfig.tls.getHostPort());
//                    break;
//                case ConfigDefine.CONFIG_COMMUNICATE_USB_IP:
//                    strReturn = mCtmsConfig.usb.getHostIp();
//                    break;
//                case ConfigDefine.CONFIG_COMMUNICATE_TCP_TRANSMISSION_SIZE:
//                    strReturn = String.valueOf(mCtmsConfig.tcp.getTransmissionSize());
//                    break;
//                case ConfigDefine.CONFIG_COMMUNICATE_TCP_CONNECT_TIMEOUT:
//                    strReturn = String.valueOf(mCtmsConfig.tcp.getConnectTimeout());
//                    break;
//                case ConfigDefine.CONFIG_COMMUNICATE_TCP_TX_TIMEOUT:
//                    strReturn = String.valueOf(mCtmsConfig.tcp.getTxTimeout());
//                    break;
//                case ConfigDefine.CONFIG_COMMUNICATE_TCP_RX_TIMEOUT:
//                    strReturn = String.valueOf(mCtmsConfig.tcp.getRxTimeout());
//                    break;
//                default:
//                    strReturn = "command error!";
//                    break;
//
//            }
//
//        } else {
//            strReturn = "FileNotExist";
//        }
//        return strReturn;
//    }
//
//    public static void setConfig(byte bCommand, String strConfig) {
//        String strDefaultPath = AppSingle.getInstance().getDefalutPath();
//        String strTemCtmsConfig;
//        if (FileUtil.isDirectoryExist(strDefaultPath + CTMS_CONFIG_NAME)) {
//            String strRead = FileUtil.readFile(strDefaultPath + CTMS_CONFIG_NAME);
//            CtmsConfig mCtmsConfig = new Gson().fromJson(strRead, CtmsConfig.class);
//            switch (bCommand) {
//                case ConfigDefine.CONFIG_TERMINAL_SN:
//                    mCtmsConfig.setSerialNumber(strConfig);
//                    strTemCtmsConfig = new Gson().toJson(mCtmsConfig);
//                    FileUtil.writeFile(strDefaultPath + CTMS_CONFIG_NAME, strTemCtmsConfig);
//                    break;
//                case ConfigDefine.CONFIG_SERVER_COMPATIBLE:
//                    mCtmsConfig.setCompatibleFlag(Integer.parseInt(strConfig));
//                    strTemCtmsConfig = new Gson().toJson(mCtmsConfig);
//                    FileUtil.writeFile(strDefaultPath + CTMS_CONFIG_NAME, strTemCtmsConfig);
//                    break;
//                case ConfigDefine.CONFIG_RETRY_COUNT:
//                    mCtmsConfig.setRetryCount(Integer.parseInt(strConfig));
//                    strTemCtmsConfig = new Gson().toJson(mCtmsConfig);
//                    FileUtil.writeFile(strDefaultPath + CTMS_CONFIG_NAME, strTemCtmsConfig);
//                    break;
//                case ConfigDefine.CONFIG_DOWNLOAD_CONFIG:
//                    mCtmsConfig.setDownloadConfiguration(Integer.parseInt(strConfig));
//                    strTemCtmsConfig = new Gson().toJson(mCtmsConfig);
//                    FileUtil.writeFile(strDefaultPath + CTMS_CONFIG_NAME, strTemCtmsConfig);
//                    break;
//                case ConfigDefine.CONFIG_COMMUNICATE_TCP_IP:
//                    mCtmsConfig.tcp.setHostIp(strConfig);
//                    strTemCtmsConfig = new Gson().toJson(mCtmsConfig);
//                    FileUtil.writeFile(strDefaultPath + CTMS_CONFIG_NAME, strTemCtmsConfig);
//                    break;
//                case ConfigDefine.CONFIG_COMMUNICATE_TCP_PORT:
//                    mCtmsConfig.tcp.setHostPort(Integer.parseInt(strConfig));
//                    strTemCtmsConfig = new Gson().toJson(mCtmsConfig);
//                    FileUtil.writeFile(strDefaultPath + CTMS_CONFIG_NAME, strTemCtmsConfig);
//                    break;
//                case ConfigDefine.CONFIG_COMMUNICATE_TLS_IP:
//                    mCtmsConfig.tls.setHostIp(strConfig);
//                    strTemCtmsConfig = new Gson().toJson(mCtmsConfig);
//                    FileUtil.writeFile(strDefaultPath + CTMS_CONFIG_NAME, strTemCtmsConfig);
//                    break;
//                case ConfigDefine.CONFIG_COMMUNICATE_TLS_PORT:
//                    mCtmsConfig.tls.setHostPort(Integer.parseInt(strConfig));
//                    strTemCtmsConfig = new Gson().toJson(mCtmsConfig);
//                    FileUtil.writeFile(strDefaultPath + CTMS_CONFIG_NAME, strTemCtmsConfig);
//                    break;
//                case ConfigDefine.CONFIG_COMMUNICATE_USB_IP:
//                    mCtmsConfig.usb.setHostPort(Integer.parseInt(strConfig));
//                    strTemCtmsConfig = new Gson().toJson(mCtmsConfig);
//                    FileUtil.writeFile(strDefaultPath + CTMS_CONFIG_NAME, strTemCtmsConfig);
//                    break;
//                case ConfigDefine.CONFIG_COMMUNICATE_TCP_TRANSMISSION_SIZE:
//                    mCtmsConfig.tcp.setTransmissionSize(Integer.parseInt(strConfig));
//                    strTemCtmsConfig = new Gson().toJson(mCtmsConfig);
//                    FileUtil.writeFile(strDefaultPath + CTMS_CONFIG_NAME, strTemCtmsConfig);
//                    break;
//                case ConfigDefine.CONFIG_COMMUNICATE_TCP_CONNECT_TIMEOUT:
//                    mCtmsConfig.tcp.setConnectTimeout(Integer.parseInt(strConfig));
//                    strTemCtmsConfig = new Gson().toJson(mCtmsConfig);
//                    FileUtil.writeFile(strDefaultPath + CTMS_CONFIG_NAME, strTemCtmsConfig);
//                    break;
//                case ConfigDefine.CONFIG_COMMUNICATE_TCP_TX_TIMEOUT:
//                    mCtmsConfig.tcp.setTxTimeout(Integer.parseInt(strConfig));
//                    strTemCtmsConfig = new Gson().toJson(mCtmsConfig);
//                    FileUtil.writeFile(strDefaultPath + CTMS_CONFIG_NAME, strTemCtmsConfig);
//                    break;
//                case ConfigDefine.CONFIG_COMMUNICATE_TCP_RX_TIMEOUT:
//                    mCtmsConfig.tcp.setRxTimeout(Integer.parseInt(strConfig));
//                    strTemCtmsConfig = new Gson().toJson(mCtmsConfig);
//                    FileUtil.writeFile(strDefaultPath + CTMS_CONFIG_NAME, strTemCtmsConfig);
//                    break;
//                default:
//
//                    break;
//
//            }
//
//        } else {
//
//        }
//
//    }
//
//    public static String getConnectTime() {
//        String strDefaultPath = AppSingle.getInstance().getDefalutPath();
//        String strReturn;
//        if (FileUtil.isDirectoryExist(strDefaultPath + ENABLE_CONFIG_NAME)) {
//            String strRead = FileUtil.readFile(strDefaultPath + ENABLE_CONFIG_NAME);
//            CtmsEnable mCtmsEnable = new Gson().fromJson(strRead, CtmsEnable.class);
//            SimpleDateFormat mSDF = new SimpleDateFormat(TIME_FORMAT);
//            Date mDate = new Date(mCtmsEnable.mConnect.getTime());
//            strReturn = mSDF.format(mDate);
//
//        } else {
//            strReturn = "FileNotExist";
//        }
//        return strReturn;
//    }
//
//    public static void setConnectTime(int iConnectTime) {
//        String strDefaultPath = AppSingle.getInstance().getDefalutPath();
//        if (FileUtil.isDirectoryExist(strDefaultPath + ENABLE_CONFIG_NAME)) {
//            String strRead = FileUtil.readFile(strDefaultPath + ENABLE_CONFIG_NAME);
//            CtmsEnable mCtmsEnable = new Gson().fromJson(strRead, CtmsEnable.class);
//            mCtmsEnable.mConnect.setTime(iConnectTime);
//            String strCtmsEnable= new Gson().toJson(mCtmsEnable);
//            FileUtil.writeFile(strDefaultPath + ENABLE_CONFIG_NAME, strCtmsEnable);
//        }
//    }
//
//    public static String getLeaveTime() {
//        String strDefaultPath = AppSingle.getInstance().getDefalutPath();
//        String strReturn;
//        if (FileUtil.isDirectoryExist(strDefaultPath + ENABLE_CONFIG_NAME)) {
//            String strRead = FileUtil.readFile(strDefaultPath + ENABLE_CONFIG_NAME);
//            CtmsEnable mCtmsEnable = new Gson().fromJson(strRead, CtmsEnable.class);
//            SimpleDateFormat mSDF = new SimpleDateFormat(TIME_FORMAT);
//            Date mDate = new Date(mCtmsEnable.mLeave.getTime());
//            strReturn = mSDF.format(mDate);
//
//        } else {
//            strReturn = "FileNotExist";
//        }
//        return strReturn;
//    }
//
//
//    public static void setLeaveTime(int iLeaveTime) {
//        String strDefaultPath = AppSingle.getInstance().getDefalutPath();
//        if (FileUtil.isDirectoryExist(strDefaultPath + ENABLE_CONFIG_NAME)) {
//            String strRead = FileUtil.readFile(strDefaultPath + ENABLE_CONFIG_NAME);
//            CtmsEnable mCtmsEnable = new Gson().fromJson(strRead, CtmsEnable.class);
//            mCtmsEnable.mLeave.setTime(iLeaveTime);
//            String strCtmsEnable= new Gson().toJson(mCtmsEnable);
//            FileUtil.writeFile(strDefaultPath + ENABLE_CONFIG_NAME, strCtmsEnable);
//        }
//    }
//
//    public static String getTriggerTime() {
//        String strDefaultPath = AppSingle.getInstance().getDefalutPath();
//        String strReturn;
//        if (FileUtil.isDirectoryExist(strDefaultPath + TRIGGER_CONFIG_NAME)) {
//            String strRead = FileUtil.readFile(strDefaultPath + TRIGGER_CONFIG_NAME);
//            CtmsTrigger mCtmsTrigger = new Gson().fromJson(strRead, CtmsTrigger.class);
//            SimpleDateFormat mSDF = new SimpleDateFormat(TIME_FORMAT);
//            Date mDate = new Date(mCtmsTrigger.mSchedule.getUnixtime());
//            strReturn = mSDF.format(mDate);
//
//        } else {
//            strReturn = "FileNotExist";
//        }
//        return strReturn;
//    }
//
//
//    public static void setTriggerTime(int iTriggerTime) {
//        String strDefaultPath = AppSingle.getInstance().getDefalutPath();
//        if (FileUtil.isDirectoryExist(strDefaultPath + TRIGGER_CONFIG_NAME)) {
//            String strRead = FileUtil.readFile(strDefaultPath + TRIGGER_CONFIG_NAME);
//            CtmsTrigger mCtmsTrigger = new Gson().fromJson(strRead, CtmsTrigger.class);
//            mCtmsTrigger.mSchedule.setUnixtime(iTriggerTime);
//            String strCtmsTrigger= new Gson().toJson(mCtmsTrigger);
//            FileUtil.writeFile(strDefaultPath + TRIGGER_CONFIG_NAME, strCtmsTrigger);
//        }
//    }
//
//    public static int getTriggerImmediately() {
//
//        String strDefaultPath = AppSingle.getInstance().getDefalutPath();
//        int iReturn;
//        if (FileUtil.isDirectoryExist(strDefaultPath + TRIGGER_CONFIG_NAME)) {
//            String strRead = FileUtil.readFile(strDefaultPath + TRIGGER_CONFIG_NAME);
//            CtmsTrigger mCtmsTrigger = new Gson().fromJson(strRead, CtmsTrigger.class);
//            iReturn = mCtmsTrigger.mImmediately.getExecute();
//        } else {
//            iReturn = -1;
//        }
//        return iReturn;
//    }
//
//    public static void setTriggerImmediately(int iTriggerImmediately) {
//        String strDefaultPath = AppSingle.getInstance().getDefalutPath();
//        if (FileUtil.isDirectoryExist(strDefaultPath + TRIGGER_CONFIG_NAME)) {
//            String strRead = FileUtil.readFile(strDefaultPath + TRIGGER_CONFIG_NAME);
//            CtmsTrigger mCtmsTrigger = new Gson().fromJson(strRead, CtmsTrigger.class);
//            mCtmsTrigger.mImmediately.setExecute(iTriggerImmediately);
//            String strCtmsTrigger= new Gson().toJson(mCtmsTrigger);
//            FileUtil.writeFile(strDefaultPath + TRIGGER_CONFIG_NAME, strCtmsTrigger);
//        }
//    }
//
//
//    public static String getActiveTime() {
//        String strDefaultPath = AppSingle.getInstance().getDefalutPath();
//        String strReturn;
//        if (FileUtil.isDirectoryExist(strDefaultPath + ACTIVE_CONFIG_NAME)) {
//            String strRead = FileUtil.readFile(strDefaultPath + ACTIVE_CONFIG_NAME);
//            CtmsActive mCtmsActive = new Gson().fromJson(strRead, CtmsActive.class);
//            SimpleDateFormat mSDF = new SimpleDateFormat(TIME_FORMAT);
//            Date mDate = new Date(mCtmsActive.mActive.getUnixtime());
//            strReturn = mSDF.format(mDate);
//
//        } else {
//            strReturn = "FileNotExist";
//        }
//        return strReturn;
//    }
//
//    public static int getActiveSwitchAction() {
//        String strDefaultPath = AppSingle.getInstance().getDefalutPath();
//        int iReturn;
//        if (FileUtil.isDirectoryExist(strDefaultPath + ACTIVE_CONFIG_NAME)) {
//            String strRead = FileUtil.readFile(strDefaultPath + ACTIVE_CONFIG_NAME);
//            CtmsActive mCtmsActive = new Gson().fromJson(strRead, CtmsActive.class);
//
//            iReturn = mCtmsActive.mSwitch.getAction();
//
//        } else {
//            iReturn = -1;
//        }
//        return iReturn;
//    }
//
//    public static int getActivePostponeMinute() {
//        String strDefaultPath = AppSingle.getInstance().getDefalutPath();
//        int iReturn;
//        if (FileUtil.isDirectoryExist(strDefaultPath + ACTIVE_CONFIG_NAME)) {
//            String strRead = FileUtil.readFile(strDefaultPath + ACTIVE_CONFIG_NAME);
//            CtmsActive mCtmsActive = new Gson().fromJson(strRead, CtmsActive.class);
//
//            iReturn = mCtmsActive.mPostpone.getMinute();
//        } else {
//            iReturn = -1;
//        }
//        return iReturn;
//    }
//
//    public static void setActiveTime(int iActiveTime) {
//        String strDefaultPath = AppSingle.getInstance().getDefalutPath();
//        if (FileUtil.isDirectoryExist(strDefaultPath + ACTIVE_CONFIG_NAME)) {
//            String strRead = FileUtil.readFile(strDefaultPath + ACTIVE_CONFIG_NAME);
//            CtmsActive mCtmsActive = new Gson().fromJson(strRead, CtmsActive.class);
//            mCtmsActive.mActive.setUnixtime(iActiveTime);
//            String strCtmsActive= new Gson().toJson(mCtmsActive);
//            FileUtil.writeFile(strDefaultPath + ACTIVE_CONFIG_NAME, strCtmsActive);
//        }
//    }
//
//    public static int getActiveImmediately() {
//
//        String strDefaultPath = AppSingle.getInstance().getDefalutPath();
//        int iReturn;
//        if (FileUtil.isDirectoryExist(strDefaultPath + ACTIVE_CONFIG_NAME)) {
//            String strRead = FileUtil.readFile(strDefaultPath + ACTIVE_CONFIG_NAME);
//            CtmsActive mCtmsActive = new Gson().fromJson(strRead, CtmsActive.class);
//            iReturn = mCtmsActive.mImmediately.getExecute();
//        } else {
//            iReturn = -1;
//        }
//        return iReturn;
//    }
//
//    public static void setActiveImmediately(int iActiveImmediately) {
//        String strDefaultPath = AppSingle.getInstance().getDefalutPath();
//        if (FileUtil.isDirectoryExist(strDefaultPath + ACTIVE_CONFIG_NAME)) {
//            String strRead = FileUtil.readFile(strDefaultPath + ACTIVE_CONFIG_NAME);
//            CtmsActive mCtmsActive = new Gson().fromJson(strRead, CtmsActive.class);
//            mCtmsActive.mImmediately.setExecute(iActiveImmediately);
//            String strCtmsTrigger= new Gson().toJson(mCtmsActive);
//            FileUtil.writeFile(strDefaultPath + ACTIVE_CONFIG_NAME, strCtmsTrigger);
//        }
//    }
//    public static void resetAllConfig() {
//        String strDefaultPath = AppSingle.getInstance().getDefalutPath();
//        if (FileUtil.isDirectoryExist(strDefaultPath + CTMS_CONFIG_NAME)) {
//            FileUtil.deletlFile(strDefaultPath + CTMS_CONFIG_NAME);
//            makeDefaultCtmsConfig();
//        }
//        if (FileUtil.isDirectoryExist(strDefaultPath + ACTIVE_CONFIG_NAME)) {
//            FileUtil.deletlFile(strDefaultPath + ACTIVE_CONFIG_NAME);
//            makeDefaultCtmsActive();
//        }
//        if (FileUtil.isDirectoryExist(strDefaultPath + ENABLE_CONFIG_NAME)) {
//            FileUtil.deletlFile(strDefaultPath + ENABLE_CONFIG_NAME);
//            makeDefaultCtmsEnable();
//        }
//        if (FileUtil.isDirectoryExist(strDefaultPath + TRIGGER_CONFIG_NAME)) {
//            FileUtil.deletlFile(strDefaultPath + TRIGGER_CONFIG_NAME);
//            makeDefaultCtmsTrigger();
//        }
//    }
//
//    public static boolean isPollingEnable() {
//
//        boolean boReturn = false;
//        String strBootConnectJson = AppSingle.getInstance().getDefalutPath() + TRIGGER_CONFIG_NAME;
//
//        //檔案在且有內容
//        if (FileUtil.isDirectoryExist(strBootConnectJson) &&
//                !FileUtil.isFileEmpty(strBootConnectJson)) {
//            String strRead = FileUtil.readFile(strBootConnectJson);
//            CtmsTrigger mCtmsTrigger = new Gson().fromJson(strRead, CtmsTrigger.class);
//            boReturn = mCtmsTrigger.mPolling.getEnable() == 1;
//
//        }
//        return boReturn;
//    }
//
//    public static int getPollingCycle() {
//
//        int iReturn = -1;
//
//        String strBootConnectJson = AppSingle.getInstance().getDefalutPath() + TRIGGER_CONFIG_NAME;
//
//        //檔案在且有內容
//        if (FileUtil.isDirectoryExist(strBootConnectJson) &&
//                !FileUtil.isFileEmpty(strBootConnectJson)) {
//            String strRead = FileUtil.readFile(strBootConnectJson);
//            CtmsTrigger mCtmsTrigger = new Gson().fromJson(strRead, CtmsTrigger.class);
//            iReturn = mCtmsTrigger.mPolling.getCycle();
//
//        }
//        return iReturn;
//    }
}

