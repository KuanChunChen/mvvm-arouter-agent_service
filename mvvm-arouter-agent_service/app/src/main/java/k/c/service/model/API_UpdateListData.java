package k.c.service.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class API_UpdateListData {
    @SerializedName("INFO")
    public List<UpdateListForCTMS> updateListForCTMS = new ArrayList<>();



    public static class UpdateListForCTMS {
        /**
         * STATUS : 1
         * ROOTAP_NAME : wwwwwwcom.example.casw2_d_link.ctms_app
         * SUBAP_NAME :
         * RB : 0
         * ID : 1
         * TYPE : 253
         * STYPE : 0
         * VR : 0.0.0
         */
        @SerializedName("STATUS")
        private int status;
        @SerializedName("ROOTAP_NAME")
        private String rootApName;
        @SerializedName("SUBAP_NAME")
        private String subApName;
        @SerializedName("RB")
        private int rb;
        @SerializedName("ID")
        private int id;
        @SerializedName("TYPE")
        private int type;
        @SerializedName("STYPE")
        private int stype;
        @SerializedName("VR")
        private String vr;
        public UpdateListForCTMS(int status, String rootApName, String subApName, int rb, int id, int type, int stype, String vr) {
            this.status = status;
            this.rootApName = rootApName;
            this.subApName = subApName;
            this.rb = rb;
            this.id = id;
            this.type = type;
            this.stype = stype;
            this.vr = vr;
        }
        @Override
        public String toString() {
            return "UpdateListForCTMS{" +
                    "status=" + status +
                    ", rootApName='" + rootApName + '\'' +
                    ", subApName='" + subApName + '\'' +
                    ", rb=" + rb +
                    ", id=" + id +
                    ", type=" + type +
                    ", stype=" + stype +
                    ", vr='" + vr + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "API_UpdateListData{" +
                "updateListForCTMS=" + updateListForCTMS +
                '}';
    }
}
