package k.c.module.polling.service;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;

import k.c.common.lib.logTool.LogTool;
import k.c.module.commonbusiness.base.BaseServiceImpl;
import k.c.module.commonbusiness.common.CommonConst;
import k.c.module.commonbusiness.common.CommonFileTool;
import k.c.module.commonbusiness.model.GetInfoDataModel;
import k.c.module.commonbusiness.service.PollingService;
import k.c.module.polling.module.polling.PollingManager;

@Route(path = "/polling/pollingService")
public class PollingServiceImpl extends BaseServiceImpl implements PollingService {

    private PollingManager pollingManager;

    @Override
    public void init(Context context) {
        LogTool.d("PollingServiceImpl Init.");
        pollingManager = new PollingManager();
    }

    @Override
    public void updateNow() {
        pollingManager.updateNow();
    }

    @Override
    public void startSchedule() {
        GetInfoDataModel getInfoDataModel = CommonFileTool.getInfoData();
        if(getInfoDataModel == null){
            LogTool.d("get info is null, may be not first connect");
            pollingManager.updateNow();
            return;
        }

        switch (getInfoDataModel.triggerMode){
            case CommonConst.Mode.TRIGGER_MODE_POLLING:
                LogTool.d("Get mode success and is POLLING_MODE");
                long pollingTime = getInfoDataModel.getCurrentTriggerTime();
                pollingManager.startPolling(pollingTime, false);
                break;
            case CommonConst.Mode.TRIGGER_MODE_ENABLE:
            case CommonConst.Mode.TRIGGER_MODE_AUTOMATIC:
                LogTool.d("Get mode success and is TRIGGER_MODE");
                long triggerDelayTime = getInfoDataModel.getCurrentTriggerTime();
                pollingManager.startTrigger(triggerDelayTime);
                break;
            default:
                LogTool.d("can not support this mode");
                break;
        }
    }

    @Override
    public void stopSchedule() {
        pollingManager.stopSchedule();
    }
}
