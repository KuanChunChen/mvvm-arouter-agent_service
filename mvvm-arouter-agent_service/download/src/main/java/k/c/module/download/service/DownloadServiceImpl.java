package k.c.module.download.service;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;

import org.reactivestreams.Publisher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import k.c.common.lib.Util.FileFastUtil;
import k.c.common.lib.Util.FileUtil;
import k.c.module.commonbusiness.CommonSingle;
import k.c.module.commonbusiness.base.BaseServiceImpl;
import k.c.module.commonbusiness.common.CommonConst;
import k.c.module.commonbusiness.listener.DownloadListener;
import k.c.module.commonbusiness.model.DownloadResultModel;
import k.c.module.commonbusiness.model.UpdateDataModel;
import k.c.module.commonbusiness.service.DownloadService;
import k.c.module.download.api.DownloadAPI;
import k.c.module.download.api.DownloadHttpApi;
import k.c.module.download.download.DownloadManager;
import k.c.module.download.model.UpdateList;
import k.c.module.http.interceptor.RetryRequestByFaild;
import okhttp3.ConnectionPool;
import okhttp3.Request;

@Route(path = "/download/downloadService")
public class DownloadServiceImpl extends BaseServiceImpl implements DownloadService{

    private DownloadManager downloadManager = new DownloadManager();

    @Override
    public void init(Context context) {
        RetryRequestByFaild retryRequestByFaild = new RetryRequestByFaild.Builder().retryInterval(10000).executionCount(30).build();
        DownloadHttpApi.resetHttpClient(DownloadAPI.class,
                DownloadHttpApi.reBuilder(false, retryRequestByFaild)
                    .connectionPool(new ConnectionPool(1, 60, TimeUnit.SECONDS))
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        String credential = "PHPSESSID=" + CommonSingle.getInstance().getSessionId();
                        Request.Builder requestBuilder = original.newBuilder()
                                .addHeader("cookie", credential);
                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                }));
    }

    @Override
    public void downloadUpdateList(int updateListFileId, DownloadListener downloadListener) {
        downloadManager.excuteDownload(CommonConst.DownloadConst.DOWNLOAD_MODE_SLOW, updateListFileId, CommonConst.Http.UPDATELIST_FILE_TYPE, downloadListener);
    }

    @Override
    public Publisher<DownloadResultModel> download(int downloadMode, int fileId, int fileType) {
        return downloadManager.warpExecuteDownload(downloadMode, fileId, fileType);
    }

    @Override
    public void download(int downloadMode, int fileId, int fileType, DownloadListener downloadListener) {
        downloadManager.excuteDownload(downloadMode, fileId, fileType, downloadListener);
    }

    @Override
    public void clearDownload() {
        FileUtil.deleteFile(new File(CommonConst.FileConst.DOWNLOAD_FILE_SAVE_PATH));
//        FileUtil.deleteFile(new File(Constants.File.LOG_STATUS_FILE_PATH));
    }

    @Override
    public List<UpdateDataModel> convertUpdateInfo(){
        List<UpdateDataModel> updateDataModelList = new ArrayList<>();
        try {
//            int updateListId = Objects.requireNonNull(CommonFileTool.getInfoData()).updateListId;
            String content = FileFastUtil.fileReader(CommonConst.Http.UPDATELIST_FILE_PATH + CommonConst.Http.UPDATELIST_FILE_NAME);
            UpdateList updateList = new Gson().fromJson(content, UpdateList.class);
            for (UpdateList.UpdateInfo updateInfo : updateList.updateInfoList) {
                UpdateDataModel updateDataModel = new UpdateDataModel();
                updateDataModel.id = updateInfo.id;
                //updateDataModel.status = updateInfo.status;
                updateDataModel.type = updateInfo.type;
                updateDataModel.sType = updateInfo.sType;
                updateDataModel.packageName = updateInfo.rootApName;
                updateDataModel.versionName = updateInfo.fileVersion;
                updateDataModelList.add(updateDataModel);
            }
            return updateDataModelList;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
