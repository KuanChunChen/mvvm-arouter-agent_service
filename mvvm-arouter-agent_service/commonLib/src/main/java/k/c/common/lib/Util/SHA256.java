package k.c.common.lib.Util;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SHA256 {

    public static String encrypt(String strSrc) {
        try {
            return encrypt(strSrc.getBytes("ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            Log.e("","encrypt error, error message = %s" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static String encrypt(String strSrc, String charsetName) {
        try {
            return encrypt(strSrc.getBytes(charsetName));
        } catch (UnsupportedEncodingException e) {
            Log.e("","encrypt error, error message = %s" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static String encrypt(byte[] strBytes) {
        MessageDigest md = null;
        String strDes = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(strBytes);
            // to HexString
            strDes = hexBytes2HexString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            Log.e("","encrypt error, error message = %s" + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return strDes;
    }

    public static String encryptFile(String path) throws IOException {
        MessageDigest md = null;
        String strDes = null;
        FileInputStream fis = null;
        FileChannel fic = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            fis = new FileInputStream(new File(path));
            fic = fis.getChannel();
            ByteBuffer readBuffer = ByteBuffer.allocate(2 * 1024);
            while (true) {
                int readBytes = fic.read(readBuffer);
                if (readBytes > 0) {
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    readBuffer.clear();

                    md.update(bytes, 0, readBytes);
                } else {
                    break;
                }
            }
            // to HexString
            strDes = hexBytes2HexString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            Log.e("","encrypt error, error message = %s" + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fic != null) {
                    fic.close();
                }
                if (fis != null) {
                    fis.close();
                }
            }catch (IOException e){
                Log.e("","encrypt error, error message = %s" + e.getMessage());
                e.printStackTrace();
                strDes = null;
            }
        }
        return strDes;
    }

    // ************************* From Hex and To Hex *************************
    /**
     * Convert the hex byte values to the Hex String e.g byte[] b = {0xDF, 0x40,
     * 0x01}; ==> String = "DF4001"
     *
     * @param bs
     * @return String
     */
    public static String hexBytes2HexString(byte[] bs) {
        if (bs == null || bs.length <= 0) {
            return null;
        }

        StringBuilder returnStr = new StringBuilder();
        for (int i = 0; i < bs.length; i++) {
            int b = bs[i] & 0xFF;
            String hex = Integer.toHexString(b);

            if (hex.length() == 1) {
                // the length of hexString should % 2 == 0
                // if not, then add the prxfix '0'
                hex = "0" + hex;
            }
            returnStr.append(hex.toUpperCase());
        }
        return returnStr.toString();
    }
}
