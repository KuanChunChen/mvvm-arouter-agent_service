package k.c.common.lib.Util;


import android.util.Log;

import java.util.Locale;
import java.util.TimeZone;

public class LocateUtil {

    public static String getCurrentArea(){
        String strCurrentCountry = Locale.getDefault().getCountry();
        Log.d("Current Country :", strCurrentCountry);
        return strCurrentCountry;

    }

    public static String getCurrentLanuage(){
        String languageName = Locale.getDefault().getLanguage();
        return languageName;
    }

    public static String getCurrentAreaInEn(){
        String strCurrentCountry = Locale.ENGLISH.getDefault().getCountry();
        Locale loc = new Locale("en",strCurrentCountry);
        return loc.getDisplayCountry();
    }

    public static String getCurrentTimeZoneID(){
        String strTimeZoneID = TimeZone.getDefault().getID();
        return strTimeZoneID;
    }
}
