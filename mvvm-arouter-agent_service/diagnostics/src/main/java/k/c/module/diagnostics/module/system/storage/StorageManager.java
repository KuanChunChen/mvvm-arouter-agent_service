package k.c.module.diagnostics.module.system.storage;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

public class StorageManager {



    public static long getAvailableInternalStorageSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return formatSize(availableBlocks * blockSize);
    }

    public static long getTotalInternalStorageSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return formatSize(totalBlocks * blockSize);
    }



    private static boolean isExternalStorageAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    public static long getAvailableExternalStorageSize() {
        if (isExternalStorageAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            return formatSize(availableBlocks * blockSize);
        } else {
            return 0;
        }
    }

    public static long getTotalExternalStorageSize() {
        if (isExternalStorageAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long totalBlocks = stat.getBlockCountLong();
            return formatSize(totalBlocks * blockSize);
        } else {
            return 0;
        }
    }

    private static long formatSize(long size) {

        long memoryUnit = 1024;
//        int kb = inputMermorySize / memoryUnit;
        long mb =  (size / (memoryUnit * memoryUnit));
//        int gb = inputMermorySize / (memoryUnit * memoryUnit * memoryUnit);
//        int tb = inputMermorySize / (memoryUnit * memoryUnit * memoryUnit * memoryUnit);
        return mb;

    }
}
