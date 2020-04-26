package k.c.module.polling.data.getInfo;

import java.util.List;

public class GetTerminalInfoData {


    private List<GetINFOBean> INFO;

    public List<GetINFOBean> getINFO() {
        return INFO;
    }


    public static class GetINFOBean {
        private String NAME;
        private String SUBAP_NAME;
        private int TYPE;
        private String VR;

        public GetINFOBean(String strName, String strSubName, int iTYPE, String strVR) {

            this.NAME = strName;
            this.SUBAP_NAME = strSubName;
            this.TYPE = iTYPE;
            this.VR = strVR;
        }


        public String getNAME() {
            return NAME;
        }

        public void setNAME(String NAME) {
            this.NAME = NAME;
        }

        public String getSUBAP_NAME() {
            return SUBAP_NAME;
        }

        public void setSUBAP_NAME(String SUBAP_NAME) {
            this.SUBAP_NAME = SUBAP_NAME;
        }


        public int getTYPE() {
            return TYPE;
        }

        public void setTYPE(int TYPE) {
            this.TYPE = TYPE;
        }

        public String getVR() {
            return VR;
        }

        public void setVR(String VR) {
            this.VR = VR;
        }
    }
}
