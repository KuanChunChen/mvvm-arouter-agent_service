package k.c.module.config.model;


import com.google.gson.annotations.SerializedName;

public class ReportAndroidSettingData {


    @SerializedName("SN")
    public String serialNum;
    @SerializedName("RTC")
    public String RTC;
    @SerializedName("Status")
    public int status;
    @SerializedName("ID")
    public int settingID;

    public ReportAndroidSettingData(String strSN, String strRTC, int iStatus, int iSettingID) {
        this.serialNum = strSN;
        this.RTC = strRTC;
        this.status = iStatus;
        this.settingID = iSettingID;
    }


}

