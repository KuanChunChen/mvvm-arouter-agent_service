package k.c.module.getinfo.model.dataexchange;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class DataExchangeInfo {

    @SerializedName("SN")
    private String mSN = "0000000000000000";
    @SerializedName("DATA")
    private Data mData = new Data();

    public class Data {
        @SerializedName("SESSION_ID")
        private String mSESSION_ID = "0000000000000000";
        @SerializedName("Upload_List")
        private List<Upload_List> mSystemList;
}

    public class Upload_List {
        @SerializedName("FT")
        private String mFT = "0000000000000000";
        @SerializedName("FN")
        private String mFN = "0000000000000000";
        @SerializedName("FV")
        private String mFV = "0000000000000000";
        @SerializedName("FS")
        private String mFS = "0000000000000000";
        @SerializedName("CSM")
        private String mCSM = "0000000000000000";
        @SerializedName("CS")
        private String mCS = "0000000000000000";
    }
}
