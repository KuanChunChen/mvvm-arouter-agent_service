package k.c.module.polling.process;

import k.c.module.commonbusiness.model.CTMSProcessInfo;
import k.c.module.commonbusiness.model.GetInfoDataModel;

public interface MainProcessInterface {

    void initialize(CTMSProcessInfo proxyData);
    void startProcess(CTMSProcessInfo proxyData);
    void getInfoData();
    void downloadUpdateList(GetInfoDataModel getInfoDataModel);
    void executeUpdateList(int updateListId);
    void callVendorExecute();
    void endProcess(int status);

}
