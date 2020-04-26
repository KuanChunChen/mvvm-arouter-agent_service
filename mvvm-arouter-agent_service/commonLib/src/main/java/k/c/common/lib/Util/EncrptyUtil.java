package k.c.common.lib.Util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

public class EncrptyUtil {

    public static String encrypt(String strData) throws UnsupportedEncodingException {
        byte[] data = strData.getBytes("UTF-8");
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        return base64;
    }


    public static String decrypt(String strBase64) throws UnsupportedEncodingException {
        byte[] data = Base64.decode(strBase64, Base64.DEFAULT);
        String strPlainText = new String(data, "UTF-8");
        return strPlainText;
    }


}
