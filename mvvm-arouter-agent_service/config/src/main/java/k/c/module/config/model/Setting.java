package k.c.module.config.model;


import com.google.gson.annotations.SerializedName;

public class Setting {
    @SerializedName("Host")
    private String host;
    @SerializedName("Port")
    private int port;
    @SerializedName("Boot_connect")
    private int bootConnect;
    @SerializedName("CM_Mode")
    private int cmMode;
    @SerializedName("Timeout")
    private int timeout;
    @SerializedName("Set_id")
    private int setId;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getBootConnect() {
        return bootConnect;
    }

    public void setBootConnect(int bootConnect) {
        this.bootConnect = bootConnect;
    }

    public int getCmMode() {
        return cmMode;
    }

    public void setCmMode(int cmMode) {
        this.cmMode = cmMode;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getSetId() {
        return setId;
    }

    public void setSetId(int setId) {
        this.setId = setId;
    }
}
