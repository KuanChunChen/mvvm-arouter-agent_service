package k.c.common.lib.dialogUtil;

/**
 * Created by Willy Chen on 2019/04/16.
 *
 * @author Willy Chen
 * @Purpose  Android permission related.
 */

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import k.c.common.lib.CommonLib;
import k.c.common.lib.DialogActivity;

public class PermissionManager {
    /**
     * @param activity       for it activity
     * @param strPremissions all permission that would check and request
     * @param requestCode    this time's request code
     */
    public PermissionManager(Activity activity, String[] strPremissions, int requestCode) {
        if (!checkPermission(strPremissions)) {
            requestPermissions(activity, strPremissions, requestCode);

        }else{
            DialogActivity.sendFinishDialog();
        }
    }


    /**
     * @param strPremissions
     * @return true : all permission are granted.
     */
    public static boolean checkPermission(String[] strPremissions) {
        for (String strSinglePermission : strPremissions) {
            if (ActivityCompat.checkSelfPermission(CommonLib.getAppContext(), strSinglePermission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;

    }

    /**
     * @param strPremissions the permission that need to open
     * @param requestCode    the request code
     */

    public void requestPermissions(Activity activity, String[] strPremissions, int requestCode) {

        ActivityCompat.requestPermissions(activity, strPremissions, requestCode);
    }


}
