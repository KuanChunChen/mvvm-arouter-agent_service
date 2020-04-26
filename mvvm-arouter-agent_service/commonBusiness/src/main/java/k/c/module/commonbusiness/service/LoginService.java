package k.c.module.commonbusiness.service;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.template.IProvider;

import k.c.module.commonbusiness.listener.LogoutListener;
import k.c.module.commonbusiness.model.LoginInfoRequestModel;
import k.c.module.commonbusiness.listener.LoginListener;

public interface LoginService extends IProvider {
    boolean checkLogin();
    void login(LoginInfoRequestModel loginInfoRequestModel, @NonNull LoginListener loginListener);
    void logout(@NonNull LogoutListener logoutListener);

}
