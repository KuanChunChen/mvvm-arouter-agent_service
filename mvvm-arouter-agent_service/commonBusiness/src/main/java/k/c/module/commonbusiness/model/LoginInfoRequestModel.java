package k.c.module.commonbusiness.model;

public class LoginInfoRequestModel {
    public String serialNum;
    public int communicationMethod;
    public String version;
    public String ip;

    public LoginInfoRequestModel(String serialNum, int communicationMethod, String version, String ip) {
        this.serialNum = serialNum;
        this.communicationMethod = communicationMethod;
        this.version = version;
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "LoginInfoRequestModel{" +
                "serialNum='" + serialNum + '\'' +
                ", communicationMethod=" + communicationMethod +
                ", versionName=" + version +
                ", ip='" + ip + '\'' +
                '}';
    }
}
