package k.c.module.commonbusiness.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

import k.c.common.lib.CommonLib;


public class ScreenListener {
    private ScreenBroadcastReceiver mScreenReceiver;
    private ScreenStateListener mScreenStateListener;

    public ScreenListener() {
        mScreenReceiver = new ScreenBroadcastReceiver();
    }


    private class ScreenBroadcastReceiver extends BroadcastReceiver {
        private String action = null;

        @Override
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                // open screen
                mScreenStateListener.onScreenOn();
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                // lock screen
                mScreenStateListener.onScreenOff();
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                // unlock the screen
                mScreenStateListener.onScreenLock();
            }
        }
    }

    /**
     * Start listening for screen state
     *
     * @param listener
     */
    public void begin(ScreenStateListener listener) {
        mScreenStateListener = listener;
        registerListener();
        getScreenState();
    }

    /**
     * get screen status
     */
    private void getScreenState() {
        PowerManager manager = (PowerManager) CommonLib.getAppContext()
                .getSystemService(Context.POWER_SERVICE);
        if(manager == null){
            return;
        }
        if (manager.isScreenOn()) {
            if (mScreenStateListener != null) {
                mScreenStateListener.onScreenOn();
            }
        } else {
            if (mScreenStateListener != null) {
                mScreenStateListener.onScreenOff();
            }
        }
    }

    /**
     * unregisterListener
     */
    public void unregisterListener() {
        CommonLib.getAppContext().unregisterReceiver(mScreenReceiver);
    }

    /**
     * registerListener
     */
    private void registerListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        CommonLib.getAppContext().registerReceiver(mScreenReceiver, filter);
    }

    public interface ScreenStateListener {
        void onScreenOn();

        void onScreenOff();

        void onScreenLock();
    }
}
