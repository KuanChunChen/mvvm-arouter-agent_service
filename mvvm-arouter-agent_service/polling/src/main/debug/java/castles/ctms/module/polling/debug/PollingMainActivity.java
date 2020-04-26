package k.c.module.polling.debug;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Autowired;

import java.util.Iterator;
import java.util.List;

import k.c.common.lib.Util.TimeUtil;
import k.c.common.lib.logTool.LogTool;
import k.c.module.commonbusiness.CommonSingle;
import k.c.module.commonbusiness.common.CommonFileTool;
import k.c.module.commonbusiness.listener.PollingListener;
import k.c.module.commonbusiness.model.UpdateDataModel;
import k.c.module.commonbusiness.service.PollingService;
import k.c.module.polling.constants.Constants;
import k.c.module.polling.module.polling.PollingManager;
import io.reactivex.Observable;


public class PollingMainActivity extends Activity {

    private Button btnTest;
    private Button btnTest2;
    public Normal_New mNormal_New = new Normal_New();
    public Observable mObservable;

    @Autowired
    PollingService pollingService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnTest = findViewById(R.id.btn_Test);
        btnTest2 = findViewById(R.id.btn_Clear);
//        verifyStoragePermissions(this);
        pollingService = CommonSingle.getInstance().getPollingService();

        LogTool.d("CT : " + TimeUtil.getCurrentSysMillisTime());

//        new PollingManager().startPolling(Constants.Mode.POLLING_MODE, 10, new PollingListener() {
//            @Override
//            public void pollingFinish() {
//                LogTool.d("pollingFinish.");
//            }
//
//            @Override
//            public void pollingFail(int errorCode) {
//                LogTool.d("pollingFail.");
//            }
//        });



        btnTest.setOnClickListener(view -> {
            LogTool.d("CT  button: " + TimeUtil.getCurrentSysMillisTime());
            LogTool.d("test" + CommonFileTool.getInfoData().toString());
//
            List<UpdateDataModel> updateList = CommonFileTool.getUpdateList(CommonFileTool.getInfoData().updateListId);
            Iterator it1 = updateList.iterator();
            while (it1.hasNext()) {
                LogTool.d("test ", it1.next().toString());
            }


        });

        btnTest2.setOnClickListener(view -> {
            new Normal_New().updateNow();
        });
    }

//    private String[] PERMISSIONS_STORAGE = {
//            "android.permission.READ_EXTERNAL_STORAGE",
//            "android.permission.WRITE_EXTERNAL_STORAGE" };
//
//    public void verifyStoragePermissions(Activity activity) {
//        try {
//            //检测是否有写的权限
//            int permission = ActivityCompat.checkSelfPermission(activity,
//                    "android.permission.WRITE_EXTERNAL_STORAGE");
//            if (permission != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,1);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
