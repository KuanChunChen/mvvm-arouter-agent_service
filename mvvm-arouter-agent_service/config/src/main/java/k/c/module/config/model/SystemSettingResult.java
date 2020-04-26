package k.c.module.config.model;

import com.google.gson.annotations.SerializedName;

public class SystemSettingResult {

    @SerializedName("ID")
    public String id;

    @SerializedName("OF_PWD")
    public String old_FirstPassword;

    @SerializedName("OS_PWD")
    public String old_SecondPassword;

    @SerializedName("F_PWD")
    public String new_FirstPassword;

    @SerializedName("S_PWD")
    public String new_SecondPassword;
}
