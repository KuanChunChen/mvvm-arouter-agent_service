package k.c.module.commonbusiness.listener;

public interface GetInfoListener {
    void getInfoSuccess(int fileId);
    void getInfoFail(int code);
    void error();
    void loginFail();
}
