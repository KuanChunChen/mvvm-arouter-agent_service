package k.c.module.commonbusiness.listener;

public interface DownloadListener {
    void onStart(int id);

    void onProgress(int currentLength);

    void onFinish(String path, long size, int id);

    void onFailure(int statusCode);
}
