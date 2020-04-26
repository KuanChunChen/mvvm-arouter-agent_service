package k.c.module.getinfo.api;

import k.c.module.getinfo.model.dataexchange.DataExchangeInfo;
import k.c.module.getinfo.model.dataexchange.DataExchangeResult;
import k.c.module.getinfo.model.getinfo.GetInfoRequest;
import k.c.module.getinfo.model.getinfo.GetInfoResult;
import k.c.module.http.RetrofitResultObserver;
import k.c.module.http.base.BaseHttpClientAPI;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static k.c.module.http.RetrofitClient.http;

public class GetInfoHttpAPI extends BaseHttpClientAPI {

    public static void getInfo(GetInfoRequest mGetInfoRequest, RetrofitResultObserver<GetInfoResult> result) {

        http(GetInfoAPI.class).getInfo(mGetInfoRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(result);
    }

    public static void dataExchange(DataExchangeInfo mDataExchangeInfo, RetrofitResultObserver<DataExchangeResult> result) {

        http(GetInfoAPI.class).dataExchange(mDataExchangeInfo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(result);
    }
}
