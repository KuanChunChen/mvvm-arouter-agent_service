package k.c.module.download.api;

import java.nio.ByteBuffer;

import k.c.common.lib.logTool.LogTool;
import k.c.module.commonbusiness.common.CommonConst;
import k.c.module.download.model.DownloadHandshakeInfo;
import k.c.module.download.model.DownloadHandshakeResult;
import k.c.module.http.DownloadResultObserver;
import k.c.module.http.RetrofitResultObserver;
import k.c.module.http.base.BaseHttpClientAPI;
import k.c.module.http.model.DownloadFileResult;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

import static k.c.module.http.RetrofitClient.http;

public class DownloadHttpApi extends BaseHttpClientAPI {

    public static void downloadHandShake(DownloadHandshakeInfo mDownloadHandshakeInfo, String sessionId, RetrofitResultObserver<DownloadHandshakeResult> result) {

        http(DownloadAPI.class).downloadHS(mDownloadHandshakeInfo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(result);
    }

    public static void downloadFile(String url, String range, String sessionId, DownloadResultObserver downloadResultObserver) {

        http(DownloadAPI.class).downloadFile(url, range)
                .map(responseBodyResponse -> {
                    ResponseBody responseBody = responseBodyResponse.body();
                    if (responseBodyResponse.isSuccessful()){
                        if (responseBody == null){
                            LogTool.d("ResponseBody = null");
                            return null;
                        }
                        DownloadFileResult downloadResult = new DownloadFileResult();

                        if(responseBody.contentLength() > CommonConst.FileConst.DOWNLOAD_BIG_DATA_SIZE){
                            downloadResult.status = CommonConst.returnCode.CTMS_SUCCESS_INT;
                            downloadResult.isBigDataMode = true;
                            downloadResult.responseBody = responseBody;
                            LogTool.d("responseBody.contentLength = %s" + responseBody.contentLength());
                        }else{
                            byte[] bytes = responseBody.bytes();
                            ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
                            byteBuffer.put(bytes);

                            byteBuffer.flip();
                            short status = CommonConst.returnCode.CTMS_SUCCESS_INT;
                            LogTool.d("responseBody return status = %s", status);

                            byte[] data = new byte[bytes.length];
                            byteBuffer.get(data, 0, data.length);
                            downloadResult.data = data;
                            downloadResult.status = status;
                            downloadResult.isBigDataMode = false;
                            LogTool.d("downloadResult data length = %s", downloadResult.data.length);
                        }
                        LogTool.d("downloadResult isBigDataMode = %s", downloadResult.isBigDataMode);
                        return downloadResult;
                    }else{
                        DownloadFileResult downloadResult = new DownloadFileResult();
                        if(responseBodyResponse.code() == 404){
                            downloadResult.status = 0x01;
                            return downloadResult;
                        }
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(downloadResultObserver);

    }
}
