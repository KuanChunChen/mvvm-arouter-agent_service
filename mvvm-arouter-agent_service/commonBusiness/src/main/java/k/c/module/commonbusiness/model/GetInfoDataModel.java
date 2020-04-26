package k.c.module.commonbusiness.model;

import com.google.gson.annotations.SerializedName;

import k.c.common.lib.logTool.LogTool;
import k.c.module.commonbusiness.common.CommonConst;

public class GetInfoDataModel {
    @SerializedName("configId")
    public String configId;
    @SerializedName("triggerTime")
    public long triggerTime;
    @SerializedName("activeTime")
    public long activeTime;
    @SerializedName("pollingTime")
    public long pollingTime;
    @SerializedName("triggerMode")
    public int triggerMode;
    @SerializedName("activeMode")
    public int activeMode;
    @SerializedName("exchangeData")
    public boolean exchangeData;
    @SerializedName("diagnosticEnable")
    public int diagnosticEnable;

    @SerializedName("serverTimeZone")
    public String serverTimeZone;
    @SerializedName("greenwichMeanTime")
    public String greenwichMeanTime;

    @SerializedName("updateListId")
    public int updateListId;

    @SerializedName("wifiTransmissionSize")
    public int wifiTransmissionSize;
    @SerializedName("gprsTransmissionSize")
    public int gprsTransmissionSize;
    @SerializedName("umtsTransmissionSize")
    public int umtsTransmissionSize;
    @SerializedName("lteTransmissionSize")
    public int lteTransmissionSize;
    @SerializedName("usbTransmissionSize")
    public int usbTransmissionSize;

    @SerializedName("lastPollingTime")
    public long lastPollingTime = 0;

    public long getCurrentTriggerTime(){
        long currentTriggerTime = -1;
        switch (triggerMode){
            case CommonConst.Mode.TRIGGER_MODE_POLLING:
                long pollingTimestamp = pollingTime * 60 * 1000;
                LogTool.d("polling time = %s", pollingTime);
                if(pollingTimestamp < 2 * 60 * 1000){
                    pollingTimestamp = 2 * 60 * 1000;
                }
                currentTriggerTime = pollingTimestamp - (System.currentTimeMillis() - lastPollingTime);
                if(currentTriggerTime < 0){
                    LogTool.d("currentTriggerTime < 0 = %s", currentTriggerTime);
                    currentTriggerTime = 60 * 1000;
                }
                LogTool.d("currentTriggerTime = %s", currentTriggerTime);
                return currentTriggerTime;
            case CommonConst.Mode.TRIGGER_MODE_ENABLE:
            case CommonConst.Mode.TRIGGER_MODE_AUTOMATIC:
                currentTriggerTime = (triggerTime * 1000) - System.currentTimeMillis();
                LogTool.d("triggerDelayTime = %s", currentTriggerTime);
                if(currentTriggerTime < 0){
                    currentTriggerTime = 0;
                }
                return currentTriggerTime;
            default:
                return currentTriggerTime;
        }
    }

    public long getSpecifyTimeDelay(){
        long specifyTimeDelay = -1;
        if (activeMode == CommonConst.Mode.ACTIVE_MODE_SPECIFIED) {
            specifyTimeDelay = (activeTime * 1000) - System.currentTimeMillis();
            LogTool.d("specifyTimeDelay time = %s", specifyTimeDelay);
            if (specifyTimeDelay < 0) {
                specifyTimeDelay = 0;
            }
            return specifyTimeDelay;
        }
        return specifyTimeDelay;
    }

    @Override
    public String toString() {
        return "GetInfoDataModel{" +
                "configId='" + configId + '\'' +
                ", triggerTime='" + triggerTime + '\'' +
                ", activeTime='" + activeTime + '\'' +
                ", pollingTime='" + pollingTime + '\'' +
                ", triggerMode=" + triggerMode +
                ", activeMode=" + activeMode +
                ", exchangeData=" + exchangeData +
                ", serverTimeZone='" + serverTimeZone + '\'' +
                ", greenwichMeanTime='" + greenwichMeanTime + '\'' +
                ", updateListId=" + updateListId +
                ", wifiTransmissionSize=" + wifiTransmissionSize +
                ", gprsTransmissionSize=" + gprsTransmissionSize +
                ", umtsTransmissionSize=" + umtsTransmissionSize +
                ", lteTransmissionSize=" + lteTransmissionSize +
                ", usbTransmissionSize=" + usbTransmissionSize +
                '}';
    }
}
