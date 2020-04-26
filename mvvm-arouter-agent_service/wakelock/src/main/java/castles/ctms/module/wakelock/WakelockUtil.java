package k.c.module.wakelock;

import android.content.Context;
import android.os.PowerManager;

import k.c.common.lib.CommonLib;

public class WakelockUtil {
    private static PowerManager.WakeLock wakeLock = null;

    public static void wakelockON(){
        PowerManager pm = (PowerManager) CommonLib.getAppContext().getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "CTMS");
        wakeLock.acquire();
    }
    public static void wakelockOFF(){
        PowerManager pm = (PowerManager) CommonLib.getAppContext().getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "CTMS");
        wakeLock.release();
    }

}
