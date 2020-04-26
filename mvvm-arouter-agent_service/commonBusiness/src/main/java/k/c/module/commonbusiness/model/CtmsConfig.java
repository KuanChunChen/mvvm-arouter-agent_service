package k.c.module.commonbusiness.model;


import com.google.gson.annotations.SerializedName;

public class CtmsConfig {
    @SerializedName("Serial_Number")
    public String serialNumber;
    @SerializedName("Compatible_Flag")
    public int compatibleFlag;
    @SerializedName("Retry_Count")
    public int retryCount;
    @SerializedName("Download_Configuration")
    public int downloadConfiguration;
    @SerializedName("CM_Mode")
    public int cmMode;
    @SerializedName("MERID")
    public String merchantID;
    @SerializedName("TCP")
    public Tcp tcp = new Tcp();
    @SerializedName("TLS")
    public Tls tls = new Tls();
    @SerializedName("USB")
    public Usb usb = new Usb();
    @SerializedName("NAC")
    public Nac nac = new Nac();

    @Override
    public String toString() {
        return "CtmsConfig{" +
                "serialNumber='" + serialNumber + '\'' +
                ", compatibleFlag='" + compatibleFlag + '\'' +
                ", retryCount=" + retryCount +
                ", downloadConfiguration='" + downloadConfiguration + '\'' +
                ", communicationMode='" + cmMode + '\'' +
                ", merchantID='" + merchantID + '\'' +
                ", tcp='" + tcp.toString() + '\'' +
                ", tls='" + tls.toString() + '\'' +
                ", usb='" + usb.toString() + '\'' +
                ", nac='" + nac.toString() + '\'' +
                '}';
    }

    public class Tcp {
        @SerializedName("Host_IP")
        public String hostIp;
        @SerializedName("Host_Port")
        public int hostPort;
        @SerializedName("Transmission_Size")
        public int transmissionSize;
        @SerializedName("Connect_Timeout")
        public int connectTimeout;
        @SerializedName("Tx_Timeout")
        public int txTimeout;
        @SerializedName("Rx_Timeout")
        public int rxTimeout;

        @Override
        public String toString() {
            return "Tcp{" +
                    "hostIp='" + hostIp + '\'' +
                    ", hostPort='" + hostPort + '\'' +
                    ", transmissionSize=" + transmissionSize +
                    ", connectTimeout='" + connectTimeout + '\'' +
                    ", txTimeout='" + txTimeout + '\'' +
                    ", rxTimeout='" + rxTimeout + '\'' +
                    '}';
        }
    }
    public class Tls {
        @SerializedName("Host_IP")
        public String hostIp;
        @SerializedName("Host_Port")
        public int hostPort;
        @SerializedName("Verify_Peer")
        public boolean verifyPeer;

        @Override
        public String toString() {
            return "Tls{" +
                    "hostIp='" + hostIp + '\'' +
                    ", hostPort='" + hostPort + '\'' +
                    ", verifyPeer=" + verifyPeer +
                    '}';
        }
    }
    public class Nac {
        @SerializedName("Protocol")
        public String protocol;
        @SerializedName("BlockSize")
        public int blockSize;
        @SerializedName("SourceAddr")
        public String sourceAddr;
        @SerializedName("DestAddr")
        public String destAddr;
        @SerializedName("LenType")
        public int lenType;
        @SerializedName("AddLenFlag")
        public int addLenFlag;

        @Override
        public String toString() {
            return "Nac{" +
                    "protocol='" + protocol + '\'' +
                    ", blockSize='" + blockSize + '\'' +
                    ", sourceAddr=" + sourceAddr +
                    ", destAddr='" + destAddr + '\'' +
                    ", lenType='" + lenType + '\'' +
                    ", addLenFlag='" + addLenFlag + '\'' +
                    '}';
        }
    }
    public class Usb {
        @SerializedName("Host_IP")
        public String hostIp;
        @SerializedName("Host_Port")
        public int hostPort;
        @Override
        public String toString() {
            return "Usb{" +
                    "Host_IP='" + hostIp + '\'' +
                    ", Host_Port='" + hostPort + '\'' +
                    '}';
        }
    }

}

