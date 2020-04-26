package k.c.module.commonbusiness.base;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

import k.c.common.lib.BuildConfig;
import k.c.common.lib.CommonLib;
import k.c.module.commonbusiness.CommonSingle;

public class ModuleBaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            ARouter.openLog();  //开启打印日志
            ARouter.openDebug();// 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this);
        CommonLib.openDebug();
        CommonLib.init(this);
        CommonSingle.getInstance().getConfigService().initConfig();
    }

}
