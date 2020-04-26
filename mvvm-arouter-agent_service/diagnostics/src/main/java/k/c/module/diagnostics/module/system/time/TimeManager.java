package k.c.module.diagnostics.module.system.time;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeManager {

    public final static int GMT_TIME_ZONE_BEGIN_LENGTH = 3;
    public final static int GMT_TIME_ZONE_END_LENGTH = 9;

    public static String getGmtTimeZone() {
        Calendar calendar = Calendar.getInstance();
        String strCurrentTime = calendar.getTime().toString();
        int iGMTposition = strCurrentTime.indexOf("GMT");
        String strGMT = strCurrentTime.substring(iGMTposition + GMT_TIME_ZONE_BEGIN_LENGTH, iGMTposition + GMT_TIME_ZONE_END_LENGTH);
        return strGMT;
    }

    public static String getGmtTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(calendar.getTime());
    }
}
