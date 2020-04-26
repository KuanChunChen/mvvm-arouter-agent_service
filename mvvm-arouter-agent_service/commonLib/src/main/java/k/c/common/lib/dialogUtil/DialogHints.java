package k.c.common.lib.dialogUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import k.c.common.lib.R;

public class DialogHints {

    public static Dialog showHintsDialog(Context context, final OnHintsDialogListener listener) {

        if (context == null) {
            return null;
        }
        final Dialog dialog = new Dialog(context, R.style.baseDialogStyle);

//        String strDeviceModel = KernelUtil.getDeviceModel();
        View view = initBaseView(context, R.layout.dialog_hints);

        if (context.getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE){
            //橫屏
            LinearLayout layout = view.findViewById(R.id.allDialog_hints);
            ViewGroup.LayoutParams params = layout.getLayoutParams();
            params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 320, view.getResources().getDisplayMetrics());
            params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 340, view.getResources().getDisplayMetrics());
            layout.setLayoutParams(params);
        }
        Button okBtn = view.findViewById(R.id.btn_ok);
        Button retryBtn = view.findViewById(R.id.btn_retry);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvHints = view.findViewById(R.id.tv_Hints);
        tvTitle.setText(R.string.dialog_hints_title);
        tvHints.setText(R.string.dialog_hints_content);
        okBtn.setText(R.string.dialog_hints_btn_ok);
        retryBtn.setText(R.string.dialog_hints_btn_retry);

        retryBtn.setOnClickListener(v -> {
            dialog.cancel();
            listener.onRetry();
        });

        okBtn.setOnClickListener(v -> {
            listener.onOk();
        });


        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.show();
        return dialog;

    }

    private static View initBaseView(Context context, int viewLayoutId){
        return LayoutInflater.from(context).inflate(viewLayoutId, null);
    }

    public interface OnHintsDialogListener {
        void onRetry();
        void onOk();
    }
}
