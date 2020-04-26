package k.c.service;

import java.util.List;

import k.c.common.lib.logTool.LogTool;
import k.c.module.commonbusiness.base.ModuleBaseApplication;
import k.c.module.commonbusiness.common.CommonFileTool;
import k.c.module.commonbusiness.common.UpdateListManager;
import k.c.module.commonbusiness.model.GetInfoDataModel;
import k.c.module.commonbusiness.model.UpdateDataModel;

public class AppModuleApplication extends ModuleBaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        GetInfoDataModel getInfoDataModel = CommonFileTool.getInfoData();
        if(getInfoDataModel != null){
            LogTool.d("application init, update result status");
            List<UpdateDataModel> oldUpdateDataList = CommonFileTool.getUpdateList(getInfoDataModel.updateListId);
            List<UpdateDataModel> updateList = UpdateListManager.updateResultList(oldUpdateDataList);
            if(updateList != null && updateList.size() > 0){
                LogTool.d("application init, saveUpdateDataList = %s", updateList);
                CommonFileTool.saveUpdateDataList(updateList, getInfoDataModel.updateListId);
            }
        }else{
            LogTool.d("application init get info is null.");
        }


    }
}
