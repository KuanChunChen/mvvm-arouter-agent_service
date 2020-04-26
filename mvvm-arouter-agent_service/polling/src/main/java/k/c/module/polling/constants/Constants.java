package k.c.module.polling.constants;

public class Constants {
    public static final String MODULE_NAME = "Polling";

    public static final String CTMS_BOOT_CONNECT_PATH = "/bootconnect.json";
    public static final String CTMS_WAITFORUPLOAD_PATH = "/WaitForUpload.txt";
    public static final String CTMS_CONFIG_TRIGGER_JSON_FILE = "/trigger.json";
    public static final String CTMS_CONFIG_ACTIVE_JSON_FILE = "/active.json";
    public static final String CTMS_CONFIG_CONFIG_JSON_FILE = "/configuration.json";
    public static final String UPDATE_INDEX_FILE_PATH = "Index.txt";
    public static final int CTMS_RETRY_COUNT = 3;
    public static final int CTMS_RETRY_DELAY_SECOND = 900;

    public static final String CTMS_UPLOAD_FOLDER = "/CTMS/Upload";
    public static final String CTMS_SETTING_FILE = "/ctms_setting.json";

    public static final String UPDATE_LIST_FILE_NEW_PATH = "/Update_List_new";
    public static final String UPDATE_LIST_FILE_PATH = "/Update_List";
    public static final String UPDATE_LIST_FILE_FOR_CONTINUE = "/Update_List_continue";
    public static final String UPDATE_LIST_FILE_FOR_ENSURE_CRADLE_VER = "/Update_List_ForCradleVer";
    public static final String UPDATE_OTA_FILE_RECORD = "/OTAindex";
    public static final String UPDATE_OTA_FILE_RECORD_TMP = "/OTAindex_tmp";


    public static final int CTMS_FEATURE_DISABLE = 0;
    public static final int CTMS_FEATURE_ENABLE = 1;

    public static final String JSON_CTMS_BOOT_CONNECT_REWRITE = "{\n" +
            " \"Connect\":0\n" +
            "}";

    //active
    public static final int ACTIVE_DISABLE = 0;
    public static final int ACTIVE_ENABLE = 1;
    public static final int ACTIVE_AP_CONTROL = 2;
    public static final int ACTIVE_REBOOT = 3;

    //type
    public static final int CTMS_TYPE_FW = 0x00;
    public static final int CTMS_TYPE_FILE = 0x02;

    public static final int CTMS_TYPE_APP = 0x01;
    public static final int CTMS_TYPE_APP_LIBRARY = 0x05;
    public static final int CTMS_TYPE_APP_FILE = 0x02;
    public static final int CTMS_TYPE_SHARE_LIBRARY = 0x04;
    public static final int CTMS_TYPE_SHARE_FILE = 0x02;
    public static final int CTMS_TYPE_BIOS = 0x00;
    public static final int CTMS_TYPE_SULD = 0x00;
    public static final int CTMS_TYPE_KERNEL = 0x00;
    public static final int CTMS_TYPE_ROOTFS = 0x00;
    public static final int CTMS_TYPE_FONTS = 0x00;
    public static final int CTMS_TYPE_CRADLE = 0x00;
    public static final int CTMS_TYPE_SYSTEM_SETTING = 0x00;
    public static final int CTMS_TYPE_VOLATILE_PATCH = 0x08;
    public static final int CTMS_TYPE_PLUGIN = 0x09;
    public static final int CTMS_TYPE_EXTRA_LIBRARY = 0x20;
    public static final int CTMS_TYPE_PRM = 0x07;
    public static final int CTMS_TYPE_INFO_UPDATE_LIST = 0xFF;
    public static final int CTMS_TYPE_OTA = 0xFE;
    public static final int CTMS_TYPE_APK = 0xFD;
    public static final int CTMS_TYPE_SMF = 0xFC;
    public static final int CTMS_TYPE_CMF = 0xFB;


    //stype

    public static final int CTMS_STYPE_APP = 0x00;
    public static final int CTMS_STYPE_APP_LIBRARY = 0x24;
    public static final int CTMS_STYPE_APP_FILE = 0x02;
    public static final int CTMS_STYPE_SHARE_LIBRARY = 0x20;
    public static final int CTMS_STYPE_SHARE_FILE = 0x01;
    public static final int CTMS_STYPE_BIOS = 0x1C;
    public static final int CTMS_STYPE_SULD = 0x14;
    public static final int CTMS_STYPE_KERNEL = 0x17;
    public static final int CTMS_STYPE_ROOTFS = 0x12;
    public static final int CTMS_STYPE_FONTS = 0x11;
    public static final int CTMS_STYPE_CRADLE = 0x18;
    public static final int CTMS_STYPE_SYSTEM_SETTING = 0x23;
    public static final int CTMS_STYPE_VOLATILE_PATCH = 0x26;
    public static final int CTMS_STYPE_PLUGIN = 0x27;
    public static final int CTMS_STYPE_EXTRA_LIBRARY = 0x28;
    public static final int CTMS_STYPE_PRM = 0x00;
    public static final int CTMS_STYPE_INFO_UPDATE_LIST = 0xFF;
    public static final int CTMS_STYPE_OTA = 0xFE;
    public static final int CTMS_STYPE_APK = 0xFD;
    public static final int CTMS_STYPE_SMF = 0xFC;
    public static final int CTMS_STYPE_CMF = 0xFB;

    public static final String FILE_TYPE_NAME_APP = "Application";
    public static final String FILE_TYPE_NAME_APP_FILE = "App File";
    public static final String FILE_TYPE_NAME_APP_LIB = "App Library";
    public static final String FILE_TYPE_NAME_SHARE_FILE = "Share File";
    public static final String FILE_TYPE_NAME_SHARE_LIB = "Share Library";
    public static final String FILE_TYPE_NAME_BIOS = "BIOS";
    public static final String FILE_TYPE_NAME_SULD = "SULD";
    public static final String FILE_TYPE_NAME_KERNEL = "Kernel";
    public static final String FILE_TYPE_NAME_ROOTFS = "RootFS";
    public static final String FILE_TYPE_NAME_FONTS = "Fonts";
    public static final String FILE_TYPE_NAME_CRADLE = "Cradle";
    public static final String FILE_TYPE_NAME_SYSTEM_SETTING = "System Setting";
    public static final String FILE_TYPE_NAME_PRM = "App Parameter";
    public static final String FILE_TYPE_NAME_VOLATILE_PATCH = "Volatile Patch";
    public static final String FILE_TYPE_NAME_PLUGIN = "Plugin";
    public static final String FILE_TYPE_NAME_EXTRA_LIB = "Extra Library";
    public static final String FILE_TYPE_NAME_OTA = "Android System";
    public static final String FILE_TYPE_NAME_APK = "Android App";
    public static final String FILE_TYPE_NAME_SMF = "SMF";
    public static final String FILE_TYPE_NAME_UNEXPECTED = "Unexpected";
    public static final String FILE_TYPE_NAME_UNEXPECTED_FW = "Unexpected FW";
    public static final String FILE_TYPE_NAME_CMF = "CMF";

    public static final int CTMS_CONFIG_TRIGGER_TYPE_IMMEDIATELY_DISABLE = 0;
    public static final int CTMS_CONFIG_TRIGGER_TYPE_IMMEDIATELY_ENABLE = 1;

    public static final int CTMS_CONFIG_ACTIVE_TYPN_IMMEDIATELY_DISABLE = 0;
    public static final int CTMS_CONFIG_ACTIVE_TYPE_IMMEDIATELY_ENABLE = 1;


    public static final int PROCESS_NORMAL_MODE_ENTIRE = 0x01;
    public static final int PROCESS_NORMAL_MODE_CHECK_AVAILIBLE_UPDATE = 0x02;
    public static final int PROCESS_NORMAL_MODE_DATA_EXCHANGE_ONLY = 0x03;
    public static final int PROCESS_NORMAL_MODE_UPLOAD_ONLY = 0x04;
    public static final int PROCESS_NORMAL_MODE_DOWNLOAD_ONLY = 0x05;
    public static final int PROCESS_NORMAL_MODE_UPDATE = 0x06;
    public static final int PROCESS_NORMAL_MODE_ACTIVE = 0x07;
    public static final int PROCESS_NORMAL_MODE_REBOOT = 0x08;
    public static final int PROCESS_NORMAL_MODE_CONTINUE = 0x09;

    public static final int PROCESS_NORMAL_STEP_INIT = 0x01;
    public static final int PROCESS_NORMAL_STEP_CONNECT = 0x02;
    public static final int PROCESS_NORMAL_STEP_LOGIN = 0x03;
    public static final int PROCESS_NORMAL_STEP_GET_INFO = 0x04;
    public static final int PROCESS_NORMAL_STEP_DATA_EXCHANGE = 0x05;
    public static final int PROCESS_NORMAL_STEP_UPLOAD = 0x06;
    public static final int RPOCESS_NORMAL_STEP_DOWNLOAD_HS = 0x07;
    public static final int PROCESS_NORMAL_STEP_DOWNLOAD = 0x08;
    public static final int PROCESS_NORMAL_STEP_LOGOUT = 0x09;
    public static final int PROCESS_NORMAL_STEP_DISCONNECT = 0x0A;
    public static final int PROCESS_NORMAL_STEP_UPDATE = 0x0B;
    public static final int PROCESS_NORMAL_STEP_FINISH = 0x0C;
    public static final int PROCESS_NORMAL_STEP_PARSE = 0x0D;
    public static final int PROCESS_NORMAL_STEP_ACTIVE = 0x0E;
    public static final int PROCESS_NORMAL_STEP_CONFIRM = 0x0F;
    public static final int PROCESS_NORMAL_STEP_GET_BATTERY = 0x10;


    public static final int UPDATE_LIST_INFO_STATUS_NOT_UPDATE = 0x01;
    public static final int UPDATE_LIST_INFO_STATUS_UPDATED = 0x02;
    public static final int UPDATE_LIST_INFO_STATUS_ENFORCE_UPDATE = 0x03;
    public static final int UPDATE_LIST_INFO_STATUS_DOWNLOADED = 0x04;
    public static final int UPDATE_LIST_INFO_STATUS_UPDATE_FAIL = 0x05;
    public static final int UPDATE_LIST_INFO_STATUS_AP_RUNNING = 0x06;


    //
    public static final int CTMS_ACTIVE_UNREACHED_TIME = 0x00;
    public static final int CTMS_ACTIVE_EXECUTE = 0x01;
    public static final int CTMS_ACTIVE_ZEROTIME = 0x02;

    public static final int TRIGGER_DISABLE = 0;
    public static final int TRIGGER_ENABLE = 1;
    public static final int TRIGGER_AUTO = 2;


    public static final int ACTION_UPDATE_NOW = 1;
    public static final int ACTION_AP_CONTROL = 2;
    public static final int ACTION_AP_TEST = 3;

    public static final int MAIN_PROCESS_STATUS_LOGIN_FAIL = 0;
    public static final int MAIN_PROCESS_STATUS_GET_INFO_FAIL = 1;
    public static final int MAIN_PROCESS_STATUS_DOWNLOAD_UPDATE_LIST_FAIL = 2;
    public static final int MAIN_PROCESS_STATUS_EXECUTE_UPDATE_LIST_FAIL = 3;
    public static final int MAIN_PROCESS_STATUS_MAIN_PROCESS_FINISH = 4;

    public static final long GETINFO_TIMEOUT = 15 * 60 * 1000;
}
