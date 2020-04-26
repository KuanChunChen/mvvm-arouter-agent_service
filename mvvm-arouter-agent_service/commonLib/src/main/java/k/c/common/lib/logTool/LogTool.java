package k.c.common.lib.logTool;


import com.orhanobut.logger.LogAdapter;
import com.orhanobut.logger.Logger;

import k.c.common.lib.CommonLib;

public class LogTool {

    public final static int LOGGER_LEVEL = Logger.DEBUG;
    public final static int LOGGER_LEVEL_DISK = Logger.INFO;
    public final static boolean IS_DEBUG = CommonLib.debuggable();

    public static void tag(String tag) {
        Logger.t(tag);
    }

    public static void v(String msg) {
        Logger.v(msg);
    }

    public static void v(String tag, String msg) {
        Logger.t(tag);
        Logger.v(tag, msg);
    }

    public static void d(String msg) {
        Logger.d(msg);
    }

    public static void d(Object msg) {
        Logger.d(msg);
    }

    public static void d(String msg, Object... args) {
        Logger.d(msg, args);
    }

    public static void i(String msg) {
        Logger.i(msg);
    }

    public static void i(String moduleNmae, String step, String returnCode){
        Logger.i(moduleNmae + "." + step + "." + returnCode);
    }

    public static void i(String moduleNmae, String step, int returnCode){
        Logger.i(moduleNmae + "." + step + "." + returnCode);
    }

    public static void i(String msg, Object... args) {
        Logger.i(msg, args);
    }

    public static void w(String msg) {
        Logger.w(msg);
    }

    public static void w(String tag, String msg) {
        Logger.t(tag);
        Logger.w(tag, msg);
    }

    public static void e(String msg) {
        if(msg == null){
            return;
        }
        Logger.e(msg);
    }

    public static void e(String tag, String msg) {
        if(msg == null){
            return;
        }
        Logger.t(tag);
        Logger.e(tag, msg);
    }

    public static void json(String msg) {
        Logger.json(msg);
    }

    public static void addLogAdapter(LogAdapter adapter) {
        Logger.addLogAdapter(adapter);
    }

}
