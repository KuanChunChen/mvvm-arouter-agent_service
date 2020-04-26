package k.c.module.getinfo.model.getinfo;

import com.google.gson.annotations.SerializedName;

public class GetInfoResult {

    @SerializedName("INFO")
    public Info info = new Info();
    @SerializedName("NEXT_PATH")
    public String nextPath;

    public class Info{
        @SerializedName("SYSTEM")
        public System system = new System();
        @SerializedName("TIME")
        public Time time = new Time();
        @SerializedName("UPDATE_LIST")
        public UpdateInfo updateInfo = new UpdateInfo();
        @SerializedName("TS_SIZE")
        public TransmissionSize transmissionSize = new TransmissionSize();
        @SerializedName("DIAGNOSTICS")
        public int diagnosticEnable = 0;

        public class System{
            @SerializedName("C_ID")
            public String cId;
            @SerializedName("TRIGGER_TIME")
            public long triggerTime;
            @SerializedName("ACTIVE_TIME")
            public long activeTime;
            @SerializedName("POLLING_TIME")
            public long pollingTime;
            @SerializedName("TRIGGER_MODE")
            public int triggerMode;
            @SerializedName("ACTIVE_MODE")
            public int activeMode;

            @Override
            public String toString() {
                return "System{" +
                        "cId='" + cId + '\'' +
                        ", triggerTime='" + triggerTime + '\'' +
                        ", activeTime='" + activeTime + '\'' +
                        ", pollingTime='" + pollingTime + '\'' +
                        ", triggerMode=" + triggerMode +
                        ", activeMode=" + activeMode +
                        '}';
            }
        }

        public class Time{
            @SerializedName("STZ")
            public String serverTimeZone;
            @SerializedName("GMT")
            public String greenwichMeanTime;

            @Override
            public String toString() {
                return "Time{" +
                        "serverTimeZone='" + serverTimeZone + '\'' +
                        ", greenwichMeanTime='" + greenwichMeanTime + '\'' +
                        '}';
            }
        }

        public class UpdateInfo{
            @SerializedName("ID")
            public int id;

            @Override
            public String toString() {
                return "UpdateInfo{" +
                        "id='" + id + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "Info{" +
                    "system=" + system +
                    ", time=" + time +
                    ", updateInfo=" + updateInfo +
                    ", transmissionSize=" + transmissionSize +
                    '}';
        }
    }

    public class TransmissionSize{
        @SerializedName("WIFI")
        public int wifi;
        @SerializedName("GPRS")
        public int gprs;
        @SerializedName("UMTS")
        public int umts;
        @SerializedName("LTE")
        public int lte;
        @SerializedName("USB")
        public int usb;

        @Override
        public String toString() {
            return "TransmissionSize{" +
                    "wifi=" + wifi +
                    ", gprs=" + gprs +
                    ", umts=" + umts +
                    ", lte=" + lte +
                    ", usb=" + usb +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GetInfoResult{" +
                "info=" + info +
                ", nextPath='" + nextPath + '\'' +
                '}';
    }
}
