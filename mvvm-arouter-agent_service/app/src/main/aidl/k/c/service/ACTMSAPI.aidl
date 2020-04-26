// ICTMSAPI.aidl
package k.c.service;

// Declare any non-default types here with import statements

interface ACTMSAPI {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    String getAllConfig();

    int setAllConfig(String strConfigContent);

    String getConfig(byte bConfigType);

    int setConfig(byte bConfigType, String strConfigContent);

    long getTrigger();

    long getActiveTime();

    int setBootConnectEnable(byte openType);

    int getBootConnectStatus();

    int setCTMSEnable(byte openType);

    int setCM_Mode(byte openType);

    String getUpdateList();

    int getCTMSStatus();

    long getConnectTime();

    long getLeaveTime();

    int UpdateActive();

    int ResetFolder();

    int UpdateImmediately();

    int resetSetting();

}
