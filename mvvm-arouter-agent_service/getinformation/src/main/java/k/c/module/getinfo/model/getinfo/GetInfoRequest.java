package k.c.module.getinfo.model.getinfo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetInfoRequest {

    @SerializedName("SN")
    public String serialNum;
    @SerializedName("DATA")
    public Data data = new Data();


    public class Data {
        @SerializedName("SESSION_ID")
        public String sessionId;
        @SerializedName("INFO_VERSION")
        public String infoVersion;
        @SerializedName("INFO")
        public INFO info = new INFO();
        @SerializedName("DATA_EXCHANGE")
        public boolean dataExchange;
        @SerializedName("DL_CONFIG")
        public int dlConfig;

        @Override
        public String toString() {
            return "Data{" +
                    "sessionId='" + sessionId + '\'' +
                    ", infoVersion='" + infoVersion + '\'' +
                    ", info=" + info +
                    ", dataExchange=" + dataExchange +
                    ", dlConfig=" + dlConfig +
                    '}';
        }
    }

    public class INFO {
        @SerializedName("SYSTEM")
        public List<System> systemList = new ArrayList<>();
        @SerializedName("DEVICE")
        public Device device = new Device();

        public class System {
            @SerializedName("FT")
            public int fileType;
            @SerializedName("FVC")
            public int mFVC;
            @SerializedName("FN")
            public String fileName;
            @SerializedName("FV")
            public String fileVersion;
            @SerializedName("PN")
            public String mPN;
            @SerializedName("IT")
            public String mIT;
            @SerializedName("UT")
            public String mUT;

            public System() {
            }

            public System(int mFT, int mFVC, String mFN, String mFV, String PN, String IT, String UT) {
                this.fileType = mFT;
                this.mFVC = mFVC;
                this.fileName =  mFN;
                this.fileVersion = mFV;
                this.mPN = PN;
                this.mIT = IT;
                this.mUT = UT;
            }

            @Override
            public String toString() {
                return "System{" +
                        "fileType=" + fileType +
                        ", mFVC=" + mFVC +
                        ", fileName='" + fileName + '\'' +
                        ", fileVersion='" + fileVersion + '\'' +
                        ", mPN='" + mPN + '\'' +
                        ", mIT='" + mIT + '\'' +
                        ", mUT='" + mUT + '\'' +
                        '}';
            }
        }

        public class Device {
            @SerializedName("CT_CNT")
            public int contactCardCount;
            @SerializedName("CL_CNT")
            public int contactlessCardCount;
            @SerializedName("MSR_CNT")
            public int msrCount;
            @SerializedName("CT_HOUR")
            public int mCT_HOUR;
            @SerializedName("CL_HOUR")
            public int mCL_HOUR;
            @SerializedName("MSR_HOUR")
            public int mMSR_HOUR;
            @SerializedName("PRINTER_LEN")
            public int printerUseLen;
            @SerializedName("BTN_CNT")
            public int btnUseCount;
            @SerializedName("CHARGE_TIME")
            public int changeTime;
            @SerializedName("SCREEN_TIME")
            public int screenTime;

            @Override
            public String toString() {
                return "Device{" +
                        "contactCardCount=" + contactCardCount +
                        ", contactlessCardCount=" + contactlessCardCount +
                        ", msrCount=" + msrCount +
                        ", mCT_HOUR=" + mCT_HOUR +
                        ", mCL_HOUR=" + mCL_HOUR +
                        ", mMSR_HOUR=" + mMSR_HOUR +
                        ", printerUseLen=" + printerUseLen +
                        ", btnUseCount=" + btnUseCount +
                        ", changeTime=" + changeTime +
                        ", screenTime=" + screenTime +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "INFO{" +
                    "systemList=" + systemList.toString() +
                    ", device=" + device.toString() +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GetInfoRequest{" +
                "serialNum='" + serialNum + '\'' +
                ", data=" + data.toString() +
                '}';
    }
}
