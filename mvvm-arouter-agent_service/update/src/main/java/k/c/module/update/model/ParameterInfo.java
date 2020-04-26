package k.c.module.update.model;

import com.google.gson.annotations.SerializedName;

public class ParameterInfo {

    @SerializedName("NAME")
    public String prmFileName;
    @SerializedName("PKG")
    public String appPackageName;

    @Override
    public String toString() {
        return "ParameterInfo{" +
                "prmFileName='" + prmFileName + '\'' +
                ", appPackageName='" + appPackageName + '\'' +
                '}';
    }
}

