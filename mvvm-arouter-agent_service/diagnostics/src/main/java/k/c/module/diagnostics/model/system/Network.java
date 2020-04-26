package k.c.module.diagnostics.model.system;

import com.google.gson.annotations.SerializedName;

public class Network {


    @SerializedName("IPv4")
    public String IPv4;
    @SerializedName("IPv6")
    public String IPv6;
    @SerializedName("WLAN Mac Address")
    public String macAddress;
    @SerializedName("Bluetooth Address")
    public String bluetoothAddress;

}
