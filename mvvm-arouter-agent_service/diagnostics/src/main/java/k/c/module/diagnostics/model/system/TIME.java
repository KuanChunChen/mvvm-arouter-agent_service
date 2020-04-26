package k.c.module.diagnostics.model.system;

import com.google.gson.annotations.SerializedName;

public class TIME {
    /**
     * STZ : +8
     * GMT : 2019-10-25 15:24:37
     */
    @SerializedName("STZ")
    public String standrandTimeZone;
    @SerializedName("GTZ")
    public String GTimeZone;
}
