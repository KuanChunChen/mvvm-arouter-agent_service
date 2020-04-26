package k.c.module.diagnostics.model.system;

import com.google.gson.annotations.SerializedName;

public class Storage {

    @SerializedName("Available Space")
    public long free_storage;
    @SerializedName("Total Space")
    public long total_storage;


}
