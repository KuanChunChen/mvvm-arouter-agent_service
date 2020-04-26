package k.c.module.download.download;

import java.util.concurrent.ThreadFactory;

public class DownloadThreadFactory implements ThreadFactory {

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName("CTMS download thread->" + Math.random());
        return thread;
    }
}
