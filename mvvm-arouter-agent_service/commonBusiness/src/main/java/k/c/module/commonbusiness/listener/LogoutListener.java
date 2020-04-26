package k.c.module.commonbusiness.listener;

public interface LogoutListener {
    void logoutSuccess();
    void logoutFail(int code);
    void httpError();
}
