package k.c.module.polling.data;

import java.util.List;

public class UpdateListInfoData {

    private List<INFOBean> INFO;

    public List<INFOBean> getINFO() {
        return INFO;
    }

    public void setINFO(List<INFOBean> INFO) {
        this.INFO = INFO;
    }

    public static class INFOBean {
        /**
         * STATUS : 1
         * ROOTAP_NAME : wwwwwwcom.example.casw2_d_link.ctms_app
         * SUBAP_NAME :
         * RB : 0
         * ID : 51
         * TYPE : 253
         * STYPE : 0
         * VR : 0.0.0
         */

        private int STATUS;
        private String ROOTAP_NAME;
        private String SUBAP_NAME;
        private int RB;
        private int ID;
        private int TYPE;
        private int STYPE;
        private String VR;
        private String PATH;
        public int getSTATUS() {
            return STATUS;
        }

        public void setSTATUS(int STATUS) {
            this.STATUS = STATUS;
        }

        public String getROOTAP_NAME() {
            return ROOTAP_NAME;
        }

        public void setROOTAP_NAME(String ROOTAP_NAME) {
            this.ROOTAP_NAME = ROOTAP_NAME;
        }

        public String getSUBAP_NAME() {
            return SUBAP_NAME;
        }

        public void setSUBAP_NAME(String SUBAP_NAME) {
            this.SUBAP_NAME = SUBAP_NAME;
        }

        public int getRB() {
            return RB;
        }

        public void setRB(int RB) {
            this.RB = RB;
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public int getTYPE() {
            return TYPE;
        }

        public void setTYPE(int TYPE) {
            this.TYPE = TYPE;
        }

        public int getSTYPE() {
            return STYPE;
        }

        public void setSTYPE(int STYPE) {
            this.STYPE = STYPE;
        }

        public String getVR() {
            return VR;
        }

        public void setVR(String VR) {
            this.VR = VR;
        }

        public String getPATH() {
            return PATH;
        }

        public void setPATH(String PATH) {
            this.PATH = PATH;
        }
    }
}
