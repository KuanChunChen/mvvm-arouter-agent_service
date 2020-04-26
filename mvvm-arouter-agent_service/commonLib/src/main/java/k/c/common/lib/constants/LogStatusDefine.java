package k.c.common.lib.constants;

import k.c.common.lib.CommonLib;
import k.c.common.lib.Util.FileUtil;

public class LogStatusDefine {

    public final static String ANDROID_LOGGER_TAG = "CTMS_SERVICE_LOGGER";
    public final static String DATE_TIME_FORMAT_STYLE_01 = "yyyy-MM-dd HH:mm:ss";
    public final static String DATE_TIME_FORMAT_STYLE_02= "yyyy-MM-dd HH:mm:ss:SSS";

    public static final String CTMS_LOG_STATUS_PATH = FileUtil.getDefaultFolder(CommonLib.getAppContext()) + "/Logstatus.txt";

}
