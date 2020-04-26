package k.c.module.polling.data;

public class SettingFileData {
    /**
     * Serial_Number : HW
     * CM_Mode : 2
     * TCP_IP : 218.211.35.210
     * TCP_Port : 8080
     * TLS_IP : 218.211.35.210
     * TLS_Port : 443
     * Transmission_Size : 25000
     * Connect_Timeout : 25000
     * Tx_Timeout : 30000
     * Rx_Timeout : 30000
     * CTMS_Enable : true
     * BootConnect : false
     */

    private String Serial_Number;
    private int CM_Mode;
    private String TCP_IP;
    private int TCP_Port;
    private String TLS_IP;
    private int TLS_Port;
    private int Transmission_Size;
    private int Connect_Timeout;
    private int Tx_Timeout;
    private int Rx_Timeout;
    private int CTMS_Enable;
    private int BootConnect;

    public int getTransmission_Size() {
        return Transmission_Size;
    }

    public void setTransmission_Size(int Transmission_Size) {
        this.Transmission_Size = Transmission_Size;
    }

    public String getSerial_Number() {
        return Serial_Number;
    }

    public void setSerial_Number(String Serial_Number) {
        this.Serial_Number = Serial_Number;
    }

    public int getCM_Mode() {
        return CM_Mode;
    }

    public void setCM_Mode(int CM_Mode) {
        this.CM_Mode = CM_Mode;
    }

    public String getTCP_IP() {
        return TCP_IP;
    }

    public void setTCP_IP(String TCP_IP) {
        this.TCP_IP = TCP_IP;
    }

    public int getTCP_Port() {
        return TCP_Port;
    }

    public void setTCP_Port(int TCP_Port) {
        this.TCP_Port = TCP_Port;
    }

    public String getTLS_IP() {
        return TLS_IP;
    }

    public void setTLS_IP(String TLS_IP) {
        this.TLS_IP = TLS_IP;
    }

    public int getTLS_Port() {
        return TLS_Port;
    }

    public void setTLS_Port(int TLS_Port) {
        this.TLS_Port = TLS_Port;
    }

    public int getConnect_Timeout() {
        return Connect_Timeout;
    }

    public void setConnect_Timeout(int Connect_Timeout) {
        this.Connect_Timeout = Connect_Timeout;
    }

    public int getTx_Timeout() {
        return Tx_Timeout;
    }

    public void setTx_Timeout(int Tx_Timeout) {
        this.Tx_Timeout = Tx_Timeout;
    }

    public int getRx_Timeout() {
        return Rx_Timeout;
    }

    public void setRx_Timeout(int Rx_Timeout) {
        this.Rx_Timeout = Rx_Timeout;
    }

    public int getCTMS_Enable() {
        return CTMS_Enable;
    }

    public void setCTMS_Enable(int CTMS_Enable) {
        this.CTMS_Enable = CTMS_Enable;
    }

    public int getBootConnect() {
        return BootConnect;
    }

    public void setBootConnect(int BootConnect) {
        this.BootConnect = BootConnect;
    }
}
