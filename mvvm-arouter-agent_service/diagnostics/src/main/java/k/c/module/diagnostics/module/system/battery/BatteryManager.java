package k.c.module.diagnostics.module.system.battery;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

public class BatteryManager {

    public static boolean isCharging(Context context){
        if(context == null){
            return false;
        }
        Intent intent = new ContextWrapper(context).
                registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        if(intent == null){
            return false;
        }
        int charging = intent.getIntExtra(android.os.BatteryManager.EXTRA_PLUGGED, 0);
        switch (charging){
            case android.os.BatteryManager.BATTERY_PLUGGED_AC:
            case android.os.BatteryManager.BATTERY_PLUGGED_USB:
            case android.os.BatteryManager.BATTERY_PLUGGED_WIRELESS:
                return true;
            default:
                return false;
        }
    }

    public static String CharingStatus(Context context){
        String returnStatus;
        if(context == null){
            return null;
        }
        Intent intent = new ContextWrapper(context).
                registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        if(intent == null){
            return "NOT_PLUGGED";
        }
        int charging = intent.getIntExtra(android.os.BatteryManager.EXTRA_PLUGGED, 0);
        switch (charging){
            case android.os.BatteryManager.BATTERY_PLUGGED_AC:
                returnStatus = "BATTERY_PLUGGED_AC";
                break;
            case android.os.BatteryManager.BATTERY_PLUGGED_USB:
                returnStatus = "BATTERY_PLUGGED_USB";
                break;
            case android.os.BatteryManager.BATTERY_PLUGGED_WIRELESS:
                returnStatus = "BATTERY_PLUGGED_WIRELESS";
                break;
            default:
                return null;
        }
        return returnStatus;
    }


    public static int getBatteryLevel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            android.os.BatteryManager batteryManager = (android.os.BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
            return batteryManager.getIntProperty(android.os.BatteryManager.BATTERY_PROPERTY_CAPACITY);
        } else {
            Intent intent = new ContextWrapper(context).
                    registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            return (intent.getIntExtra(android.os.BatteryManager.EXTRA_LEVEL, -1) * 100) /
                    intent.getIntExtra(android.os.BatteryManager.EXTRA_SCALE, -1);
        }
    }

    public static String getBatteryTemperature(Context context) {
        if(context == null){
            return null;
        }

        Intent intent = new ContextWrapper(context).registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        if(intent == null){
            return "NOT_PLUGGED";
        }
        float  temp   = ((float) intent.getIntExtra(android.os.BatteryManager.EXTRA_TEMPERATURE,0)) / 10;
        return temp + "°C";
    }

    public static String getBatteryVoltage(Context context) {
        if(context == null){
            return null;
        }

        Intent intent = new ContextWrapper(context).registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        if(intent == null){
            return "NOT_PLUGGED";
        }

        int voltage   =  intent.getIntExtra(android.os.BatteryManager.EXTRA_VOLTAGE,-1) ;
        if (voltage> 1000) {
            return voltage / 1000f + "V";
        }
        else{
            return voltage + "mV";

        }
    }
}