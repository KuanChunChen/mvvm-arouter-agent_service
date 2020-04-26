package k.c.module.config.service;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.io.File;

import k.c.common.lib.Util.FileUtil;
import k.c.common.lib.Util.KernelUtil;
import k.c.common.lib.Util.TimeUtil;
import k.c.common.lib.logTool.LogTool;
import k.c.module.commonbusiness.CommonSingle;
import k.c.module.commonbusiness.base.BaseServiceImpl;
import k.c.module.commonbusiness.common.CommonConst;
import k.c.module.commonbusiness.model.CtmsConfig;
import k.c.module.commonbusiness.po.ActiveConfig;
import k.c.module.commonbusiness.po.BaseConfig;
import k.c.module.commonbusiness.po.EnableConfig;
import k.c.module.commonbusiness.po.TriggerConfig;
import k.c.module.commonbusiness.service.ConfigService;
import k.c.module.config.api.SettingCongigHttpAPI;
import k.c.module.config.model.CtmsActive;
import k.c.module.config.model.CtmsEnable;
import k.c.module.config.model.CtmsTrigger;
import k.c.module.config.model.ReportSystemResult;
import k.c.module.config.model.ReportSystemSettingData;
import k.c.module.config.model.SystemSettingInfo;
import k.c.module.config.model.SystemSettingResult;
import k.c.module.config.module.ConfigManager;
import k.c.module.http.Result;
import k.c.module.http.RetrofitResultObserver;

@Route(path = "/config/configService")
public class ConfigServiceImpl extends BaseServiceImpl implements ConfigService {

    @Override
    public void initConfig() {
        ConfigManager.makeDefaultCtmsConfig();
        ConfigManager.makeDefaultCtmsEnable();
        ConfigManager.makeDefaultCtmsTrigger();
        ConfigManager.makeDefaultCtmsActive();

        CtmsConfig ctmsConfig = ConfigManager.getBaseConfig();
        if(ctmsConfig == null){
            LogTool.d("Base config init fail.....");
            return;
        }

        CtmsEnable ctmsEnable = ConfigManager.getEnableConfig();
        if(ctmsEnable == null){
            LogTool.d("Enable config init fail.....");
            return;
        }

        CtmsTrigger ctmsTrigger = ConfigManager.getTriggerConfig();
        if(ctmsTrigger == null){
            LogTool.d("Trigger config init fail.....");
            return;
        }

        CtmsActive ctmsActive = ConfigManager.getActiveConfig();
        if(ctmsActive == null){
            LogTool.d("Active config init fail.....");
            return;
        }

        LogTool.d("file base config = %s", ctmsConfig);
        LogTool.d("file enable config = %s", ctmsEnable);
        LogTool.d("file trigger config = %s", ctmsTrigger);
        LogTool.d("file active config = %s", ctmsActive);
        BaseConfig baseConfig = new BaseConfig();
        baseConfig.serialNumber = ctmsConfig.serialNumber;
        baseConfig.compatibleFlag = ctmsConfig.compatibleFlag;
        baseConfig.retryCount = ctmsConfig.retryCount;
        baseConfig.downloadConfiguration = ctmsConfig.downloadConfiguration;
        baseConfig.communicationMode = ctmsConfig.cmMode;
        baseConfig.merchantID = ctmsConfig.merchantID;
        baseConfig.tcpHostIp = ctmsConfig.tcp.hostIp;
        baseConfig.tcpHostPort = ctmsConfig.tcp.hostPort;
        baseConfig.tcpTransmissionSize = ctmsConfig.tcp.transmissionSize;
        baseConfig.tcpConnectTimeout = ctmsConfig.tcp.connectTimeout;
        baseConfig.tcpTxTimeout = ctmsConfig.tcp.txTimeout;
        baseConfig.tcpRxTimeout = ctmsConfig.tcp.rxTimeout;
        baseConfig.tlsHostIp = ctmsConfig.tls.hostIp;
        baseConfig.tlsHostPort = ctmsConfig.tls.hostPort;
        baseConfig.tlsVerifyPeer = ctmsConfig.tls.verifyPeer;
        baseConfig.nacProtocol = ctmsConfig.nac.protocol;
        baseConfig.nacBlockSize = ctmsConfig.nac.blockSize ;
        baseConfig.nacSourceAddr = ctmsConfig.nac.sourceAddr;
        baseConfig.nacDestAddr = ctmsConfig.nac.destAddr;
        baseConfig.nacLenType = ctmsConfig.nac.lenType;
        baseConfig.nacAddLenFlag = ctmsConfig.nac.addLenFlag;
        baseConfig.usbHostIp = ctmsConfig.usb.hostIp;
        baseConfig.usbHostPort = ctmsConfig.usb.hostPort;
        CommonSingle.getInstance().setBaseConfig(baseConfig);

        EnableConfig enableConfig = new EnableConfig();
        enableConfig.ctmsEnable = ctmsEnable.mCtms.enable == CommonConst.ENABLE_CONFIG_CTMS_ENABLE_VALUE;
        enableConfig.bootConnectEnable = ctmsEnable.mBootConnect.enable == CommonConst.ENABLE_CONFIG_BOOT_CONNECT_ENABLE_VALUE;
        enableConfig.connectTime = ctmsEnable.mConnect.time;
        enableConfig.leaveTime = ctmsEnable.mLeave.time;
        CommonSingle.getInstance().setEnableConfig(enableConfig);

        TriggerConfig triggerConfig = new TriggerConfig();
        triggerConfig.scheduleYear = ctmsTrigger.schedule.year;
        triggerConfig.scheduleMonth = ctmsTrigger.schedule.month;
        triggerConfig.scheduleDay = ctmsTrigger.schedule.day;
        triggerConfig.scheduleHour = ctmsTrigger.schedule.hour;
        triggerConfig.scheduleMinute = ctmsTrigger.schedule.minute;
        triggerConfig.scheduleSecond = ctmsTrigger.schedule.second;
        triggerConfig.scheduleTimezone = ctmsTrigger.schedule.timezone;
        triggerConfig.scheduleUnixtime = ctmsTrigger.schedule.unixtime;
        triggerConfig.switchTrigger = ctmsTrigger.mSwitch.trigger;
        triggerConfig.immediateExecute = ctmsTrigger.immediately.execute;
        triggerConfig.pollingEnable = ctmsTrigger.polling.enable == CommonConst.TRIGGER_CONFIG_POLLING_ENABLE_VALUE;
        triggerConfig.pollingCycle = ctmsTrigger.polling.cycle;
        CommonSingle.getInstance().setTriggerConfig(triggerConfig);

        ActiveConfig activeConfig = new ActiveConfig();
        activeConfig.activeYear = ctmsActive.active.year;
        activeConfig.activeMonth = ctmsActive.active.month;
        activeConfig.activeDay = ctmsActive.active.day;
        activeConfig.activeHour = ctmsActive.active.hour;
        activeConfig.activeMinute = ctmsActive.active.minute;
        activeConfig.activeSecond = ctmsActive.active.second;
        activeConfig.activeTimezone = ctmsActive.active.timezone;
        activeConfig.activeUnixtime = ctmsActive.active.unixtime;
        activeConfig.switchAction = ctmsActive.mSwitch.action;
        activeConfig.installLock = ctmsActive.install.lock;
        activeConfig.immediatelyExecute = ctmsActive.immediately.execute;
        activeConfig.postponeMinute = ctmsActive.postpone.minute;
        CommonSingle.getInstance().setActiveConfig(activeConfig);
    }

    @Override
    public void saveBasicConfig() {
        ConfigManager.saveBaseConfig();
    }

    @Override
    public void saveActiveConfig() {
        ConfigManager.saveActiveConfig();
    }

    @Override
    public void saveEnableConfig() {
        ConfigManager.saveEnableConfig();
    }

    @Override
    public void saveTriggerConfig() {
        ConfigManager.saveTriggerConfig();
    }

    @Override
    public void init(Context context) {
    }

    @Override
    public void clearLocalLog() {
        FileUtil.deleteFile(new File(CommonConst.FileConst.LOG_STATUS_FILE_PATH));
    }

    @Override
    public void changeSystemPanelPassword() {


        SystemSettingInfo systemSettingInfo = new SystemSettingInfo();
        systemSettingInfo.merchantID = CommonSingle.getInstance().getBaseConfig().merchantID;
        systemSettingInfo.serialNumber = CommonSingle.getInstance().getBaseConfig().serialNumber;



        if (systemSettingInfo.serialNumber.length() != 16) {
            LogTool.i(CommonConst.ModuleName.UPDATE_MODULE_NAME, "ChangePassword", CommonConst.returnCode.CTMS_CONFIG_CHANGE_PASSWORD_SERIAL_NUMBER_LENGTH_ERROR);
            return;
        }

        SettingCongigHttpAPI.getSystemSetting(systemSettingInfo, new RetrofitResultObserver<SystemSettingResult>() {
            @Override
            public void onSuccess(SystemSettingResult systemSettingResult) {

                if (systemSettingResult.new_FirstPassword.length() != CommonConst.Common.SYSTEM_PANEL_PASSWORD_LENGTH ||
                        systemSettingResult.new_SecondPassword.length() != CommonConst.Common.SYSTEM_PANEL_PASSWORD_LENGTH) {
                    return;
                }

                int status = KernelUtil.ChangeSystemPanelPassword(
                        systemSettingResult.old_FirstPassword, systemSettingResult.old_FirstPassword.length(),
                        systemSettingResult.old_SecondPassword, systemSettingResult.old_SecondPassword.length(),
                        systemSettingResult.new_FirstPassword, systemSettingResult.new_FirstPassword.length(),
                        systemSettingResult.new_SecondPassword, systemSettingResult.new_SecondPassword.length());

                if (status == 0) {

                    LogTool.i(CommonConst.ModuleName.UPDATE_MODULE_NAME, "ChangePassword", CommonConst.returnCode.CTMS_SUCCESS_STRING);
                } else {

                    LogTool.i(CommonConst.ModuleName.UPDATE_MODULE_NAME, "ChangePassword", status);
                }

                ReportSystemSettingData reportSystemSettingData = new ReportSystemSettingData();
                reportSystemSettingData.serialNumber = CommonSingle.getInstance().getBaseConfig().serialNumber;
                reportSystemSettingData.merchantID = CommonSingle.getInstance().getBaseConfig().merchantID;
                reportSystemSettingData.RTC = TimeUtil.getLongSysMillisTime();
                reportSystemSettingData.serverStatus = 0;
                reportSystemSettingData.status = status;

                SettingCongigHttpAPI.reportSystemSetting(reportSystemSettingData, new RetrofitResultObserver<ReportSystemResult>() {
                    @Override
                    public void onSuccess(ReportSystemResult data) {
                        LogTool.i(CommonConst.ModuleName.UPDATE_MODULE_NAME, "ReportSetting", CommonConst.returnCode.CTMS_SUCCESS_STRING);
                    }

                    @Override
                    public void onFailure(RetrofitResultObserver.RetrofitResultException e){
                        super.onFailure(e);
                        LogTool.i(CommonConst.ModuleName.UPDATE_MODULE_NAME,"ReportSetting", CommonConst.returnCode.CTMS_CONFIG_CHANGE_PASSWORD_REPORT_FAILED);
                    }
                    @Override
                    protected void onResultCode(int resultCode, Result result) {
                        super.onResultCode(resultCode, result);
                        LogTool.i(CommonConst.ModuleName.UPDATE_MODULE_NAME,"ReportSetting", resultCode);

                    }
                });

            }

            @Override
            public void onFailure(RetrofitResultObserver.RetrofitResultException e){
                super.onFailure(e);
                LogTool.i(CommonConst.ModuleName.UPDATE_MODULE_NAME,"ChangePassword", CommonConst.returnCode.CTMS_CONFIG_CHANGE_PASSWORD_GET_SETTING_ERROR);
            }
            @Override
            protected void onResultCode(int resultCode, Result result) {
                super.onResultCode(resultCode, result);
                LogTool.i(CommonConst.ModuleName.UPDATE_MODULE_NAME,"ChangePassword", resultCode);

            }
        });


    }
}
