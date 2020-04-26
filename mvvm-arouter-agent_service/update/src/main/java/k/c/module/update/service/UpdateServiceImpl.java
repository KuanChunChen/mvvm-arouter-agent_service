package k.c.module.update.service;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.List;

import k.c.common.lib.logTool.LogTool;
import k.c.module.commonbusiness.base.BaseServiceImpl;
import k.c.module.commonbusiness.model.UpdateDataModel;
import k.c.module.commonbusiness.service.UpdateService;
import k.c.module.update.process.UpdateProcessManager;


@Route(path = "/update/updateService")
public class UpdateServiceImpl extends BaseServiceImpl implements UpdateService {

    private UpdateProcessManager updateProcessManager;

    @Override
    public void init(Context context) {
        LogTool.d("PollingServiceImpl Init.");
        updateProcessManager = new UpdateProcessManager();
    }

    @Override
    public void initUpdateProcess() {
        updateProcessManager.initUpdateProcess();
    }

    @Override
    public void setRebootInstallMode() {
        updateProcessManager.setRebootInstallMode();
    }

    @Override
    public void setAPControlInstallMode() {
        updateProcessManager.setApControlInstallMode();
    }

    @Override
    public List<UpdateDataModel> getWaitUpdateList(){
        return updateProcessManager.getWaitingUpdateList();
    }

    @Override
    public void clearWaitUpdateList(){
        updateProcessManager.clearWaitUpdateList();
    }
}
