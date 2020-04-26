package k.c.module.diagnostics.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import k.c.module.diagnostics.model.application.AppInformation;
import k.c.module.diagnostics.model.application.RunningApplication;
import k.c.module.diagnostics.model.application.RunningService;

public class Application {

    @SerializedName("Apps")
    public List<AppInformation> appInformations;
    @SerializedName("Running Apps")
    public List<RunningApplication> runningApplicationsInfo;
    @SerializedName("Running Services")
    public List<RunningService> runningServicesInfo;

}
