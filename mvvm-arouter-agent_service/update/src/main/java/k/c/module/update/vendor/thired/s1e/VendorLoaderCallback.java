package k.c.module.update.vendor.thired.s1e;


import CTOS.CtLoader;
import CTOS.CtLoaderSecurityModuleInstallStatus;

import k.c.common.lib.logTool.LogTool;

public abstract class VendorLoaderCallback implements CtLoader.LoaderCallBack {
    private static final int d_CAP_UPDATE_FINISHED = 0x0064;
    private static final int d_CAP_RESET_REQUIRED = 0x0065;
    private static final int d_CAP_REBOOT_REQUIRED = 0x0066;
    private static final int d_FRD_RX_TIMEOUT = 0x9009;
    private static final int d_FRD_CONNECT_NOT_READY = 0x901D;

    public final static String RESULT_CREATE_SOCKET_FAILURE = "Create_Socket_Failure";
    public final static String RESULT_BIND_DEVICE_FAILURE = "Bind_To_Device_Failure";
    public final static String RESULT_CONNECT_FAILURE = "Connect_Failure";
    public final static String RESULT_SEND_FAILURE = "Send_Failure";
    public final static String RESULT_RECV_FAILURE = "Recv_Failure";
//    private UpdateData mUpdateData;
//    private String strAppName, strType, strAppVersion, strFilePath;
//    private int id, iIndex = 0;

    private VendorPackageS1E vendorPackageS1E;

//    private NotificationCompat.Builder BB = new NotificationCompat.Builder(CommonLib.getAppContext());
//    private NotificationManager MM = (NotificationManager) CommonLib.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);

    public VendorLoaderCallback(VendorPackageS1E vendorPackageS1E) {
        this.vendorPackageS1E = vendorPackageS1E;
//        this.mUpdateData = mUpdateData;
//        strAppName = mUpdateData.getNAME();
//        strType = mUpdateData.getTYPE();
//        strAppVersion = mUpdateData.getVR();
//        strFilePath = mUpdateData.getPATH();
    }

    @Override
    public void onUpdateUninstallStatus(int uninstallIsRunning, String uninstallInf, int uninstallRet) {
        LogTool.d("onUpdateUninstallStatus_Run:" + uninstallIsRunning + " ,Inf:" + uninstallInf + " ,Ret:" + uninstallRet);

        if (uninstallIsRunning == 0) {
            int is_success = 0;
            if (uninstallRet == 0) {
                is_success = 1;
                LogTool.d("Uninstall success.");
            } else {
                LogTool.d("Uninstall mci fail.");
                if (uninstallInf != null && !"null".equals(uninstallInf)) {
                    LogTool.d("UninstallInf:" + uninstallInf);
                }
            }
            if (is_success == 1) {
                LogTool.d("Uninstall success.");
                onUninstallSuccess(uninstallIsRunning, uninstallInf, uninstallRet);
            } else {
                LogTool.d("Uninstall fail.");
                onUninstallFail(uninstallIsRunning, uninstallInf, uninstallRet);
            }
            onComplete();
        }

    }

    @Override
    public void onUpdateDownloadStatus(int i, String s, String s1, String s2, String s3, int i1, String[] strings) {
        LogTool.d("onUpdateDownloadStatus");
    }

    @Override
    public void onUpdateInstallStatus(int installIsRunning, String installStage, String installCAPType
            , String installInf, int installRet, CtLoaderSecurityModuleInstallStatus securityInstallStatus) {


        if (installIsRunning == 0) {

            LogTool.d("[CtLoaderCallBack] show download Install dIRun:" + installIsRunning +
                    ", dStage:" + installStage +
                    ", dInf:" + installInf +
                    ", iRet:" + installRet +
                    ", installCAPType:" + installCAPType);
            int is_success = 0;
            if (installCAPType.equals("APK") || installCAPType.equals("OTA") || installCAPType.equals("SBL") || installCAPType.equals("AME")) {
                if (installRet == 0) {
                    is_success = 1;

                } else {

                    LogTool.d("Install mci fail.");
                    if (installInf != null && !"null".equals(installInf)) {
                        LogTool.d("installInf:" + installInf);
                    }

                }
            } else if (installCAPType.equals("SMF") || installCAPType.equals("SME")) {
                if (installRet == d_CAP_UPDATE_FINISHED) {
                    is_success = 1;

                } else if (installRet == d_CAP_RESET_REQUIRED || installRet == d_CAP_REBOOT_REQUIRED) {
                    vendorPackageS1E.system.securityReboot();
                    byte sn_buf[] = new byte[16];
                    while (true) {
                        installRet = vendorPackageS1E.system.getSerialNumber(sn_buf);
                        LogTool.d("system.getSerialNumber: " + installRet);
                        if (installRet == 0) {
                            is_success = 1;
                            LogTool.d("Install success.");
                            break;
                        } else if (installRet == d_FRD_RX_TIMEOUT || installRet == d_FRD_CONNECT_NOT_READY) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            LogTool.e("[CtLoaderCallBack]Get response from 5891 fail.");
                            break;
                        }
                    }
                }
            } else if (installCAPType.equals("CMF")) {
                if (installRet == 0) {
                    is_success = 1;

                } else {

                    if (installInf.equals(RESULT_BIND_DEVICE_FAILURE) ||
                            installInf.equals(RESULT_CONNECT_FAILURE) ||
                            installInf.equals(RESULT_CREATE_SOCKET_FAILURE) ||
                            installInf.equals(RESULT_RECV_FAILURE) ||
                            installInf.equals(RESULT_SEND_FAILURE)) {
                    } else {
                    }
                }
            } else {
                LogTool.d("Install mci fail.");
                if (installInf != null && !"null".equals(installInf)) {
                    LogTool.d("installInf:" + installInf);
                }
            }

            if (is_success == 1) {
                LogTool.d("Install success.");
                LogTool.d("[CtLoaderCallBack]Install " + installCAPType + " finish.");
//                BB.setContentTitle(strAppName + "." + strAppVersion);
//                BB.setContentText(strType + " Update Success");
//                BB.setProgress(1, 1, false);
//                MM.notify(id, BB.build());
                onInstallSuccess(installIsRunning, installStage, installCAPType
                        , installInf, installRet, securityInstallStatus);
            } else {
//                BB.setContentTitle(strAppName + "." + strAppVersion);
//                BB.setContentText(strType + " Update Failure " + installInf);
//                BB.setProgress(1, 1, false);
//                MM.notify(id, BB.build());
                LogTool.d("Install fail.");
                onInstallFail(installIsRunning, installStage, installCAPType
                        , installInf, installRet, securityInstallStatus);
            }
            onComplete();
        }else {
            LogTool.d("installIsRunning:" + installIsRunning);
        }

    }

    protected abstract void onInstallSuccess(int installIsRunning, String installStage, String installCAPType
            , String installInf, int installRet, CtLoaderSecurityModuleInstallStatus securityInstallStatus);

    protected abstract void onInstallFail(int installIsRunning, String installStage, String installCAPType
            , String installInf, int installRet, CtLoaderSecurityModuleInstallStatus securityInstallStatus);

    protected abstract void onUninstallSuccess(int uninstallIsRunning, String uninstallInf, int uninstallRet);

    protected abstract void onUninstallFail(int uninstallIsRunning, String uninstallInf, int uninstallRet);

    protected abstract void onComplete();
}

