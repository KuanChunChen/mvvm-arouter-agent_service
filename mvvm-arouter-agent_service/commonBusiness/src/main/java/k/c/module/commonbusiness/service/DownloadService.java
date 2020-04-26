package k.c.module.commonbusiness.service;

import com.alibaba.android.arouter.facade.template.IProvider;

import org.reactivestreams.Publisher;

import java.util.List;

import k.c.module.commonbusiness.listener.DownloadListener;
import k.c.module.commonbusiness.model.DownloadResultModel;
import k.c.module.commonbusiness.model.UpdateDataModel;

public interface DownloadService extends IProvider {
    void download(int downloadMode, int fileId, int fileType, DownloadListener downloadListener);

    Publisher<DownloadResultModel> download(int downloadMode, int fileId, int fileType);

    void clearDownload();

    void downloadUpdateList(int updateListFileId, DownloadListener downloadListener);

    List<UpdateDataModel> convertUpdateInfo();
}
