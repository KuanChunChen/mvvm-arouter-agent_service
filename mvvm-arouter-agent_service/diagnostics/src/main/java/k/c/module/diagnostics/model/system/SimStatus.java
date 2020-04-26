package k.c.module.diagnostics.model.system;

import com.google.gson.annotations.SerializedName;

public class SimStatus {


    /**
     * Network : Not Available
     * Signal Strengh : Not Available
     * Cellular Network Type : Not Available
     * Operator Info : Not Available
     * Service State : Not Available
     * isRoaming : Not Available
     * Cellular Network State : Not Available
     * My Phone Number : xxx-xxx-xxxx
     * imei : Not Available
     * imei SV : Not Available
     * imei (Slot1) : 86506737496794
     * imei SV (Slot1) : Not Available
     * imei (Slot2) : 86506737495294
     * imei SV (Slot2) : Not Available
     */
    @SerializedName("Network")
    public String network;
    @SerializedName("Signal Strengh")
    public String signalStrengh;
    @SerializedName("Cellular Network Type")
    public String cellularNetworkType;
    @SerializedName("Operator Info")
    public String operatorInfo;
    @SerializedName("Service State")
    public String serviceStatus;
    @SerializedName("isRoaming")
    public String isRoaming;
    @SerializedName("Cellular Network State")
    public String cellularNetworkStatus;
    @SerializedName("PhoneNumber")
    public String phoneNumber;
    @SerializedName("imei")
    public String imei;
    @SerializedName("imei SV")
    public String imeiSv;
    @SerializedName("imei (Slot1)")
    public long imelSlot1;
    @SerializedName("imei SV (Slot1)")
    public String imeiSvSlot1;
    @SerializedName("imei (Slot2)")
    public long imelSlot2;
    @SerializedName("imei SV (Slot2)")
    public String imeiSvSlot2;

}
