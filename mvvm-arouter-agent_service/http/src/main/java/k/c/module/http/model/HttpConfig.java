package k.c.module.http.model;

import java.net.Proxy;

public class HttpConfig {
    public String baseUrl;
    public long connectTimeout = -1;
    public long txTimeout = -1;
    public long rxTimeout = -1;
    public Proxy proxy = Proxy.NO_PROXY;

    @Override
    public String toString() {
        String httpConfigString = "HttpConfig{" +
                "baseUrl='" + baseUrl + '\'' +
                ", connectTimeout=" + connectTimeout +
                ", txTimeout=" + txTimeout +
                ", rxTimeout=" + rxTimeout;

        if(proxy != Proxy.NO_PROXY){
            httpConfigString += "Proxy=null";
        }else{
            httpConfigString += ("Proxy=" + proxy);
        }
        httpConfigString += '}';
        return httpConfigString;

    }
}
