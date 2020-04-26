package k.c.module.update.battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import k.c.common.lib.CommonLib;
import k.c.common.lib.logTool.LogTool;

public class BatteryDetector {

    private BatteryBroadcastReceiver receiver;

    private BatteryStateListener mBatteryStateListener;

    public BatteryDetector() {
        receiver = new BatteryBroadcastReceiver();
    }

    public void register(BatteryStateListener listener) {
        mBatteryStateListener = listener;
        if (receiver != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_BATTERY_CHANGED);
            filter.addAction(Intent.ACTION_BATTERY_LOW);
            filter.addAction(Intent.ACTION_BATTERY_OKAY);
            filter.addAction(Intent.ACTION_POWER_CONNECTED);
            filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
            CommonLib.getAppContext().registerReceiver(receiver, filter);
        }
    }

    public void unregister() {
        try {
            if (receiver != null){
                CommonLib.getAppContext().unregisterReceiver(receiver);
            }
        }catch (IllegalArgumentException e){
            LogTool.d("Receiver has not register.");
            e.printStackTrace();
        }
    }

    private class BatteryBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent == null){
                return;
            }
            String strAction = intent.getAction();
            if(strAction == null){
                return;
            }
            switch (strAction) {
                case Intent.ACTION_BATTERY_CHANGED://電量發生改變
                    if (mBatteryStateListener != null) {
                        LogTool.d("BatteryDetector BatteryBroadcastReceiver --> onReceive--> ACTION_BATTERY_CHANGED");
                        mBatteryStateListener.onStateChanged(intent);
                    }
                    break;
                case Intent.ACTION_BATTERY_LOW://電量低
                    if (mBatteryStateListener != null) {
                        LogTool.d("BatteryDetector BatteryBroadcastReceiver --> onReceive--> ACTION_BATTERY_LOW");
//                            mBatteryStateListener.onStateLow();
                    }
                    break;
                case Intent.ACTION_BATTERY_OKAY://電量充滿
                    if (mBatteryStateListener != null) {
                        LogTool.d("BatteryDetector BatteryBroadcastReceiver --> onReceive--> ACTION_BATTERY_OKAY");
//                            mBatteryStateListener.onStateOkay();
                    }
                    break;
                case Intent.ACTION_POWER_CONNECTED://接通電源
                    if (mBatteryStateListener != null) {
                        LogTool.d("BatteryDetector BatteryBroadcastReceiver --> onReceive--> ACTION_POWER_CONNECTED");
                        mBatteryStateListener.onStatePowerConnected();
                    }
                    break;
                case Intent.ACTION_POWER_DISCONNECTED://拔出電源
                    if (mBatteryStateListener != null) {
                        LogTool.d("BatteryDetector BatteryBroadcastReceiver --> onReceive--> ACTION_POWER_DISCONNECTED");
                        mBatteryStateListener.onStatePowerDisconnected();
                    }
                    break;
            }
        }
    }

    public interface BatteryStateListener {

        void onStateChanged(Intent mIntent);

//        void onStateLow();
//
//        void onStateOkay();
//
        void onStatePowerConnected();

        void onStatePowerDisconnected();
    }

}
