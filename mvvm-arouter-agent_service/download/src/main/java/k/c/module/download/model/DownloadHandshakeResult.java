package k.c.module.download.model;

import com.google.gson.annotations.SerializedName;

public class DownloadHandshakeResult {
    @SerializedName("FILE")
    public File file = new File();
    @SerializedName("NEXT_PATH")
    public String nextPath;

    public class File{
        @SerializedName("PATH")
        public String path;
        @SerializedName("FN")
        public String fileName;
        @SerializedName("FS")
        public int fileSize;
        @SerializedName("CSM")
        public String checksumMethod;
        @SerializedName("CS")
        public String checksum;
    }

}
