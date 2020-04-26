package k.c.module.polling.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SystemData {

    @SerializedName("Info")
    List<Info> listInfo = null;

    public SystemData(List<Info> mlistInfo) {
        this.listInfo = mlistInfo;
    }

    public static class Info{
        @SerializedName("Name")
        public String strName;
        @SerializedName("P_Name")
        public String strP_Name;
        @SerializedName("Type")
        public int iType;
        @SerializedName("Install_Time")
        public String strInstall_Time;
        @SerializedName("Update_Time")
        public String strUpdate_Time;
        @SerializedName("Version")
        public String strVersion;
        @SerializedName("VersionCode")
        public int iVersionCode;

        public Info(String strName, String strP_Name, int iType, String strInstall_Time, String strUpdate_Time, String strVersion, int iVersionCode) {
            this.strName = strName;
            this.strP_Name = strP_Name;
            this.iType = iType;
            this.strInstall_Time = strInstall_Time;
            this.strUpdate_Time = strUpdate_Time;
            this.strVersion = strVersion;
            this.iVersionCode = iVersionCode;
        }
    }

    @SerializedName("LOCAL_TIMEZONE")
    public String strTZ;
    @SerializedName("LOCAL_AREA")
    public String strArea;

    public void setSystemData(List<Info> listInfo) {
        this.listInfo = listInfo;
    }
    public void setTimeZone(String strTZ) {
        this.strTZ = strTZ;
    }
    public void setTimeArea(String strArea) {
        this.strArea = strArea;
    }
}

