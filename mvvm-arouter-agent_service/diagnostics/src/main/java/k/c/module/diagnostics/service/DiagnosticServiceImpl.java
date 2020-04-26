package k.c.module.diagnostics.service;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;


import k.c.common.lib.AppSingle;
import k.c.common.lib.CommonLib;
import k.c.common.lib.logTool.LogTool;
import k.c.module.commonbusiness.CommonSingle;
import k.c.module.commonbusiness.common.CommonConst;
import k.c.module.commonbusiness.po.BaseConfig;
import k.c.module.commonbusiness.service.DiagnosticService;
import k.c.module.diagnostics.api.DiagnosticHttpAPI;
import k.c.module.diagnostics.model.Application;
import k.c.module.diagnostics.model.DiagnosticData;
import k.c.module.diagnostics.model.System;
import k.c.module.diagnostics.model.system.Battery;
import k.c.module.diagnostics.model.system.Device;
import k.c.module.diagnostics.model.system.Location;
import k.c.module.diagnostics.model.system.Memory;
import k.c.module.diagnostics.model.system.Network;
import k.c.module.diagnostics.model.system.Security;
import k.c.module.diagnostics.model.system.SimStatus;
import k.c.module.diagnostics.model.system.Storage;
import k.c.module.diagnostics.model.system.TIME;
import k.c.module.diagnostics.module.application.applications.ApplicationManager;
import k.c.module.diagnostics.module.system.battery.BatteryManager;
import k.c.module.diagnostics.module.system.device.AndroidDeviceManager;
import k.c.module.diagnostics.module.system.location.GpsManager;
import k.c.module.diagnostics.module.system.memory.MemoryManager;
import k.c.module.diagnostics.module.system.network.NetworkManager;
import k.c.module.diagnostics.module.system.simStatus.SimManager;
import k.c.module.diagnostics.module.system.storage.StorageManager;
import k.c.module.diagnostics.module.system.time.TimeManager;
import k.c.module.http.Result;
import k.c.module.http.RetrofitResultObserver;

@Route(path = "/diagnostic/diagnosticService")
public class DiagnosticServiceImpl implements DiagnosticService {

    private GpsManager gpsManager;

    @Override
    public String getDiagnosticData() {

        if (AppSingle.getInstance().getOperationPermission() != 0) {
            LogTool.d("getDiagnosticData not permission");
            return null;
        }

        DiagnosticData diagnosticData = new DiagnosticData();

        BaseConfig baseConfig = CommonSingle.getInstance().getBaseConfig();

        diagnosticData.serialNumber = baseConfig.serialNumber;
        diagnosticData.free_memory = new MemoryManager().getAvailMemory();
        diagnosticData.free_storage = StorageManager.getAvailableInternalStorageSize();
        diagnosticData.systemData = new System();

        diagnosticData.systemData.location = new Location();

        if (gpsManager.getLastLocation() != null){
            diagnosticData.latitude = gpsManager.getLastLocation().getLatitude();
            diagnosticData.longitude = gpsManager.getLastLocation().getLongitude();
            diagnosticData.systemData.location.latitude = gpsManager.getLastLocation().getLatitude();
            diagnosticData.systemData.location.longitude = gpsManager.getLastLocation().getLongitude();
        }else {
            diagnosticData.latitude = 0;
            diagnosticData.longitude = 0;
            diagnosticData.systemData.location.latitude = 0;
            diagnosticData.systemData.location.longitude = 0;
        }

        BatteryManager batteryManager = new BatteryManager();
        diagnosticData.systemData.battery = new Battery();

        if (batteryManager.isCharging(CommonLib.getAppContext())) {
            diagnosticData.systemData.battery.batteryStatus = "Charging";
        } else {
            diagnosticData.systemData.battery.batteryStatus = "Not Charging";
        }

        diagnosticData.batteryLevel= batteryManager.getBatteryLevel(CommonLib.getAppContext());

        diagnosticData.systemData.battery.batteryLevel = batteryManager.getBatteryLevel(CommonLib.getAppContext());
        diagnosticData.systemData.battery.status = batteryManager.CharingStatus(CommonLib.getAppContext());
        diagnosticData.systemData.battery.batteryTemperature = batteryManager.getBatteryTemperature(CommonLib.getAppContext());
        diagnosticData.systemData.battery.voltage = batteryManager.getBatteryVoltage(CommonLib.getAppContext());


        diagnosticData.device = new Device();
        diagnosticData.device.androidVersion = Build.VERSION.RELEASE;
        diagnosticData.device.buildVersion = Build.DISPLAY;
        diagnosticData.device.kernelVersion = AndroidDeviceManager.getKernelVersion();
        diagnosticData.device.AndroidSecurityPatchLevel = AndroidDeviceManager.getSecurityPatchLevel();
        diagnosticData.device.basebandVersion = Build.getRadioVersion();
        diagnosticData.device.upTime = AndroidDeviceManager.getUpTime();
        diagnosticData.device.terminalModel = AndroidDeviceManager.getDeviceModel(AndroidDeviceManager.SYSTEM_COMMAND_GETPROP, AndroidDeviceManager.SYSTEM_COMMAND_RO_OEM_DEVICE);


        diagnosticData.systemData.device = new Device();
        diagnosticData.systemData.device.androidVersion = Build.VERSION.RELEASE;
        diagnosticData.systemData.device.buildVersion = Build.DISPLAY;
        diagnosticData.systemData.device.kernelVersion = AndroidDeviceManager.getKernelVersion();
        diagnosticData.systemData.device.AndroidSecurityPatchLevel = AndroidDeviceManager.getSecurityPatchLevel();
        diagnosticData.systemData.device.basebandVersion = Build.getRadioVersion();
        diagnosticData.systemData.device.upTime = AndroidDeviceManager.getUpTime();
        diagnosticData.systemData.device.terminalModel = AndroidDeviceManager.getDeviceModel(AndroidDeviceManager.SYSTEM_COMMAND_GETPROP, AndroidDeviceManager.SYSTEM_COMMAND_RO_OEM_DEVICE);

        diagnosticData.systemData.storage = new Storage();
        diagnosticData.systemData.storage.free_storage = StorageManager.getAvailableInternalStorageSize();
        diagnosticData.systemData.storage.total_storage = StorageManager.getTotalInternalStorageSize();

        diagnosticData.systemData.memory = new Memory();
        diagnosticData.systemData.memory.totalMemory = new MemoryManager().getTotalMemory();
        diagnosticData.systemData.memory.availableMemory = new MemoryManager().getAvailMemory();


        diagnosticData.systemData.network = new Network();
        diagnosticData.systemData.network.IPv4 = NetworkManager.getLocalIpAddress(NetworkManager.TYPE_IPV4);
        diagnosticData.systemData.network.IPv6 = NetworkManager.getLocalIpAddress(NetworkManager.TYPE_IPV6);
        diagnosticData.systemData.network.macAddress = NetworkManager.getMacAddress();
        diagnosticData.systemData.network.bluetoothAddress = NetworkManager.getBluetoothAddress();

        diagnosticData.systemData.time = new TIME();
        diagnosticData.systemData.time.standrandTimeZone = TimeManager.getGmtTimeZone();
        diagnosticData.systemData.time.GTimeZone = TimeManager.getGmtTime();

        //目前缺serviceStatus,cellularNetworkStatus,imeiSv,imelSlot1,imeiSvSlot1,imelSlot2,imeiSvSlot2
        diagnosticData.systemData.simStatus = new SimStatus();
        diagnosticData.systemData.simStatus.network = SimManager.getNetworkType();
        diagnosticData.systemData.simStatus.cellularNetworkType = SimManager.getCelluarNetworkType();
        diagnosticData.systemData.simStatus.operatorInfo = SimManager.getOperatorName();
        diagnosticData.systemData.simStatus.isRoaming = SimManager.getRomingStatus();
        diagnosticData.systemData.simStatus.phoneNumber = SimManager.getPhoneNumber();
        diagnosticData.systemData.simStatus.imei = SimManager.getDeviceImei();
        diagnosticData.systemData.simStatus.signalStrengh = SimManager.getSignalStrength();

        diagnosticData.application = new Application();
        diagnosticData.application.appInformations = ApplicationManager.getApplicationsList();
        diagnosticData.application.runningApplicationsInfo = ApplicationManager.getRunningApplications();
        diagnosticData.application.runningServicesInfo = ApplicationManager.getRunningServiceInfo();

        diagnosticData.security = new Security();

        DiagnosticHttpAPI.uploadDignosticData(diagnosticData, new RetrofitResultObserver() {
            @Override
            public void onSuccess(Object data) {
                LogTool.i(CommonConst.ModuleName.DIAGNOSTIC_MODULE_NAME,"Diagnostic", CommonConst.returnCode.CTMS_SUCCESS_STRING);
            }
            @Override
            public void onFailure(RetrofitResultObserver.RetrofitResultException e){
                super.onFailure(e);
                LogTool.i(CommonConst.ModuleName.DIAGNOSTIC_MODULE_NAME,"Diagnostic", CommonConst.returnCode.CTMS_DIAGNOSTIC_UPLOAD_FAIL);
            }
            @Override
            protected void onResultCode(int resultCode, Result result) {
                super.onResultCode(resultCode, result);
                LogTool.i(CommonConst.ModuleName.DIAGNOSTIC_MODULE_NAME,"Diagnostic", resultCode);

            }
        });

        //STRING長度太長 故分段顯示LOG

            String allData = new Gson().toJson(diagnosticData);

//        LogTool.d("diagnosticData :" + allData);
            if (allData.length() > 4000) {
                for (int i = 0; i < allData.length(); i += 4000) {

                    if (i + 4000 < allData.length()) {
                        LogTool.d("diagnosticData :" + i, allData.substring(i, i + 4000));
                    } else {
                        LogTool.d("diagnosticData :" + i, allData.substring(i, allData.length()));
                    }
                }
            }


        return new Gson().toJson(diagnosticData);
    }

    @Override
    public void init(Context context) {
        gpsManager = new GpsManager();
        Log.d("", "init diagnostic");
    }
}
