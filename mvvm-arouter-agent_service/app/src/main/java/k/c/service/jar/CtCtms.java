package k.c.service.jar;


import android.content.Context;
import android.util.Log;
import java.util.Calendar;

public class CtCtms {

    private BaseAidlApi baseAidlApi;

    public CtCtms(Context context) {

        baseAidlApi = BaseAidlApi.getBaseApi(context);

    }

    public String getAllConfig() {

        String configuration = baseAidlApi.getAllConfig();
        return configuration;
    }

    public void setAllConfig(String config) {

        baseAidlApi.setAllConfig(config);
    }

    public String getConfig(byte configType) {

        String configuration = baseAidlApi.getConfig(configType);

        try {
            Log.i("getConfig :", configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return configuration;
    }

    public void setConfig(byte configType, String configContent) {
        baseAidlApi.setConfig(configType, configContent);
    }

    public void setBootConnectEnable(byte openType) {

        baseAidlApi.setBootConnectEnable(openType);
    }

    public int getBootConnectStatus() {

        int bootConnectStatus = baseAidlApi.getBootConnectStatus();

        try {
            Log.i("getBootConnectStatus :", String.valueOf(bootConnectStatus));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bootConnectStatus;
    }

    public void setCTMSEnable(byte openType) {

        baseAidlApi.setCTMSEnable(openType);
    }

    public void setCM_Mode(byte openType) {

        baseAidlApi.setCM_Mode(openType);
    }

    public String getUpdateList() {

        String updatList = baseAidlApi.getUpdateList();

        try {
            Log.i("getUpdateList :", updatList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updatList;
    }

    public int getCTMSStatus() {

        int CTMSStatus = baseAidlApi.getCTMSStatus();

        try {
            Log.i("getCTMSStatus :", String.valueOf(CTMSStatus));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CTMSStatus;
    }

    public Calendar getTrigger() {

        Calendar triggercalendar = baseAidlApi.getTrigger();
        return triggercalendar;
    }

    public Calendar getActiveTime() {

        Calendar activeTimeCalendar = baseAidlApi.getActiveTime();
        return activeTimeCalendar;
    }

    public Calendar getConnectTime() {

        Calendar connectTimeCalendar = baseAidlApi.getConnectTime();
        return connectTimeCalendar;
    }

    public Calendar getLeaveTime() {

        Calendar leaveTimeCalendar = baseAidlApi.getLeaveTime();
        return leaveTimeCalendar;
    }

    public int ResetFolder() {
        int returnValue = baseAidlApi.ResetFolder();
        return returnValue;
    }

    public int UpdateActive() {

        int returnValue = baseAidlApi.UpdateActive();
        return returnValue;
    }

    public int UpdateImmediately() {

        int returnValue = baseAidlApi.UpdateImmediately();
        return returnValue;
    }

    public int resetSetting() {

        int returnValue = baseAidlApi.resetSetting();
        return returnValue;
    }

    public boolean isServiceAlive() {

        boolean returnValue = baseAidlApi.isServiceAlive();
        return returnValue;
    }
}