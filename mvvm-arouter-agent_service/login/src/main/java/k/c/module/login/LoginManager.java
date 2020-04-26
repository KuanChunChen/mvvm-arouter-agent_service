package k.c.module.login;

import com.alibaba.android.arouter.launcher.ARouter;

import k.c.common.lib.logTool.LogTool;
import k.c.module.commonbusiness.CommonSingle;
import k.c.module.commonbusiness.common.CommonConst;
import k.c.module.commonbusiness.model.LoginInfoRequestModel;
import k.c.module.commonbusiness.listener.LoginListener;
import k.c.module.commonbusiness.po.BaseConfig;

public class LoginManager {

    private LoginManager(){
        ARouter.getInstance().inject(this);
    }

    public static synchronized LoginManager getInstance(){
        return LoginManager.SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final LoginManager INSTANCE = new LoginManager();
    }

    public boolean checkLogin(){
        if(!CommonSingle.getInstance().isLogin()){
            LogTool.d("not login, start restart login");
            return restartLogin();
        }else{
            return true;
        }
    }

//    private static final long LOGIN_TIMEOUT = 20 * 1000;
    private volatile boolean executed;
    private volatile boolean loginSuccess;
    protected synchronized boolean restartLogin(){
        BaseConfig config = CommonSingle.getInstance().getBaseConfig();

        if(config == null){
            LogTool.d("base config is null");
            return false;
        }

        if(config.serialNumber.length() != CommonConst.Common.FORMAT_TERMINAL_SN_LENGTH){
            LogTool.d("serialNumber length is not correct, sn = %s", config.serialNumber);
            return false;
        }
        if(CommonConst.Common.TERMINAL_SN_EMPTY.equals(config.serialNumber)){
            LogTool.d("serialNumber is 0, sn = %s", config.serialNumber);
            return false;
        }

        executed = false;
        loginSuccess = false;
        LoginInfoRequestModel loginInfoRequestModel = new LoginInfoRequestModel(
                CommonSingle.getInstance().getBaseConfig().serialNumber,
                CommonSingle.getInstance().getBaseConfig().communicationMode,
                "1.0.2",
                "192.168.1.1");
        CommonSingle.getInstance().getLoginService().login(loginInfoRequestModel, new LoginListener() {
            @Override
            public void loginSuccess(String sessionId) {
                LogTool.d("restart login success, sessionId = %s", sessionId);
                executed = true;
                loginSuccess = true;
            }

            @Override
            public void loginFail(int code) {
                LogTool.d("restart login fail, code = %s", code);
                executed = true;
                loginSuccess = false;
            }

            @Override
            public void httpError() {
                LogTool.d("restart http error");
                executed = true;
                loginSuccess = false;
            }
        });

//        long startTime = System.currentTimeMillis(), endTime;
        while(!executed){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            endTime = System.currentTimeMillis();
//            if(endTime - startTime >= LOGIN_TIMEOUT){
//                LogTool.d("Login TIME OUT");
//                break;
//            }
        }

        return loginSuccess;
    }
}
