package k.c.module.getinfo.service;

import android.content.Context;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.List;
import java.util.Locale;

import k.c.common.lib.CommonLib;
import k.c.common.lib.Util.CtmsModuleUtil;
import k.c.common.lib.Util.FileFastUtil;
import k.c.common.lib.Util.FileUtil;
import k.c.common.lib.Util.ShareUtil;
import k.c.common.lib.logTool.LogTool;
import k.c.module.commonbusiness.CommonSingle;
import k.c.module.commonbusiness.base.BaseServiceImpl;
import k.c.module.commonbusiness.common.CommonConst;
import k.c.module.commonbusiness.common.CommonFileTool;
import k.c.module.commonbusiness.common.TimeSettingTool;
import k.c.module.commonbusiness.listener.GetInfoListener;
import k.c.module.commonbusiness.model.GetInfoDataModel;
import k.c.module.commonbusiness.model.GetInfoRequestModel;
import k.c.module.commonbusiness.po.BaseConfig;
import k.c.module.commonbusiness.service.GetInfoService;
import k.c.module.getinfo.api.GetInfoAPI;
import k.c.module.getinfo.api.GetInfoHttpAPI;
import k.c.module.getinfo.model.getinfo.GetInfoRequest;
import k.c.module.getinfo.model.getinfo.GetInfoResult;
import k.c.module.getinfo.module.GetSystemInfoManager;
import k.c.module.http.Result;
import k.c.module.http.RetrofitResultObserver;
import okhttp3.Request;

@Route(path = "/getinfo/getInfoService")
public class GetInfoServiceImpl extends BaseServiceImpl implements GetInfoService {

    @Override
    public void getInfo(GetInfoRequestModel getInfoRequestModel, @NonNull final GetInfoListener getInfoListener) {

        if (!checkLogin()) {
            LogTool.d("try getInfoRequest, login fail");
            getInfoListener.loginFail();
            return;
        }

        BaseConfig baseConfig = CommonSingle.getInstance().getBaseConfig();
        if(baseConfig.serialNumber.length() != CommonConst.Common.FORMAT_TERMINAL_SN_LENGTH){
            LogTool.d("check serialNumber length fail");
            getInfoListener.getInfoFail(CommonConst.returnCode.CTMS_TERMINAL_SN_LENGTH_NOT_CORRECT);
            return;
        }


        if (CtmsModuleUtil.isOtaIndexExist()) {

            FileUtil.deletlFile(k.c.common.lib.constants.Constants.Path.UPDATE_OTA_FILE_RECORD);
        }

        List<GetInfoRequest.INFO.System> systemList = new GetSystemInfoManager().getSystemInfo();
        if (systemList == null) {
            LogTool.d("Get system info data from devices failed.");
            getInfoListener.getInfoFail(CommonConst.returnCode.CTMS_GETINFO_SYSTEM_INFO_FAIL);
            return;
        }

        LogTool.d("start getInfoRequest");
        GetInfoRequest getInfoRequest = new GetInfoRequest();

        getInfoRequest.data.info.systemList = systemList;

        getInfoRequest.serialNum = baseConfig.serialNumber;
        getInfoRequest.data.sessionId = CommonSingle.getInstance().getSessionId();
        getInfoRequest.data.infoVersion = "Test_Info_Version";
        getInfoRequest.data.dataExchange = getInfoRequestModel.exchangeData;
        getInfoRequest.data.dlConfig = baseConfig.downloadConfiguration;

        /** This feature is waiting for api release */
        //// This is test too, need to implement after API is release.
        //			 In future, need to program code over here.
        //			 Pack the Contact Card Count Information.
        getInfoRequest.data.info.device.btnUseCount = 10050;
        getInfoRequest.data.info.device.changeTime = 15000;
        getInfoRequest.data.info.device.contactCardCount = 100;
        getInfoRequest.data.info.device.contactlessCardCount = 150;
        getInfoRequest.data.info.device.msrCount = 350;
        getInfoRequest.data.info.device.mCL_HOUR = 120;
        getInfoRequest.data.info.device.mCT_HOUR = 50;
        getInfoRequest.data.info.device.mMSR_HOUR = 80;
        getInfoRequest.data.info.device.printerUseLen = 900;
        getInfoRequest.data.info.device.screenTime = 25000;
        /** This feature is waiting for api release */

        GetInfoHttpAPI.getInfo(getInfoRequest, new RetrofitResultObserver<GetInfoResult>() {
            @Override
            public void onSuccess(GetInfoResult data) {
                LogTool.i(CommonConst.ModuleName.GETINFO_MODULE_NAME,"Request", CommonConst.returnCode.CTMS_SUCCESS_STRING);
                LogTool.d("GetInfoResult === %s", data);
                GetInfoDataModel getInfoDataModel = new GetInfoDataModel();

                getInfoDataModel.configId = data.info.system.cId;
                getInfoDataModel.triggerTime = data.info.system.triggerTime;
                getInfoDataModel.activeTime = data.info.system.activeTime;
                getInfoDataModel.pollingTime = data.info.system.pollingTime;
                getInfoDataModel.triggerMode = data.info.system.triggerMode;
                getInfoDataModel.activeMode = data.info.system.activeMode;
                getInfoDataModel.diagnosticEnable = data.info.diagnosticEnable;

                getInfoDataModel.serverTimeZone = data.info.time.serverTimeZone;
                getInfoDataModel.greenwichMeanTime = data.info.time.greenwichMeanTime;

                getInfoDataModel.updateListId = data.info.updateInfo.id;

                getInfoDataModel.wifiTransmissionSize = data.info.transmissionSize.wifi;
                getInfoDataModel.gprsTransmissionSize = data.info.transmissionSize.gprs;
                getInfoDataModel.umtsTransmissionSize = data.info.transmissionSize.umts;
                getInfoDataModel.lteTransmissionSize = data.info.transmissionSize.lte;
                getInfoDataModel.usbTransmissionSize = data.info.transmissionSize.usb;
                getInfoDataModel.lastPollingTime = System.currentTimeMillis();
                TimeSettingTool.setActiveTime(data.info.system.activeTime);
                TimeSettingTool.setTriggerTime(data.info.system.triggerTime);
                CommonFileTool.saveInfoData(getInfoDataModel);
                getInfoListener.getInfoSuccess(data.info.updateInfo.id);
            }

            @Override
            protected void onResultCode(int resultCode, Result result) {
                super.onResultCode(resultCode, result);
                LogTool.i(CommonConst.ModuleName.GETINFO_MODULE_NAME,"Request",resultCode);
                getInfoListener.getInfoFail(resultCode);
            }

            @Override
            public void onFailure(RetrofitResultException e) {
                super.onFailure(e);
                LogTool.i(CommonConst.ModuleName.GETINFO_MODULE_NAME,"Request", CommonConst.returnCode.CTMS_GETINFO_FAIL);
                getInfoListener.error();
            }
        });
    }

    @Override
    public void clearGetInfoFile() {
        LogTool.d("clearGetInfoFile start");
        GetInfoDataModel getInfoDataModel = CommonFileTool.getInfoData();
        if(getInfoDataModel != null){
            FileFastUtil.deleteFile(String.format(Locale.getDefault(), CommonConst.FileConst.PATH_WITH_UPDATE_LIST_REAL_NAME, getInfoDataModel.updateListId));
            FileFastUtil.deleteFile(CommonConst.FileConst.PATH_WITH_GET_INFO_REAL_NAME);
            ShareUtil.save(CommonLib.getAppContext(), CommonConst.SHARE_KEY_HAS_REBOOT_INSTALL, false);
            LogTool.d("clearGetInfoFile end");
        }else{
            LogTool.d("no clearGetInfoFile");
        }
    }

    @Override
    public void init(Context context) {
        LogTool.d("GetInfoServiceImpl init");
        GetInfoHttpAPI.resetHttpClient(GetInfoAPI.class, GetInfoHttpAPI.getBuilder(GetInfoAPI.class).addInterceptor(chain -> {
            Request original = chain.request();
            String credential = "PHPSESSID=" + CommonSingle.getInstance().getSessionId();
            Request.Builder requestBuilder = original.newBuilder()
                    .addHeader("cookie", credential);
            Request request = requestBuilder.build();
            return chain.proceed(request);
        }));
    }
}
