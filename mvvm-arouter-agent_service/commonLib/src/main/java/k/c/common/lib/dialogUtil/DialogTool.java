package k.c.common.lib.dialogUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import k.c.common.lib.R;
import k.c.common.lib.constants.Constants;

public class DialogTool {
    /**
     * @param context context
     * @param listener listener
     * @return dialog instance
     */
    public static Dialog showDialog(Context context, DialogConfig dialogConfig,
                                    final OnDialogListener listener) {
        if (context == null) {
            return null;
        }
        final Dialog dialog = new Dialog(context, R.style.baseDialogStyle);
        View view = initBaseView(context, R.layout.dialog_view_s1e);

        TextView tipContentText = view.findViewById(R.id.tv_content);
        Button setBtn = view.findViewById(R.id.btn_set);
        Button cancelBtn = view.findViewById(R.id.btn_cancel);
        TextView timeView = view.findViewById(R.id.dialog_time);

        TextView dialogContent = view.findViewById(R.id.dialogContent);
        Button setBtnOnly = view.findViewById(R.id.btn_set_only);
        LinearLayout layoutYesOnly = view.findViewById(R.id.btn_layout_yes_only);
        LinearLayout layoutYesAndNo = view.findViewById(R.id.btn_layout_yes_and_no);

        DialogCountDownTimer countDownTime;

        View img = view.findViewById(R.id.img);
        Animation rotate = AnimationUtils.loadAnimation(context, R.anim.rotate_anim);
        LinearInterpolator lin = new LinearInterpolator();


        if (context.getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE){
            //橫屏
            LinearLayout allLayout = view.findViewById(R.id.allDialog);
            ViewGroup.LayoutParams allLayoutParams = allLayout.getLayoutParams();
            allLayoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, view.getResources().getDisplayMetrics());
            allLayoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 340, view.getResources().getDisplayMetrics());

            allLayout.setLayoutParams(allLayoutParams);

            ViewGroup.LayoutParams laParams;
            laParams = img.getLayoutParams();
            laParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, view.getResources().getDisplayMetrics());
            laParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, view.getResources().getDisplayMetrics());
            img.setLayoutParams(laParams);
        }

        DialogCountDownTimer.CountDownTimeListener countDownListener = new DialogCountDownTimer.CountDownTimeListener() {
            @Override
            public void onTick(long millisUntilFinished) {
                Resources res = context.getResources();
                String text = String.format(res.getString(R.string.countDownTime),
                        millisUntilFinished / Constants.COUNTDOWN_INTERVAL);
                timeView.setText(text);
            }

            @Override
            public void onFinish() {
                dialog.dismiss();
                rotate.setInterpolator(lin);
                if (rotate != null) {
                    img.startAnimation(rotate);
                }
                tipContentText.setText(R.string.dialog_panasonic_loading_title);
                dialogContent.setText(R.string.dialog_panasonic_loading_content);
                timeView.setVisibility(View.GONE);
                layoutYesOnly.setVisibility(View.GONE);
                layoutYesAndNo.setVisibility(View.GONE);
                if(listener != null){
                    listener.onTimeout();
                }
            }

            @Override
            public String getClazzName() {
                return DialogTool.class.getName();
            }
        };

        countDownTime = new DialogCountDownTimer(countDownListener);

        tipContentText.getPaint().setFakeBoldText(true);
        if(dialogConfig.titleRes <= 0){
            tipContentText.setText("");
        }else{
            tipContentText.setText(dialogConfig.titleRes);
        }
        setBtn.getPaint().setFakeBoldText(true);
        if(dialogConfig.sureText <= 0){
            setBtn.setText(R.string.positiveBtn);
        }else{
            setBtn.setText(dialogConfig.sureText);
        }
        setBtnOnly.getPaint().setFakeBoldText(true);
        if(dialogConfig.sureText <= 0){
            setBtnOnly.setText(R.string.positiveBtn);
        }else{
            setBtnOnly.setText(dialogConfig.sureText);
        }
        if (dialogConfig.cancelText <= 0) {
            cancelBtn.setText(R.string.negativeBtn);
        } else {
            cancelBtn.setText(dialogConfig.cancelText);
        }
        if (dialogConfig.content <= 0) {
            dialogContent.setText("");
        } else {
            dialogContent.setText(dialogConfig.content);
        }
        cancelBtn.setOnClickListener(v -> {
            countDownTime.cancel();
            dialog.dismiss();
            if(listener != null){
                listener.cancel();
            }
        });

        setBtn.setOnClickListener(v -> {
            rotate.setInterpolator(lin);
            if (rotate != null) {
                img.startAnimation(rotate);
            }
            countDownTime.cancel();
            tipContentText.setText(R.string.dialog_panasonic_loading_title);
            dialogContent.setText(R.string.dialog_panasonic_loading_content);
            timeView.setVisibility(View.GONE);
            layoutYesOnly.setVisibility(View.GONE);
            layoutYesAndNo.setVisibility(View.GONE);
            dialog.dismiss();
            if(listener != null){
                listener.sure();
            }
        });
        setBtnOnly.setOnClickListener(v -> {
            rotate.setInterpolator(lin);
            if (rotate != null) {
                img.startAnimation(rotate);
            }
            countDownTime.cancel();
            tipContentText.setText(R.string.dialog_panasonic_loading_title);
            dialogContent.setText(R.string.dialog_panasonic_loading_content);
            timeView.setVisibility(View.GONE);
            layoutYesOnly.setVisibility(View.GONE);
            layoutYesAndNo.setVisibility(View.GONE);
            dialog.dismiss();
            if(listener != null){
                listener.sure();
            }
        });

        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        countDownTime.start();
        dialog.show();
        return dialog;
    }

    private static View initBaseView(Context context, int viewLayoutId){
        return LayoutInflater.from(context).inflate(
                viewLayoutId, null);
    }

    public interface OnDialogListener {
        void onTimeout();
        void cancel();
        void sure();
    }
}
