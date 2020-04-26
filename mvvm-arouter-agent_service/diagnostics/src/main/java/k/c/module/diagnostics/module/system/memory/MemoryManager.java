package k.c.module.diagnostics.module.system.memory;

import android.app.ActivityManager;
import android.content.Context;

import k.c.common.lib.CommonLib;

public class MemoryManager {

    private int totalMemory;
    private int availMemory;

    public MemoryManager() {
        getMemorySizeHumanized();
    }

    public int getTotalMemory(){
        return totalMemory;
    }

    public int getAvailMemory(){
        return availMemory;
    }


    private void getMemorySizeHumanized() {

        ActivityManager activityManager = (ActivityManager) CommonLib.getAppContext().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        int totalMemorySize = (int) memoryInfo.totalMem;
        int availMemorySize = (int) memoryInfo.availMem;

        totalMemory = memoryCounter(totalMemorySize);
        availMemory = memoryCounter(availMemorySize);

    }

    private int memoryCounter(int inputMermorySize) {

//        String returnSize;
//        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        int memoryUnit = 1024;
//        int kb = inputMermorySize / memoryUnit;
        int mb = inputMermorySize / (memoryUnit * memoryUnit);
//        double gb = inputMermorySize / (memoryUnit * memoryUnit * memoryUnit);
//        double tb = inputMermorySize / (memoryUnit * memoryUnit * memoryUnit * memoryUnit);

//        if (tb > 1) {
//            returnSize = decimalFormat.format(tb).concat(" TB");
//        } else if (gb > 1) {
//            returnSize = decimalFormat.format(gb).concat(" GB");
//        } else if (mb > 1) {
//            returnSize = decimalFormat.format(mb).concat(" MB");
//        } else if (kb > 1) {
//            returnSize = decimalFormat.format(mb).concat(" KB");
//        } else {
//            returnSize = decimalFormat.format(inputMermorySize).concat(" Bytes");
//        }

        return mb;
    }

}
