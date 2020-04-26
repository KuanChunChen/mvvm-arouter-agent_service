package k.c.module.config.model;


import com.google.gson.annotations.SerializedName;

public class CtmsEnable {

    @SerializedName("CTMS")
    public CTMS mCtms = new CTMS();
    @SerializedName("BootConnect")
    public BootConnect mBootConnect = new BootConnect();
    @SerializedName("Connect")
    public Connect mConnect = new Connect();
    @SerializedName("Leave")
    public Leave mLeave = new Leave();
    @Override
    public String toString() {
        return "{" +
                ", CTMS='" + mCtms.toString() + '\'' +
                ", Connect='" + mConnect.toString() + '\'' +
                ", Leave='" + mLeave.toString() + '\'' +
                '}';
    }
    public class CTMS{
        @SerializedName("Enable")
        public int enable;
        @Override
        public String toString() {
            return "CTMS{" +
                    "Enable='" + enable + '\'' +
                    '}';
        }
    }

    public class BootConnect{
        @SerializedName("Enable")
        public int enable;
        @Override
        public String toString() {
            return "BootConnect{" +
                    "Enable='" + enable + '\'' +
                    '}';
        }
    }
    public class Connect{
        @SerializedName("Time")
        public long time;
        @Override
        public String toString() {
            return "Connect{" +
                    "Time='" + time + '\'' +
                    '}';
        }
    }
    public class Leave{
        @SerializedName("Time")
        public long time;
        public String toString() {
            return "Leave{" +
                    "Time='" + time + '\'' +
                    '}';
        }
    }
}

