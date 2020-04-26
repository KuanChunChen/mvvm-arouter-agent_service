package k.c.module.login.model.login;

import com.google.gson.annotations.SerializedName;

public class LoginInfo {
    @SerializedName("SN")
    public String serialNum;
    @SerializedName("CM")
    public int communicationMethod;
    @SerializedName("DATA")
    public Data data = new Data();

    public class Data{
        @SerializedName("VR")
        public String version;
        @SerializedName("SN")
        public String serialNum;
        @SerializedName("IP")
        public String ip;


        @Override
        public String toString() {
            return "Data{" +
                    "versionName=" + version +
                    ", serialNum='" + serialNum + '\'' +
                    ", ip='" + ip + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LoginInfo{" +
                "serialNum='" + serialNum + '\'' +
                ", communicationMethod=" + communicationMethod +
                ", data=" + data.toString() +
                '}';
    }
}