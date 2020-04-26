package k.c.module.getinfo.module;


import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import k.c.common.lib.CommonLib;
import k.c.common.lib.Util.AndroidUtil;
import k.c.common.lib.Util.FileUtil;
import k.c.common.lib.Util.GetModuleVersion;
import k.c.common.lib.logTool.LogTool;
import k.c.module.commonbusiness.common.CommonConst;
import k.c.module.getinfo.model.getSystemInfo.ModuleInfo;
import k.c.module.getinfo.model.getinfo.GetInfoRequest;


public class GetSystemInfoManager {


    public List<GetInfoRequest.INFO.System> getSystemInfo() {


        List<GetInfoRequest.INFO.System> mGetInfoDataInfo = new ArrayList<>();

        parseAppListFromSystem(mGetInfoDataInfo);
        parseSmfVersionFromSystem(mGetInfoDataInfo);
        parseOTAVersionFromSystem(mGetInfoDataInfo);
        parseCradleVersionFromSystem(mGetInfoDataInfo);
        parseSblVersionFromSystem(mGetInfoDataInfo);
        parseSmeAndAmeVersionFromSystem(mGetInfoDataInfo);

        return mGetInfoDataInfo;
    }

    public List<GetInfoRequest.INFO.System> parseAppListFromSystem(List<GetInfoRequest.INFO.System> getSystemInfoItemList) {

        final PackageManager packageManager = CommonLib.getAppContext().getPackageManager();
        List<ApplicationInfo> applicationInfoList = AndroidUtil.getApplicationInfoList();
        if (applicationInfoList != null) {

            for (ApplicationInfo appInfo : applicationInfoList) {

                if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                    // System application so do nothing
                } else {

                    // Installed by user
//                LogTool.i("[Installed by user] Name : " + appInfo.loadLabel(packageManager));
//                    LogTool.i("CTMS", "Name: " + appInfo.loadLabel(packageManager));
                    try {
                        PackageInfo pinfo = packageManager.getPackageInfo(appInfo.packageName, 0);
//                        LogTool.v("User Version Name string:" + pinfo.versionName + "\r\n" +
//                                "User Version  package packageName :" + appInfo.packageName);
                        LogTool.d( "User Version Name string:" + pinfo.versionName);
                        LogTool.d("User Version  package packageName :" + appInfo.packageName);

                        getSystemInfoItemList.add(new GetInfoRequest().new INFO().new System(0xFD,
                                0,
                                (String) appInfo.loadLabel(packageManager),
                                pinfo.versionName,
                                appInfo.packageName,
                                Long.toString(pinfo.firstInstallTime),
                                Long.toString(pinfo.lastUpdateTime)));

                    } catch (PackageManager.NameNotFoundException p) {
                        Log.e("CTMS", "NO NAME " + p.toString());
//                        LogTool.e("Name Not Found : " + p.toString());
                    }

                }
            }
        }

        return getSystemInfoItemList;
    }

    public static List<GetInfoRequest.INFO.System> parseSmfVersionFromSystem(List<GetInfoRequest.INFO.System> getSystemInfoItemList) {

        String strSmfVersion = AndroidUtil.getSmfVersion("0000000000000000");

        if (strSmfVersion != null) {
            LogTool.d( "Get SMF versionName success and versionName is : " + strSmfVersion);
            getSystemInfoItemList.add(new GetInfoRequest().new INFO().new System(0xFC,
                    0,
                    "SMF",
                    strSmfVersion,
                    "",
                    Long.toString(0),
                    Long.toString(0)));

        }

        return getSystemInfoItemList;
    }


    public  List<GetInfoRequest.INFO.System> parseOTAVersionFromSystem(List<GetInfoRequest.INFO.System> getSystemInfoItemList) {

        String strOtaVersion = Build.DISPLAY.substring(6, 12);

        if (strOtaVersion != null) {
            LogTool.d( "Get OTA versionName success and versionName is : " + strOtaVersion);
            getSystemInfoItemList.add(new GetInfoRequest().new INFO().new System(0xFE,
                    0,
                    "OTA",
                    strOtaVersion,
                    "",
                    Long.toString(0),
                    Long.toString(0)));

        }

        return getSystemInfoItemList;
    }


    public  List<GetInfoRequest.INFO.System> parseCradleVersionFromSystem(List<GetInfoRequest.INFO.System> getSystemInfoItemList) {

        String strCmfVersion = AndroidUtil.getCmfVersion();
        if (strCmfVersion != null) {
            LogTool.d( "Get CMF versionName success and versionName is : " + strCmfVersion);
            getSystemInfoItemList.add(new GetInfoRequest().new INFO().new System(0xFB,
                    0,
                    "CMF",
                    strCmfVersion,
                    "",
                    Long.toString(0),
                    Long.toString(0)));

        }

        return getSystemInfoItemList;
    }

    public List<GetInfoRequest.INFO.System> parseSblVersionFromSystem(List<GetInfoRequest.INFO.System> getSystemInfoItemList) {

        String strSblVersion = AndroidUtil.getSblVersion();
        if (strSblVersion != null) {
            LogTool.d( "Get SBL versionName success and versionName is : " + strSblVersion);
            getSystemInfoItemList.add(new GetInfoRequest().new INFO().new System(0xFA,
                    0,
                    "SBL",
                    strSblVersion,
                    "",
                    Long.toString(0),
                    Long.toString(0)));

        }

        return getSystemInfoItemList;
    }

    public static List<GetInfoRequest.INFO.System> parseSmeAndAmeVersionFromSystem(List<GetInfoRequest.INFO.System> getSystemInfoItemList) {

        if (FileUtil.isDirectoryExist(CommonConst.FileConst.PATH_SME_AME)) {
            ModuleInfo mMoudleInfoData = new Gson().fromJson(FileUtil.readFile(CommonConst.FileConst.PATH_SME_AME), ModuleInfo.class);
            if (mMoudleInfoData.getSME() != null) {
                List<ModuleInfo.SME> mSmeList = mMoudleInfoData.getSME();
                for (int index = 0; index < mSmeList.size(); index++) {
                    ModuleInfo.SME mSmeData = mSmeList.get(index);
                    String strName = mSmeData.getName();
                    int iStype = mSmeData.getSType();
                    String strMoudleVer = GetModuleVersion.getModuleVersion(strName, iStype);
                    getSystemInfoItemList.add(new GetInfoRequest().new INFO().new System(0xF9,
                            0,
                            strName,
                            strMoudleVer,
                            "",
                            Long.toString(0),
                            Long.toString(0)));
                }
            }

            if (mMoudleInfoData.getAME() != null) {
                List<ModuleInfo.AME> mAmeList = mMoudleInfoData.getAME();
                for (int index = 0; index < mAmeList.size(); index++) {
                    ModuleInfo.AME mAmeData = mAmeList.get(index);
                    String strName = mAmeData.getName();
                    int iStype = mAmeData.getSType();
                    String strMoudleVer = GetModuleVersion.getModuleVersion(strName, iStype);
                    getSystemInfoItemList.add(new GetInfoRequest().new INFO().new System(0xF8,
                            0,
                            strName,
                            strMoudleVer,
                            "",
                            Long.toString(0),
                            Long.toString(0)));
                }
            }
        }

        return getSystemInfoItemList;
    }



}
