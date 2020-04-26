package k.c.module.diagnostics.model.application;

import com.google.gson.annotations.SerializedName;

public class AppInformation {

    /**
     * PackagName : castles.ctms.service
     * VersionCode : 95
     * VersionName : 0.9.5
     * InstallDateTime : 2019-10-25 15:24:37
     * UpdateDateTime : 2019-10-25 15:24:37
     * ProcessName : ApplicationInfo.processName
     * Description : ApplicatinInfo.descriptionRes
     */
    @SerializedName("PackagName")
    public String appPackageName;
    @SerializedName("VRC")
    public int versionCode;
    @SerializedName("VRN")
    public String versionName;
    @SerializedName("InstallDateTime")
    public long appInstallTime;
    @SerializedName("UpdateDateTime")
    public long appUpdateTime;
    @SerializedName("ProcessName")
    public String appProcessName;
    @SerializedName("Description")
    public int appDescription;


}
