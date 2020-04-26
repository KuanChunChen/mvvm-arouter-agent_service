package k.c.module.login.api;

import k.c.module.commonbusiness.CommonSingle;
import k.c.module.commonbusiness.common.CommonConst;
import k.c.module.http.RetrofitResultObserver;
import k.c.module.http.base.BaseHttpClientAPI;
import k.c.module.login.model.login.LoginInfo;
import k.c.module.login.model.login.LoginResult;
import k.c.module.login.model.logout.LogoutInfo;
import k.c.module.login.model.logout.LogoutResult;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static k.c.module.http.RetrofitClient.http;

public class LoginHttpAPI extends BaseHttpClientAPI {


    public static void login(LoginInfo mLoginInfo, RetrofitResultObserver<LoginResult> result) {
        http(LoginAPI.class).postLogin(mLoginInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(result);
    }

    public static void logout(LogoutInfo mLogoutInfo, RetrofitResultObserver<LogoutResult> result) {
        http(LoginAPI.class).logout(mLogoutInfo, CommonConst.Http.HTTP_PHPSESSID + CommonSingle.getInstance().getSessionId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(result);
    }

}

