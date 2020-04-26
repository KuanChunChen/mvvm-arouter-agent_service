package k.c.module.commonbusiness;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;

import k.c.module.commonbusiness.common.CommonFileTool;
import k.c.module.commonbusiness.po.ActiveConfig;
import k.c.module.commonbusiness.po.BaseConfig;
import k.c.module.commonbusiness.po.EnableConfig;
import k.c.module.commonbusiness.po.TriggerConfig;
import k.c.module.commonbusiness.service.ConfigService;
import k.c.module.commonbusiness.service.DiagnosticService;
import k.c.module.commonbusiness.service.DownloadService;
import k.c.module.commonbusiness.service.GetInfoService;
import k.c.module.commonbusiness.service.LoginService;
import k.c.module.commonbusiness.service.PollingService;
import k.c.module.commonbusiness.service.UpdateService;

public class CommonSingle {

    private boolean isLogin = false;
    private String sessionId;
    private String serverProtocolVersion;
    private BaseConfig baseConfig;
    private EnableConfig enableConfig;
    private TriggerConfig triggerConfig;
    private ActiveConfig activeConfig;
    private volatile boolean isRegisterInstallAlarm;
    @Autowired
    LoginService loginService;
    @Autowired
    GetInfoService getInfoService;
    @Autowired
    ConfigService configService;
    @Autowired
    DownloadService downloadService;
    @Autowired
    PollingService pollingService;
    @Autowired
    UpdateService updateService;
    @Autowired
    DiagnosticService diagnosticService;

    private CommonSingle(){
        ARouter.getInstance().inject(this);
    }

    public static synchronized CommonSingle getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final CommonSingle INSTANCE = new CommonSingle();
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getServerProtocolVersion() {
        return serverProtocolVersion;
    }

    public void setServerProtocolVersion(String serverProtocolVersion) {
        this.serverProtocolVersion = serverProtocolVersion;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public BaseConfig getBaseConfig() {
        return baseConfig;
    }

    public void setBaseConfig(BaseConfig baseConfig) {
        this.baseConfig = baseConfig;
        configService.saveBasicConfig();
    }

    public EnableConfig getEnableConfig() {
        return enableConfig;
    }

    public void setEnableConfig(EnableConfig enableConfig) {
        this.enableConfig = enableConfig;
        configService.saveEnableConfig();
    }

    public TriggerConfig getTriggerConfig() {
        return triggerConfig;
    }

    public void setTriggerConfig(TriggerConfig triggerConfig) {
        this.triggerConfig = triggerConfig;
        configService.saveTriggerConfig();
    }

    public ActiveConfig getActiveConfig() {
        return activeConfig;
    }

    public void setActiveConfig(ActiveConfig activeConfig) {
        this.activeConfig = activeConfig;
        configService.saveActiveConfig();
    }

    public LoginService getLoginService() {
        return loginService;
    }

    public GetInfoService getGetInfoService() {
        return getInfoService;
    }

    public ConfigService getConfigService() {
        return configService;
    }

    public DownloadService getDownloadService() {
        return downloadService;
    }

    public PollingService getPollingService() {
        return pollingService;
    }

    public UpdateService getUpdateService() {
        return updateService;
    }

    public DiagnosticService getDiagnosticService(){return diagnosticService;}

    public long getCurrentTriggerTime() {
        if(CommonFileTool.getInfoData() == null){
            return -1;
        }
        return CommonFileTool.getInfoData().getCurrentTriggerTime();
    }

    public long getSpecifyTimeDelay() {
        if(CommonFileTool.getInfoData() == null){
            return -1;
        }
        return CommonFileTool.getInfoData().getSpecifyTimeDelay();
    }

    public boolean isRegisterInstallAlarm() {
        return isRegisterInstallAlarm;
    }

    public void setRegisterInstallAlarm(boolean registerInstallAlarm) {
        isRegisterInstallAlarm = registerInstallAlarm;
    }
}
