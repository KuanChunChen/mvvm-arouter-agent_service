package k.c.module.download.download;

import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import org.reactivestreams.Publisher;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import k.c.common.lib.Util.FileFastUtil;
import k.c.common.lib.Util.FileUtil;
import k.c.common.lib.Util.SHA256;
import k.c.common.lib.logTool.LogTool;
import k.c.module.commonbusiness.CommonSingle;
import k.c.module.commonbusiness.common.CommonConst;
import k.c.module.commonbusiness.common.CommonFileTool;
import k.c.module.commonbusiness.listener.DownloadListener;
import k.c.module.commonbusiness.model.DownloadResultModel;
import k.c.module.download.api.DownloadHttpApi;
import k.c.module.download.model.DownloadFileInfo;
import k.c.module.download.model.DownloadHandshakeInfo;
import k.c.module.download.model.DownloadHandshakeResult;
import k.c.module.http.DownloadResultObserver;
import k.c.module.http.Result;
import k.c.module.http.RetrofitResultObserver;
import k.c.module.http.model.DownloadFileResult;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import okio.Buffer;
import okio.BufferedSource;

public class DownloadManager {

    public final Map<String, Future> downloadMap = new HashMap<>();

    public ExecutorService downloadExecutor;

    private long requestTime = 0;
    private int retryTimes = 0;
    public int handlerStatus;
    private boolean isStarting = false;

    public DownloadManager(){
        this.downloadExecutor = new ThreadPoolExecutor(
                1,
                1,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new DownloadThreadFactory());
    }

    public Publisher<DownloadResultModel> warpExecuteDownload(int downloadMode, int fileId, int fileType){
        return Flowable.create(emitter -> excuteDownload(downloadMode, fileId, fileType, new DownloadListener() {
            @Override
            public void onStart(int id) {
                LogTool.d("warp onStart");
                emitter.onNext(DownloadResultModel.buildOnStartModel(fileId));
            }

            @Override
            public void onProgress(int currentLength) {
                LogTool.d("warp onProgress");
                emitter.onNext(DownloadResultModel.buildOnProgressModel(currentLength));
            }

            @Override
            public void onFinish(String path, long size, int id) {
                LogTool.d("warp onFinish");
                emitter.onNext(DownloadResultModel.buildOnFinishModel(id, path, size));
            }

            @Override
            public void onFailure(int statusCode) {
                LogTool.d("warp onFailure");
                emitter.onNext(DownloadResultModel.buildOnFailureModel(statusCode));
            }
        }), BackpressureStrategy.BUFFER);
    }

    public void excuteDownload(int downloadMode, int fileId, int fileType, DownloadListener downloadListener){
        synchronized (downloadMap){
            Future executeFuture = downloadMap.get(fileId + ":" + fileType);
            if(executeFuture == null){
                Future future = downloadExecutor.submit(() -> {
                    handlerStatus = 1;
                    LogTool.d("acquire downloadWakeLock");
                    startHandShake(downloadMode, fileId, fileType, downloadListener);
                    LogTool.d("handlerStatus loop wait start");
                    for (;;){
                        if(handlerStatus != 0){
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            continue;
                        }
                        downloadMap.remove(fileId + ":" + fileType);

                        LogTool.d("handlerStatus loop wait end");
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                });
                downloadMap.put(fileId + ":" + fileType, future);
            }else{
                LogTool.d("has exist the command on the download map.");
            }
        }
    }

    private void startHandShake(int downloadMode, int fileId, int fileType, DownloadListener downloadListener){
        DownloadHandshakeInfo downloadHandshakeInfo = new DownloadHandshakeInfo();
        downloadHandshakeInfo.data.fileId = fileId;
        downloadHandshakeInfo.data.fileType = fileType;
        downloadHandshakeInfo.serialNum = CommonSingle.getInstance().getBaseConfig().serialNumber;
        downloadHandshakeInfo.data.sessionId = CommonSingle.getInstance().getSessionId();
        DownloadHttpApi.downloadHandShake(downloadHandshakeInfo, CommonConst.Http.HTTP_PHPSESSID + CommonSingle.getInstance().getSessionId(),new RetrofitResultObserver<DownloadHandshakeResult>() {
            @Override
            public void onSuccess(DownloadHandshakeResult result) {
                if (result == null){
                    LogTool.d("Handshake failed.");
                    failuerDownload(downloadListener, CommonConst.returnCode.CTMS_DOWNLOAD_HANDSHAKE_FAIL);
                    return;
                }
                File mFile;
                String type = result.file.path.substring(result.file.path.lastIndexOf(".") + 1);
                if (fileType == CommonConst.FileType.FILE_TYPE_UPDATELIST){
                    FileFastUtil.deleteFile(CommonConst.Http.UPDATELIST_FILE_PATH + CommonConst.Http.UPDATELIST_FILE_NAME);
                    mFile = FileUtil.fileInit(CommonConst.Http.UPDATELIST_FILE_PATH, CommonConst.Http.UPDATELIST_FILE_NAME);
                }else {
                    mFile = FileUtil.fileInit(CommonConst.FileConst.DOWNLOAD_FILE_SAVE_PATH, result.file.checksum + "." + type);
                }
                if (!isEnoughForDownload(result.file.fileSize)){
                    LogTool.d("Terminal does not have enough space to download.");
                    failuerDownload(downloadListener, CommonConst.returnCode.CTMS_TERMINAL_LEFT_SPACE_NOT_ENOUGH);
                    return;
                }
                isStarting = false;
                downloadListener.onStart(fileId);
                LogTool.d("Start check complete of the download file");
                requestTime = System.currentTimeMillis();
                startDownload(mFile, result, fileId, fileType, downloadMode, downloadListener);
            }

            @Override
            public void onFailure(RetrofitResultObserver.RetrofitResultException e){
                super.onFailure(e);
                LogTool.i(CommonConst.ModuleName.DOWNLOAD_MODULE_NAME,"Handshake", CommonConst.returnCode.CTMS_DOWNLOAD_HANDSHAKE_FAIL);
                failuerDownload(downloadListener, CommonConst.returnCode.CTMS_DOWNLOAD_HANDSHAKE_FAIL);
            }

            @Override
            protected void onResultCode(int resultCode, Result result) {
                super.onResultCode(resultCode, result);
                LogTool.i(CommonConst.ModuleName.DOWNLOAD_MODULE_NAME,"Handshake", resultCode);
                failuerDownload(downloadListener, CommonConst.returnCode.CTMS_DOWNLOAD_HANDSHAKE_FAIL);
            }
        });
    }

    private void startDownload(File mFile, DownloadHandshakeResult result, int fileId, int fileType, int downloadMode, DownloadListener downloadListener){
        if (!isStarting){
            if (mFile.length() == result.file.fileSize){
                String checksum = "";
                try {
                    checksum = SHA256.encryptFile(mFile.getAbsolutePath());
                }catch (IOException e){
                    LogTool.d("Create checksum error, error = %s", e.getMessage());
                    e.printStackTrace();
                }

                LogTool.d("Start checksum of the download file");
                if (result.file.checksum == null){
                    return;
                }
                if (result.file.checksum.toUpperCase().equals(checksum)){
                    LogTool.d("Checksum pass, checksum = " + checksum);
                    int unZipResult = FileUtil.unZipFolder(mFile.getAbsolutePath(), mFile.getParent() + "/" + checksum);
                    String capLocal = "";

                    switch (unZipResult){
                        case k.c.common.lib.constants.Constants.File.FILE_NOT_NEED_UNZIP:
                            LogTool.d("File dont need unzip.");
                            break;
                        case k.c.common.lib.constants.Constants.File.FILE_UNZIP_SUCCESS:
                            if (fileType == CommonConst.FileType.FILE_TYPE_PRM){
                                capLocal = mFile.getParent() + "/" + result.file.checksum;
                                break;
                            }
                            capLocal = findCapLocalOnFile(mFile.getParent() + "/" + result.file.checksum);
                            LogTool.d("UnZip success.");
                            if(TextUtils.isEmpty(capLocal)){
                                LogTool.d("cap file is empty or not complete");
                                FileFastUtil.delete(mFile.getAbsolutePath());
                                FileFastUtil.delete(mFile.getParent() + "/" + result.file.checksum);
                                failuerDownload(downloadListener,6);
                                return;
                            }
                            break;
                        case k.c.common.lib.constants.Constants.File.FILE_UNZIP_FAIL:
                            LogTool.d("UnZip failed.");
                            FileUtil.deleteFile(mFile.getAbsoluteFile());
                            break;
                    }
                    downloadListener.onFinish(capLocal, result.file.fileSize, fileId);
                    handlerStatus = 0;
                    return;
                }else {
                    if (retryTimes >= CommonConst.Http.DOWNLOAD_RETRY_TIMES){
                        LogTool.d("retry times out, retry = %d", retryTimes);
                        LogTool.i(CommonConst.ModuleName.DOWNLOAD_MODULE_NAME, "Download", CommonConst.returnCode.CTMS_DOWNLOAD_CHECKSUM_FAIL);
                        failuerDownload(downloadListener, CommonConst.returnCode.CTMS_DOWNLOAD_RETRY_TIMES_OUT);
                        return;
                    }
                    retryTimes++;
                    LogTool.d("Checksum failed.");
                    FileUtil.deleteFile(mFile.getAbsoluteFile());
//                    failuerDownload(downloadListener, castles.ctms.module.commonbusiness.constants.Constants.returnCode.CTMS_DOWNLOAD_CHECKSUM_FAIL);
                }
            }else if (mFile.length() > result.file.fileSize){
                LogTool.d("Size error.");
                FileUtil.deleteFile(mFile.getAbsoluteFile());
            }
            DownloadFileInfo downloadFileInfo = new DownloadFileInfo();
            downloadFileInfo.serialNum = CommonSingle.getInstance().getBaseConfig().serialNumber;
            downloadFileInfo.data.sessionId = CommonSingle.getInstance().getSessionId();
            downloadFileInfo.data.filePath = result.file.path;
            downloadFileInfo.data.downloadPosition = mFile.length();
            downloadFileInfo.data.downloadSize = result.file.fileSize;
            String range;
            if (CommonConst.DownloadConst.DOWNLOAD_MODE_FAST == downloadMode) {
                long maxSize = result.file.fileSize;
                if(maxSize >= CommonConst.FileConst.DOWNLOAD_FAST_MODE_MAX_SIZE){
                    maxSize = CommonConst.FileConst.DOWNLOAD_FAST_MODE_MAX_SIZE;
                }
                range = String.format("bytes=%s-%s", downloadFileInfo.data.downloadPosition, downloadFileInfo.data.downloadPosition + maxSize);
            }else {
                range = String.format("bytes=%s-%s", downloadFileInfo.data.downloadPosition, downloadFileInfo.data.downloadPosition + (CommonFileTool.getInfoData().wifiTransmissionSize - 1));
            }
            LogTool.d("download range = %s", range);
            DownloadHttpApi.downloadFile(downloadFileInfo.data.filePath, range, CommonSingle.getInstance().getSessionId(), new DownloadResultObserver(){
                @Override
                public void onSuccess(DownloadFileResult downloadResult, boolean isBigData){
                    LogTool.d("okhttp -- isBigData = %s", isBigData);
                    boolean appendOk = false;
                    if(isBigData){
                        if(downloadResult.responseBody == null || downloadResult.responseBody.source() == null || downloadResult.responseBody.contentLength() <= 0){
                            LogTool.d("Request failed.");
                            failuerDownload(downloadListener, CommonConst.returnCode.CTMS_DOWNLOAD_REQUEST_FAIL);
                            return;
                        }
                        try {
                            BufferedSource source = downloadResult.responseBody.source();
                            Buffer buffer = new Buffer();
                            long temp = 0;
                            while((temp = source.read(buffer, CommonConst.FileConst.DOWNLOAD_BIG_DATA_SIZE)) != -1){
                                LogTool.d("temp : " + temp);
                                appendOk = FileUtil.appendFileContent(mFile, buffer.readByteArray());
                                if(appendOk){
                                    buffer.clear();
                                    LogTool.d("okhttp -- append buffer ok");
                                }else{
                                    LogTool.d("okhttp -- append buffer fail");
                                    break;
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(downloadResult.data == null || downloadResult.data.length <= 0){
                            failuerDownload(downloadListener, CommonConst.returnCode.CTMS_DOWNLOAD_DATA_ERROR);
                            return;
                        }
                        appendOk = FileUtil.appendFileContent(mFile, downloadResult.data);
                    }

                    if (appendOk){
                        LogTool.d("okhttp -- length = %s",mFile.length());
                        int currentLength = (int) (100 * mFile.length() / result.file.fileSize);
                        downloadListener.onProgress(currentLength);
                        isStarting = false;
                    }
                    startDownload(mFile, result, fileId, fileType, downloadMode, downloadListener);
                }

                @Override
                public void onFailure() {
                    failuerDownload(downloadListener, CommonConst.returnCode.CTMS_DOWNLOAD_FAIL);
                    super.onFailure();
                }

                @Override
                protected void onResultCode(short resultCode) {
                    failuerDownload(downloadListener, resultCode);
                    super.onResultCode(resultCode);
                    LogTool.i(CommonConst.ModuleName.DOWNLOAD_MODULE_NAME,"Download",resultCode);
                }

                @Override
                public void onError(Throwable throwable) {
                    failuerDownload(downloadListener, CommonConst.returnCode.CTMS_DOWNLOAD_FAIL);
                    super.onError(throwable);
                    LogTool.i(CommonConst.ModuleName.DOWNLOAD_MODULE_NAME,"Download", CommonConst.returnCode.CTMS_DOWNLOAD_FAIL);
                }
            });
        }else {
            LogTool.d("Download running");
        }
    }

    private void failuerDownload(DownloadListener downloadListener, int statusCode){
        downloadListener.onFailure(statusCode);
        handlerStatus = 0;
        LogTool.d("Download failure");
    }

    public boolean isEnoughForDownload(long downloadSize)    {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        int avCounts = statFs.getAvailableBlocks();
        LogTool.d("avCounts = %s", avCounts);
        long blockSize = statFs.getBlockSize();
        LogTool.d("blockSize = %s", blockSize);
        long spaceLeft = avCounts * blockSize;
        LogTool.d("spaceLeft = %s", spaceLeft);
        LogTool.d("downloadSize = %s", downloadSize);
        return spaceLeft > downloadSize;
    }

    private String findCapLocalOnFile(String folderName){
        try {
            File dir = new File(folderName);
            if(!dir.exists()){
                return "";
            }
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if(file.isDirectory()){
                        return findCapLocalOnFile(file.getAbsolutePath());
                    }else{
                        String fileName = file.getName();
                        if (fileName.endsWith(".mci")) {
                            LogTool.d("Find mci and cap file");
                            String capName = FileFastUtil.fileReader(file.getAbsolutePath());
                            capName = capName.replace("\r\n", "");
                            LogTool.d("FindCapNameOnFile capName = %s", capName);
                            File capLocal = new File(file.getParent() + "/" + capName);
                            if (capLocal.exists()) {
                                LogTool.d("FindCapNameOnFile capName = %s", capName);
                                return capLocal.getAbsolutePath();
                            }else{
                                LogTool.d("capLocal is not exists");
                            }
                        }else if(fileName.endsWith(".apk")){
                            LogTool.d("Find apk file");
                            return file.getAbsolutePath();
                        }
                    }
                }
            }
        } catch (IOException e){
            LogTool.d("FindCapNameOnFile error, e = %s", e.getMessage());
            return "";
        }
        LogTool.d("FindCapNameOnFile no find cap");
        return "";
    }
}
