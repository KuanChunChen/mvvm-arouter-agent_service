package k.c.module.download.debug;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import k.c.common.lib.logTool.LogTool;
import k.c.module.commonbusiness.CommonSingle;
import k.c.module.commonbusiness.common.UpdateListManager;
import k.c.module.commonbusiness.listener.DownloadListener;
import k.c.module.commonbusiness.listener.GetInfoListener;
import k.c.module.commonbusiness.model.GetInfoRequestModel;
import k.c.module.commonbusiness.model.UpdateDataModel;
import k.c.module.commonbusiness.service.DownloadService;
import k.c.module.commonbusiness.service.GetInfoService;
import k.c.module.http.base.BaseHttpClientAPI;

public class DownloadMainActivity extends AppCompatActivity {

    @Autowired
    DownloadService downloadService;
    GetInfoService getInfoService = CommonSingle.getInstance().getGetInfoService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ARouter.getInstance().inject(this);
        verifyStoragePermissions(this);
        Button button = findViewById(R.id.btn_Test);
        Button clearBtn = findViewById(R.id.btn_Clear);
        Button compareBtn = findViewById(R.id.btn_Compare);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getInfoService.getInfo(new GetInfoRequestModel(), new GetInfoListener() {
//                    @Override
//                    public void getInfoSuccess(int fileId) {
//                        downloadService.download(1, 1, 253, new DownloadListener() {
//                            @Override
//                            public void onStart(int id) {
//
//                            }
//
//                            @Override
//                            public void onProgress(int currentLength) {
//                                LogTool.d("okhttp -- currentLength = %s", currentLength);
//                            }
//
//                            @Override
//                            public void onFinish(String path, long size, int id) {
//                                LogTool.d("okhttp -- path = %s size = %s", path, size);
//                            }
//
//                            @Override
//                            public void onFailure(int statusCode) {
//
//                            }
//                        });
//                        downloadService.downloadUpdateList(fileId, new DownloadListener() {
//                            @Override
//                            public void onStart() {
//
//                            }
//
//                            @Override
//                            public void onProgress(int currentLength) {
//
//                            }
//
//                            @Override
//                            public void onFinish() {
//
//                            }
//
//                            @Override
//                            public void onFailure(int statusCode) {
//
//                            }
//                        });
//                    }

//                    @Override
//                    public void getInfoFail(int code) {
//
//                    }
//
//                    @Override
//                    public void error() {
//
//                    }
//                });

            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadService.clearDownload();
            }
        });

        compareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateDataModel o1 = new UpdateDataModel();
                o1.id = 1;
                o1.status = 2;
                UpdateDataModel o2 = new UpdateDataModel();
                o2.id = 2;
                o2.status = 1;
                UpdateDataModel o3 = new UpdateDataModel();
                o3.id = 3;
                UpdateDataModel o4 = new UpdateDataModel();
                o4.id = 4;
                UpdateDataModel n1 = new UpdateDataModel();
                n1.id = 1;
                n1.status = 1;
                UpdateDataModel n2 = new UpdateDataModel();
                n2.id = 2;
                n2.status = 1;
                UpdateDataModel n5 = new UpdateDataModel();
                n5.id = 5;
                n5.status = 1;
                List<UpdateDataModel> oul = new ArrayList<>();
                List<UpdateDataModel> nul = new ArrayList<>();
                oul.add(o1);
                oul.add(o2);
                oul.add(o3);
                oul.add(o4);
                nul.add(n1);
                nul.add(n2);
                nul.add(n5);
                List<UpdateDataModel> ful = UpdateListManager.getMergeCommandList(oul,nul);
                for (UpdateDataModel updateDataModel : ful) {
                    LogTool.d("okhttp -- ful id = %s type = %s packageName = %s status = %s size = %s path = %s versionName = %s",
                            updateDataModel.id,
                            updateDataModel.type,
                            updateDataModel.name,
                            updateDataModel.status,
                            updateDataModel.size,
                            updateDataModel.path,
                            updateDataModel.version
                    );
                }

                List<UpdateDataModel> dul = UpdateListManager.getDiffCommandList(oul,nul);
                for (UpdateDataModel updateDataModel : dul) {
                    LogTool.d("okhttp -- dul id = %s type = %s packageName = %s status = %s size = %s path = %s versionName = %s",
                            updateDataModel.id,
                            updateDataModel.type,
                            updateDataModel.name,
                            updateDataModel.status,
                            updateDataModel.size,
                            updateDataModel.path,
                            updateDataModel.version
                    );
                }

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
