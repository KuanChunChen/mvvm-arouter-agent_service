package k.c.module.polling.module.polling;

import java.util.concurrent.TimeUnit;

import k.c.common.lib.DialogActivity;
import k.c.common.lib.logTool.LogTool;
import k.c.common.lib.proxy.ProxyManager;
import k.c.module.commonbusiness.CommonSingle;
import k.c.module.commonbusiness.common.LifeManager;
import k.c.module.commonbusiness.listener.LogoutListener;
import k.c.module.commonbusiness.model.CTMSProcessInfo;
import k.c.module.polling.constants.Constants;
import k.c.module.polling.process.CtmsMainProcessImpl;
import k.c.module.polling.process.MainProcessInterface;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PollingManager {

    private Disposable pollingDisposable;
    private Disposable triggerDisposable;
    private Disposable mainDisposable;
    private volatile boolean isProcessRunning = false;
    private long getInfoStartTime = 0;
    private MainProcessInterface mainProcess = new CtmsMainProcessImpl() {
        @Override
        public void endProcess(int status) {
            if(status == Constants.MAIN_PROCESS_STATUS_LOGIN_FAIL){
                handlerStepByStatus(status);
            }else{
                CommonSingle.getInstance().getLoginService().logout(new LogoutListener() {
                    @Override
                    public void logoutSuccess() {
                        LogTool.d("endProcess logout success");
                        handlerStepByStatus(status);
                    }

                    @Override
                    public void logoutFail(int code) {
                        LogTool.d("endProcess logoutFail, code = %s", code);
                        handlerStepByStatus(status);
                    }

                    @Override
                    public void httpError() {
                        LogTool.d("endProcess logout fail http error");
                        handlerStepByStatus(status);
                    }
                });
            }
        }
    };

    public void handlerStepByStatus(int status){
        CommonSingle.getInstance().setLogin(false);
        CommonSingle.getInstance().setSessionId("");
        isProcessRunning = false;
        switch (status){
            case Constants.MAIN_PROCESS_STATUS_LOGIN_FAIL:
            case Constants.MAIN_PROCESS_STATUS_GET_INFO_FAIL:
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(System.currentTimeMillis() - getInfoStartTime >= Constants.GETINFO_TIMEOUT){
                    LogTool.d("get info time out");
                    getInfoStartTime = 0;
                    return;
                }
                CommonSingle.getInstance().getPollingService().updateNow();
                break;
            case Constants.MAIN_PROCESS_STATUS_DOWNLOAD_UPDATE_LIST_FAIL:
                LifeManager.getInstance().refreshWakeLockProcess();
                break;
            case Constants.MAIN_PROCESS_STATUS_EXECUTE_UPDATE_LIST_FAIL:
                LifeManager.getInstance().refreshWakeLockProcess();
                break;
            case Constants.MAIN_PROCESS_STATUS_MAIN_PROCESS_FINISH:
                mainProcess.callVendorExecute();
                break;
        }
    }

    public synchronized void startMainProcess(){
        LogTool.d("start lock the terminal");
        LifeManager.getInstance().wakeLockProcess();
        if(isProcessRunning){
            LogTool.d("main process running");
            LifeManager.getInstance().refreshWakeLockProcess();
            return;
        }
        isProcessRunning = true;
        if(getInfoStartTime == 0){
            getInfoStartTime = System.currentTimeMillis();
        }
        if(mainDisposable != null && !mainDisposable.isDisposed()){
            mainDisposable.dispose();
        }

        mainDisposable = Observable.create((ObservableOnSubscribe<CTMSProcessInfo>) e -> {
            CTMSProcessInfo processInfo = new CTMSProcessInfo();
            processInfo.proxyData = ProxyManager.getProxyInfo();
            e.onNext(processInfo);
            e.onComplete();
        })
        .observeOn(Schedulers.newThread())
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(processInfo -> {
            DialogActivity.startAskPermission();
            if(CommonSingle.getInstance().getEnableConfig().ctmsEnable){
                mainProcess.startProcess(processInfo);
            }else{
                LogTool.d("ctms enable is false.");
                LifeManager.getInstance().refreshWakeLockProcess();
            }
        });
    }

    public void updateNow(){
        startMainProcess();
    }

    public Disposable startPolling(int intervalTimeMilli) {

        return startPolling(intervalTimeMilli, true);
    }

    public Disposable startPolling(long intervalTimeMilli, boolean initialRun){
        pollingProcess(intervalTimeMilli, initialRun);
        return pollingDisposable;
    }

    private void pollingProcess(long intervalTimeMilli, boolean initialRun) {
        stopSchedule();
        if (initialRun) {
            pollingDisposable = Flowable.interval(0, intervalTimeMilli, TimeUnit.MILLISECONDS)
                    .onBackpressureDrop()
                    .subscribe(aLong -> {
                        LogTool.d("initial run polling.....%s", intervalTimeMilli);
                        startMainProcess();
                    });
        } else {
            pollingDisposable = Flowable.interval(intervalTimeMilli, TimeUnit.MILLISECONDS)
                    .onBackpressureDrop()
                    .subscribe(aLong -> {
                        LogTool.d("Restart polling.....%s", intervalTimeMilli);
                        startMainProcess();
                    });
        }

        LifeManager.getInstance().refreshWakeLockProcess();
    }

    public void startTrigger(long triggerDelayTime){
        stopSchedule();
        triggerDisposable = Flowable.timer(triggerDelayTime, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    LogTool.d("trigger time hint", triggerDelayTime);
                    startMainProcess();
                });
        LifeManager.getInstance().refreshWakeLockProcess();
    }


    public void stopSchedule(){
        if(pollingDisposable != null){
            pollingDisposable.dispose();
            pollingDisposable = null;
        }
        if(triggerDisposable != null){
            triggerDisposable.dispose();
            triggerDisposable = null;
        }
    }
}
