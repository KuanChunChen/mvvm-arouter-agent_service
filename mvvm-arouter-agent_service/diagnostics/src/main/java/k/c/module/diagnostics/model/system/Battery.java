package k.c.module.diagnostics.model.system;

import com.google.gson.annotations.SerializedName;

public class Battery {
    @SerializedName("Battery Status")
    public String batteryStatus;
    @SerializedName("level")
    public int batteryLevel;
    @SerializedName("Status")
    public String status;
    @SerializedName("Temperature")
    public String batteryTemperature;
    @SerializedName("Current")
    public String current;
    @SerializedName("Voltage")
    public String voltage;
}
