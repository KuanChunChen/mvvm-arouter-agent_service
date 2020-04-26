package k.c.module.download.model;

import com.google.gson.annotations.SerializedName;

public class DownloadFileInfo {

    @SerializedName("SN")
    public String serialNum;
    @SerializedName("DATA")
    public DATA data = new DATA();

    public class DATA {
        @SerializedName("SESSION_ID")
        public String sessionId;
        @SerializedName("Path")
        public String filePath;
        @SerializedName("DP")
        public long downloadPosition = 0;
        @SerializedName("DS")
        public int downloadSize = 62780;
    }
}
