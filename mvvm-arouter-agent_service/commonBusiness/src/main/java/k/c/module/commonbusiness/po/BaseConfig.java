package k.c.module.commonbusiness.po;

public class BaseConfig {
    public String serialNumber;
    public int compatibleFlag;
    public int retryCount;
    public int downloadConfiguration;
    public int communicationMode = 2;//1:tcp 2:tls
    public String merchantID = "";

    public String tcpHostIp;
    public int tcpHostPort;
    public int tcpTransmissionSize;
    public int tcpConnectTimeout;
    public int tcpTxTimeout;
    public int tcpRxTimeout;

    public String tlsHostIp;
    public int tlsHostPort;
    public boolean tlsVerifyPeer;

    public String nacProtocol;
    public int nacBlockSize;
    public String nacSourceAddr;
    public String nacDestAddr;
    public int nacLenType;
    public int nacAddLenFlag;

    public String usbHostIp;
    public int usbHostPort;

    @Override
    public String toString() {
        return "BaseConfig{" +
                "serialNumber='" + serialNumber + '\'' +
                ", compatibleFlag=" + compatibleFlag +
                ", retryCount=" + retryCount +
                ", downloadConfiguration=" + downloadConfiguration +
                ", communicationMode=" + communicationMode +
                ", merchantID='" + merchantID + '\'' +
                ", tcpHostIp='" + tcpHostIp + '\'' +
                ", tcpHostPort=" + tcpHostPort +
                ", tcpTransmissionSize=" + tcpTransmissionSize +
                ", tcpConnectTimeout=" + tcpConnectTimeout +
                ", tcpTxTimeout=" + tcpTxTimeout +
                ", tcpRxTimeout=" + tcpRxTimeout +
                ", tlsHostIp='" + tlsHostIp + '\'' +
                ", tlsHostPort=" + tlsHostPort +
                ", tlsVerifyPeer=" + tlsVerifyPeer +
                ", nacProtocol='" + nacProtocol + '\'' +
                ", nacBlockSize=" + nacBlockSize +
                ", nacSourceAddr='" + nacSourceAddr + '\'' +
                ", nacDestAddr='" + nacDestAddr + '\'' +
                ", nacLenType=" + nacLenType +
                ", nacAddLenFlag=" + nacAddLenFlag +
                ", usbHostIp='" + usbHostIp + '\'' +
                ", usbHostPort=" + usbHostPort +
                '}';
    }
}
