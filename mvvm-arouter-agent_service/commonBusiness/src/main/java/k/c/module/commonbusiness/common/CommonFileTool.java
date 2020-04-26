package k.c.module.commonbusiness.common;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

import k.c.common.lib.Util.AndroidUtil;
import k.c.common.lib.Util.FileFastUtil;
import k.c.common.lib.logTool.LogTool;
import k.c.module.commonbusiness.model.GetInfoDataModel;
import k.c.module.commonbusiness.model.UpdateDataModel;

public class CommonFileTool {

    public static GetInfoDataModel getInfoData() {
        try {
            String jsonContent = FileFastUtil.fileReader(CommonConst.FileConst.PATH_WITH_GET_INFO_REAL_NAME);
            return new Gson().fromJson(jsonContent,GetInfoDataModel.class);
        }catch (IOException e) {
            LogTool.e(e.getMessage());
            return null;
        }
    }

    public static void saveInfoData(GetInfoDataModel getInfoDataModel) {
        saveLatestContent(getInfoDataModel, CommonConst.FileConst.PATH_WITH_GET_INFO_REAL_NAME);
    }

    public static List<UpdateDataModel> getUpdateList(int updateListId) {
        try {
            String fileName = String.format(Locale.getDefault(), CommonConst.FileConst.PATH_WITH_UPDATE_LIST_REAL_NAME, updateListId);
            String jsonContent = FileFastUtil.fileReader(fileName);
            Type type = new TypeToken<List<UpdateDataModel>>() {}.getType();
            return new Gson().fromJson(jsonContent,type);
        }catch (IOException e) {
            LogTool.e(e.getMessage());
            return null;
        }
    }

    public static void saveUpdateDataList(List<UpdateDataModel> updateDataModels, int updateListId) {
        saveLatestContent(updateDataModels, String.format(Locale.getDefault(), CommonConst.FileConst.PATH_WITH_UPDATE_LIST_REAL_NAME, updateListId));
    }

    public static void changeUpdateDataResultById(int updateListId, int updateDataId, int result){
        changeUpdateDataListById(updateListId, updateDataId, null, 0, -1, result);
    }

    public static void changeUpdateDataStatusById(int updateListId, int updateDataId, int status){
        changeUpdateDataListById(updateListId, updateDataId, null, 0, status, -1);
    }

    public static void changeUpdateDataStatusById(int updateListId, int updateDataId, String path, long size, int status){
        changeUpdateDataListById(updateListId, updateDataId, path, size, status, -1);
    }

    public static void changeUpdateDataListById(int updateListId, int updateDataId, String path, long size, int status, int result){
        LogTool.d("Execute the updateResult, updateListId = %s, updateDataId = %s, path = %s, size = %s, status = %s, result = %d", updateListId, updateDataId, path, size, status, result);
        List<UpdateDataModel> updateList = getUpdateList(updateListId);
        if(updateList != null && updateList.size() > 0){
            for(UpdateDataModel updateDataModel : updateList){
                if(updateDataModel == null){
                    break;
                }
                if(updateDataModel.id == null){
                    break;
                }
                if(updateDataId == updateDataModel.id){
                    if(path != null){
                        updateDataModel.path = path;
                    }
                    if(size != 0){
                        updateDataModel.size = size;
                    }
                    if(status != -1){
                        updateDataModel.status = status;
                    }
                    if(result != -1){
                        updateDataModel.updateResult = result;
                    }
                }
            }
        }else{
            LogTool.d("updateList is null.");
            return;
        }
        LogTool.d("Update the change update list, update list = %s", updateList);
        saveUpdateDataList(updateList, updateListId);
    }

    /**
     * Save the content by path
     * @param content content info
     * @param path save path
     */
    private static <T> boolean saveLatestContent(T content, String path){
        String jsonString = new Gson().toJson(content);
        try {
            FileFastUtil.fileWrite(path, jsonString);
        } catch (IOException e) {
            LogTool.e(e.getMessage());
            return false;
        }
        return true;
    }

    //Minimum unit M
    public static boolean checkFreeSpace(int minimum) {
        long size = 30 * 1024 * 1024;
        long freeSpace = AndroidUtil.getSDFreeSpace();
        LogTool.d("freeSpace = %s", freeSpace);
        return freeSpace > size;
    }
}
