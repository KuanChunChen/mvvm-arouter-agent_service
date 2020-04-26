package k.c.module.commonbusiness.service;

import com.alibaba.android.arouter.facade.template.IProvider;

public interface ConfigService extends IProvider {
    void initConfig();
    void saveBasicConfig();
    void saveActiveConfig();
    void saveEnableConfig();
    void saveTriggerConfig();
    void clearLocalLog();
    void changeSystemPanelPassword();
}
