package k.c.service.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import k.c.common.lib.Util.ShareUtil;
import k.c.common.lib.logTool.LogTool;
import k.c.module.commonbusiness.CommonSingle;
import k.c.module.commonbusiness.common.CommonConst;


public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            boolean hasRebootInstall = ShareUtil.read(context, CommonConst.SHARE_KEY_HAS_REBOOT_INSTALL, false);
            ShareUtil.clear(context, CommonConst.SBL_INSTALLED_BUT_NOT_REBOOT);
            LogTool.d("BootCompletedReceiver hasRebootInstall is %s", hasRebootInstall);
            if (hasRebootInstall){

                CommonSingle.getInstance().getUpdateService().setRebootInstallMode();
                CommonSingle.getInstance().getUpdateService().initUpdateProcess();
            }else{
                boolean bootConnect = CommonSingle.getInstance().getEnableConfig().bootConnectEnable;
                LogTool.d("BootCompletedReceiver boot connect is %s", bootConnect);
                if(bootConnect){
                    CommonSingle.getInstance().getPollingService().updateNow();
                }
            }
        }
    }
}
