package k.c.common.lib;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import k.c.common.lib.constants.LogStatusDefine;

public class CommonLib {

    private volatile static boolean hasInit = false;
    private static Context mContext;
    /**
     * Init
     */
    public static void init(Application application) {
        if (!hasInit) {
            Log.d(LogStatusDefine.ANDROID_LOGGER_TAG, "CommonLib init start.");
            mContext = application;
            hasInit = _CommonLib.init();
            Log.d(LogStatusDefine.ANDROID_LOGGER_TAG, "CommonLib init over.");
        }
    }

    public static synchronized CommonLib getInstance(){
        return CommonLib.SingletonHolder.INSTANCE;
    }


    private static class SingletonHolder {
        private static final CommonLib INSTANCE = new CommonLib();
    }

    private CommonLib(){
    }


    public static Context getAppContext(){
        return mContext;
    }

    public static synchronized void openDebug() {
        _CommonLib.openDebug();
    }

    static synchronized void openLog() {
        _CommonLib.openLog();
    }

    static synchronized void setDisturbLog(String _disturbLog) {
        _CommonLib.setDisturbLog(_disturbLog);
    }

    public static String getDisturbLog() {
        return _CommonLib.getDisturbLog();
    }

    public static boolean debuggable() {
        return _CommonLib.debuggable();
    }

    public static boolean showLogable(){
        return _CommonLib.showLogable();
    }
}
