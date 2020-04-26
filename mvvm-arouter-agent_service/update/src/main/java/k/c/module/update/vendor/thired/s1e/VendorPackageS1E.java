package k.c.module.update.vendor.thired.s1e;

import android.text.TextUtils;

import CTOS.CtLoader;
import CTOS.CtLoaderSecurityModuleInstallStatus;
import CTOS.CtSystem;
import k.c.common.lib.CommonLib;
import k.c.common.lib.Util.AndroidUtil;
import k.c.common.lib.logTool.LogTool;
import k.c.module.commonbusiness.common.CommonConst;
import k.c.module.update.constants.Constants;
import k.c.module.update.model.ParameterInfo;
import k.c.module.update.module.ParameterManager;
import k.c.module.update.module.parameter.ParameterCallback;
import k.c.module.update.vendor.common.VendorApiResult;
import k.c.module.update.vendor.common.VendorPackageInterface;


public class VendorPackageS1E implements VendorPackageInterface {

    public CtSystem system = new CtSystem();
    private CtLoader loader = new CtLoader();

    private ParameterManager parameterManager = new ParameterManager();


    @Override
    public void setCallback(VendorApiResult vendorApiResult) {


        VendorLoaderCallback loaderCb = new VendorLoaderCallback(this) {
            @Override
            protected void onInstallSuccess(int installIsRunning, String installStage, String installCAPType, String installInf, int installRet, CtLoaderSecurityModuleInstallStatus securityInstallStatus) {
                vendorApiResult.success();
            }

            @Override
            protected void onInstallFail(int installIsRunning, String installStage, String installCAPType, String installInf, int installRet, CtLoaderSecurityModuleInstallStatus securityInstallStatus) {
                vendorApiResult.fail();
            }

            @Override
            protected void onUninstallSuccess(int uninstallIsRunning, String uninstallInf, int uninstallRet) {
                vendorApiResult.success();
            }

            @Override
            protected void onUninstallFail(int uninstallIsRunning, String uninstallInf, int uninstallRet) {
                vendorApiResult.fail();
            }

            @Override
            protected void onComplete() {
                vendorApiResult.complete();
            }
        };
        this.loader.setLoaderCallback(loaderCb);
    }

    @Override
    public void installStart(String path) {

        this.loader.installStart(path);
    }

    @Override
    public void setPrmCallback(VendorApiResult vendorApiResult) {

        ParameterCallback parameterCallback = new ParameterCallback() {
            @Override
            public void onSuccess() {
                vendorApiResult.success();
            }

            @Override
            public void onFail(int errorCode) {
                LogTool.i(CommonConst.ModuleName.UPDATE_MODULE_NAME,"Update", errorCode);
                vendorApiResult.fail();
            }
        };

        this.parameterManager.setParameterCallback(parameterCallback);

    }

    @Override
    public void installPrmStart(String path) {

        ParameterInfo[] parameterInfo = this.parameterManager.preparePrmData(path);

        if (parameterInfo != null) {
            for (ParameterInfo parameterInfoMember : parameterInfo) {
                this.parameterManager.saveParameterThroughKernel(path, parameterInfoMember);
            }
        }

    }

    @Override
    public void uninstallStart(String packageName) {
        this.loader.uninstallStart(packageName);
    }

    @Override
    public void rebootStart() {
        LogTool.d("S1E is not reboot operation");
    }

    @Override
    public boolean isDefaultApp(String currentPackageName) {
        String packageName = system.getDefaultApp();
        LogTool.d("The default app packageName = %s", packageName);
        if(TextUtils.isEmpty(packageName)){
            return false;
        }
        return packageName.equals(currentPackageName);
    }

    @Override
    public void launchDefaultApp() {
        String packageName = system.getDefaultApp();
        LogTool.d("The default app packageName = %s", packageName);
        if(TextUtils.isEmpty(packageName)){
            return;
        }
        LogTool.d("start launch the default app");
        AndroidUtil.launchAppByPackageName(CommonLib.getAppContext(), packageName);
    }

    @Override
    public String getVendorPackageName() {
        return Constants.VENDOR_PACKAGE_NAME_S1E;
    }
}