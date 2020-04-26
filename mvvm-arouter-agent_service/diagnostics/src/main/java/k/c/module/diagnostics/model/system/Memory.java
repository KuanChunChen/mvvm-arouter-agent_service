package k.c.module.diagnostics.model.system;

import com.google.gson.annotations.SerializedName;

public class Memory {
    @SerializedName("Total Space")
    public int totalMemory;
    @SerializedName("Available Space")
    public int availableMemory;
}
