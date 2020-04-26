package k.c.module.download.model;

import com.google.gson.annotations.SerializedName;

public class DownloadHandshakeInfo {

    @SerializedName("SN")
    public String serialNum;
    @SerializedName("DATA")
    public DATA data = new DATA();

    public class DATA {
        @SerializedName("SESSION_ID")
        public String sessionId;
        @SerializedName("FT")
        public int fileType;
        @SerializedName("ID")
        public int fileId;
    }

}
