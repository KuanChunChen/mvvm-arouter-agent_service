package k.c.common.lib.logTool;

import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogStrategy;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CTMSCsvFormatStrategy implements FormatStrategy {
    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final String NEW_LINE_REPLACEMENT = " <br> ";
    private static final String SEPARATOR = ",";
    @NonNull
    private final Date date;
    @NonNull
    private final SimpleDateFormat dateFormat;
    @NonNull
    private final LogStrategy logStrategy;
    @Nullable
    private final String tag;

    private CTMSCsvFormatStrategy(@NonNull CTMSCsvFormatStrategy.Builder builder) {
        this.date = builder.date;
        this.dateFormat = builder.dateFormat;
        this.logStrategy = builder.logStrategy;
        this.tag = builder.tag;
    }

    @NonNull
    public static CTMSCsvFormatStrategy.Builder newBuilder() {
        return new CTMSCsvFormatStrategy.Builder();
    }

    public void log(int priority, @NonNull String step, @NonNull String message) {
        String[] messages = message.split("\\.");
        if(messages.length != 3){
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(System.currentTimeMillis() / 1000);
        builder.append(",");
        builder.append(messages[0]);
        builder.append(",");
        builder.append(messages[1]);
        builder.append(",");
        builder.append(messages[2]);
        builder.append(NEW_LINE);
        this.logStrategy.log(priority, tag, builder.toString());
    }

    public static final class Builder {
        private static final int MAX_BYTES = 512000;
        Date date;
        SimpleDateFormat dateFormat;
        LogStrategy logStrategy;
        String tag;

        private Builder() {
            this.tag = "PRETTY_LOGGER";
        }

        @NonNull
        public CTMSCsvFormatStrategy.Builder date(@Nullable Date val) {
            this.date = val;
            return this;
        }

        @NonNull
        public CTMSCsvFormatStrategy.Builder dateFormat(@Nullable SimpleDateFormat val) {
            this.dateFormat = val;
            return this;
        }

        @NonNull
        public CTMSCsvFormatStrategy.Builder logStrategy(@Nullable LogStrategy val) {
            this.logStrategy = val;
            return this;
        }

        @NonNull
        public CTMSCsvFormatStrategy.Builder tag(@Nullable String tag) {
            this.tag = tag;
            return this;
        }

        @NonNull
        public CTMSCsvFormatStrategy build() {
            if (this.date == null) {
                this.date = new Date();
            }

            if (this.dateFormat == null) {
                this.dateFormat = new SimpleDateFormat("yyyy/MM/dd-HH:mm", Locale.TAIWAN);
            }

            if (this.logStrategy == null) {
                String diskPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                String folder = diskPath + File.separatorChar + "ctmslog";
                HandlerThread ht = new HandlerThread("AndroidFileLogger." + folder);
                ht.start();
                Handler handler = new CTMSDiskLogStrategy.CTMSWriteHandler(ht.getLooper(), folder, 512000);
                this.logStrategy = new CTMSDiskLogStrategy(handler);
            }

            return new CTMSCsvFormatStrategy(this);
        }
    }
}
