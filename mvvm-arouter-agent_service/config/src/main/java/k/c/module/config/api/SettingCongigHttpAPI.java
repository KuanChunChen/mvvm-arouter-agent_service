package k.c.module.config.api;

import k.c.module.config.model.ReportSystemResult;
import k.c.module.config.model.ReportSystemSettingData;
import k.c.module.config.model.RequestInfo;
import k.c.module.config.model.ReportAndroidSettingData;
import k.c.module.config.model.SettingResult;
import k.c.module.config.model.SystemSettingInfo;
import k.c.module.config.model.SystemSettingResult;
import k.c.module.http.RetrofitResultObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static k.c.module.http.RetrofitClient.http;

public class SettingCongigHttpAPI {

    public static void getSettingParam(String sn, RetrofitResultObserver<SettingResult> result) {
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setSn(sn);
        http(SettingConfigAPI.class).getConfigSetting(requestInfo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(result);
    }

    public static void sentResult(ReportAndroidSettingData mReportAndroidSettingData, RetrofitResultObserver<SettingResult> result) {

        http(SettingConfigAPI.class).reportAndroidSetting(mReportAndroidSettingData)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(result);
    }

    public static void getSystemSetting(SystemSettingInfo systemSettingInfo , RetrofitResultObserver<SystemSettingResult> result) {

        http(SettingConfigAPI.class).getSystemSetting(systemSettingInfo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(result);
    }

    public static void reportSystemSetting(ReportSystemSettingData reportSystemSettingData, RetrofitResultObserver<ReportSystemResult> result) {

        http(SettingConfigAPI.class).reportSystemSetting(reportSystemSettingData)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(result);
    }

}
