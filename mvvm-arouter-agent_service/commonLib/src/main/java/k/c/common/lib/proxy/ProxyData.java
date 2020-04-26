package k.c.common.lib.proxy;

import com.google.gson.annotations.SerializedName;

public class ProxyData {
    /**
     * proxyIp : a.a.a.a
     * proxyPort : 12
     */

    @SerializedName("PROXY_IP")
    public String proxyIp = "0.0.0.0";

    @SerializedName("PROXY_PORT")
    public int proxyPort = 80;

}
