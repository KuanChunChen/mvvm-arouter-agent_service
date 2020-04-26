package k.c.module.diagnostics.module.system.simStatus;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.util.List;

import k.c.common.lib.CommonLib;


public class SimManager {


    public static String getPhoneNumber() {

        TelephonyManager telManager = (TelephonyManager) CommonLib.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(CommonLib.getAppContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CommonLib.getAppContext(),
                Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CommonLib.getAppContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Log.d("", "No permission");
            return "Not available";
        }
        return telManager.getLine1Number();
    }


    public static String getDeviceImei() {
        TelephonyManager telManager = (TelephonyManager) CommonLib.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        String deviceIMEI = "Not available";
        try {
            if( Build.VERSION.SDK_INT >= 26 ) {
                deviceIMEI = telManager.getImei();
            }else {
                deviceIMEI = telManager.getDeviceId();
            }
        } catch (SecurityException e) {
            e.toString();
        }
        return deviceIMEI;
    }

    public static String getRomingStatus(){

        TelephonyManager telManager = (TelephonyManager) CommonLib.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        return telManager.isNetworkRoaming() ? "Roming" : "Not Roming";
    }

    public static String getSimCountry(){

        TelephonyManager telManager = (TelephonyManager) CommonLib.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        String networkCountryIso = telManager.getNetworkCountryIso();
        if (networkCountryIso == null) {
            networkCountryIso = "Not available";
        }
        return networkCountryIso;
    }

    public static String getOperator(){

        TelephonyManager telManager = (TelephonyManager) CommonLib.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        String networkOperator = telManager.getNetworkOperator();
        if (networkOperator == null) {
            networkOperator = "Not available";
        }
        return networkOperator;
    }

    public static String getOperatorName(){

        TelephonyManager telManager = (TelephonyManager) CommonLib.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        String networkOperatorName = telManager.getNetworkOperatorName();
        if (networkOperatorName == null) {
            networkOperatorName = "Not available";
        }
        return networkOperatorName;
    }

    public static String getNetworkType(){

        TelephonyManager telManager = (TelephonyManager) CommonLib.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        String[] networkTypeArray = {"UNKNOWN", "GPRS", "EDGE", "UMTS", "CDMA", "EVDO 0", "EVDO A", "1xRTT", "HSDPA", "HSUPA", "HSPA"};
        return networkTypeArray[telManager.getNetworkType()];
    }

    public static String getCelluarNetworkType(){

        TelephonyManager telManager = (TelephonyManager) CommonLib.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        String[] celluarTypeArray = {"NONE", "GSM", "CDMA"};
        return celluarTypeArray[telManager.getPhoneType()];
    }

    public static String getSignalStrength() throws SecurityException {
        TelephonyManager telephonyManager = (TelephonyManager) CommonLib.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        String strength = "Not available";
        List<CellInfo> cellInfos = telephonyManager.getAllCellInfo();   //This will give info of all sims present inside your mobile
        if(cellInfos != null) {
            for (int i = 0 ; i < cellInfos.size() ; i++) {
                if (cellInfos.get(i).isRegistered()) {
                    if (cellInfos.get(i) instanceof CellInfoWcdma) {
                        CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfos.get(i);
                        CellSignalStrengthWcdma cellSignalStrengthWcdma = cellInfoWcdma.getCellSignalStrength();
                        strength = String.valueOf(cellSignalStrengthWcdma.getDbm());
                    } else if (cellInfos.get(i) instanceof CellInfoGsm) {
                        CellInfoGsm cellInfogsm = (CellInfoGsm) cellInfos.get(i);
                        CellSignalStrengthGsm cellSignalStrengthGsm = cellInfogsm.getCellSignalStrength();
                        strength = String.valueOf(cellSignalStrengthGsm.getDbm());
                    } else if (cellInfos.get(i) instanceof CellInfoLte) {
                        CellInfoLte cellInfoLte = (CellInfoLte) cellInfos.get(i);
                        CellSignalStrengthLte cellSignalStrengthLte = cellInfoLte.getCellSignalStrength();
                        strength = String.valueOf(cellSignalStrengthLte.getDbm());
                    } else if (cellInfos.get(i) instanceof CellInfoCdma) {
                        CellInfoCdma cellInfoCdma = (CellInfoCdma) cellInfos.get(i);
                        CellSignalStrengthCdma cellSignalStrengthCdma = cellInfoCdma.getCellSignalStrength();
                        strength = String.valueOf(cellSignalStrengthCdma.getDbm());
                    }
                }
            }
        }
        return strength;
    }

}
