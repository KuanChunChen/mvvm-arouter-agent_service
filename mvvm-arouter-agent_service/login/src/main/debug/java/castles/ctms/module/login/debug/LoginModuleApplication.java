package k.c.module.login.debug;

import k.c.common.lib.CommonLib;
import k.c.common.lib.base.ModuleBaseApplication;

public class LoginModuleApplication extends ModuleBaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        CommonLib.openDebug();
        CommonLib.init(this);
    }
}
