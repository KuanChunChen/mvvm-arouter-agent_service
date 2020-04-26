package k.c.common.lib.dialogUtil;

public interface DialogListener {
    void onSure();

    void onCancel();

    void onTimeout();

    void onSelect(int time);
}
