package k.c.module.commonbusiness.common;

import android.os.Environment;

import k.c.common.lib.CommonLib;

public class CommonConst {

    public static final String HTTPS_SERVER = "https://cltms.castlestech.com";
    public static final int HTTP_PORT = 443;

    public class Common{
        public static final int FORMAT_TERMINAL_SN_LENGTH = 16;
        public static final String TERMINAL_PROTOCOL_VER = "1.0.2";
        public static final String TERMINAL_SN_EMPTY = "0000000000000000";
        public static final int BATTERY_AP_INSTALL_LIMIT = 10;
        public static final int BATTERY_FW_INSTALL_LIMIT = 30;

        public static final int SYSTEM_PANEL_PASSWORD_LENGTH = 64;
    }

    public class ModuleName{
        public static final String DOWNLOAD_MODULE_NAME = "Download";
        public static final String GETINFO_MODULE_NAME = "GetInfo";
        public static final String DIAGNOSTIC_MODULE_NAME = "Diagnostic";
        public static final String HTTP_MODULE_NAME = "Http";
        public static final String LOGIN_MODULE_NAME = "Login";
        public static final String LOGOUT_MODULE_NAME = "Logout";
        public static final String UPDATE_MODULE_NAME = "Update";
        public static final String POLLING_MODULE_NAME = "Polling";
    }

    public interface FileConst {
        String INTERNAL_PRIVATE_FILES_ROOTS = CommonLib.getAppContext().getFilesDir().getAbsolutePath();
        String LOG_STATUS_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + java.io.File.separatorChar + "ctmslog";


        String CONFIG_NAME_WITH_BASE_SETTING = "base_config.json";
        String CONFIG_NAME_WITH_ENABLE_SETTING = "enable_config.json";
        String CONFIG_NAME_WITH_TRIGGER_SETTING = "trigger_config.json";
        String CONFIG_NAME_WITH_ACTIVE_SETTING = "active_config.json";
        String UPDATE_LIST_NAME = "updateList_%d.json";
        String GET_INFO_JSON_NAME = "getInfo.json";
        String PATH_WITH_PUBLIC_BASE_CONFIG_REAL_NAME = INTERNAL_PRIVATE_FILES_ROOTS + "/" + CONFIG_NAME_WITH_BASE_SETTING;
        String PATH_WITH_PUBLIC_ENABLE_CONFIG_REAL_NAME = INTERNAL_PRIVATE_FILES_ROOTS + "/" + CONFIG_NAME_WITH_ENABLE_SETTING;
        String PATH_WITH_PUBLIC_TRIGGER_CONFIG_REAL_NAME = INTERNAL_PRIVATE_FILES_ROOTS + "/" + CONFIG_NAME_WITH_TRIGGER_SETTING;
        String PATH_WITH_PUBLIC_ACTIVE_CONFIG_REAL_NAME = INTERNAL_PRIVATE_FILES_ROOTS + "/" + CONFIG_NAME_WITH_ACTIVE_SETTING;
        String PATH_WITH_UPDATE_LIST_REAL_NAME = INTERNAL_PRIVATE_FILES_ROOTS + "/" + UPDATE_LIST_NAME;
        String PATH_WITH_GET_INFO_REAL_NAME = INTERNAL_PRIVATE_FILES_ROOTS + "/" + GET_INFO_JSON_NAME;
        String PATH_PARAMETER_FILE_NAME = "/" + "prmInfo.txt";


        String PATH_SME_AME = "/ExtraModuleList";
        String DOWNLOAD_FILE_SAVE_PATH = "/mnt/sdcard/Download/download/";
        String UPLOAD_BACKUP_PATH = "/mnt/sdcard/Download/data/backup/";
        int DOWNLOAD_BIG_DATA_SIZE = 2 * 1024 * 1024;
        int DOWNLOAD_FAST_MODE_MAX_SIZE = 10 * 1024 * 1024;
        int UPLOAD_ZIP_MAX_SIZE = 100 * 1024 * 1024;//100M
    }

    public static final class Http {
        public static final String HTTP_SERVER = "https://cltms.castlestech.com:443";
        public static final String HTTP_SERVER_TEST = "http://192.120.98.177:7088";
        public static final String HTTP_SERVER_STAGING = "https://staging-ctms.castlestech.net";
        public static final String HTTP_SERVER_DLC_STAGING = "https://nttd-dlc-staging.castlestech.cloud";
        public static final String HTTP_SERVER_SGP_CTMS_STAGING = "https://staging-pos.castechcentre.com";
        public static final String HTTP_SERVER_CTMS_STAGING = "https://ctms-nttd-staging-pos.castlestech.cloud:443";
        public static final String HTTP_PHPSESSID = "PHPSESSID=";
        public static final String UPDATELIST_FILE_PATH = "/mnt/sdcard/Download/updatelists/";
        public static final String UPDATELIST_FILE_NAME = "updateList.json";

        public static final int DOWNLOAD_RETRY_TIMES = 2;
        public static final int UPDATELIST_FILE_TYPE = 255;
        public final static long MAX_SLEEP_TIME_MILL = 100;
    }

    public interface DownloadConst {
        int DOWNLOAD_MODE_SLOW = 0;
        int DOWNLOAD_MODE_FAST = 1;
    }

    public static final class Mode{

        public final static int ATTACH_MODE_UPDATE_NOW = 1;
        public final static int ATTACH_MODE_POLLING = 2;
        public final static int ATTACH_MODE_TRIGGER = 3;
        public final static int ATTACH_MODE_BOOT_CONNECT = 4;

        public final static int TRIGGER_MODE_POLLING = 0;
        public final static int TRIGGER_MODE_ENABLE = 1;
        public final static int TRIGGER_MODE_AUTOMATIC = 2;
        public final static int POLLING_MODE_DISABLE = 0;
        public final static int POLLING__MODE_ENABLE = 1;

        public static final int ACTIVE_MODE_IMMEDIATELY = 0;
        public static final int ACTIVE_MODE_SPECIFIED = 1;
        public static final int ACTIVE_MODE_AP_CONTROL = 2;
        public static final int ACTIVE_MODE_REBOOT = 3;

        public static final int DIAGNOSTIC_ENABLE = 1;

        public final static int ERROR_FORMAT_ERROR= 0x00;
        public final static int ERROR_ACTIVE_NOT_OPEN= 0x01;
        public final static int ERROR_POLLING_TIME_NOT_CORRECT= 0x02;
        public final static int ERROR_UNKNOWN_TRIGGER_MODE = 0x03;

    }

    public class returnCode{
        public static final String CTMS_SUCCESS_STRING = "0000";
        public static final int CTMS_SUCCESS_INT = 0x00;
        public static final int CTMS_FILE_WRITE_FAIL = 0x2001;
        public static final int CTMS_FILE_OPEN_FAIL = 0x2002;
        public static final int CTMS_FILE_NOT_FOUND = 0x2003;
        public static final int CTMS_FILE_UNZIP_FAIL = 0x2004;
        public static final int CTMS_FILE_SIZE_ERROR = 0x2005;
        public static final int CTMS_FILE_APPEND_FAIL = 0x2006;
        public static final int CTMS_FILE_CAP_PATH_NOT_FOUND = 0x2007;
        public static final int CTMS_SESSION_ID_IS_NULL = 0x2008;
        public static final int CTMS_SESSION_ID_TIME_OUT = 0x2009;
        public static final int CTMS_TERMINAL_LEFT_SPACE_NOT_ENOUGH = 0x200A;
        public static final int CTMS_TERMINAL_SN_LENGTH_NOT_CORRECT = 0x200B;
        public static final int CTMS_TERMINAL_SN_NOT_MATCH = 0x200C;
        public static final int CTMS_TERMINAL_BASE_CONFIG_NULL = 0x200D;
        public static final int CTMS_ENCRYPT_ERROR = 0x200E;
        public static final int CTMS_BATTERY_LOW = 0x200F;
        public static final int CTMS_LOGIN_FAIL = 0x2100;
        public static final int CTMS_LOGIN_TIME_OUT = 0x2101;
        public static final int CTMS_GETINFO_FAIL = 0x2200;
        public static final int CTMS_GETINFO_SYSTEM_INFO_FAIL = 0x2201;
        public static final int CTMS_DOWNLOAD_FAIL = 0x2300;
        public static final int CTMS_DOWNLOAD_HANDSHAKE_FAIL = 0x2301;
        public static final int CTMS_DOWNLOAD_CHECKSUM_FAIL = 0x2302;
        public static final int CTMS_DOWNLOAD_REQUEST_FAIL = 0x2303;
        public static final int CTMS_DOWNLOAD_DATA_ERROR = 0x2304;
        public static final int CTMS_DOWNLOAD_RETRY_TIMES_OUT = 0x2305;
        public static final int CTMS_HTTP_FAIL = 0x2400;
        public static final int CTMS_HTTP_JSON_PARSE_ERROR = 0x2401;
        public static final int CTMS_HTTP_NETWORK_ERROR = 0x2402;
        public static final int CTMS_HTTP_HTTP_ERROR = 0x2403;
        public static final int CTMS_CONFIG_FAIL = 0x2500;
        public static final int CTMS_CONFIG_BASE_INIT_FAIL = 0x2501;
        public static final int CTMS_CONFIG_BASE_IS_NULL = 0x2502;
        public static final int CTMS_CONFIG_ENABLE_INIT_FAIL = 0x2503;
        public static final int CTMS_CONFIG_ENABLE_IS_NULL = 0x2504;
        public static final int CTMS_CONFIG_TRIGGER_INIT_FAIL = 0x2505;
        public static final int CTMS_CONFIG_TRIGGER_IS_NULL = 0x2506;
        public static final int CTMS_CONFIG_ACTIVE_INIT_FAIL = 0x2507;
        public static final int CTMS_CONFIG_ACTIVE_IS_NULL = 0x2508;
        public static final int CTMS_CONFIG_CHANGE_PASSWORD_SERIAL_NUMBER_LENGTH_ERROR = 0x2509;
        public static final int CTMS_CONFIG_CHANGE_PASSWORD_GET_SETTING_ERROR = 0x250A;
        public static final int CTMS_CONFIG_CHANGE_PASSWORD_REPORT_FAILED = 0x250B;
        public static final int CTMS_POLLING_FAIL = 0x2600;
        public static final int CTMS_POLLING_GET_INFO_ERROR = 0x2601;
        public static final int CTMS_POLLING_TIME_NOT_CORRECT = 0x2602;
        public static final int CTMS_UPDATE_FAIL = 0x2700;
        public static final int CTMS_UPDATE_UPDATELIST_IS_EMPTY = 0x2701;
        public static final int CTMS_DIAGNOSTIC_UPLOAD_FAIL = 0x2800;
        public static final int CTMS_UPDATE_PARAMETER_INFO_FILE_NOT_EXIST = 0x2702;
        public static final int CTMS_UPDATE_PARAMETER_APP_NAME_FORMAT_ERROR = 0x2703;
        public static final int CTMS_UPDATE_PARAMETER_FILE_NAME_FORMAT_ERROR = 0x2704;
        public static final int CTMS_UPDATE_PARAMETER_FILE_VERSION_FORMAT_ERROR = 0x2705;
        public static final int CTMS_UPDATE_PARAMETER_MERBER_TAG_FORMAT_ERROR = 0x2706;
        public static final int CTMS_UPDATE_PARAMETER_MERBER_CONTENT_FORMAT_ERROR = 0x2707;
        public static final int CTMS_UPDATE_PARAMETER_APPLICATION_NOT_INSTALLED = 0x2708;
        public static final int CTMS_UPDATE_PARAMETER_SAVE_TO_KERNEL_FAIL = 0x2709;
        public static final int CTMS_UPDATE_PARAMETER_GET_SAVEPATH_ERROR = 0x270A;
        public static final int CTMS_UPDATE_PARAMETER_GET_PRMPATH_ERROR = 0x270B;


    }

    public class Status{
        public static final int CTMS_POST_TO_SERVER_SUCCESS = 0;
        public static final int CTMS_RUNNING_STATUS_LOGIN_START = 1;
        public static final int CTMS_RUNNING_STATUS_LOGIN_END = 2;
        public static final int CTMS_RUNNING_STATUS_GETINFO_START = 4;
        public static final int CTMS_RUNNING_STATUS_GETINFO_END = 5;
        public static final int CTMS_RUNNING_STATUS_DOWNLOAD_HANDSHAKE_START = 7;
        public static final int CTMS_RUNNING_STATUS_DOWNLOAD_HANDSHAKE_END = 8;
        public static final int CTMS_RUNNING_STATUS_DOWNLOAD_START = 10;
        public static final int CTMS_RUNNING_STATUS_DOWNLOAD_END = 11;
        public static final int CTMS_RUNNING_STATUS_INSTALL_START = 13;
        public static final int CTMS_RUNNING_STATUS_INSTALL_END = 14;
        public static final int CTMS_RUNNING_STATUS_INSTALL_FAIL = 15;
        public static final int CTMS_RUNNING_STATUS_UPLOAD_HANDSHAKE_START = 16;
        public static final int CTMS_RUNNING_STATUS_UPLOAD_HANDSHAKE_END = 17;
        public static final int CTMS_RUNNING_STATUS_UPLOAD_HANDSHAKE_FAIL = 18;
        public static final int CTMS_RUNNING_STATUS_UPLOAD_START = 19;
        public static final int CTMS_RUNNING_STATUS_UPLOAD_END = 20;
        public static final int CTMS_RUNNING_STATUS_UPLOAD_FAIL = 21;
        public static final int CTMS_RUNNING_STATUS_LOGOUT_START = 22;
        public static final int CTMS_RUNNING_STATUS_LOGOUT_END = 23;
        public static final int CTMS_RUNNING_STATUS_LOGOUT_FAIL = 24;
    }

    public class FileType{
        public static final int FILE_TYPE_UPDATELIST = 0xFF;
        public static final int FILE_TYPE_OTA = 0xFE;
        public static final int FILE_TYPE_APK = 0xFD;
        public static final int FILE_TYPE_SMF = 0xFC;
        public static final int FILE_TYPE_CMF = 0xFB;
        public static final int FILE_TYPE_SBL = 0xFA;
        public static final int FILE_TYPE_SME = 0xF9;
        public static final int FILE_TYPE_AME = 0xF8;
        //    public static final int UPDATE_DATA_TYPE_FW = 0x00;
//    public static final int UPDATE_DATA_TYPE_AP = 0x01;
//    public static final int UPDATE_DATA_TYPE_FILE = 0x02;
//    public static final int UPDATE_DATA_TYPE_SHARE_LIB = 0x04;
//    public static final int UPDATE_DATA_TYPE_AP_LIB = 0x05;
        public static final int FILE_TYPE_PRM = 0x07;

        public static final int FILE_STYPE_EMV = 37;
        public static final int FILE_STYPE_EMVCL = 38;
    }

    public static final int COMMUNICATION_MODE_TCP = 1;
    public static final int COMMUNICATION_MODE_TLS = 2;

    public static final int UPDATE_STATUS_UNEXECUTE = 1;
    public static final int UPDATE_STATUS_EXECUTING = 2;
    public static final int UPDATE_STATUS_EXECUTED = 3;

    public static final int UPDATE_RESULT_NO_RESULT = 0;
    public static final int UPDATE_RESULT_SUCCESS = 1;
    public static final int UPDATE_RESULT_FAIL = 2;

    public static final int ENABLE_CONFIG_CTMS_NOT_ENABLE_VALUE = 0;
    public static final int ENABLE_CONFIG_CTMS_ENABLE_VALUE = 1;
    public static final int ENABLE_CONFIG_BOOT_CONNECT_NOT_ENABLE_VALUE = 0;
    public static final int ENABLE_CONFIG_BOOT_CONNECT_ENABLE_VALUE = 1;
    public static final int TRIGGER_CONFIG_POLLING_NOT_ENABLE_VALUE = 0;
    public static final int TRIGGER_CONFIG_POLLING_ENABLE_VALUE = 1;

    public static final int BATTERY_LOW_LEVEL_OTA = 30;
    public static final int BATTERY_LOW_LEVEL_APK = 10;

    public static int SERVICE_ACTION_TYPE_START_POLLING_PROCESS = 0;
    public static int SERVICE_ACTION_TYPE_START_INSTALL_PROCESS = 1;
    public static String ACTION_TYPE_KEY = "actionType";
    public static String SHARE_KEY_HAS_REBOOT_INSTALL = "hasRebootInstall";
    public static String SBL_INSTALLED_BUT_NOT_REBOOT = "sblVersion";
    public static String SBL_DEFAULT_VERSION = "000000";
}
