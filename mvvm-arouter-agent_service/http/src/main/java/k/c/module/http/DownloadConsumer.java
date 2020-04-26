package k.c.module.http;

import org.reactivestreams.Subscription;

import k.c.common.lib.logTool.LogTool;
import k.c.module.commonbusiness.model.DownloadResultModel;
import io.reactivex.FlowableSubscriber;

public abstract class DownloadConsumer implements FlowableSubscriber<DownloadResultModel> {

    public abstract void onStart(int id);

    public abstract void onProgress(int currentLength);

    public abstract void onFinish(int id, String path, long size);

    public abstract void onFailure(int statusCode);

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Integer.MAX_VALUE);
    }

    @Override
    public void onNext(DownloadResultModel downloadResult) {
        switch (downloadResult.step){
            case 0:
                onStart(downloadResult.id);
                break;
            case 1:
                onProgress(downloadResult.currentLength);
                break;
            case 2:
                onFinish(downloadResult.id, downloadResult.path, downloadResult.size);
                break;
            case 3:
                onFailure(downloadResult.statusCode);
                break;
            default:
                break;
        }
    }

    @Override
    public void onError(Throwable throwable) {
        LogTool.d(throwable.getMessage());
    }

    @Override
    public void onComplete() {
        LogTool.d("download onComplete");
    }
}
