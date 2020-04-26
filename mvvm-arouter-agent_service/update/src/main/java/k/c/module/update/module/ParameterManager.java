package k.c.module.update.module;

import android.content.pm.PackageManager;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import k.c.common.lib.CommonLib;
import k.c.common.lib.Util.FileUtil;
import k.c.common.lib.Util.KernelUtil;
import k.c.common.lib.logTool.LogTool;
import k.c.module.commonbusiness.common.CommonConst;
import k.c.module.update.model.ParameterInfo;
import k.c.module.update.module.parameter.ParameterCallback;


public class ParameterManager {

    ParameterCallback parameterCallback;

    public void setParameterCallback(ParameterCallback parameterCallback){

        this.parameterCallback = parameterCallback;
    }

    public ParameterInfo[] preparePrmData(String prmFilePath) {

        String content;
        String readResult = "";
        FileInputStream fileInputStream;
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;
        File prmInfoFile = new File(prmFilePath + CommonConst.FileConst.PATH_PARAMETER_FILE_NAME);
        ParameterInfo[] parameterInfo = null;
        LogTool.d("Get PRM path is :" + prmFilePath + CommonConst.FileConst.PATH_PARAMETER_FILE_NAME);

        if (!prmInfoFile.exists()) {
            LogTool.d( "[PRM] Parameter info file not exist.");
            parameterCallback.onFail(CommonConst.returnCode.CTMS_UPDATE_PARAMETER_INFO_FILE_NOT_EXIST);
            return null;
        }

        try {

            fileInputStream = new FileInputStream(prmInfoFile);
            inputStreamReader = new InputStreamReader(fileInputStream);
            bufferedReader = new BufferedReader(inputStreamReader);

            while ((content = bufferedReader.readLine()) != null) {
                readResult += content;
            }
            parameterInfo = new Gson().fromJson(readResult, ParameterInfo[].class);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LogTool.d("[PRM] FileNotFoundException.");

        } catch (IOException e) {
            e.printStackTrace();
            LogTool.d("[PRM] IOException.");
        }
        return parameterInfo;
    }

    public int saveParameterThroughKernel(String prmPath, ParameterInfo parameterInfo) {

        /*** This part still wait kernel to finish the api ***/
        LogTool.d("Paramter info :" + parameterInfo.toString());

        prmPath = prmPath + "/" + parameterInfo.prmFileName;
        //check if parameter file is exist.
        if (!FileUtil.fileIsExists(prmPath)) {
            parameterCallback.onFail(CommonConst.returnCode.CTMS_UPDATE_PARAMETER_GET_PRMPATH_ERROR);
            LogTool.d("[PRM] Parameter file not found.");
            return CommonConst.returnCode.CTMS_UPDATE_PARAMETER_GET_PRMPATH_ERROR;
        }
        //check if assign application is exist.
        if (!appInstalledOrNot(parameterInfo.appPackageName.toLowerCase())) {
            parameterCallback.onFail(CommonConst.returnCode.CTMS_UPDATE_PARAMETER_APPLICATION_NOT_INSTALLED);
            LogTool.d("[PRM] Assigned application not installed.");
            return CommonConst.returnCode.CTMS_UPDATE_PARAMETER_APPLICATION_NOT_INSTALLED;
        }
        //check if assign save path is exist.
        String savePath = getSavePathFromPackageName(parameterInfo.appPackageName.toLowerCase());
        if (!new File(savePath).exists()) {
            parameterCallback.onFail(CommonConst.returnCode.CTMS_UPDATE_PARAMETER_GET_SAVEPATH_ERROR);
            LogTool.d("[PRM] Get parameter save path error.");
            return CommonConst.returnCode.CTMS_UPDATE_PARAMETER_GET_SAVEPATH_ERROR;
        }
        LogTool.d("Update parameter file location :" + prmPath);
        LogTool.d("Update parameter save path :" + savePath);


        int status = KernelUtil.updatePRMFile(prmPath, savePath);
        LogTool.d("Update parameter status : " + status);

        if (status == 0) {
            parameterCallback.onSuccess();
            LogTool.d("[PRM] Save parameter to kernel success.");
            return status;
        } else {
            parameterCallback.onFail(CommonConst.returnCode.CTMS_UPDATE_PARAMETER_SAVE_TO_KERNEL_FAIL);
            LogTool.d("[PRM] Save parameter to kernel failed.");
            return CommonConst.returnCode.CTMS_UPDATE_PARAMETER_SAVE_TO_KERNEL_FAIL;
        }

    }

    private String getSavePathFromPackageName(String packageName){

     return   "/data/data/" + packageName;
    }

    private boolean appInstalledOrNot(String packageName) {

        boolean app_installed = false;
        PackageManager pm = CommonLib.getAppContext().getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return app_installed;
    }

}
