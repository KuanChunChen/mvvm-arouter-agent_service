package k.c.common.lib.Util;

import android.util.Log;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import k.c.common.lib.constants.Constants;
import k.c.common.lib.logTool.LogTool;

import static k.c.common.lib.constants.LogStatusDefine.DATE_TIME_FORMAT_STYLE_02;


public class TimeUtil {

    public static long getLongSysMillisTime(){
        long timecurrentTimeMillis = System.currentTimeMillis();
        LogTool.d("Current Time : " + timecurrentTimeMillis);
        return timecurrentTimeMillis;
    }

    public static String getCurrentSysMillisTime(){
        long timecurrentTimeMillis = System.currentTimeMillis();
        LogTool.d("Current Time : " + timecurrentTimeMillis);
        return String.valueOf(timecurrentTimeMillis);
    }

    public static String getCurrentSysTime() {

        return new SimpleDateFormat(DATE_TIME_FORMAT_STYLE_02).format(new Date(System.currentTimeMillis()));
    }

    public static String longToStringSysTime(long timecurrentTimeMillis) {

        return new SimpleDateFormat(DATE_TIME_FORMAT_STYLE_02).format(new Date(timecurrentTimeMillis));
    }

    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    public static String getGmtTimeZone() {
        Calendar mCal = Calendar.getInstance();
        String strCurrentTime = mCal.getTime().toString();
        int iGMTposition = strCurrentTime.indexOf("GMT");
        String strGMT = strCurrentTime.substring(iGMTposition + Constants.Time.GMT_TIME_ZONE_BEGIN_LENGTH, iGMTposition + Constants.Time.GMT_TIME_ZONE_END_LENGTH);
        Log.d("GMT position :", String.valueOf(iGMTposition));
        Log.d("GMT : ", strGMT);
        return strGMT;
    }

}
