package k.c.common.lib.logTool;


import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogAdapter;
import com.orhanobut.logger.LogcatLogStrategy;
import com.orhanobut.logger.PrettyFormatStrategy;

import k.c.common.lib.CommonLib;
import k.c.common.lib.Util.AndroidUtil;
import k.c.common.lib.Util.CommonUtil;
import k.c.common.lib.constants.LogStatusDefine;

public class AndroidLogAdapter implements LogAdapter {

    private final FormatStrategy formatStrategy;
    private boolean isLoggable = LogTool.IS_DEBUG;

    public AndroidLogAdapter() {
        this.formatStrategy = PrettyFormatStrategy.newBuilder()
                .logStrategy(new LogcatLogStrategy())
                .tag(randomKey() + "/" + LogStatusDefine.ANDROID_LOGGER_TAG + "/" + AndroidUtil.getVersionName(CommonLib.getAppContext()).versionName)
                .build();
    }

    public AndroidLogAdapter(FormatStrategy formatStrategy) {
        this.formatStrategy = formatStrategy;
    }

    @Override
    public boolean isLoggable(int priority, String tag) {
        return (isLoggable || CommonLib.showLogable()) && priority >= LogTool.LOGGER_LEVEL;
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

