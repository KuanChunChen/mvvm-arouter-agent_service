package k.c.module.login.model.login;

import com.google.gson.annotations.SerializedName;

public class LoginResult {
    @SerializedName("INFO")
    public String statusInfo;
    @SerializedName("SESSION_ID")
    public String sessionId;
    @SerializedName("VR")
    public String serverProtocolVersion;
    @SerializedName("NEXT_PATH")
    public String nextPath;

    @Override
    public String toString() {
        return "LoginResult{" +
                "statusInfo='" + statusInfo + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", nextPath='" + nextPath + '\'' +
                '}';
    }
}
