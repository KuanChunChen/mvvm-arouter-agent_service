package k.c.common.lib.logTool;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.orhanobut.logger.LogStrategy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import k.c.common.lib.Util.CommonUtil;

public class CTMSDiskLogStrategy implements LogStrategy {

    @NonNull
    private final Handler handler;
    private final static String FILE_NAME = "logs";

    public CTMSDiskLogStrategy(@NonNull Handler handler) {
        this.handler = CommonUtil.checkNotNull(handler);
    }

    @Override
    public void log(int level, @Nullable String tag, @NonNull String message) {
        CommonUtil.checkNotNull(message);
        this.handler.sendMessage(this.handler.obtainMessage(level, message));
    }

    static class CTMSWriteHandler extends Handler {
        @NonNull
        private final String folder;
        private final int maxFileSize;

        CTMSWriteHandler(@NonNull Looper looper, @NonNull String folder, int maxFileSize) {
            super(CommonUtil.checkNotNull(looper));
            this.folder = CommonUtil.checkNotNull(folder);
            this.maxFileSize = maxFileSize;
        }

        public void handleMessage(@NonNull Message msg) {
            String content = (String)msg.obj;
            FileWriter fileWriter = null;
            File logFile = this.getLogFile(this.folder);

            try {
                if(logFile.length() >= (long)this.maxFileSize){
                    fileWriter = new FileWriter(logFile);
                    fileWriter.write("");
                    fileWriter.flush();
                    fileWriter.close();
                }

                fileWriter = new FileWriter(logFile, true);
                this.writeLog(fileWriter, content);
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException var8) {
                if (fileWriter != null) {
                    try {
                        fileWriter.flush();
                        fileWriter.close();
                    } catch (IOException var7) {
                        ;
                    }
                }
            }

        }

        private void writeLog(@NonNull FileWriter fileWriter, @NonNull String content) throws IOException {
            CommonUtil.checkNotNull(fileWriter);
            CommonUtil.checkNotNull(content);
            fileWriter.append(content);
        }

        private File getLogFile(@NonNull String folderName) {
            CommonUtil.checkNotNull(folderName);
            File folder = new File(folderName);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            File newFile;
            newFile = new File(folder, String.format("%s.csv", CTMSDiskLogStrategy.FILE_NAME));

            return newFile;
        }
    }
}
