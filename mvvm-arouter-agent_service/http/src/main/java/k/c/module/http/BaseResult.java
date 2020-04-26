package k.c.module.http;

import com.google.gson.annotations.SerializedName;


public class BaseResult {
    @SerializedName("Status")
    public Integer code;

    @SerializedName("msg")
    public String msg;
}