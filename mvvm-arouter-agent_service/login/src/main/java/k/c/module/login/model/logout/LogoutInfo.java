package k.c.module.login.model.logout;

import com.google.gson.annotations.SerializedName;

public class LogoutInfo {


    @SerializedName("SN")
    public String SN ;
    @SerializedName("DATA")
    public DATA Data = new DATA();

    public class DATA {
        @SerializedName("SESSION_ID")
        public String SESSION_ID ;
    }
}
