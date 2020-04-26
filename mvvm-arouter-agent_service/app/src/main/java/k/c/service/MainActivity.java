package k.c.service;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import k.c.common.lib.Util.BatteryUtil;
import k.c.common.lib.Util.FileUtil;
import k.c.common.lib.logTool.LogTool;
import k.c.module.commonbusiness.CommonSingle;
import k.c.module.commonbusiness.common.CommonConst;
import k.c.module.commonbusiness.po.BaseConfig;
import k.c.module.commonbusiness.po.EnableConfig;
import k.c.module.update.battery.CommonBatteryManager;
import k.c.service.constants.Constants;
import k.c.service.jar.CtCtms;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

//import castles.ctms.module.diagnostics.module.logStatus.LocalLogStatus;


//import castles.ctms.module.configuration.module.ConfigManager;
//import static castles.ctms.module.configuration.constants.ConfigDefine.CONFIG_COMMUNICATE_TCP_IP;
//import static castles.ctms.module.configuration.constants.ConfigDefine.CONFIG_RETRY_COUNT;
//import static castles.ctms.module.configuration.constants.ConfigDefine.CONFIG_SERVER_COMPATIBLE;
//import static castles.ctms.module.configuration.constants.ConfigDefine.CONFIG_TERMINAL_SN;
//import static castles.ctms.module.configuration.constants.ConfigDefine.JSON_CTMS_CONFIG_REWRITE_B;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_1)
    TextView tv1;
    @BindView(R.id.btn_Test)
    Button btnTest;
    @BindView(R.id.btn_leave)
    Button btnLeave;
    @BindView(R.id.btn_Compare)
    Button btnCom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //init();
////        testModuleConfiguration();
////        testModuleDiagnostics();
//        testGetModuleUtil();
//        httpTest();
//        httpLambda();
//        testSysPwd();
        verifyStoragePermissions(this);
        EnableConfig enableConfig = CommonSingle.getInstance().getEnableConfig();
        enableConfig.ctmsEnable = true;
        enableConfig.bootConnectEnable = false;
        enableConfig.connectTime = 0;
        enableConfig.leaveTime = 0;
        CommonSingle.getInstance().setEnableConfig(enableConfig);

        BaseConfig baseConfig = CommonSingle.getInstance().getBaseConfig();
        baseConfig.serialNumber = "3300000000000458";
        baseConfig.communicationMode = 2;
        baseConfig.tlsHostIp = "218.211.35.212";
        baseConfig.tlsHostPort = CommonConst.HTTP_PORT;
        CommonSingle.getInstance().setBaseConfig(baseConfig);

        CommonBatteryManager commonBatteryManager = new CommonBatteryManager();
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CommonSingle.getInstance().getPollingService().updateNow();
                int aa = commonBatteryManager.getBatteryLevel();
                boolean bb = commonBatteryManager.isBatteryCharging();
                Toast.makeText(MainActivity.this, "getBatteryLevel ==" + aa, Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "isBatteryCharging ==" + bb, Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "getBatteryLevel ==" + BatteryUtil.getBatteryLevel(MainActivity.this), Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "charging ==" + BatteryUtil.isCharging(MainActivity.this), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void testAIDL(){
        Intent myIntent = new Intent(this, CtmsApiService.class);
        this.startService(myIntent);

    }

    private void testSysPwd(){
       String SYSTEM_PANEL_FIRST_PWD = "/data/First_Password_Hash";
       String SYSTEM_PANEL_SECOND_PWD = "/data/Second_Password_Hash";
        LogTool.d(FileUtil.isDirectoryExist(SYSTEM_PANEL_FIRST_PWD));

        LogTool.d(FileUtil.isDirectoryExist(SYSTEM_PANEL_SECOND_PWD));

    }
    private void makeDefaultTerminalFile() {
        String strTerminalTest = "{\n" +
                "  \"TermList\": [\n" +
                "    {\n" +
                "      \"termID\": \"30000000000000003\",\n" +
                "      \"rpreTID\": \"TID3000000003\",\n" +
                "      \"Model\": 2,\n" +
                "      \"pid\": \"PID30003\",\n" +
                "      \"Group\": \"PAN00000005\",\n" +
                "      \"Merchant\": \"GXQ00000006\",\n" +
                "      \"Store\": \"\",\n" +
                "      \"Memo\": \"zilywwwwwa\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        JSONArray mJsonArray = new JSONArray();
        try {
            for (int i = 1; i < 5001; i++) {
//                int tes = 0003;
                int add = i;


                if (add < 10 && add > 0) {
                    mJsonArray.put(new JSONObject("{\n" +
                            "      \"termID\": \"9999999999999000" + add + "\",\n" +
                            "      \"rpreTID\": \"TID500000000" + add + "\",\n" +
                            "      \"Model\": 2,\n" +
                            "      \"pid\": \"PID30003\",\n" +
                            "      \"Group\": \"PAN00000005\",\n" +
                            "      \"Merchant\": \"GXQ00000006\",\n" +
                            "      \"Store\": \"\",\n" +
                            "      \"Memo\": \"zilywwwwwa\"\n" +
                            "    }"));

                } else if (add > 9 && add < 100) {
                    mJsonArray.put(new JSONObject("{\n" +
                            "      \"termID\": \"999999999999900" + add + "\",\n" +
                            "      \"rpreTID\": \"TID50000000" + add + "\",\n" +
                            "      \"Model\": 2,\n" +
                            "      \"pid\": \"PID30003\",\n" +
                            "      \"Group\": \"PAN00000005\",\n" +
                            "      \"Merchant\": \"GXQ00000006\",\n" +
                            "      \"Store\": \"\",\n" +
                            "      \"Memo\": \"zilywwwwwa\"\n" +
                            "    }"));
                } else if (add < 1000 && add > 99) {
                    mJsonArray.put(new JSONObject("{\n" +
                            "      \"termID\": \"99999999999990" + add + "\",\n" +
                            "      \"rpreTID\": \"TID5000000" + add + "\",\n" +
                            "      \"Model\": 2,\n" +
                            "      \"pid\": \"PID30003\",\n" +
                            "      \"Group\": \"PAN00000005\",\n" +
                            "      \"Merchant\": \"GXQ00000006\",\n" +
                            "      \"Store\": \"\",\n" +
                            "      \"Memo\": \"zilywwwwwa\"\n" +
                            "    }"));

                } else {
                    mJsonArray.put(new JSONObject("{\n" +
                            "      \"termID\": \"9999999999999" + add + "\",\n" +
                            "      \"rpreTID\": \"TID500000" + add + "\",\n" +
                            "      \"Model\": 2,\n" +
                            "      \"pid\": \"PID30003\",\n" +
                            "      \"Group\": \"PAN00000005\",\n" +
                            "      \"Merchant\": \"GXQ00000006\",\n" +
                            "      \"Store\": \"\",\n" +
                            "      \"Memo\": \"zilywwwwwa\"\n" +
                            "    }"));
                }
            }

            JSONObject mObject = new JSONObject();

            mObject.put("TermList", mJsonArray);
            FileUtil.makeDefaultFile(FileUtil.getDefaultFolder(CtmsApplication.getInstance()) + "/term_5000.json");
            FileUtil.writeFile(FileUtil.getDefaultFolder(CtmsApplication.getInstance()) + "/term_5000.json", mObject.toString(1));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void makeDefaultDelieveryFile() {
        FileUtil.makeDefaultFile(FileUtil.getDefaultFolder(CtmsApplication.getInstance()) + "/delilnst_20190925013333.csv");
        String strAll = "";
        String strTest = "";
        for (int i = 1; i < 5001; i++) {
//            int tes = 0003;
            int add = i;
            if (add < 10 && add > 0) {
                strTest = "0,9999999999999000" + add + ",000001,,,201909011000,201909011000,00,0,0,0,0,,,,,,,0,\n\r" + "\r\n";
//                strTest = "1,," + "5000000000000000" + add+ ",,000000000001,0.0.1,201909011000,201909011000,00,201909151000,0,0,1,0,,,,,,,,0,\r\n";
            } else if (add > 9 && add < 100) {
                strTest = "0,999999999999900" + add + ",000001,,,201909011000,201909011000,00,0,0,0,0,,,,,,,0," + "\r\n";
//                strTest = "1,," + "500000000000000" + add + ",,000000000001,0.0.1,201909011000,201909011000,00,201909151000,0,0,1,0,,,,,,,,0,\r\n";
            } else if(add<1000 && add > 99){
                strTest = "0,99999999999990" + add + ",000001,,,201909011000,201909011000,00,0,0,0,0,,,,,,,0," + "\r\n";
//                strTest = "1,," + "50000000000000" + (tes + i) + ",,000000000001,0.0.1,201909011000,201909011000,00,201909151000,0,0,1,0,,,,,,,,0,\r\n";

            }else{
                strTest = "0,9999999999999" + add + ",000001,,,201909011000,201909011000,00,0,0,0,0,,,,,,,0," + "\r\n";
//                strTest = "1,," + "5000000000000" + (tes + i) + ",,000000000001,0.0.1,201909011000,201909011000,00,201909151000,0,0,1,0,,,,,,,,0,\r\n";
            }

            strAll = strAll + strTest;


        }
        Log.d("test :", strAll);
        FileUtil.writeFile(FileUtil.getDefaultFolder(CtmsApplication.getInstance()) + "/delilnst_20190925013333.csv", strAll);
    }

//    private void testModuleDiagnostics() {
//        LocalLogStatus.WriteLog(0x01, 0x01, 0x01);
//    }

//    private void testGetModuleUtil() {
//        GetModuleVersion.getModuleVersion(ID_EMV_SO, 37);
//        GetModuleVersion.getModuleVersion(ID_EMVCL_SO, 38);
//        LogTool.d(GetModuleVersion.getAMEVersion(ID_ANDROID_SDK));
//        LogTool.d(GetModuleVersion.getAMEVersion(ID_ANDROID_RELEASE));
//        LogTool.d(GetModuleVersion.getAMEVersion(ID_LITTLE_KERNEL));
//        LogTool.d(GetModuleVersion.getAMEVersion(ID_ANDROID_LINUX_KERNEL));
//    }






    private void testModuleConfiguration() {

//        AppSingle.getInstance().setDefalutPath(FileUtil.getDefaultFolder(this) + "/");
//        String strDefaultPath = AppSingle.getInstance().getDefalutPath();
//        ConfigManager.makeDefaultCtmsConfig();
//        ConfigManager.makeDefaultCtmsEnable();
//        ConfigManager.makeDefaultCtmsTrigger();
//        ConfigManager.makeDefaultCtmsActive();
//        Log.d("strDefaultPath : ", strDefaultPath);
//        ConfigManager.setCtmeEnable(1);
//        ConfigManager.setCM_Mode(2);
//        ConfigManager.resetAllConfig();
//        ConfigManager.setBootConnect(123);
//        ConfigManager.setBaseConfig(JSON_CTMS_CONFIG_REWRITE_B);
//        ConfigManager.setActiveTime(1);
//        ConfigManager.setConnectTime(2);
//        ConfigManager.setLeaveTime(3);
//        ConfigManager.setConfig(CONFIG_TERMINAL_SN, "0000000000000000");
//        ConfigManager.setConfig(CONFIG_SERVER_COMPATIBLE, "13");
//        ConfigManager.setConfig(CONFIG_RETRY_COUNT, "134");
//        ConfigManager.setConfig(CONFIG_COMMUNICATE_TCP_IP, "13.321.23.12");
//        Log.d("strDefaultPath : ", String.valueOf(ConfigManager.getConfig(CONFIG_TERMINAL_SN)));
//        Log.d("strDefaultPath : ", String.valueOf(ConfigManager.getConfig(CONFIG_SERVER_COMPATIBLE)));
//        Log.d("strDefaultPath : ", String.valueOf(ConfigManager.getConfig(CONFIG_RETRY_COUNT)));
//        Log.d("strDefaultPath : ", String.valueOf(ConfigManager.getConfig(CONFIG_COMMUNICATE_TCP_IP)));
//        Log.d("tesetste", ConfigManager.getBaseConfig());
//        tv1.setText(ConfigManager.getBaseConfig() + "\r\n" +
//                "CtmsEnable :" + ConfigManager.getCtmeEnable() + "\r\n" +
//                "getActiveTime : " + ConfigManager.getActiveTime() + "\r\n" +
//                "Cm_mode : " + ConfigManager.getCM_Mode() + "\r\n" +
//                "getSerialNumber : " + ConfigManager.getSerialNumber() + "\r\n" +
//                "getConnectTime : " + ConfigManager.getConnectTime() + "\r\n" +
//                "getLeaveTime : " + ConfigManager.getLeaveTime() + "\r\n" +
//                "getTriggerTime : " + ConfigManager.getTriggerTime() + "\r\n" +
//                "getBootConnect : " + ConfigManager.getBootConnect() + "\r\n");

    }

    CtCtms ctCtms;
    private void init() {
        verifyStoragePermissions(this);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ctCtms = new CtCtms(CommonLib.getAppContext());
                LogTool.i("Login", "Login", CommonConst.returnCode.CTMS_SUCCESS_STRING);
//                CommonSingle.getInstance().getPollingService().updateNow();
            }
        });
        btnLeave.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                LogTool.d("getAllConfig : " + ctCtms.getAllConfig());
                LogTool.d("getBootConnectStatus : " + ctCtms.getBootConnectStatus());
                LogTool.d("getCTMSStatus : " + ctCtms.getCTMSStatus());
                LogTool.d("CONFIG_TERMINAL_SN : " + ctCtms.getConfig(Constants.CONFIG.CONFIG_TERMINAL_SN));
                LogTool.d("CONFIG_SERVER_COMPATIBLE : " + ctCtms.getConfig(Constants.CONFIG.CONFIG_SERVER_COMPATIBLE));
                LogTool.d("CONFIG_RETRY_COUNT : " + ctCtms.getConfig(Constants.CONFIG.CONFIG_RETRY_COUNT));
                LogTool.d("CONFIG_DOWNLOAD_CONFIG : " + ctCtms.getConfig(Constants.CONFIG.CONFIG_DOWNLOAD_CONFIG));
                LogTool.d("CONFIG_COMMUNICATE_TCP : " + ctCtms.getConfig(Constants.CONFIG.CONFIG_COMMUNICATE_TCP));
                LogTool.d("CONFIG_COMMUNICATE_TLS : " + ctCtms.getConfig(Constants.CONFIG.CONFIG_COMMUNICATE_TLS));
                LogTool.d("CONFIG_COMMUNICATE_USB : " + ctCtms.getConfig(Constants.CONFIG.CONFIG_COMMUNICATE_USB));
                LogTool.d("CONFIG_COMMUNICATE_NAC : " + ctCtms.getConfig(Constants.CONFIG.CONFIG_COMMUNICATE_NAC));
                LogTool.d("getActiveTime : " + new SimpleDateFormat().format(ctCtms.getActiveTime().getTime()));
                LogTool.d("getTrigger : " + new SimpleDateFormat().format(ctCtms.getTrigger().getTime()));
                LogTool.d("getConnectTime : " + new SimpleDateFormat().format(ctCtms.getConnectTime().getTime()));
                LogTool.d("getLeaveTime : " + new SimpleDateFormat().format(ctCtms.getLeaveTime().getTime()));
                LogTool.d("getUpdateList : " + ctCtms.getUpdateList());
                LogTool.d("UpdateActive : " + ctCtms.UpdateActive());
                LogTool.d("ResetFolder : " + ctCtms.ResetFolder());


                //                finish();
            }
        });
    }

    private void httpLambda() {

        String strMyCsv =
                "--YY8CBaVruPGDVTpIhyxGBTcF_oY_xOleL\n" +
                        "Content-Disposition: form-data; name=CMD\n" +
                        "Content-Type: text/plain; charset=US-ASCII\n" +
                        "Content-Transfer-Encoding: 8bit\n" +
                        "\n" +
                        "{\"Authority\":{\"UserName\":\"admin\",\"Password\":\"castles8418\"},\"FuncName\":\"CREATEDELIVERY\",\"CreateMode\":2}\n" +
                        "--YY8CBaVruPGDVTpIhyxGBTcF_oY_xOleL\n" +
                        "Content-Disposition: form-data; name=\"Upload\"; filename=\"20190725140355118.csv\"\n" +
                        "Content-Type: application/octet-stream\n" +
                        "Content-Transfer-Encoding: binary\n" +
                        "\n" +
                        "<actual file content, not shown here>\n" +
                        "--YY8CBaVruPGDVTpIhyxGBTcF_oY_xOleL--";
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("CMD",
                        "{\"Authority\":{\"UserName\":\"admin\",\"Password\":\"castles8418\"},\"FuncName\":\"CREATEDELIVERY\",\"CreateMode\":2}\n")
                .addFormDataPart("Upload", "20190725140355118.csv", RequestBody.create(MediaType.parse("text/csv"),
                        strMyCsv))
                .build();
//        CTMSHttpApi.csvPost(requestBody, new RetrofitResultObserver<resultLambda>() {
//            @Override
//            public void onSuccess(resultLambda data) {
//                Log.d("testasdf","success!!");
//            }
//        });

    }

    private void httpTestLogin() {



//        LoginInfo mLoginInfo = new Gson().fromJson(Constants.JSON_LOGIN_INFO, LoginInfo.class);
//        CTMSHttpApi.getLogin(mLoginInfo, new RetrofitObserver<LoginResult>() {
//            @Override
//            public void onNext(LoginResult loginResult) {
//                if (loginResult == null){
//                    LogTool.d("onNext :" + loginResult.toString());
//
//                    return;
//                }
//            }
//
//            @Override
//            public void onSuccess(LoginResult data) {
//                if (data == null){
//                    LogTool.d(data.toString());
//
//                    return;
//                }
//            }
//        });
    }


//    private void downloadHSTest() {
//        LoginInfo mLoginInfo = new Gson().fromJson(Constants.JSON_LOGIN_INFO, LoginInfo.class);
//        CTMSHttpApi.getLogin(mLoginInfo, new RetrofitObserver<LoginResult>() {
//            @Override
//            public void onNext(LoginResult loginResult) {
//                if (loginResult == null){
//                    LogTool.d("onNext :" + loginResult.toString());
//
//                    return;
//                }
//            }
//
//            @Override
//            public void onSuccess(LoginResult data) {
//                if (data == null){
//                    LogTool.d(data.toString());
//
//                    return;
//                }
//            }
//        });
//    }
    private void httpTest() {

//        Log.d("MyTEST", Constants.JSON_LOGIN_INFO);
//        LoginInfo mLoginInfo = new Gson().fromJson(Constants.JSON_LOGIN_INFO, LoginInfo.class);
////        CommonBatteryManager.openBatteryManager();
////        Log.d("boolean", String.valueOf(CommonBatteryManager.battryProtect()));
//
////        try {
////            mLoginInfo.mCM = EncrptyUtil.encrypt(String.valueOf(mLoginInfo.mCM));
////            mLoginInfo.setSn(EncrptyUtil.encrypt(mLoginInfo.getSn()));
////            mLoginInfo.setData(EncrptyUtil.encrypt(mLoginInfo.getDataToString()));
////        } catch (UnsupportedEncodingException e) {
////            e.printStackTrace();
////        }
////        GetInfo mGetInfo = new GetInfo();
////
////        String json = new Gson().toJson(mGetInfo);
////        Log.d("teswetst", json);
//        String strPath = "";
//
//        Call<byte[]> call = null;
//        try {
//            call = httpByte().postLoginTest(strPath, mLoginInfo.mSN, mLoginInfo.mCM, EncrptyUtil.encrypt(mLoginInfo.mData.toString()));
//            call.enqueue(new Callback<byte[]>() {
//                @Override
//                public void onResponse(Call<byte[]> call, Response<byte[]> response) {
//                    String result = new String(response.body());
//                    Log.i("TESTSETSATSDGF", String.valueOf(response.body()[0]));
//                    Log.i("TESTSETSATSDGF", String.valueOf(response.body()[1]));
//                    try {
//                        Log.i("TESTSETSATSDGF", EncrptyUtil.decrypt(result));
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                    Log.d("Response code:", "===========" + response.code());
//
//                    Log.d("==Cookie==1===", "===" + response.raw().request().headers().get("Set-Cookie"));
//                    Log.d("==Cookie==2==", "===" + response.headers().get("Set-Cookie"));
//                    Log.d("==Content-Type====", "===" + response.headers().get("Content-Type"));
//                }
//
//                @Override
//                public void onFailure(Call<byte[]> call, Throwable t) {
//                    Log.d("==Content-Type====", "===" + "fasdfafdsafsadf");
//
//                }
//            });
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
////        } catch (JSONException e) {
////            e.printStackTrace();
//        } catch(Exception e){
//            e.printStackTrace();
//            Log.d("testst", "aeras");
//        }
//
//
//
////        try {
////            mLoginInfo.setCM(EncrptyUtil.encrypt(mLoginInfo.getCM()));
////            mLoginInfo.setSn(EncrptyUtil.encrypt(mLoginInfo.getSn()));
////            LoginInfo.DATA mData = mLoginInfo.getData();
//////            mLoginInfo.setData(EncrptyUtil.encrypt());
////            Log.d("tesag14ts", new Gson().toJson(mData));
////
////
////        } catch (UnsupportedEncodingException e) {
////            e.printStackTrace();
////        }
////        String strConfig = new Gson().toJson(mLoginInfo);
////        Log.d("MyTEST", strConfig);
////
////
////        try {
////            String strTest = EncrptyUtil.encrypt(strConfig);
////            Log.d("testst4est : ", strTest);
////
////
////            Log.d("33333 : ", EncrptyUtil.decrypt(strTest));
////        } catch (UnsupportedEncodingException e) {
////            e.printStackTrace();
////        }
//
//
////        CTMSHttpApi.getInfo(mGetInfo,new RetrofitResultObserver<LoginResult>() {
////
////
////            @Override
////            public void onSuccess(LoginResult data) {
//////                Log.d("taetteste : ", new Gson().toJson(data));
////            }
////            @Override
////            public void onFailure(RetrofitResultObserver.RetrofitResultException e) {
////                super.onFailure(e);
////            }
////        });
//
////        GetAndroidInfo.getApplicationInfo();
////        CTMSHttpApi.getLogin(mLoginInfo,new RetrofitObserver<LoginResult>() {
////
////
////            @Override
////            public void onNext(LoginResult loginResult) {
////
////            }
////
////            @Override
////            public void onSuccess(LoginResult data) {
////                Log.d("taetteste : ", new Gson().toJson(data));
////            }
////            @Override
////            public void onFailure(RetrofitResultObserver.RetrofitResultException e) {
////                super.onFailure(e);
////            }
////        });

    }

    private String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    public void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
