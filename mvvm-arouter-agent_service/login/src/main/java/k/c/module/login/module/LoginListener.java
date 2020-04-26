package k.c.module.login.module;

import k.c.module.login.model.login.LoginResult;

public interface LoginListener {

    void onSuccess(LoginResult data);

    void onNullFailure();

    void onFailure(Throwable e);

    void onResultCode(int resultCode);

}
