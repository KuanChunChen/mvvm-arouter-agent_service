package k.c.module.diagnostics.module.application.applications;

import android.app.ActivityManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import k.c.common.lib.CommonLib;
import k.c.module.diagnostics.model.application.AppInformation;
import k.c.module.diagnostics.model.application.RunningApplication;
import k.c.module.diagnostics.model.application.RunningService;

import static android.content.Context.ACTIVITY_SERVICE;


public class ApplicationManager {


    public static  List<AppInformation> getApplicationsList(){
        final PackageManager packageManager = CommonLib.getAppContext().getPackageManager();
        List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES);
        List<AppInformation> appInformationsList = new ArrayList<>();
        for (ApplicationInfo appInfoFromAndroid : installedApplications) {
            try {
                PackageInfo pInfo = packageManager.getPackageInfo(appInfoFromAndroid.packageName, 0);

                AppInformation appInformation = new AppInformation();
                appInformation.appPackageName = pInfo.packageName;
                appInformation.versionCode = pInfo.versionCode;
                appInformation.versionName = pInfo.versionName;
                appInformation.appInstallTime = pInfo.firstInstallTime;
                appInformation.appUpdateTime =  pInfo.lastUpdateTime;
                appInformation.appProcessName = appInfoFromAndroid.processName;
                appInformation.appDescription = appInfoFromAndroid.descriptionRes;

                if ((appInfoFromAndroid.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                    if (appInfoFromAndroid.loadLabel(packageManager).toString().equals("CTMS")) {

                        appInformationsList.add(0, appInformation);

                    }
                } else {
                    appInformationsList.add(appInformation);
                }
            } catch (PackageManager.NameNotFoundException name) {
                name.printStackTrace();
            }
        }
        return appInformationsList;
    }



    public static  List<RunningApplication> getRunningApplications(){
        final PackageManager packageManager = CommonLib.getAppContext().getPackageManager();
        List<RunningApplication> runningApplications = new ArrayList<>();
        // 查询所有已经安装的应用程序
        List<ApplicationInfo> listAppcations = packageManager.getInstalledApplications(PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES);
        Collections.sort(listAppcations,new ApplicationInfo.DisplayNameComparator(packageManager));// 排序

        // 保存所有正在运行的包名 以及它所在的进程信息
        Map<String, ActivityManager.RunningAppProcessInfo> pgkProcessAppMap = new HashMap<>();

        ActivityManager mActivityManager = (ActivityManager) CommonLib.getAppContext().getSystemService(ACTIVITY_SERVICE);
        // 通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
        List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager
                .getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcessList) {
            // 获得该packageName的 pid 和 processName

            int pid = appProcess.pid; // pid
            String processName = appProcess.processName;
//            Log.d("RunningApplications :", "processName: " + processName + "  pid: " + pid);



            for (ApplicationInfo app : listAppcations) {
                // 如果该包名存在 则构造一个RunningAppInfo对象
                try {
                    PackageInfo pInfo = packageManager.getPackageInfo(app.packageName, 0);


                    if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                        if (app.loadLabel(packageManager).toString().equals("CTMS") && processName.equals("com.example.casw2_d_link.ctms_app")) {
                            Log.d("RunningApplications :", "processName: " + processName + "  pid: " + pid);
                            RunningApplication runningAppInfos = new RunningApplication();
                            runningAppInfos.ApplicationName = processName;
                            runningAppInfos.ProcessID = pid;
                            runningApplications.add(runningAppInfos);

                        }
                    } else {
                        if (pInfo.packageName.equals(processName)) {
                            RunningApplication runningAppInfos = new RunningApplication();
                            runningAppInfos.ApplicationName = processName;
                            runningAppInfos.ProcessID = pid;
                            runningApplications.add(runningAppInfos);
                        }

                    }

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }


            }

        }

        return runningApplications;
    }





    public static List<RunningService> getRunningServiceInfo() {

        int defaultNum = 20;

        ActivityManager mActivityManager = (ActivityManager) CommonLib.getAppContext().getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runServiceList = mActivityManager
                .getRunningServices(defaultNum);

        List<RunningService> serviceInfoList = new ArrayList<>();


        for (ActivityManager.RunningServiceInfo runServiceInfo : runServiceList) {

            // 获得Service所在的进程的信息
            int pid = runServiceInfo.pid; // service所在的进程ID号
            String processName = runServiceInfo.process;
            // 该Service启动时的时间值
            long activeSince = runServiceInfo.activeSince;
            int crashCount = runServiceInfo.crashCount;
            long lastActivityTime = runServiceInfo.lastActivityTime;

            RunningService runService = new RunningService();
            runService.serviceName = processName;
            runService.processID = pid;
            runService.activeSince = activeSince;
            runService.crashCount = crashCount;
            runService.lastActivityTime = lastActivityTime;

            if (!runService.serviceName.startsWith("com.") && !runService.serviceName.startsWith("system")) {

                // 添加至集合中
                serviceInfoList.add(runService);
            }



        }
        return serviceInfoList;
    }
}
