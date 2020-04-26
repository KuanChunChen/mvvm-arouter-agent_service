package k.c.module.diagnostics.model.system;

import com.google.gson.annotations.SerializedName;

public class Device {
    @SerializedName("Model Number")
    public String terminalModel;
    @SerializedName("Android Version")
    public String androidVersion;
    @SerializedName("ASPL")
    public String AndroidSecurityPatchLevel;
    @SerializedName("Baseband Version")
    public String basebandVersion;
    @SerializedName("Kernel Version")
    public String kernelVersion;
    @SerializedName("Build Version")
    public String buildVersion;
    @SerializedName("Up time")
    public String upTime;
}
