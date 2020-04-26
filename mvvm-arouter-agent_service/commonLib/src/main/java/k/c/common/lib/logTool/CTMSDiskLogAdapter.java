package k.c.common.lib.logTool;

import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogAdapter;

import k.c.common.lib.Util.CommonUtil;


public class CTMSDiskLogAdapter implements LogAdapter {

    private final FormatStrategy formatStrategy;
    private boolean isLoggable = true;

    public CTMSDiskLogAdapter() {
        this.formatStrategy = CTMSCsvFormatStrategy.newBuilder()
                .build();
    }

    public void setLoggable(boolean loggable) {
        isLoggable = loggable;
    }

    public CTMSDiskLogAdapter(FormatStrategy formatStrategy) {
        this.formatStrategy = formatStrategy;
    }

    @Override
    public boolean isLoggable(int priority, String tag) {
        return isLoggable && priority == LogTool.LOGGER_LEVEL_DISK;
    }

    @Override
    public void log(int priority, String tag, String message) {
        this.formatStrategy.log(priority, tag + "/" + CommonUtil.timeStamp2Date(System.currentTimeMillis(), null), message);
    }

    private int last;

    private String randomKey() {
        int random = (int) (10 * Math.random());
        if (random == last) {
            random = (random + 1) % 10;
        }
        last = random;
        return String.valueOf(random);
    }
}
