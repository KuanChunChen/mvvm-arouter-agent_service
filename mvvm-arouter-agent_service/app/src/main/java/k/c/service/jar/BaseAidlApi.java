package k.c.service.jar;

import android.content.Context;
import android.content.pm.PackageManager;

import java.util.Calendar;

public abstract class BaseAidlApi {


    private static final String ACAMSAPI_PACKAGE ="castles.ctms.service" ;
    private static boolean IS_NEW_CTMS_AGENT = false;

    public static BaseAidlApi getBaseApi(Context context){

        BaseAidlApi baseAidlApi;

        IS_NEW_CTMS_AGENT = isNewCtmsServiceExist(context);
//        IS_NEW_CTMS_AGENT = true; //This line just for test.

        if (IS_NEW_CTMS_AGENT) {
            baseAidlApi = new AndroidApi(context);
        } else {
            baseAidlApi = new KernelApi();
        }
        return baseAidlApi;
    }

    private static boolean isNewCtmsServiceExist(Context context){
        boolean isExist = false;
        try {
            context.getPackageManager().getPackageInfo(ACAMSAPI_PACKAGE,0);
            isExist = true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return isExist;
    }


    protected abstract String getAllConfig();
    protected abstract void setAllConfig(String config);
    protected abstract String getConfig(byte configType);
    protected abstract void setConfig(byte configType, String configContent);
    protected abstract void setBootConnectEnable(byte openType);
    protected abstract int getBootConnectStatus();
    protected abstract void setCTMSEnable(byte openType);
    protected abstract void setCM_Mode(byte openType);
    protected abstract String getUpdateList();
    protected abstract int getCTMSStatus();
    protected abstract Calendar getTrigger();
    protected abstract Calendar getActiveTime();
    protected abstract Calendar getConnectTime();
    protected abstract Calendar getLeaveTime();
    protected abstract int ResetFolder();
    protected abstract int UpdateActive();
    protected abstract int UpdateImmediately();
    protected abstract int resetSetting();
    protected abstract boolean isServiceAlive();


}
