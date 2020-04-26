package k.c.module.config.model;

import com.google.gson.annotations.SerializedName;

public class ReportSystemSettingData {


    @SerializedName("SN")
    public String serialNumber;

    @SerializedName("RTC")
    public long RTC;

    //機器更新密碼的status
    @SerializedName("Status")
    public int status;

    //服務端的status
    @SerializedName("ID")
    public int serverStatus;

    @SerializedName("MERID")
    public String merchantID = "";

    @Override
    public String toString() {
        return "ReportSystemSettingData{" +
                "serialNumber='" + serialNumber + '\'' +
                ", RTC='" + RTC + '\'' +
                ", status=" + status +
                ", settingID=" + serverStatus +
                ", merchantID='" + merchantID + '\'' +
                '}';
    }
}
