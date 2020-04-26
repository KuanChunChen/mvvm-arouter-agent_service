package k.c.module.update.process.base;

import k.c.module.commonbusiness.model.UpdateDataModel;

public interface UpdateHandleListener {
    void batteryNotEnough();
    void onceExecuteFail(UpdateDataModel updateData);
    void onceExecuteSuccess(UpdateDataModel updateData);
    void allExecuted();
    boolean isApControlInstallMode();
    boolean isRebootInstallMode();
}
