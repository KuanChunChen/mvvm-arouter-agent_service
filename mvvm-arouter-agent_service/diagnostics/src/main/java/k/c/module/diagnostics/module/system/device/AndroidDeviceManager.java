package k.c.module.diagnostics.module.system.device;

import android.os.SystemClock;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class AndroidDeviceManager {

    public final static String SYSTEM_COMMAND_GETPROP = "getprop";
    public final static String SYSTEM_COMMAND_RO_OEM_DEVICE = "ro.oem.device";

    public static String getSecurityPatchLevel() {

        String returnValue = null;
        try {
            Process process = new ProcessBuilder()
                    .command("/system/bin/getprop")
                    .redirectErrorStream(true)
                    .start();

            InputStream is = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = br.readLine()) != null) {
                returnValue += line + "\n";
                if (returnValue.contains("security_patch")) {
                    String[] splitted = line.split(":");
                    if (splitted.length == 2) {
                        return splitted[1];
                    }
                    break;
                }
            }
            br.close();
            process.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return returnValue;
    }

    public static String getKernelVersion() {

        try {
            Process p = Runtime.getRuntime().exec("uname -a");
            InputStream is = null;
            if (p.waitFor() == 0) {
                is = p.getInputStream();
            } else {
                is = p.getErrorStream();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            br.close();
            return line.substring(16,30);
        } catch (Exception ex) {
            Log.d("", "ERROR: " + ex.getMessage());
            return "Not available";
        }
    }


    public static String getUpTime() {

        // Get the whole uptime
        long uptimeMillis = SystemClock.elapsedRealtime();
        String wholeUptime = String.format(
                "%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(uptimeMillis),
                TimeUnit.MILLISECONDS.toMinutes(uptimeMillis)
                        - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
                        .toHours(uptimeMillis)),
                TimeUnit.MILLISECONDS.toSeconds(uptimeMillis)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                        .toMinutes(uptimeMillis)));

        return wholeUptime;
    }

    public static String getDeviceModel(String command, String command1) {
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
}
