package k.c.module.commonbusiness.common;

import android.content.pm.PackageInfo;
import android.os.Build;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import k.c.common.lib.CommonLib;
import k.c.common.lib.Util.AndroidUtil;
import k.c.common.lib.Util.CommonUtil;
import k.c.common.lib.Util.GetModuleVersion;
import k.c.common.lib.Util.ShareUtil;
import k.c.common.lib.constants.ModuleDefine;
import k.c.module.commonbusiness.model.AppInfo;
import k.c.module.commonbusiness.model.BaseModel;
import k.c.module.commonbusiness.model.UpdateDataModel;

public class UpdateListManager {

    /**
     * get the merge command list by originList and latestList
     * @param originList origin list
     * @param latestList latest list
     * @return merge list
     */
    public static List<UpdateDataModel> getMergeCommandList(List<UpdateDataModel> originList, List<UpdateDataModel> latestList) {
        List<UpdateDataModel> mergeList = new ArrayList<>();

        if(latestList == null){
            return mergeList;
        }

        Map<String, UpdateDataModel> originModelMap = listTransformMap(originList, UpdateDataModel.class);
        for(int i = 0; i < latestList.size(); i++){

            UpdateDataModel updateDataModel = latestList.get(i);

            if(updateDataModel == null){
                continue;
            }
            UpdateDataModel originUpdateInfo = originModelMap.get(updateDataModel.getDiffKey());
            if (originUpdateInfo == null){
                mergeList.add(updateDataModel);
                continue;
            }
            UpdateDataModel merge = getMergeCommand(originUpdateInfo, updateDataModel);
            mergeList.add(merge);

        }
        return mergeList;
    }

    /**
     * transform list to map by BaseModel(method packageName is 'getKeyForMap')
     * @param list origin list
     * @param c type of model
     * @param <T> entry of model
     * @return map
     */
    public static <T extends BaseModel> Map<String, T> listTransformMap(List<T> list, Class<T> c){
        return listTransformMap(list, BaseModel.class.getDeclaredMethods()[0].getName(), c);
    }

    /**
     * transform list to map
     * @param list origin list
     * @param methodNameOfGetKey method packageName of getKey method
     * @param c type of model
     * @param <V> entry of model
     * @return map
     */
    private static <V> Map<String, V> listTransformMap(List<V> list, String methodNameOfGetKey, Class<V> c){
        Map<String, V> map = new HashMap<>();
        if(list != null){
            try {
                Method methodGetKey = c.getMethod(methodNameOfGetKey);
                for(int i = 0; i < list.size(); i++) {
                    V value = list.get(i);
                    String key = (String) methodGetKey.invoke(list.get(i));
                    map.put(key, value);
                }
            }catch (Exception e){
                throw new IllegalArgumentException("Can't match the key method!");
            }
        }
        return map;
    }

    /**
     * get the one merge command
     * @param originUpdateInfo origin command
     * @param latestUpdateInfo latest command
     * @return merge command
     */
    private static UpdateDataModel getMergeCommand(UpdateDataModel originUpdateInfo, UpdateDataModel latestUpdateInfo){
        UpdateDataModel mergeUpdateInfo;
        if(originUpdateInfo == null){
            mergeUpdateInfo = latestUpdateInfo;
        }else{
            if(originUpdateInfo.status != 0){
                switch (originUpdateInfo.status){
                    case CommonConst.UPDATE_STATUS_UNEXECUTE:
                        mergeUpdateInfo = latestUpdateInfo;
                        break;
                    case CommonConst.UPDATE_STATUS_EXECUTING:
                    case CommonConst.UPDATE_STATUS_EXECUTED:
                        if(originUpdateInfo.isSame(latestUpdateInfo)){
                            if(originUpdateInfo.updateResult == CommonConst.UPDATE_RESULT_NO_RESULT){
                                mergeUpdateInfo = originUpdateInfo;
                            }else{
                                mergeUpdateInfo = latestUpdateInfo;
                            }

                        }else{
                            //TODO delete the old download app
                            mergeUpdateInfo = latestUpdateInfo;
                        }
                        break;
                    default:
                        mergeUpdateInfo = latestUpdateInfo;
                }
            } else {
                mergeUpdateInfo = latestUpdateInfo;
            }

        }
        return mergeUpdateInfo;

    }

    public static List<UpdateDataModel> getDiffCommandList(List<UpdateDataModel> originList, List<UpdateDataModel> latestList) {
        List<UpdateDataModel> diffList = originList;

        if(latestList == null){
            return diffList;
        }

        Map<String, UpdateDataModel> originModelMap = listTransformMap(originList, UpdateDataModel.class);
        for(int i = 0; i < latestList.size(); i++){

            UpdateDataModel updateDataModel = latestList.get(i);

            if(updateDataModel == null){
                break;
            }
            UpdateDataModel originUpdateInfo = originModelMap.get(updateDataModel.getDiffKey());
            if (originUpdateInfo != null){
                diffList.remove(originUpdateInfo);
            }

        }
        return diffList;
    }

    private static List<AppInfo> getInstallAppList(){
        List<PackageInfo> installAppList = AndroidUtil.getAppInfos(CommonLib.getAppContext());
        if(installAppList == null){
            return null;
        }
        List<AppInfo> appInfos = new ArrayList<>();
        for (PackageInfo packageInfo : installAppList){
            AppInfo appInfo = new AppInfo();
            appInfo.name = packageInfo.applicationInfo.loadLabel(CommonLib.getAppContext().getPackageManager()).toString();
            appInfo.packageName = packageInfo.packageName;
            appInfo.versionName = packageInfo.versionName;
            appInfo.versionCode = packageInfo.versionCode;
            appInfo.installTime = CommonUtil.timeStamp2Date(packageInfo.firstInstallTime, null);
            appInfo.updateTime = CommonUtil.timeStamp2Date(packageInfo.lastUpdateTime, null);
            appInfos.add(appInfo);
        }
        return appInfos;
    }

    public static List<UpdateDataModel> updateResultList(List<UpdateDataModel> latestList){
        List<UpdateDataModel> updateDataModelList = new ArrayList<>();
        if(latestList == null){
            return null;
        }

        List<AppInfo> appInfoList = getInstallAppList();
        for(int i = 0; i < latestList.size(); i++){
            UpdateDataModel updateDataModel = latestList.get(i);
            if (updateDataModel.type == CommonConst.FileType.FILE_TYPE_OTA){
                if(updateDataModel.versionName.equals(Build.DISPLAY.substring(6, 12))){
                    updateDataModel.status = CommonConst.UPDATE_STATUS_EXECUTED;
                    updateDataModel.updateResult = CommonConst.UPDATE_RESULT_SUCCESS;
                }
            }else if(updateDataModel.type == CommonConst.FileType.FILE_TYPE_APK){
                if(appInfoList == null){
                    continue;
                }
                for(int j = 0; j < appInfoList.size(); j++){
                    if(!updateDataModel.packageName.equals(appInfoList.get(j).packageName)){
                        continue;
                    }

                    if(updateDataModel.versionName.equals(appInfoList.get(j).versionName)){
                        updateDataModel.status = CommonConst.UPDATE_STATUS_EXECUTED;
                        updateDataModel.updateResult = CommonConst.UPDATE_RESULT_SUCCESS;
                    }
                }
            }else if(updateDataModel.type == CommonConst.FileType.FILE_TYPE_SMF){
                if (updateDataModel.versionName.equals(AndroidUtil.getSmfVersion(updateDataModel.versionName))){
                    updateDataModel.status = CommonConst.UPDATE_STATUS_EXECUTED;
                    updateDataModel.updateResult = CommonConst.UPDATE_RESULT_SUCCESS;
                }
            }else if(updateDataModel.type == CommonConst.FileType.FILE_TYPE_SME){
                String smeName = updateDataModel.sType == CommonConst.FileType.FILE_STYPE_EMV ? ModuleDefine.ID_EMV_SO : ModuleDefine.ID_EMVCL_SO;
                if (updateDataModel.versionName.equals(GetModuleVersion.getModuleVersion(smeName, updateDataModel.sType))){
                    updateDataModel.status = CommonConst.UPDATE_STATUS_EXECUTED;
                    updateDataModel.updateResult = CommonConst.UPDATE_RESULT_SUCCESS;
                }
            }else if(updateDataModel.type == CommonConst.FileType.FILE_TYPE_CMF){
                if (updateDataModel.versionName.equals(AndroidUtil.getCmfVersion())){
                    updateDataModel.status = CommonConst.UPDATE_STATUS_EXECUTED;
                    updateDataModel.updateResult = CommonConst.UPDATE_RESULT_SUCCESS;
                }
            }else if(updateDataModel.type == CommonConst.FileType.FILE_TYPE_AME){
                //TODO check versionName
            }else if(updateDataModel.type == CommonConst.FileType.FILE_TYPE_SBL){
                String originSblVersion = ShareUtil.read(CommonLib.getAppContext(), CommonConst.SBL_INSTALLED_BUT_NOT_REBOOT, CommonConst.SBL_DEFAULT_VERSION);
                if (updateDataModel.versionName.equals(originSblVersion)){
                    updateDataModel.status = CommonConst.UPDATE_STATUS_EXECUTED;
                    updateDataModel.updateResult = CommonConst.UPDATE_RESULT_SUCCESS;
                }else if (updateDataModel.versionName.equals(AndroidUtil.getSblVersion()) && originSblVersion.equals(CommonConst.SBL_DEFAULT_VERSION)){
                    updateDataModel.status = CommonConst.UPDATE_STATUS_EXECUTED;
                    updateDataModel.updateResult = CommonConst.UPDATE_RESULT_SUCCESS;
                }
            }
            updateDataModelList.add(updateDataModel);
        }
        return updateDataModelList;
    }

}
