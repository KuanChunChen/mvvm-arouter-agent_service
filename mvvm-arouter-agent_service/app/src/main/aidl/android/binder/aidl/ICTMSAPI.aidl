// ACTMSAPI.aidl
package android.binder.aidl;

// Declare any non-default types here with import statements

interface ICTMSAPI{
	int getAllConfig(out byte[] baOutBuff);
	int setAllConfig(in int datasize,in byte[] baInBuff);
	int getFileSize(out int[] filesize);
	int getConfig(in byte[] type, out int[] configsize, out byte[] baOutBuff);
	int setConfig(in byte[] type, in String configcontent);
	int getTrigger(out byte[] TimeInMillis, out int[] datearray);
	int setTrigger(in String TimeInMillis, in byte[] type, in int[] datearray);
	int getOptionstatus(in byte[] type);
	int getActive(out byte[] TimeInMillis, out int[] datearray);
	int setActive(in String TimeInMillis, in int[] datearray);
	int getUpdateInfo(out int[] infosize, out byte[] baOutBuff);
	int setTriggerImmediately(in byte[] type);
	int setActiveSwitch(in byte[] type);
	int getTriggerSwitch(out int[] datearry);
	int setTriggerSwitch(in byte[] type);
	int setInstallLock(in byte[] type);
	int setCTMSEnable(in byte[] type);
	int setPollingEnable(in byte[] type);
	int setActiveImmediately(in byte[] type);
	int setCMMode(in byte[] type);
	int setClear(in byte[] type);
	int getCTMSEnable(out int[] datearry);
	int getUpdateList(out int[] infosize, out byte[] baOutBuff);
	int getConnectTime(out byte[] TimeInMillis);
	int getLeaveTime(out byte[] TimeInMillis);
	int getBootInstallStatus(out int[] datearry);
	int setDebugMode(in byte[] type);
	int getDebugMode(out int[] datearry);
	int setBootConnect(in byte[] type);
	int getBootConnectStatus(out int[] datearry);
	int updatePRMFile(in String PRMFilePath, in int[] FileSize);
    int ChangeSystemPanelPassword(in String SysPanelPassword, in int[] PasswordLen);
	//int getExpireTime(out int[] datearry);
	//int setExpireTime(in byte[] type);
	//int setCycleTime();
}