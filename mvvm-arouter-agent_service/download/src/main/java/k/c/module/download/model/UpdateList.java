package k.c.module.download.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UpdateList {
    @SerializedName("INFO")
    public List<UpdateInfo> updateInfoList = new ArrayList<>();

    public class UpdateInfo{
        @SerializedName("STATUS")
        public int status;
        @SerializedName("ROOTAP_NAME")
        public String rootApName;
        @SerializedName("SUBAP_NAME")
        public String subApName;
        @SerializedName("RB")
        public int rb;
        @SerializedName("ID")
        public int id;
        @SerializedName("TYPE")
        public int type;
        @SerializedName("STYPE")
        public int sType;
        @SerializedName("VR")
        public String fileVersion;
    }
}
