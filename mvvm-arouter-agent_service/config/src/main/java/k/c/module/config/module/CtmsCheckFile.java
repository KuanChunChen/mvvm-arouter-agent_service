//package castles.ctms.module.config.module;
//
//import com.google.gson.Gson;
//
//import castles.ctms.common.lib.AppSingle;
//import castles.ctms.common.lib.Util.FileUtil;
//import castles.ctms.module.config.constants.ConfigDefine;
//import castles.ctms.module.config.model.CtmsActive;
//import castles.ctms.module.config.model.CtmsConfig;
//import castles.ctms.module.config.model.CtmsEnable;
//import castles.ctms.module.config.model.CtmsTrigger;
//
//import static castles.ctms.module.config.constants.ConfigDefine.ACTIVE_CONFIG_NAME;
//import static castles.ctms.module.config.constants.ConfigDefine.COPY_PATH_ACTIVE_CONFIG_NAME;
//import static castles.ctms.module.config.constants.ConfigDefine.COPY_PATH_CTMS_CONFIG_NAME;
//import static castles.ctms.module.config.constants.ConfigDefine.COPY_PATH_ENABLE_CONFIG_NAME;
//import static castles.ctms.module.config.constants.ConfigDefine.COPY_PATH_TRIGGER_CONFIG_NAME;
//import static castles.ctms.module.config.constants.ConfigDefine.CTMS_CONFIG_NAME;
//import static castles.ctms.module.config.constants.ConfigDefine.ENABLE_CONFIG_NAME;
//import static castles.ctms.module.config.constants.ConfigDefine.TRIGGER_CONFIG_NAME;
//
//public class CtmsCheckFile {
//
//    public static void CtmsFileProtect(String strFileName){
//        String strDefaultPath = AppSingle.getInstance().getDefalutPath();
//        String strRead;
//        switch (strFileName){
//            case CTMS_CONFIG_NAME:
//                //原檔案存在 內容為空 且 複製檔案存在 內容不為空
//                if (FileUtil.isFileEmpty(strDefaultPath + CTMS_CONFIG_NAME) &&
//                !FileUtil.isFileEmpty(COPY_PATH_CTMS_CONFIG_NAME)) {
//                    strRead = FileUtil.readFile(COPY_PATH_CTMS_CONFIG_NAME);
//                    FileUtil.writeFile(strDefaultPath + CTMS_CONFIG_NAME, strRead);
//                    //原檔案存在 內容為空 且 複製檔案存在 內容為空
//                } else if (FileUtil.isFileEmpty(strDefaultPath + CTMS_CONFIG_NAME) &&
//                        FileUtil.isFileEmpty(COPY_PATH_CTMS_CONFIG_NAME)) {
//
//                    CtmsConfig mCtmsConfig = new Gson().fromJson(ConfigDefine.JSON_CTMS_CONFIG_REWRITE_A, CtmsConfig.class);
//                    String strConfig = new Gson().toJson(mCtmsConfig);
//                    FileUtil.writeFile(strDefaultPath + CTMS_CONFIG_NAME, strConfig);
//                    //兩個檔案都不存在
//                } else if(!FileUtil.isDirectoryExist(strDefaultPath + CTMS_CONFIG_NAME) &&
//                        !FileUtil.isDirectoryExist(COPY_PATH_CTMS_CONFIG_NAME)){
//                    ConfigManager.makeDefaultCtmsConfig();
//                }
//                break;
//            case ACTIVE_CONFIG_NAME:
//                //原檔案存在 內容為空 且 複製檔案存在 內容不為空
//                if (FileUtil.isFileEmpty(strDefaultPath + ACTIVE_CONFIG_NAME) &&
//                        !FileUtil.isFileEmpty(COPY_PATH_ACTIVE_CONFIG_NAME)) {
//                    strRead = FileUtil.readFile(COPY_PATH_ACTIVE_CONFIG_NAME);
//                    FileUtil.writeFile(strDefaultPath + ACTIVE_CONFIG_NAME, strRead);
//                    //原檔案存在 內容為空 且 複製檔案存在 內容為空
//                } else if (FileUtil.isFileEmpty(strDefaultPath + ACTIVE_CONFIG_NAME) &&
//                        FileUtil.isFileEmpty(COPY_PATH_ACTIVE_CONFIG_NAME)) {
//
//                    CtmsActive mCtmsActive = new Gson().fromJson(ConfigDefine.JSON_ACTIVE_CONFIG_REWRITE, CtmsActive.class);
//                    String strCtmsActive = new Gson().toJson(mCtmsActive);
//                    FileUtil.writeFile(strDefaultPath + ACTIVE_CONFIG_NAME, strCtmsActive);
//                    //兩個檔案都不存在
//                } else if(!FileUtil.isDirectoryExist(strDefaultPath + ACTIVE_CONFIG_NAME) &&
//                        !FileUtil.isDirectoryExist(COPY_PATH_ACTIVE_CONFIG_NAME)){
//                    ConfigManager.makeDefaultCtmsActive();
//                }
//                break;
//            case TRIGGER_CONFIG_NAME:
//                if (FileUtil.isFileEmpty(strDefaultPath + TRIGGER_CONFIG_NAME) &&
//                        !FileUtil.isFileEmpty(COPY_PATH_TRIGGER_CONFIG_NAME)) {
//                    strRead = FileUtil.readFile(COPY_PATH_TRIGGER_CONFIG_NAME);
//                    FileUtil.writeFile(strDefaultPath + TRIGGER_CONFIG_NAME, strRead);
//                    //原檔案存在 內容為空 且 複製檔案存在 內容為空
//                } else if (FileUtil.isFileEmpty(strDefaultPath + TRIGGER_CONFIG_NAME) &&
//                        FileUtil.isFileEmpty(COPY_PATH_TRIGGER_CONFIG_NAME)) {
//
//                    CtmsTrigger mCtmsTrigger = new Gson().fromJson(ConfigDefine.JSON_TRIGGER_CONFIG_REWRITE, CtmsTrigger.class);
//                    String strCtmsTrigger = new Gson().toJson(mCtmsTrigger);
//                    FileUtil.writeFile(strDefaultPath + TRIGGER_CONFIG_NAME, strCtmsTrigger);
//                    //兩個檔案都不存在
//                } else if(!FileUtil.isDirectoryExist(strDefaultPath + TRIGGER_CONFIG_NAME) &&
//                        !FileUtil.isDirectoryExist(COPY_PATH_TRIGGER_CONFIG_NAME)){
//                    ConfigManager.makeDefaultCtmsTrigger();
//                }
//                break;
//            case ENABLE_CONFIG_NAME:
//
//                if (FileUtil.isFileEmpty(strDefaultPath + ENABLE_CONFIG_NAME) &&
//                        !FileUtil.isFileEmpty(ENABLE_CONFIG_NAME)) {
//                    strRead = FileUtil.readFile(COPY_PATH_ENABLE_CONFIG_NAME);
//                    FileUtil.writeFile(strDefaultPath + ENABLE_CONFIG_NAME, strRead);
//                    //原檔案存在 內容為空 且 複製檔案存在 內容為空
//                } else if (FileUtil.isFileEmpty(strDefaultPath + ENABLE_CONFIG_NAME) &&
//                        FileUtil.isFileEmpty(COPY_PATH_ENABLE_CONFIG_NAME)) {
//
//                    CtmsEnable mCtmsEnable = new Gson().fromJson(ConfigDefine.JSON_ENABLE_CONFIG_REWRITE, CtmsEnable.class);
//                    String strCtmsEnable = new Gson().toJson(mCtmsEnable);
//                    FileUtil.writeFile(strDefaultPath + ENABLE_CONFIG_NAME, strCtmsEnable);
//                    //兩個檔案都不存在
//                } else if(!FileUtil.isDirectoryExist(strDefaultPath + ENABLE_CONFIG_NAME) &&
//                        !FileUtil.isDirectoryExist(COPY_PATH_ENABLE_CONFIG_NAME)){
//                    ConfigManager.makeDefaultCtmsEnable();
//                }
//                break;
//
//        }
//    }
//}
