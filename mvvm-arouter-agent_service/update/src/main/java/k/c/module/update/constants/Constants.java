package k.c.module.update.constants;

public class Constants {
    public static final String MODULE_NAME = "Update";

    public static final String VENDOR_PACKAGE_NAME_S1E = "S1E";

    public static final int PROCESS_HANDLER_IDLE = 0;
    public static final int PROCESS_HANDLER_BUSY = 1;

    public static final long PACKAGE_SIZE_RANGE_2M = 2 * 1024 * 1024;
    public static final long PACKAGE_SIZE_RANGE_50M = 50 * 1024 * 1024;
    public static final long PACKAGE_SIZE_RANGE_200M = 200 * 1024 * 1024;

    public static final long INSTALL_DEFAULT_TIMEOUT = 60 * 1000;
    public static final long PACKAGE_RESULT_TIME_OUT_2M = 20 * 1000;
    public static final long PACKAGE_RESULT_TIME_OUT_50M = 4 * 60 * 1000;
    public static final long PACKAGE_RESULT_TIME_OUT_200M = 8 * 60 * 1000;
    public static final long PACKAGE_RESULT_TIME_OUT_MAX_M = 10 * 60 * 1000;
    public static final long PACKAGE_RESULT_TIME_OUT_SMF = 60 * 60 * 1000;

    public static final int UPDATE_STATUS_INFO_INSTALL_END = 4;
    public static final int UPDATE_STATUS_INFO_INSTALL_FAILD = 8;
}
