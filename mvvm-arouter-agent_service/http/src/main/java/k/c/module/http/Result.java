package k.c.module.http;


import com.google.gson.annotations.SerializedName;

public class Result<M> extends BaseResult {
    @SerializedName(value="DATA")
    public M value;
}

