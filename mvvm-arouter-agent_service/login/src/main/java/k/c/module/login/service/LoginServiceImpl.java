package k.c.module.login.service;

import android.content.Context;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;

import k.c.common.lib.logTool.LogTool;
import k.c.module.commonbusiness.CommonSingle;
import k.c.module.commonbusiness.base.BaseServiceImpl;
import k.c.module.commonbusiness.common.CommonConst;
import k.c.module.commonbusiness.common.TimeSettingTool;
import k.c.module.commonbusiness.listener.LoginListener;
import k.c.module.commonbusiness.listener.LogoutListener;
import k.c.module.commonbusiness.model.LoginInfoRequestModel;
import k.c.module.commonbusiness.service.LoginService;
import k.c.module.http.Result;
import k.c.module.http.RetrofitResultObserver;
import k.c.module.login.LoginManager;
import k.c.module.login.api.LoginAPI;
import k.c.module.login.api.LoginHttpAPI;
import k.c.module.login.model.login.LoginInfo;
import k.c.module.login.model.login.LoginResult;
import k.c.module.login.model.logout.LogoutInfo;
import k.c.module.login.model.logout.LogoutResult;

import static k.c.module.http.RetrofitClient.http;

@Route(path = "/login/loginService")
public class LoginServiceImpl extends BaseServiceImpl implements LoginService {

    @Override
    public void login(LoginInfoRequestModel loginInfoRequestModel, @NonNull final LoginListener loginListener) {

        final LoginInfo loginInfo = new LoginInfo();
        loginInfo.serialNum = loginInfoRequestModel.serialNum;
        loginInfo.communicationMethod = loginInfoRequestModel.communicationMethod;
        loginInfo.data.serialNum = loginInfoRequestModel.serialNum;
        loginInfo.data.version = loginInfoRequestModel.version;
        loginInfo.data.ip = loginInfoRequestModel.ip;
        TimeSettingTool.setConnectTime(System.currentTimeMillis());
        LoginHttpAPI.login(loginInfo, new RetrofitResultObserver<LoginResult>() {
            @Override
            public void onSuccess(LoginResult data) {
                LogTool.i(CommonConst.ModuleName.LOGIN_MODULE_NAME,"Login", CommonConst.returnCode.CTMS_SUCCESS_STRING);
                LogTool.d("Login success result is %s", data);
                CommonSingle.getInstance().setSessionId(data.sessionId);
                CommonSingle.getInstance().setLogin(true);
                CommonSingle.getInstance().setServerProtocolVersion(data.serverProtocolVersion);
                loginListener.loginSuccess(data.sessionId);
            }

            @Override
            protected void onResultCode(int resultCode, Result result) {
                super.onResultCode(resultCode, result);
                LogTool.i(CommonConst.ModuleName.LOGIN_MODULE_NAME,"Login", resultCode);
                CommonSingle.getInstance().setSessionId(null);
                CommonSingle.getInstance().setLogin(false);
                loginListener.loginFail(resultCode);
            }

            @Override
            public void onFailure(RetrofitResultException e) {
                super.onFailure(e);
                LogTool.i(CommonConst.ModuleName.LOGIN_MODULE_NAME,"Login", CommonConst.returnCode.CTMS_LOGIN_FAIL);
                loginListener.httpError();
            }
        });
    }

    @Override
    public void logout(@NonNull LogoutListener logoutListener) {

        final LogoutInfo logoutInfo = new LogoutInfo();
        logoutInfo.SN = CommonSingle.getInstance().getBaseConfig().serialNumber;
        logoutInfo.Data.SESSION_ID = CommonSingle.getInstance().getSessionId();
        TimeSettingTool.setLeaveTime(System.currentTimeMillis());
        LoginHttpAPI.logout(logoutInfo, new RetrofitResultObserver<LogoutResult>() {
            @Override
            public void onSuccess(LogoutResult data) {
                LogTool.i(CommonConst.ModuleName.LOGOUT_MODULE_NAME, "Logout", CommonConst.returnCode.CTMS_SUCCESS_STRING);
                logoutListener.logoutSuccess();
            }

            @Override
            protected void onResultCode(int resultCode, Result result) {
                super.onResultCode(resultCode, result);
                LogTool.i(CommonConst.ModuleName.LOGOUT_MODULE_NAME,"Logout", resultCode);
                logoutListener.logoutFail(resultCode);

            }

            @Override
            public void onFailure(RetrofitResultException e) {
                super.onFailure(e);
                LogTool.i(CommonConst.ModuleName.LOGOUT_MODULE_NAME,"Logout", CommonConst.Status.CTMS_RUNNING_STATUS_LOGOUT_FAIL);
                logoutListener.httpError();
            }
        });
    }

    @Override
    public boolean checkLogin() {
        return LoginManager.getInstance().checkLogin();
    }



        @Override
    public void init(Context context) {
        LogTool.d("LoginServiceImpl init default");
        http(LoginAPI.class);
    }
}
