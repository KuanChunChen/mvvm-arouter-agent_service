package k.c.module.diagnostics.model;

import com.google.gson.annotations.SerializedName;

import k.c.module.diagnostics.model.system.Battery;
import k.c.module.diagnostics.model.system.Device;
import k.c.module.diagnostics.model.system.Location;
import k.c.module.diagnostics.model.system.Memory;
import k.c.module.diagnostics.model.system.Network;
import k.c.module.diagnostics.model.system.SimStatus;
import k.c.module.diagnostics.model.system.Storage;
import k.c.module.diagnostics.model.system.TIME;


public class System {

    /**
     * location : {"latitude":120.5831894861,"longitude":31.2983479333}
     * Battery : {"level":90}
     */
    @SerializedName("location")
    public Location location;
    @SerializedName("Battery")
    public Battery battery;
    @SerializedName("Device")
    public Device device;
    @SerializedName("TIME")
    public TIME time;
    @SerializedName("storage")
    public Storage storage;
    @SerializedName("memory")
    public Memory memory;
    @SerializedName("SIM Status")
    public SimStatus simStatus;
    @SerializedName("Network")
    public Network network;


}

