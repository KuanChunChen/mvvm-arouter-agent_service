package k.c.common.lib;

public class AppSingle {

    private int operationPermission = -1;

    private AppSingle(){

    }

//    private int iBatteryLevel = 0;
//    private boolean boBatteryConnectStatus = false;
    private String strDefalutPath;
    public static synchronized AppSingle getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final AppSingle INSTANCE = new AppSingle();
    }

//    public int getBatteryLevel() {
//        return iBatteryLevel;
//    }
//
//    public void setBatteryLevel(int iBatteryLevel) {
//        this.iBatteryLevel = iBatteryLevel;
//    }
//
//    public boolean getBatteryConnectStatus() {
//        return boBatteryConnectStatus;
//    }
//
//    public void setBatteryConnectStatus(boolean boBatteryConnectStatus) {
//        this.boBatteryConnectStatus = boBatteryConnectStatus;
//    }

    public String getDefalutPath() {
        return strDefalutPath;
    }

    public void setDefalutPath(String strDefalutPath) {
        this.strDefalutPath = strDefalutPath;
    }

    public void setOperationPermission(int operationPermission) {
        this.operationPermission = operationPermission;
    }

    public int getOperationPermission() {
        return operationPermission;
    }
}

