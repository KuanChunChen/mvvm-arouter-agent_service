package k.c.module.getinfo.debug;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;

import java.util.concurrent.TimeUnit;

import k.c.common.lib.logTool.LogTool;
import k.c.module.commonbusiness.CommonSingle;
import k.c.module.commonbusiness.listener.GetInfoListener;
import k.c.module.commonbusiness.model.GetInfoRequestModel;
import k.c.module.commonbusiness.service.GetInfoService;
import k.c.module.commonbusiness.service.PollingService;
import k.c.module.getinfo.api.GetInfoAPI;
import k.c.module.getinfo.api.GetInfoHttpAPI;
import k.c.module.http.RetrofitClient;
import k.c.module.http.model.HttpConfig;

public class GetInfoMainActivity extends AppCompatActivity {

//    GetInfoService getInfoService = CommonSingle.getInstance().getGetInfoService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ARouter.getInstance().inject(this);
        verifyStoragePermissions(this);
        Button button = findViewById(R.id.btn_Test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PollingService pollingService = CommonSingle.getInstance().getPollingService();
                pollingService.updateNow();
//                List<GetInfo.INFO.System> mGetInfoDataInfo  =new GetSystemInfoManager().getSystemInfo();
//                for (GetInfo.INFO.System test : mGetInfoDataInfo) {
//                    LogTool.d("test : " + test.toString());
//                }
//                getInfoService.getInfo(new GetInfoRequestModel(), new GetInfoListener() {
//                    @Override
//                    public void getInfoSuccess() {
//
//                    }
//
//                    @Override
//                    public void getInfoFail(int code) {
//
//                    }
//
//                    @Override
//                    public void httpError() {
//
//                    }
//                });
            }
        });


//        GetInfoRequest getInfo = new GetInfoRequest();
//        getInfo.serialNum = "9999999999999999";
//        getInfo.data.sessionId = CommonSingle.getInstance().getSessionId();
//        getInfo.data.infoVersion = "JMeter_TestInfo";
//        getInfo.data.dataExchange = 0;
//        getInfo.data.dlConfig = 0;
//        getInfo.data.info.device.btnUseCount = 10050;
//        getInfo.data.info.device.changeTime = 15000;
//        getInfo.data.info.device.contactCardCount = 100;
//        getInfo.data.info.device.contactlessCardCount = 150;
//        getInfo.data.info.device.msrCount = 350;
//        getInfo.data.info.device.mCL_HOUR = 120;
//        getInfo.data.info.device.mCT_HOUR = 50;
//        getInfo.data.info.device.mMSR_HOUR = 80;
//        getInfo.data.info.device.printerUseLen = 900;
//        getInfo.data.info.device.screenTime = 25000;
//        GetInfoRequest.INFO.System system = getInfo.data.info.new System();
//        system.fileName = "Castles_App";
//        system.fileType = 0x01;
//        system.fileVersion = "1234";
//        system.mFVC = 5;
//        system.mIT = "1534831554";
//        system.mPN = "com.castech.Castles_App";
//        system.mUT = "1534831654";
//        getInfo.data.info.systemList.add(system);
//
//        GetInfoHttpAPI.resetHttpClient(GetInfoAPI.class, GetInfoHttpAPI.getBuilder(GetInfoAPI.class).addInterceptor(chain -> {
//            Request original = chain.request();
//            String credential = "PHPSESSID=" + CommonSingle.getInstance().getSessionId();
//            Request.Builder requestBuilder = original.newBuilder()
//                    .addHeader("cookie", credential);
//            Request request = requestBuilder.build();
//            return chain.proceed(request);
//        }));
//        GetInfoHttpAPI.getInfo(getInfo, new RetrofitResultObserver<GetInfoResult>() {
//            @Override
//            public void onSuccess(GetInfoResult data) {
//
//            }
//        });
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
