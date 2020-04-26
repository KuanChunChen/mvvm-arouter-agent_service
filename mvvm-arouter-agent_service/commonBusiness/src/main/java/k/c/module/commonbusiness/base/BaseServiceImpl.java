package k.c.module.commonbusiness.base;

import k.c.module.commonbusiness.CommonSingle;

public class BaseServiceImpl {
    protected boolean checkLogin(){
        return CommonSingle.getInstance().getLoginService().checkLogin();
    }
}
