package k.c.module.diagnostics.model.application;

import com.google.gson.annotations.SerializedName;

public class RunningService {


    @SerializedName("ServiceName")
    public String serviceName;
    @SerializedName("PID")
    public int processID;
    @SerializedName("ActiveSince")
    public long activeSince;
    @SerializedName("CrashCount")
    public int crashCount;
    @SerializedName("LastActivityTime")
    public long lastActivityTime;


}
