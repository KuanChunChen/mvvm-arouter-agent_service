package k.c.module.commonbusiness.common;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.PowerManager;

import java.util.Objects;

import k.c.common.lib.CommonLib;
import k.c.common.lib.logTool.LogTool;
import k.c.module.commonbusiness.CommonSingle;

public class LifeManager implements ScreenListener.ScreenStateListener {

    private PowerManager.WakeLock processWakeLock;
    private PowerManager.WakeLock downloadWakeLock;
    private PowerManager.WakeLock updateWakeLock;

    private volatile boolean isScreenOn;

    public static synchronized LifeManager getInstance(){
        return LifeManager.SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final LifeManager INSTANCE = new LifeManager();
    }

    private LifeManager() {
        PowerManager pm = (PowerManager)CommonLib.getAppContext().getSystemService(Context.POWER_SERVICE);
        this.processWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "CTMS_PROCESS_WAKELOCK");
        this.downloadWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "CTMS_DOWNLOAD_WAKELOCK");
        this.updateWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "CTMS_UPDATE_WAKELOCK");
        ScreenListener screenListener = new ScreenListener();
        screenListener.begin(this);
    }

    public void wakeLockProcess(){
        if(!processWakeLock.isHeld()){
            LogTool.d("processWakeLock is acquire");
            processWakeLock.acquire();
        }
        LogTool.d("try cancel alarm");
        cancelAlarm(getStartCTMSProcessIntent());
    }

    public void wakeLockDownload(){
        if(!downloadWakeLock.isHeld()){
            LogTool.d("downloadWakeLock is acquire");
            downloadWakeLock.acquire();
        }
        LogTool.d("try cancel alarm");
        cancelAlarm(getStartCTMSProcessIntent());
    }

    public void wakeLockUpdate(){
        if(!updateWakeLock.isHeld()){
            LogTool.d("updateWakeLock is acquire");
            updateWakeLock.acquire();
        }
        LogTool.d("try cancel alarm");
        cancelAlarm(getStartCTMSProcessIntent());
        cancelAlarm(getStartCTMSInstallIntent());
    }

    public void refreshWakeLockProcess(){
        if(processWakeLock.isHeld()){
            LogTool.d("processWakeLock release");
            processWakeLock.release();
        }
        LogTool.d("try register alarm");
        canRegisterAlarm();
    }
    public void refreshWakeLockDownload(){
        if(downloadWakeLock.isHeld()){
            LogTool.d("downloadWakeLock release");
            downloadWakeLock.release();
        }
        LogTool.d("try register alarm");
        canRegisterAlarm();
    }
    public void refreshWakeLockUpdate(){
        if(updateWakeLock.isHeld()){
            LogTool.d("updateWakeLock release");
            updateWakeLock.release();
        }
        LogTool.d("try register alarm");
        canRegisterAlarm();
        canRegisterStartInstallAlarm();
    }

    private void canRegisterAlarm(){
        LogTool.d("processWakeLock.isHeld() == %s", processWakeLock.isHeld());
        LogTool.d("downloadWakeLock.isHeld() == %s", downloadWakeLock.isHeld());
        LogTool.d("updateWakeLock.isHeld() == %s", updateWakeLock.isHeld());
        LogTool.d("isScreenOn == %s", isScreenOn);
        if(!processWakeLock.isHeld() && !downloadWakeLock.isHeld() && !updateWakeLock.isHeld()
            && !isScreenOn){
            LogTool.d("all lock is release, register the alarm");
            long delayTime = CommonSingle.getInstance().getCurrentTriggerTime();
            if(delayTime < 0){
                LogTool.d("delayTime < 0, not register the alarm");
                return;
            }
            cancelAlarm(getStartCTMSProcessIntent());
            registerAlarm(getStartCTMSProcessIntent(), delayTime, false);
        }
    }

    private void canRegisterStartInstallAlarm(){
        LogTool.d("StartInstall processWakeLock.isHeld() == %s", processWakeLock.isHeld());
        LogTool.d("StartInstall downloadWakeLock.isHeld() == %s", downloadWakeLock.isHeld());
        LogTool.d("StartInstall updateWakeLock.isHeld() == %s", updateWakeLock.isHeld());
        LogTool.d("StartInstall isRegisterInstallAlarm == %s", CommonSingle.getInstance().isRegisterInstallAlarm());
        LogTool.d("isScreenOn == %s", isScreenOn);
        if(!processWakeLock.isHeld() && !downloadWakeLock.isHeld() && !updateWakeLock.isHeld()
                && !isScreenOn && CommonSingle.getInstance().isRegisterInstallAlarm()){
            LogTool.d("all lock is release, register the alarm");
            long delayTime = CommonSingle.getInstance().getSpecifyTimeDelay();
            if(delayTime < 0){
                LogTool.d("delayTime < 0, not register the alarm");
                return;
            }
            cancelAlarm(getStartCTMSInstallIntent());
            registerAlarm(getStartCTMSInstallIntent(), delayTime, false);
        }
    }

    public static void registerAlarm(Intent intent, long alarmTime, boolean isRepeat){
        if(alarmTime <= 60 * 1000){
            LogTool.d("alarmTime less to 60s");
            alarmTime = 60 * 1000;
        }
        try {
            AlarmManager alarmService = (AlarmManager) CommonLib.getAppContext().getSystemService(Context.ALARM_SERVICE);
            PendingIntent service = PendingIntent.getService(CommonLib.getAppContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            if(isRepeat){
                LogTool.d("setRepeating alarm time = %s", alarmTime);
                Objects.requireNonNull(alarmService).setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), alarmTime , service);
            }else{
                LogTool.d("set alarm time = %s", alarmTime);
                Objects.requireNonNull(alarmService).set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + alarmTime, service);
            }
        }catch (NullPointerException e){
            LogTool.d("alarmService is null");
        }
    }

    public static void cancelAlarm(Intent intent){
        try {
            AlarmManager alarmService = (AlarmManager) CommonLib.getAppContext().getSystemService(Context.ALARM_SERVICE);
            PendingIntent service = PendingIntent.getService(CommonLib.getAppContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            Objects.requireNonNull(alarmService).cancel(service);
        }catch (NullPointerException e){
            LogTool.d("alarmService is null");
        }
    }

    public static Intent getStartCTMSProcessIntent(){
        Intent intent = new Intent();
        intent.setClassName("castles.ctms.service", "castles.ctms.service.CtmsApiService");
        intent.setAction("castles.android.MAIN_PROCESS");
        intent.setData(Uri.parse("content://ctms_agent/alarm/polling"));
        intent.putExtra(CommonConst.ACTION_TYPE_KEY, CommonConst.SERVICE_ACTION_TYPE_START_POLLING_PROCESS);
        return intent;
    }

    public static Intent getStartCTMSInstallIntent(){
        Intent intent = new Intent();
        intent.setClassName("castles.ctms.service", "castles.ctms.service.CtmsApiService");
        intent.setData(Uri.parse("content://ctms_agent/alarm/install"));
        intent.setAction("castles.android.INSTALL");
        intent.putExtra(CommonConst.ACTION_TYPE_KEY, CommonConst.SERVICE_ACTION_TYPE_START_INSTALL_PROCESS);
        return intent;
    }

    @Override
    public void onScreenOn() {
        LogTool.d("LifeManager onScreenOn");
        isScreenOn = true;
        cancelAlarm(getStartCTMSProcessIntent());
        cancelAlarm(getStartCTMSInstallIntent());
    }

    @Override
    public void onScreenOff() {
        LogTool.d("LifeManager onScreenOff");
        isScreenOn = false;
        canRegisterAlarm();
        canRegisterStartInstallAlarm();
    }

    @Override
    public void onScreenLock() {
        LogTool.d("LifeManager onScreenLock");
    }
}
