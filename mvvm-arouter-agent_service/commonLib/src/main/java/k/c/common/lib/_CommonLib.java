package k.c.common.lib;

import android.text.TextUtils;
import android.util.Log;

import com.orhanobut.logger.Logger;

import k.c.common.lib.Util.CommonUtil;
import k.c.common.lib.constants.Constants;
import k.c.common.lib.constants.LogStatusDefine;
import k.c.common.lib.logTool.AndroidLogAdapter;
import k.c.common.lib.logTool.CTMSCsvFormatStrategy;
import k.c.common.lib.logTool.CTMSDiskLogAdapter;
import k.c.common.lib.logTool.LogTool;

final class _CommonLib {

    private volatile static String disturbLog;
    private volatile static boolean debuggable = false;
    private volatile static boolean isShowLog = false;
    private volatile static boolean hasInit = false;

    protected static synchronized boolean init() {
        initPrepare();
        __Init();
        LogTool.d("CommonLib init success!");
        hasInit = true;

        return true;
    }

    private static void initPrepare(){
        Log.d(LogStatusDefine.ANDROID_LOGGER_TAG, "read the debug prop, key_name = " + Constants.DISTURB_LOG_KEY_NAME);
        String disturbLog = CommonUtil.readProp(Constants.DISTURB_LOG_KEY_NAME);
        Log.d(LogStatusDefine.ANDROID_LOGGER_TAG,"start set the DisturbLog, DisturbLog = " + disturbLog);
        CommonLib.setDisturbLog(disturbLog);

        Log.d(LogStatusDefine.ANDROID_LOGGER_TAG, "LifeManager initialize");
//        LifeManager.initialize();
    }

    private static void __Init() {
        initLog();
    }

    private static void initLog() {
        AndroidLogAdapter adapter = new AndroidLogAdapter();
        String disturbLog = CommonLib.getDisturbLog();
        Log.d("CTMS_LOGGER", "disturbLog = " + disturbLog);
        if(!TextUtils.isEmpty(disturbLog)){
            if(disturbLog.equals(Constants.DISTURB_LOG_WITH_OPEN)){
                Log.d(Constants.COMMON_LIB_LOGGER_TAG, "disturbLog == " + disturbLog + "then set logger open");
                CommonLib.openLog();
            }else if(disturbLog.equals(Constants.DISTURB_LOG_WITH_CLOSE)){
                Log.d(Constants.COMMON_LIB_LOGGER_TAG, "disturbLog == "+ disturbLog +", then not set logger");
            }
        }
        Logger.addLogAdapter(adapter);
        Logger.addLogAdapter(new CTMSDiskLogAdapter(CTMSCsvFormatStrategy.newBuilder().build()));
    }

    protected static synchronized _CommonLib getInstance(){
        return _CommonLib.SingletonHolder.INSTANCE;
    }


    private static class SingletonHolder {
        private static final _CommonLib INSTANCE = new _CommonLib();
    }

    private _CommonLib(){
    }

    public static synchronized void openDebug() {
        debuggable = true;
        LogTool.d("Common Lib open debug");
    }

    static synchronized void openLog() {
        isShowLog = true;
        LogTool.d("Common Lib open log");
    }

    static synchronized void setDisturbLog(String _disturbLog) {
        disturbLog = _disturbLog;
        LogTool.d("setDisturbLog == %s", disturbLog);
    }

    static String getDisturbLog() {
        return disturbLog;
    }

    static boolean debuggable() {
        return debuggable;
    }

    static boolean showLogable(){
        return isShowLog;
    }
}
