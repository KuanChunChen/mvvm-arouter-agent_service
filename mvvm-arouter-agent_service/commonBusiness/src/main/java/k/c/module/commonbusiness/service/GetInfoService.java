package k.c.module.commonbusiness.service;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.template.IProvider;

import k.c.module.commonbusiness.model.GetInfoRequestModel;
import k.c.module.commonbusiness.listener.GetInfoListener;

public interface GetInfoService extends IProvider {
    void getInfo(GetInfoRequestModel getInfoRequestModel, @NonNull final GetInfoListener getInfoListener);
    void clearGetInfoFile();
}
