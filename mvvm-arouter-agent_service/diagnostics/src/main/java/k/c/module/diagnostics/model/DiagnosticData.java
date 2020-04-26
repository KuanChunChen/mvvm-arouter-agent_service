package k.c.module.diagnostics.model;

import com.google.gson.annotations.SerializedName;

import k.c.module.diagnostics.model.system.Device;
import k.c.module.diagnostics.model.system.Security;

public class DiagnosticData {

    /**
     * sn : 0025713700257137
     * system : {"location":{"latitude":120.5831894861,"longitude":31.2983479333},"Battery":{"level":90}}
     * application : application
     */
    @SerializedName("sn")
    public String serialNumber;
    @SerializedName("latitude")
    public double latitude;
    @SerializedName("longitude")
    public double longitude;
    @SerializedName("battery_level")
    public int batteryLevel;
    @SerializedName("free_memory")
    public int free_memory;
    @SerializedName("free_storage")
    public long free_storage;
    @SerializedName("ENTID")
    public String merchantID;
    @SerializedName("system")
    public System systemData;
    @SerializedName("application")
    public Application application;
    @SerializedName("device")
    public Device device;
    @SerializedName("security")
    public Security security;

}
