package k.c.module.http;

import k.c.common.lib.logTool.LogTool;
import k.c.module.http.model.DownloadFileResult;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class DownloadResultObserver implements Observer<DownloadFileResult> {

    @Override
    public void onSubscribe(Disposable disposable) {

    }

    @Override
    public void onNext(DownloadFileResult downloadResult) {
        if(downloadResult == null){
            onFailure();
            return;
        }
        if(0 == downloadResult.status){
            if(downloadResult.isBigDataMode){
                onSuccess(downloadResult, true);
            }else{
                onSuccess(downloadResult, false);
            }
        }else{
            onResultCode(downloadResult.status);
        }
    }

    public abstract void onSuccess(DownloadFileResult  downloadResult, boolean isBigData);

    /**
     *
     * call this when getting non SUCCESS code
     */
    protected void onResultCode(short resultCode){
        LogTool.d("download return code = [%d]", resultCode);
    }

    public void onFailure(){

    }

    @Override
    public void onError(Throwable throwable) {
        LogTool.d(throwable.getMessage() + "    okhttp");
    }

    @Override
    public void onComplete() {

    }


}
