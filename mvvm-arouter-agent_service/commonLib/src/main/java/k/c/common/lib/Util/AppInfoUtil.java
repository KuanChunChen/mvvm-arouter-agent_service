package k.c.common.lib.Util;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import k.c.common.lib.CommonLib;
import k.c.common.lib.logTool.LogTool;

public class AppInfoUtil {

    private final String Tag = AppInfoUtil.class.getName();

    public static String GetAppVersion( String strPackageName) {

        String strReturn;
        PackageInfo pkgInfoToGetVersion = null;
        try {
            pkgInfoToGetVersion = CommonLib.getAppContext().getPackageManager().getPackageInfo(strPackageName +
                    "", 0);
            int verCode = pkgInfoToGetVersion.versionCode;
            strReturn = pkgInfoToGetVersion.versionName;
            LogTool.d("listApp" + "：", String.valueOf(verCode));
            LogTool.d("listApp" + "：", strReturn);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            strReturn = "";
        }


        return strReturn;
    }

    public static boolean isNewVersion(String strNewestVer, String strCurrentVer) {

        boolean boReturn;
        String[] strArrNewestVer = strNewestVer.split("\\.");
        String[] strArrCurrentVer = strCurrentVer.split("\\.");
        int minLength = Math.min(strArrNewestVer.length, strArrCurrentVer.length);
        int iIndex = 0;
        int iDifferent = 0;

        while (iIndex < minLength &&
                (iDifferent = strArrNewestVer[iIndex].length() - strArrCurrentVer[iIndex].length()) == 0 &&
                (iDifferent = strArrNewestVer[iIndex].compareTo(strArrCurrentVer[iIndex])) == 0) {
            ++iIndex;
        }

        if (iDifferent > 0) {
            boReturn = true;
        } else {
            boReturn = false;
        }
        return boReturn;
    }

    public static int isEqualVersion(String strNewestVer, String strCurrentVer) {

        int iReturn = 0;
        String[] strArrNewestVer = strNewestVer.split("\\.");
        String[] strArrCurrentVer = strCurrentVer.split("\\.");
        int minLength = Math.min(strArrNewestVer.length, strArrCurrentVer.length);
        int iIndex = 0;
        int iDiff = 0;

        while (iIndex < minLength &&
                (iDiff = strArrNewestVer[iIndex].length() - strArrCurrentVer[iIndex].length()) == 0 &&
                (iDiff = strArrNewestVer[iIndex].compareTo(strArrCurrentVer[iIndex])) == 0) {
            ++iIndex;
        }

        iReturn = iDiff;
        return iReturn;
    }

}
