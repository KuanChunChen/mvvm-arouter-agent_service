package k.c.module.config.model;

import com.google.gson.annotations.SerializedName;

public class RequestInfo {

    @SerializedName("SN")
    private String sn;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

}

