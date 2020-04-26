package k.c.module.commonbusiness.listener;

public interface LoginListener {

    void loginSuccess(String sessionId);
    void loginFail(int code);
    void httpError();

}
