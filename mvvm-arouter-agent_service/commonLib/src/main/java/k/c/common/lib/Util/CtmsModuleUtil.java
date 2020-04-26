package k.c.common.lib.Util;


import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import CTOS.CtSystem;
import CTOS.CtSystemException;
import k.c.common.lib.constants.Constants;

public class CtmsModuleUtil {



    private static boolean mHideVersionName = true;


    public static String command(String command, String command1) {
        String result = "";
        String line;
        String[] cmd = new String[]{command, command1};
        String workdirectory = "/system/bin/";
        try {
            ProcessBuilder bulider = new ProcessBuilder(cmd);
            bulider.directory(new File(workdirectory));
            bulider.redirectErrorStream(true);
            Process process = bulider.start();
            InputStream in = process.getInputStream();
            InputStreamReader isrout = new InputStreamReader(in);
            BufferedReader brout = new BufferedReader(isrout, 8 * 1024);
            while ((line = brout.readLine()) != null) {
                result += line;
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String readLine(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename), 256);
        try {
            return reader.readLine();
        } finally {
            reader.close();
        }
    }


    public static String formatKernelVersion(String rawKernelVersion) {
        // Example (see tests for more):
        // Linux version 3.0.31-g6fb96c9 (android-build@xxx.xxx.xxx.xxx.com) \
        //     (gcc version 4.6.x-xxx 20120106 (prerelease) (GCC) ) #1 SMP PREEMPT \
        //     Thu Jun 28 11:02:39 PDT 2012

        final String PROC_VERSION_REGEX =
                "Linux version (\\S+) " + /* group 1: "3.0.31-g6fb96c9" */
                        "\\((\\S+?)\\) " +        /* group 2: "x@y.com" (kernel builder) */
                        "(?:\\(gcc.+? \\)) " +    /* ignore: GCC version information */
                        "(#\\d+) " +              /* group 3: "#1" */
                        "(?:.*?)?" +              /* ignore: optional SMP, PREEMPT, and any CONFIG_FLAGS */
                        "((Sun|Mon|Tue|Wed|Thu|Fri|Sat).+)"; /* group 4: "Thu Jun 28 11:02:39 PDT 2012" */

        Matcher m = Pattern.compile(PROC_VERSION_REGEX).matcher(rawKernelVersion);
        if (!m.matches()) {
            Log.e("CTMS_SERVICE", "Regex did not match on /proc/version: " + rawKernelVersion);
            return "Unavailable";
        } else if (m.groupCount() < 4) {
            Log.e("CTMS_SERVICE", "Regex match on /proc/version only returned " + m.groupCount()
                    + " groups");
            return "Unavailable";
        }
        if (mHideVersionName) {
            return m.group(1);
        } else {
            return m.group(1) + "\n" +                     // 3.0.31-g6fb96c9
                    m.group(2) + " " + m.group(3) + "\n" + // x@y.com #1
                    m.group(4);                            // Thu Jun 28 11:02:39 PDT 2012
        }
    }

    public static boolean isOtaIndexExist() {

        String strOtaIndexPath = Constants.Path.UPDATE_OTA_FILE_RECORD;
        return FileUtil.isDirectoryExist(strOtaIndexPath) ? true : false;
    }

    public static String getFactoryNumber() {

        byte[] bArrFSN = new byte[16];
        CtSystem mCtSystem = new CtSystem();
        String str_fsn = "";
        try {
            bArrFSN = mCtSystem.getFactorySN();
            str_fsn = String.format("%c%c%c%c%c%c%c%c%c%c%c%c%c%c%c", bArrFSN[0], bArrFSN[1],
                    bArrFSN[2], bArrFSN[3], bArrFSN[4], bArrFSN[5], bArrFSN[6], bArrFSN[7], bArrFSN[8], bArrFSN[9], bArrFSN[10],
                    bArrFSN[11], bArrFSN[12], bArrFSN[13], bArrFSN[14]);
        } catch (CtSystemException eCtSystemException) {
            eCtSystemException.printStackTrace();
        } catch (RuntimeException eRuntimeException) {
            eRuntimeException.printStackTrace();
        } catch (Exception eException) {
            eException.printStackTrace();
        }

        return str_fsn;
    }


}