package k.c.module.commonbusiness.service;

import com.alibaba.android.arouter.facade.template.IProvider;

public interface PollingService extends IProvider {

    void updateNow();

    void startSchedule();
    void stopSchedule();

}
