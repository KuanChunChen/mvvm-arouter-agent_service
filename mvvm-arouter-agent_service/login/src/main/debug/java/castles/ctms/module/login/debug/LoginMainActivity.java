package k.c.module.login.debug;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import k.c.common.lib.logTool.LogTool;
import k.c.module.http.RetrofitResultObserver;
import k.c.module.login.api.LoginAPI;
import k.c.module.login.api.LoginHttpAPI;
import k.c.module.login.model.login.LoginInfo;
import k.c.module.login.model.login.LoginResult;
import okhttp3.Credentials;
import okhttp3.Request;

public class LoginMainActivity extends Activity {

    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(view -> {

            LoginInfo loginInfo = new LoginInfo();
            loginInfo.serialNum = "9999999999999999";
            loginInfo.communicationMethod = 1;
            loginInfo.data.serialNum = "9999999999999999";
            loginInfo.data.version = 1;
            loginInfo.data.ip = "192.168.1.99";
            LoginHttpAPI.resetHttpClient(LoginAPI.class, LoginHttpAPI.getBuilder(LoginAPI.class).addInterceptor(chain -> {
                Request original = chain.request();
                String credential = "11111111111111111";
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("222222222222222", credential);
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }));
            LoginHttpAPI.login(loginInfo, new RetrofitResultObserver<LoginResult>() {
                @Override
                public void onSuccess(LoginResult data) {
                    LogTool.d("Login success result is %s", data);
                }
            });
        });
    }
}
