package k.c.service;
import android.app.Application;
import android.content.Context;

import k.c.common.lib.CommonLib;

public class CtmsApplication extends Application {
    private static CtmsApplication instance = null;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        if(BuildConfig.DEBUG){
            CommonLib.openDebug();
        }
        CommonLib.init(this);
    }

    public static CtmsApplication getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        if (instance != null) {
            return instance.getApplicationContext();
        }
        throw new RuntimeException("APP instance is null");
    }

    public static Context getContext() {
        return mContext;
    }
}
