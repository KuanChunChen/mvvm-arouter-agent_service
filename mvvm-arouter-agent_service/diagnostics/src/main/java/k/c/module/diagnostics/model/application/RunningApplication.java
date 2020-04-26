package k.c.module.diagnostics.model.application;

import com.google.gson.annotations.SerializedName;

public class RunningApplication {

    @SerializedName("AppName")
    public String ApplicationName;
    @SerializedName("PID")
    public int ProcessID;


}
