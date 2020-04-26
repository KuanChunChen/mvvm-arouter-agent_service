package k.c.module.config.debug;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.core.app.ActivityCompat;
import k.c.module.config.api.SettingCongigHttpAPI;
import k.c.module.config.model.SettingResult;
import k.c.module.config.model.SystemSettingInfo;
import k.c.module.config.model.SystemSettingResult;
import k.c.module.http.RetrofitResultObserver;


public class ConfigMainActivity extends Activity {

    private Button buttonTest1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_main);

        buttonTest1 = findViewById(R.id.btn_Test1);


        buttonTest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SystemSettingInfo systemSettingInfo = new SystemSettingInfo();
                systemSettingInfo.merchantID = "0000000000000045";
                systemSettingInfo.serialNumber = "0000000000000045";



                SettingCongigHttpAPI.getSystemSetting(systemSettingInfo, new RetrofitResultObserver<SystemSettingResult>() {
                    @Override
                    public void onSuccess(SystemSettingResult data) {

                    }
                });


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


}
