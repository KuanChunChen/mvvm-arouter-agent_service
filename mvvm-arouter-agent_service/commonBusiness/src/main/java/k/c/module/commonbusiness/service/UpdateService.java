package k.c.module.commonbusiness.service;

import com.alibaba.android.arouter.facade.template.IProvider;

import java.util.List;

import k.c.module.commonbusiness.model.UpdateDataModel;

public interface UpdateService extends IProvider {

    void initUpdateProcess();

    void setRebootInstallMode();

    void setAPControlInstallMode();

    List<UpdateDataModel> getWaitUpdateList();

    void clearWaitUpdateList();
}
