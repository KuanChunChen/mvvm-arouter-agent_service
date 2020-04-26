package k.c.common.lib.Util;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import CTOS.CtSystem;
import CTOS.CtSystemException;
import k.c.common.lib.CommonLib;
import k.c.common.lib.logTool.LogTool;

public class AndroidUtil {

    private static String strCMFVersion = null;


    public static PackageInfo getVersionName(Context context) {
        return getPackageInfo(context, context.getPackageName());
    }

    private static PackageInfo getPackageInfo(Context context, String packageName) {
        PackageInfo pInfo = null;

        try {
            PackageManager pManager = context.getPackageManager();
            pInfo = pManager.getPackageInfo(packageName,
                    PackageManager.GET_CONFIGURATIONS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pInfo;
    }

    public static boolean isApRunning(String packageName){
        boolean isTaskRunning = false;
        boolean isServiceRunning = false;
        ActivityManager am = (ActivityManager) CommonLib.getAppContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (packageName.equals(info.baseActivity.getPackageName())){
                isTaskRunning = true;
            }
        }

        try {
            ApplicationInfo applicationInfo = CommonLib.getAppContext().getPackageManager().getApplicationInfo(packageName, 0);
            if (applicationInfo != null) {
                List<ActivityManager.RunningServiceInfo> runningserviceinfos = am.getRunningServices(200);
                if (runningserviceinfos.size() > 0) {
                    for (ActivityManager.RunningServiceInfo appprocess : runningserviceinfos){
                        if (applicationInfo.uid == appprocess.uid) {
                            isServiceRunning = true;
                        }
                    }
                }
            }
        }catch (Exception e){

        }
        LogTool.d(" = %s  = %s ",isTaskRunning, isServiceRunning);
        return isTaskRunning || isServiceRunning;
    }

    public static List<ApplicationInfo> getApplicationInfoList(){
        final PackageManager packageManager = CommonLib.getAppContext().getPackageManager();
        List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES);
        return installedApplications;

    }

    public static List<PackageInfo> getAppInfos(Context context) {
        List<PackageInfo> appInfos = new ArrayList<>();
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        LogTool.d("getInstalledPackages(PackageManager.COMPONENT_ENABLED_STATE_DEFAULT) cnt = %s ;" , packages.size() );
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);

            //no system app
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                appInfos.add(packageInfo);
            }

            int sysflg = packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM;
            if ( ( sysflg == 1 && packageInfo.applicationInfo.sourceDir != null && packageInfo.applicationInfo.sourceDir.startsWith("/data/app/") )
                    || ( sysflg == 1 && packageInfo.applicationInfo.sourceDir != null && packageInfo.applicationInfo.sourceDir.startsWith("/partner/priv-app/") )) {
                appInfos.add(packageInfo);
            }

        }

        return appInfos;
    }

    public static String getSmfVersion(String oldSmfVersion) {

        return getBiosVersion(oldSmfVersion.substring(0, 4)) +
                getSuldVersion(oldSmfVersion.substring(4, 8)) +
                getKernelVersion(oldSmfVersion.substring(8, 12)) +
                getRootFsVersion(oldSmfVersion.substring(12, 16));
    }

    public static String getBiosVersion(String biosVR) {

        CtSystem CTOSSystem = new CtSystem();
        if (!biosVR.equals("0000")) {
            try {
                String[] strBios_VR = CTOSSystem.getModuleVersion(31).split("-");
                biosVR = strBios_VR[0].substring(2);
            } catch (CtSystemException e) {
                e.printStackTrace();
            }
        }
        return biosVR;
    }

    public static String getSuldVersion(String suldVR) {

        CtSystem CTOSSystem = new CtSystem();
        if (!suldVR.equals("0000")) {
            try {
                String[] strSuld_VR = CTOSSystem.getModuleVersion(0).split("-");
                suldVR = strSuld_VR[0].substring(2);
            } catch (CtSystemException e) {
                e.printStackTrace();
            }
        }
        return suldVR;
    }

    public static String getKernelVersion(String kernelVR) {

        CtSystem CTOSSystem = new CtSystem();
        if (!kernelVR.equals("0000")) {
            try {
                String[] strKernel_VR = CTOSSystem.getModuleVersion(3).split("-");
                kernelVR = strKernel_VR[0].substring(2);
            } catch (CtSystemException e) {
                e.printStackTrace();
            }
        }
        return kernelVR;
    }

    public static String getRootFsVersion(String rootFSVR) {

        CtSystem CTOSSystem = new CtSystem();
        if (!rootFSVR.equals("0000")) {
            try {
                String[] strRootFS_VR = CTOSSystem.getModuleVersion(30).split("-");
                rootFSVR = strRootFS_VR[0].substring(2);
            } catch (CtSystemException e) {
                e.printStackTrace();
            }
        }
        return rootFSVR;
    }


    public static String getCmfVersion() {
        String ResBios = null, ResSuld = null, ResKernel = null, ResRootFs = null, ResMainAP = null;
        String strCMFNew = null;
        Thread getCradleThread = new Thread(new Runnable() {
            public void run() {
                strCMFVersion = new CradleUtil(CommonLib.getAppContext()).getCradleVersion();
            }
        });


        try {
            getCradleThread.start();
            getCradleThread.join();
            Log.i("Test","[ClientThread] CMF Version : \r\n" + strCMFVersion);
            if (strCMFVersion == null) {
                strCMFNew = null;
            }else{
                String[] strArrayCradle = strCMFVersion.split("\\s+");
                for (String each: strArrayCradle){

                    if(each.contains("BIOS")){
                        ResBios = each.substring(7, 11);
                        Log.i("BIOS : ---", "" + ResBios);
                    }else if(each.contains("SULD")){
                        ResSuld = each.substring(7, 11);
                        Log.i("SULD : ---", "" + ResSuld);
                    }else if(each.contains("KERNEL")){
                        ResKernel  = each.substring(9, 13);
                        Log.i("KERNEL : ---", "" + ResKernel);
                    }else if(each.contains("ROOTFS")){
                        ResRootFs = each.substring(9, 13);
                        Log.i("ROOTFS : ---", "" + ResRootFs);
                    }else if(each.contains("MainAP")){
                        ResMainAP  = each.substring(9, 13);
                        Log.i("MainAP : ---", "" + ResMainAP);
                    }
                }
                strCMFNew = ResBios + ResSuld + ResKernel + ResRootFs + ResMainAP;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return strCMFNew;
    }

    public static String getSblVersion() {
        CtSystem CTOSSystem = new CtSystem();
        String ResSblVer = CTOSSystem.getSbl1Version() != null ? CTOSSystem.getSbl1Version() : null;
        return ResSblVer;
    }

    public static void launchAppByPackageName(Context context, String packageName){
        final PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if(intent == null){
            LogTool.d("The app can not launch");
            return;
        }
        context.startActivity(intent);
    }

    public static long getSDFreeSpace() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return availableBlocks * blockSize;
    }
}