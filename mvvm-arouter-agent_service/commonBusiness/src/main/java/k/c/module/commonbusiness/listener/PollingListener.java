package k.c.module.commonbusiness.listener;

public interface PollingListener {
    void pollingFinish();
    void pollingFail(int errorCode);

}
