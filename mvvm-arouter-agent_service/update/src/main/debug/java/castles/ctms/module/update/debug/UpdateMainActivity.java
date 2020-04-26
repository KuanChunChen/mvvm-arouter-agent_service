package k.c.module.update.debug;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;

import java.io.File;

import k.c.common.lib.logTool.LogTool;
import k.c.module.commonbusiness.CommonSingle;
import k.c.module.commonbusiness.model.GetInfoDataModel;
import k.c.module.commonbusiness.po.BaseConfig;
import k.c.module.commonbusiness.po.EnableConfig;
import k.c.module.commonbusiness.service.DownloadService;
import k.c.module.update.model.ParameterData;
import k.c.module.update.module.ParameterManager;
import k.c.module.update.module.parameter.ParameterCallback;


public class UpdateMainActivity extends Activity {

    private Button btnGetInfo;
    private Button btnDownload;
    private TextView textView;
    private EditText editText_prm;
    private GetInfoDataModel getInfoDataModel;
    private GetInfoDataModel oldGetInfoDataModel;

    @Autowired
    public DownloadService downloadService;

    private int needDownloadSize = 0;
    private int completeDownloadSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        setContentView(R.layout.activity_update_main);
        textView = findViewById(R.id.text_result);
        btnGetInfo = findViewById(R.id.btn_getinfo);
        btnDownload = findViewById(R.id.btn_download);
        editText_prm = findViewById(R.id.moudle_prmPath);
        verifyStoragePermissions(this);

        String prmPath = "/sdcard/PRMTestForAP4_3.csv";
        editText_prm.setText(prmPath);

        btnGetInfo.setOnClickListener(view -> {
            BaseConfig baseConfig = CommonSingle.getInstance().getBaseConfig();
            baseConfig.communicationMode = 2;
            baseConfig.serialNumber = "3000000000000458";

            CommonSingle.getInstance().setBaseConfig(baseConfig);

            EnableConfig enableConfig = CommonSingle.getInstance().getEnableConfig();
            enableConfig.ctmsEnable = true;
            CommonSingle.getInstance().setEnableConfig(enableConfig);
            CommonSingle.getInstance().getPollingService().updateNow();
        });

        btnDownload.setOnClickListener(view -> {
            ParameterCallback parameterCallback = new ParameterCallback() {
                @Override
                public void onSuccess() {
                    LogTool.d("Test : "+"Success.");
                }

                @Override
                public void onFail(int errorCode) {
                    LogTool.d("Test : "+"failed.");
                }
            };
            String prmMainPath = editText_prm.getText().toString();
            LogTool.d("prmPath : " + prmMainPath);


            if (new File(prmMainPath).exists()) {

                ParameterManager parameterManager = new ParameterManager();
                parameterManager.setParameterCallback(parameterCallback);
                ParameterData parameterData = parameterManager.preparePrmData(prmMainPath);

                LogTool.d("Test all data : " + parameterData.toString());
                int status = parameterManager.saveParameterThroughKernel(prmMainPath, parameterData);
                textView.setText("Save parameter status :" + status);
            } else {
                textView.setText("Path " + prmMainPath + " Not exist");
            }

        });


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

    private void appendTextToResult(String result){
        String oldText = textView.getText().toString();
        textView.setText(oldText + "\r\n" + result);
    }
}
