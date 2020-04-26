package k.c.common.lib;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import k.c.common.lib.constants.Constants;
import k.c.common.lib.dialogUtil.DialogConfig;
import k.c.common.lib.dialogUtil.DialogCountDownTimer;
import k.c.common.lib.dialogUtil.DialogHints;
import k.c.common.lib.dialogUtil.DialogListener;
import k.c.common.lib.dialogUtil.DialogTool;
import k.c.common.lib.dialogUtil.PermissionManager;
import k.c.common.lib.logTool.LogTool;

public class DialogActivity extends AppCompatActivity {

    private Dialog dialog_hints;
    private AlertDialog alertDialog;
    private AlertDialog.Builder dialogBuilder;
    private String[] time = {"Now","2","15","30"};
    private int iIndex = 0;
    private static DialogListener dlListener;
    private static LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver dialogReceiver;
    private static final String DIALOG_FINISH_ACTION = "ctms.dialog.finish";
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null){
            displayChooseDialog(intent, dlListener);
            localBroadcastManager = LocalBroadcastManager.getInstance(this);
            dialogReceiver = new DialogBroadcast();
            localBroadcastManager.registerReceiver(dialogReceiver, new IntentFilter(DIALOG_FINISH_ACTION));
        }else{
            finish();
        }
    }

    private void displayChooseDialog(Intent intent, DialogListener dialogListener){
        DialogConfig dialogConfig = new DialogConfig();
        DialogTool.OnDialogListener onDialogListener = new DialogTool.OnDialogListener() {
            @Override
            public void onTimeout() {
                dialogListener.onTimeout();
            }

            @Override
            public void cancel() {
                dialogListener.onCancel();
            }

            @Override
            public void sure() {
                dialogListener.onSure();
            }
        };

        switch (intent.getIntExtra("dialogType", 1)){
            case Constants.DialogType.DIALOG_REBOOT:
                dialogConfig.dialogButtonType = 1;
                dialogConfig.titleRes = R.string.title_dialog;
                dialogConfig.sureText = R.string.dialog_sure;
                dialogConfig.cancelText = R.string.dialog_cancel;
                dialogConfig.content = R.string.dialog_content;
                DialogTool.showDialog(this, dialogConfig, onDialogListener);
                break;
            case Constants.DialogType.DIALOG_CHOOSE_TIME:
                displayChooseTimeDialog(dialogListener);
                break;
            case Constants.DialogType.DIALOG_ASK_PERMISSION:
                new PermissionManager(this, PERMISSIONS_STORAGE, 1);
                break;
            case Constants.DialogType.DIALOG_BATTERY_ENOUGH:
                dialogConfig.dialogButtonType = 1;
                dialogConfig.titleRes = R.string.title_dialog;
                dialogConfig.sureText = R.string.dialog_sure;
                dialogConfig.cancelText = R.string.dialog_cancel;
                if (intent.getIntExtra("battery", 30) == 30){
                    dialogConfig.content = R.string.dialog_content_battery_30;
                }else {
                    dialogConfig.content = R.string.dialog_content_battery_10;
                }
                DialogTool.showDialog(this, dialogConfig, onDialogListener);
                break;
            default:
                break;
        }
    }

    public void displayChooseTimeDialog(DialogListener dialogListener){
        DialogCountDownTimer countDownTime;
        DialogCountDownTimer.CountDownTimeListener countDownListener = new DialogCountDownTimer.CountDownTimeListener() {
            @Override
            public void onTick(long millisUntilFinished) {
                LogTool.d("Dialog onTick = %s", millisUntilFinished);
            }

            @Override
            public void onFinish() {
                if(alertDialog != null) {
                    alertDialog.dismiss();
                }
                if(dialogListener != null){
                    dialogListener.onSelect(0);
                }
            }

            @Override
            public String getClazzName() {
                return DialogTool.class.getName();
            }
        };

        countDownTime = new DialogCountDownTimer(countDownListener);
        dialogBuilder = new AlertDialog.Builder(DialogActivity.this, R.style.AppTheme);
        dialogBuilder.setTitle("Install/Update confirm");
        dialogBuilder.setCancelable(false);
        iIndex = 0;
        dialogBuilder.setSingleChoiceItems(time, iIndex, (dialog, which) -> iIndex = which);
        dialogBuilder.setPositiveButton("Send", (dialog, which) -> {
            LogTool.d("You select : "+ iIndex);
            dialogListener.onSelect(iIndex);
            countDownTime.cancel();
            dialog.dismiss();
        });
        alertDialog = dialogBuilder.create();
        countDownTime.start();
        alertDialog.show();
    }

    public static void startChooseDialog(DialogListener dialogListener){
        dlListener = dialogListener;
        Intent intent = new Intent(CommonLib.getAppContext(), DialogActivity.class);
        intent.putExtra("dialogType", Constants.DialogType.DIALOG_REBOOT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonLib.getAppContext().startActivity(intent);
    }

    public static void startChooseTimeDialog(DialogListener dialogListener){
        dlListener = dialogListener;
        Intent intent = new Intent(CommonLib.getAppContext(), DialogActivity.class);
        intent.putExtra("dialogType", Constants.DialogType.DIALOG_CHOOSE_TIME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonLib.getAppContext().startActivity(intent);
    }

    public static void startAskPermission(){
        if(PermissionManager.checkPermission(PERMISSIONS_STORAGE)){
            AppSingle.getInstance().setOperationPermission(0);
            return;
        }
        LogTool.d("invoke the dialog Activity for ask permission");
        Intent intent = new Intent(CommonLib.getAppContext(), DialogActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("dialogType", Constants.DialogType.DIALOG_ASK_PERMISSION);
        CommonLib.getAppContext().startActivity(intent);
    }

    public static void startBattreyEnoughDialog(int battery, DialogListener dialogListener){
        dlListener = dialogListener;
        Intent intent = new Intent(CommonLib.getAppContext(), DialogActivity.class);
        intent.putExtra("dialogType", Constants.DialogType.DIALOG_BATTERY_ENOUGH);
        intent.putExtra("dialogType", 4);
        intent.putExtra("battery", battery);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonLib.getAppContext().startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                boolean boJudge = true;
                for(int permissionGrant:grantResults){
                    Log.d("grantResults: ", String.valueOf(permissionGrant));
                    if (permissionGrant != PackageManager.PERMISSION_GRANTED) {
                        boJudge = false;
                    }
                }

                if (boJudge) {
                    sendFinishDialog();
                    AppSingle.getInstance().setOperationPermission(0);
                } else {
                    if(dialog_hints != null){
                        dialog_hints.dismiss();
                        dialog_hints = null;
                    }
                    dialog_hints = DialogHints.showHintsDialog(this, new DialogHints.OnHintsDialogListener() {
                        @Override
                        public void onRetry() {
                            new PermissionManager(DialogActivity.this, PERMISSIONS_STORAGE, 1);
                        }
                        @Override
                        public void onOk() {
                            sendFinishDialog();
                            AppSingle.getInstance().setOperationPermission(0);
                        }
                    });
                }
                break;
            }
        }
    }

    private class DialogBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogTool.d("finish the activity");
            DialogActivity.this.finish();
        }
    }

    public static void sendFinishDialog(){
        Intent intent = new Intent();
        intent.setAction(DIALOG_FINISH_ACTION);
        if (localBroadcastManager != null){
            localBroadcastManager.sendBroadcast(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogTool.d("destroy the activity");
        if(dialogReceiver != null){
            localBroadcastManager.unregisterReceiver(dialogReceiver);
            dialogReceiver = null;
        }
        if(alertDialog != null) {
            alertDialog.dismiss();
        }
    }
}
