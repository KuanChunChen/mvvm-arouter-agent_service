package k.c.common.lib.constants;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Constants {
    public final static String DISTURB_LOG_KEY_NAME = "debug.castles.ctms.logger";
    public final static String DISTURB_LOG_WITH_OPEN = "open";
    public final static String DISTURB_LOG_WITH_CLOSE = "close";
    public final static String COMMON_LIB_LOGGER_TAG = "Common_Lib_LOGGER";
    public final static Charset FILE_WRITE_READER_CHARSET = StandardCharsets.UTF_8;
    public final static int COUNTDOWN_INTERVAL = 1000;

    public final static int DIALOG_TIME_NOW = 0;
    public final static int DIALOG_TIME_2_MIN = 1;
    public final static int DIALOG_TIME_15_MIN = 2;
    public final static int DIALOG_TIME_30_MIN = 3;

    public class Path {
        public static final String UPDATE_OTA_FILE_RECORD = "/OTAindex";
    }

    public class File{
        public final static int FILE_NOT_NEED_UNZIP = 1;
        public final static int FILE_UNZIP_SUCCESS = 2;
        public final static int FILE_UNZIP_FAIL = 3;
    }

    public class Time{
        public final static String TIME_ZONE_GMT_0 = "GMT";
        public final static String TIME_ZONE_GMT_9 = "GMT 09";
        public final static int GMT_TIME_ZONE_BEGIN_LENGTH = 3;
        public final static int GMT_TIME_ZONE_END_LENGTH = 9;
    }

    public class DialogType{
        public static final int DIALOG_REBOOT = 1;
        public static final int DIALOG_CHOOSE_TIME = 2;
        public static final int DIALOG_ASK_PERMISSION = 3;
        public static final int DIALOG_BATTERY_ENOUGH = 4;
    }
}
