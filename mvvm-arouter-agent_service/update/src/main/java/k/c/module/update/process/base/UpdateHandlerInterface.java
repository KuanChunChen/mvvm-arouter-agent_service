package k.c.module.update.process.base;

import java.util.List;

import k.c.module.commonbusiness.model.GetInfoDataModel;
import k.c.module.commonbusiness.model.UpdateDataModel;

public interface UpdateHandlerInterface {
    void initData(List<UpdateDataModel> updateDataModelList);
    void execute(GetInfoDataModel activeMode);
    boolean isHandle();
    List<UpdateDataModel> getUpdateDataList();
}
