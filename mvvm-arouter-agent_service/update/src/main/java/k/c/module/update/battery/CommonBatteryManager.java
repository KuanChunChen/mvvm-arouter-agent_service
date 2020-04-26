package k.c.module.update.battery;


import android.content.Intent;

import k.c.common.lib.DialogActivity;
import k.c.common.lib.dialogUtil.DialogListener;
import k.c.common.lib.logTool.LogTool;
import k.c.module.commonbusiness.CommonSingle;


public class CommonBatteryManager {

    private BatteryDetector mListener = new BatteryDetector();
    private int batteryLevel;
    private boolean batteryChargingStatus;

    public void openBatteryListener(int batteryLimit) {
        mListener.register(new BatteryDetector.BatteryStateListener() {
            @Override
            public void onStateChanged(Intent mIntent) {
                batteryLevel = mIntent.getIntExtra("level", 0);
//                AppSingle.getInstance().setBatteryLevel(mIntent.getIntExtra("level", 0));
                if (batteryLevel > batteryLimit){
                    DialogActivity.startBattreyEnoughDialog(batteryLimit,new DialogListener() {
                        @Override
                        public void onSure() {
                            DialogActivity.sendFinishDialog();
                            CommonSingle.getInstance().getUpdateService().initUpdateProcess();
                            closeBatteryListener();
                        }

                        @Override
                        public void onCancel() {
                            DialogActivity.sendFinishDialog();
                            closeBatteryListener();
                        }

                        @Override
                        public void onTimeout() {
                            DialogActivity.sendFinishDialog();
                            CommonSingle.getInstance().getUpdateService().initUpdateProcess();
                            closeBatteryListener();
                        }

                        @Override
                        public void onSelect(int time) {

                        }
                    });
                }
                LogTool.d("MyBattery Level : %s", batteryLevel);
            }

            @Override
            public void onStatePowerConnected() {
                batteryChargingStatus = true;
                LogTool.d("batteryChargingStatus = true");
            }

            @Override
            public void onStatePowerDisconnected() {
                batteryChargingStatus = false;
                LogTool.d("batteryChargingStatus = false");
            }
        });
    }

    public void startBatteryListener(BatteryDetector.BatteryStateListener batteryStateListener){
        mListener.register(batteryStateListener);
    }

    public void closeBatteryListener() {
        mListener.unregister();
    }
    
    public int getBatteryLevel() {
        return batteryLevel;
    }

    public boolean isBatteryCharging() {
        return batteryChargingStatus;
    }
}
