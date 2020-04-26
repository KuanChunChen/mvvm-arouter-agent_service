package k.c.module.commonbusiness.model;

import com.google.gson.annotations.SerializedName;


public class AppInfo extends BaseModel {
    @SerializedName("packageName")
    public String name;
    @SerializedName("package_name")
    public String packageName;
    @SerializedName("version_code")
    public Integer versionCode;
    @SerializedName("version_name")
    public String versionName;
    @SerializedName("install_time")
    public String installTime;
    @SerializedName("update_time")
    public String updateTime;

    @Override
    public String getDiffKey(){
        return packageName;
    }

    @Override
    public void setDiffKey(String key) {
        packageName = key;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                ", packageName='" + name + '\'' +
                ", packageName='" + packageName + '\'' +
                ", versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", installTime='" + installTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
