package k.c.module.http.model;

import java.util.Arrays;

import okhttp3.ResponseBody;

public class DownloadFileResult {

    public short status;
    public byte[] data;
    public ResponseBody responseBody;
    public boolean isBigDataMode;

    @Override
    public String toString() {
        return "DownloadResult{" +
                "status = " + status +
                ", data = " + Arrays.toString(data) +
                ", responseBody =" + responseBody +
                ", isBigDataMode =" + isBigDataMode +
                '}';
    }
}
