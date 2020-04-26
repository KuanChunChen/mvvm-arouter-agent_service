package k.c.module.http;


import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;



public class RxSchedulers {

    public static final ObservableTransformer IO_TRANSFORMER = upstream -> upstream.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    public static <T> ObservableTransformer<Result<T>, Result<T>> applySchedulers(ObservableTransformer transformer) {
        return (ObservableTransformer<Result<T>, Result<T>>) transformer;
    }
}
