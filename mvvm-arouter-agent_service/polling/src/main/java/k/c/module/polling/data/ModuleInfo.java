package k.c.module.polling.data;


import java.util.List;

public class ModuleInfo {
    private List<SME> SME;
    private List<AME> AME;

    public List<SME> getSME() {
        return SME;
    }

    public void setSME(List<SME> SME) {
        this.SME = SME;
    }

    public List<AME> getAME() {
        return AME;
    }

    public void setAME(List<AME> AME) {
        this.AME = AME;
    }

    public static class SME {
        /**
         * Name : xxxx
         * SType : 123
         */

        private String Name;
        private int SType;

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public int getSType() {
            return SType;
        }

        public void setSType(int SType) {
            this.SType = SType;
        }
    }

    public static class AME {
        /**
         * Name : yyyy
         * SType : 121
         */

        private String Name;
        private int SType;

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public int getSType() {
            return SType;
        }

        public void setSType(int SType) {
            this.SType = SType;
        }
    }
}
