package k.c.common.lib.Util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import k.c.common.lib.logTool.LogTool;



public class CommonUtil {

    public static String hostFormat(String host, int port){
        if (host.startsWith("https://")){
            return host + ":" + port;
        }else {
            return "https://" + host + ":" + port;
        }
    }

    /**
     * read the prop from system.
     * @param prop prop key
     * @return prop value
     */
    public static String readProp(String prop) {
        try {
            Process process = Runtime.getRuntime().exec("getprop " + prop);
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            BufferedReader input = new BufferedReader(ir);
            String result = input.readLine();
            ir.close();
            input.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int toInt(String num){
        try {
            return Integer.parseInt(num);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Read the info from the file and convert to return as string value, filter
     * the lineSeparator when read the file.
     *
     * @param file
     * @return String
     */
    public static String parseFile(File file) {
        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        StringBuilder sb = new StringBuilder();

        try {
            // Check whether the file exists
            if (file.isFile() && file.exists()) {
                fileInputStream = new FileInputStream(file);

                // Using the GBK as encoding format
                inputStreamReader = new InputStreamReader(fileInputStream, "GBK");
                bufferedReader = new BufferedReader(inputStreamReader);
                String lineText;
                while ((lineText = bufferedReader.readLine()) != null) {
                    sb.append(lineText);
                }
            } else {
                // Can't find the file from the path.
                LogTool.d("Can't find the specified file.");
            }
        } catch (FileNotFoundException ex) {
            // The exception that can't find any files
            LogTool.d("No files be found for the specified path.");
        } catch (UnsupportedEncodingException ex) {
            // The exception that encoding error.
            LogTool.d("Encoding the file content error.");
        } catch (IOException ex) {
            // The exception that read the content of the file
            LogTool.d("Error when reading the contents of the file.");
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException ex) {
                // The exception that can't close the input stream reader.
                LogTool.d("Error when closing the read file stream");
            }
        }
        return sb.toString();
    }

    /**
     * Read the info from the file and return as string value, don't filter the
     * lineSeparator when read the file.
     *
     * @param file
     * @return String
     */
    public static String parseFileWithLineSeparator(File file) {
        StringBuilder result = new StringBuilder();
        try {
            // construct BufferedReader loader file
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s;
            // used the readLine ，a line once
            while ((s = br.readLine()) != null) {
                result.append(System.lineSeparator()).append(s);
            }
            br.close();
        } catch (IOException e) {
            // The exception that read the content of the file
            LogTool.d("Error when reading the contents of the file.");
        }
        return result.toString();
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

    /**
     * Split the hex string to hex byte array list by {splitedByteSize}, E.g. if
     * {hexString} = "00040A010000000201010000" and {splitedByteSize} = 6, then
     * the results = {00040A010000},{000201010000}
     *
     * @param hexString
     * @param splitedByteSize
     * @return List<byte[]>
     */
    public static List<byte[]> hexString2HexBytesList(String hexString, int splitedByteSize) {
        List<byte[]> byteArrayList = new ArrayList<byte[]>();
        if (isBlank(hexString) || splitedByteSize < 0) {
            return byteArrayList;
        }

        if (splitedByteSize > 0) {
            boolean splitFlag = true;
            // 1 byte = 2 hex string length, e.g. 1 byte '0xFF' = 2 hex string length
            int splitedStringSize = splitedByteSize * 2;
            while (splitFlag) {
                String splitedStringMessage;
                if (hexString.length() > splitedStringSize) {
                    splitedStringMessage = hexString.substring(0, splitedStringSize);
                    hexString = hexString.substring(splitedStringSize, hexString.length());
                } else {
                    splitedStringMessage = hexString;
                    splitFlag = false;
                }
                byte[] splitedByteMessage = hexString2HexBytes(splitedStringMessage);

                byteArrayList.add(splitedByteMessage);
            }
        } else {
            byte[] splitedByteMessage = hexString2HexBytes(hexString);
            byteArrayList.add(splitedByteMessage);
        }

        return byteArrayList;
    }

    /**
     * Convert the hex string to the hex byte array. e.g String = "D204DF" ==>
     * byte[] = {0xD2, 0x04, 0xDF}
     *
     * @param hexString
     * @return byte[]
     */
    public static byte[] hexString2HexBytes(String hexString) {
        if (isBlank(hexString)) {
            return null;
        } else if ((hexString.length() % 2) != 0) {
            // Add zero if the length is odd value.
            String validBytes = hexString.substring(0, hexString.length() - 1);
            String lastHexByte = "0" + hexString.substring(hexString.length() - 1);

            hexString = validBytes + lastHexByte;
        }

        int byteLength = hexString.length() / 2;
        byte[] returnByte = new byte[byteLength];
        byte[] tmpByte = hexString.getBytes();

        try {
            for (int i = 0; i < byteLength; i++) {
                returnByte[i] = uniteBytes(tmpByte[i * 2], tmpByte[i * 2 + 1]);
            }
        } catch (NumberFormatException e) {
            returnByte = null;
            LogTool.d(
                    "NumberFormatException in the method hexString2HexBytes, please check the value of input string, it can only be hex values.");
        }
        return returnByte;
    }

    /**
     * unite bytes
     *
     * @param b0
     * @param b1
     * @return byte
     */
    private static byte uniteBytes(byte b0, byte b1) {
        byte b0Decode = Byte.decode("0x" + new String(new byte[]{b0}));
        b0Decode = (byte) (b0Decode << 4);

        byte b1Decode = Byte.decode("0x" + new String(new byte[]{b1}));

        byte ret = (byte) (b0Decode ^ b1Decode);
        return ret;
    }

    /**
     * Convert HexInt to HexString
     *
     * @param intValue
     * @return String
     */
    public static String hexInt2HexString(int intValue) {
        String returnStr;
        if (intValue == 0) {
            returnStr = "0x0000";
        } else {
            String s = "0000" + Integer.toHexString(intValue).toUpperCase();

            returnStr = "0x" + s.substring(s.length() - 4);
        }
        return returnStr;
    }

    // ************************* To Hex *************************
    /**
     * Convert the byte values to the ASCII, then return as Hex String e.g
     * byte[] b = {'a', '2', '3'}; ==> string = "613233"
     *
     * @param b
     * @return String
     */
    public static String bytes2HexString(byte[] b) {
        if (b == null || b.length <= 0) {
            return null;
        }

        StringBuilder returnStr = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i]);

            if (hex.length() == 1) {
                // the length of hexString should % 2 == 0
                // if not, then add the prefix '0'
                hex = "0" + hex;
            }
            returnStr.append(hex.toUpperCase());
        }
        return returnStr.toString();
    }

    public static String string2HexAscii(String str) {
        StringBuilder sb = new StringBuilder();
        char[] chars = str.toCharArray();
        for (char aChar : chars) {
            int charVar = (int) aChar;
            sb.append(Integer.toString(charVar, 16));
        }
        return sb.toString().toUpperCase();
    }

    public static String unicode(String source){
        StringBuffer sb = new StringBuffer();
        char [] sourceChar = source.toCharArray();
        String unicode = null;
        for (char aSourceChar : sourceChar) {
            unicode = Integer.toHexString(aSourceChar);
            if (unicode.length() <= 2) {
                unicode = "00" + unicode;
            }
            sb.append("\\u").append(unicode);
        }
        System.out.println(sb);
        return sb.toString();
    }

    public static String decodeUnicode(String unicode) {
        StringBuilder sb = new StringBuilder();

        String[] hex = unicode.split("\\\\u");

        for (int i = 1; i < hex.length; i++) {
            int data = Integer.parseInt(hex[i], 16);
            sb.append((char) data);
        }
        return sb.toString();
    }

    // ************************* From Hex *************************
    /**
     * Convert the hex ascii to the String value. e.g str = "31303032" ==>
     * "1002"
     *
     * @param str
     * @return String
     */
    public static String hexAscii2String(String str) {
        String returnStr = "";
        if (isBlank(str)) {
            return returnStr;
        }

        byte[] bs = new byte[str.length() / 2];
        for (int i = 0; i < bs.length; i++) {
            // get the hex int values
            int s = Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
            if (s != 0) {
                bs[i] = (byte) (0xFF & s);
            }
        }

        // Fix the bug if the str value is 0000...
        for (int j = 0; j < bs.length; j++) {
            byte b = bs[j];
            if (b != 0) {
                returnStr = new String(bs);
                break;
            } else if (j == bs.length - 1) {
                returnStr = "0";
            }
        }

        return returnStr;
    }

    /**
     * Convert the value of hex String to Integer by 16 radix, then return as
     * Binary String e.g String hexString:"24" ==> 00100100
     *
     * @param hexString
     * @return String
     */
    public static String hexString2binaryString(String hexString) {
        // the length of hexString should % 2 == 0 , e.g 01 0A 2D ...
        if (isBlank(hexString) || hexString.length() % 2 != 0) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        String tmp;
        for (int i = 0; i < hexString.length(); i++) {
            // the length of one Hex = 4 Binary, so add the lost zero for every Hex String.
            // e.g if hexString is '4', then the value of bStr is 100, should be 0100;
            // if hexString is '2', then the value of bStr is 10, should be 0010
            String bStr = Integer
                    .toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));

            // Add the lost zero for the binary string value.
            tmp = "0000" + bStr;
            sb.append(tmp.substring(tmp.length() - 4));
        }

        return sb.toString();
    }

    // ************************* Validation *************************
    /**
     * verification Of Date Is Correct Date format should be "yymmdd"
     *
     * @param sDate
     * @return boolean
     */
    public static boolean verificationOfDateIsCorrect(String sDate) {
        if (isBlank(sDate)) {
            return false;
        }

        // Define regular expression
        String datePattern = "(^([0-2][0-9])(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$)";

        Pattern pattern = Pattern.compile(datePattern);
        Matcher match = pattern.matcher(sDate);
        return match.matches();
    }

    /**
     * verification of Time is Correct Time format should be "hhmmss"
     *
     * @param sTime
     * @return boolean
     */
    public static boolean verificationOfTimeIsCorrect(String sTime) {
        if (isBlank(sTime)) {
            return false;
        }

        // Define regular expression
        String timePattern = "(^([0-1][0-9]|2[0-3])([0-5][0-9])([0-5][0-9])$)";

        Pattern pattern = Pattern.compile(timePattern);
        Matcher match = pattern.matcher(sTime);
        return match.matches();
    }

    /**
     * verification of Txn Amt is correct Total length of txn amount should be
     * 12, 10 length is integer and 2 length is decimal, e.g amount = "123.43"
     * return true; amount = "123.433" return false
     *
     * @param amount
     * @return boolean
     */
    public static boolean verificationOfTxnAmtIsCorrect(String amount) {
        boolean returnFlag = false;
        if (isBlank(amount) || amount.length() > 13) {
            return returnFlag;
        }

        String[] splitStrArray = amount.split("\\.");
        String decimalStr;
        String integerStr;
        int len = splitStrArray.length;
        if (len == 0 || len > 2) {
            return returnFlag;
        } else if (len == 2) {
            integerStr = splitStrArray[0];
            decimalStr = splitStrArray[1];
        } else {
            integerStr = splitStrArray[0];
            decimalStr = "";
        }

        // return ture if 10 length is integer and 2 length is decimal
        if (decimalStr.length() > 2 || integerStr.length() > 10) {
            return returnFlag;
        }

        // Define regular expression
        String dataPattern = "(^\\d{" + integerStr.length() + "}\\.?\\d{" + decimalStr.length() + "}$)";
        Pattern pattern = Pattern.compile(dataPattern);
        Matcher match = pattern.matcher(amount);
        returnFlag = match.matches();

        return returnFlag;
    }

    /**
     * Validate Decimal Integer String As Byte
     *
     * e.g. if the validated string is blank or '0' then ignore and return true;
     * if the validated string could not match the pattern "[0-9]{1,3}" then
     * return false; if the validated string is greater than '255' then return
     * false, because max value of byte could only be 'FF' else return true.
     *
     * @param str
     * @return boolean
     */
    public static boolean validateDecimalIntStringAsByte(String str) {
        if (isBlank(str) || "0".equals(str)) {
            return true;
        }

        String strWithoutFirstZero = str.trim().replaceFirst("^0+", "");

        String dataPattern = "(^[0-9]{1,3}$)";
        Pattern pattern = Pattern.compile(dataPattern);
        Matcher match = pattern.matcher(strWithoutFirstZero);

        if (match.matches()) {
            return Integer.valueOf(strWithoutFirstZero) <= 255;
        } else {
            return false;
        }
    }

    /**
     * Check whether the port value is valid, the correct value should be
     * between 1 and 65535
     *
     * @param portValue
     * @return boolean
     */
    public static boolean isValidPort(String portValue) {
        if (isBlank(portValue)) {
            return true;
        }

        String dataPattern = "(^[1-9]\\d{0,4}$)";
        Pattern pattern = Pattern.compile(dataPattern);
        Matcher match = pattern.matcher(portValue);

        if (match.matches()) {
            return Integer.valueOf(portValue) <= 65535;
        } else {
            return false;
        }
    }

    /**
     * Check whether the ip address is valid, the correct value should match
     * IPV4
     *
     * @param ipAddress
     * @return boolean
     */
    public static boolean isValidIPAddress(String ipAddress) {
        if (isBlank(ipAddress)) {
            return true;
        }

        Pattern pattern = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");
        Matcher match = pattern.matcher(ipAddress);

        return match.matches();
    }

    /**
     * Check whether the str value is valid, the values should be Alpha or
     * Numeric
     *
     * @param str
     * @return boolean
     */
    public static boolean isValidAlphaNumeric(String str) {
        if (isBlank(str)) {
            return true;
        }

        Pattern pattern = Pattern.compile("^[A-Za-z0-9]+$");
        Matcher match = pattern.matcher(str);

        return match.matches();
    }

    /**
     * Check whether the value is Hex
     *
     * @param str
     * @return boolean
     */
    public static boolean isHexStr(String str) {
        if (isBlank(str)) {
            return false;
        }

        // Define regular expression
        String datePattern = "(^[A-Fa-f0-9]+$)";

        Pattern pattern = Pattern.compile(datePattern);
        Matcher match = pattern.matcher(str);

        return match.matches();
    }

    /**
     * Check whether the value is blank, if it's null or empty or blank then
     * return true, else return false
     *
     * @param str
     * @return boolean
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Check whether the value is not blank, if it's not blank then return true,
     * else return false
     *
     * @param str
     * @return boolean
     */
    public static boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }

    /**
     * Validate whether input str value is the Byte type, e.g. if the value is
     * '0xDF' or 'a' or '16' then return true
     *
     * @param str
     * @return boolean
     */
    public static boolean isByte(String str) {
        boolean returnValue = true;

        if (CommonUtil.isNotBlank(str)) {
            if (str.length() > 2 && "0x".equals(str.substring(0, 2))) {
                String valueWithoutPrefix = str.substring(2);
                if (!isHexStr(valueWithoutPrefix) || valueWithoutPrefix.length() > 2) {
                    returnValue = false;
                }
            } else {
                if (isDigit(str)) {
                    try {
                        int pv = Integer.parseUnsignedInt(str);
                        if (pv < 0 || pv > 255) {
                            LogTool.d("Value out of range when valid BYTE type. Value: " + str);
                            returnValue = false;
                        }
                    } catch (NumberFormatException nfe) {
                        LogTool.d("Number format exception when valid BYTE type. Value: " + str);
                        returnValue = false;
                    }
                } else {
                    returnValue = str.length() <= 1;
                }
            }
        }

        return returnValue;
    }

    /**
     * Validate whether input str value is the Long type, e.g. if the value is
     * '0xFFFFFFFF' or '16' then return true
     *
     * @param str
     * @return boolean
     */
    public static boolean isLong(String str) {
        boolean returnValue = true;

        if (CommonUtil.isNotBlank(str)) {
            if (str.length() > 2 && "0x".equals(str.substring(0, 2))) {

                String valueWithoutPrefix = str.substring(2);
                if (!isHexStr(valueWithoutPrefix) || valueWithoutPrefix.length() > 8) {
                    returnValue = false;
                }
            } else {
                if (isDigit(str)) {
                    try {
                        Long.parseUnsignedLong(str);
                    } catch (NumberFormatException nfe) {
                        LogTool.d("Value out of range or number format exception when valid Long type. Value: " + str);
                        returnValue = false;
                    }
                } else {
                    returnValue = false;
                }
            }
        }

        return returnValue;
    }

    /**
     * Validate whether input str value is the Short type, e.g. if the value is
     * "0x0002" or '12' then return true;
     *
     * @param str
     * @return boolean
     */
    public static boolean isShort(String str) {
        boolean returnValue = true;
        if (CommonUtil.isNotBlank(str)) {
            if (str.length() > 2 && "0x".equals(str.substring(0, 2))) {

                String valueWithoutPrefix = str.substring(2);
                if (!isHexStr(valueWithoutPrefix) || valueWithoutPrefix.length() > 4) {
                    returnValue = false;
                }
            } else {
                if (isDigit(str)) {
                    try {
                        // Fix bug 'Linux_BUG_028'
                        Integer.parseInt(str);
//                        Short.parseShort(str);
                    } catch (NumberFormatException nfe) {
                        LogTool.d("Value out of range or number format exception when valid Short type. Value: " + str);
                        returnValue = false;
                    }
                } else {
                    returnValue = false;
                }
            }
        }

        return returnValue;
    }

    /**
     * Validate whether input str value is the Int type
     *
     * @param str
     * @return boolean
     */
    public static boolean isInt(String str) {
        boolean returnValue = true;

        if (CommonUtil.isNotBlank(str)) {
            if (str.length() > 2 && "0x".equals(str.substring(0, 2))) {
                String valueWithoutPrefix = str.substring(2);
                if (!isHexStr(valueWithoutPrefix) || valueWithoutPrefix.length() > 8) {
                    returnValue = false;
                }
            } else {
                if (isDigit(str)) {
                    try {
                        Integer.parseUnsignedInt(str);
                    } catch (NumberFormatException nfe) {
                        LogTool.d("Value out of range or number format exception when valid INT type. Value: " + str);
                        returnValue = false;
                    }
                } else {
                    returnValue = false;
                }
            }
        }
        return returnValue;
    }

    /**
     * Validate whether input str value is the Float type
     *
     * @param str
     * @return boolean
     */
    public static boolean isFloat(String str) {
        boolean returnValue = true;

        if (CommonUtil.isNotBlank(str)) {
            try {
                Float.parseFloat(str);
            } catch (NumberFormatException nfe) {
                LogTool.d("Number format exception when valid Float type. Value: " + str);
                returnValue = false;
            }
        }else{
            returnValue = false;
        }
        return returnValue;
    }

    /**
     * Validate whether input str value is the Boolean type
     *
     * @param str
     * @return boolean
     */
    public static boolean isBoolean(String str) {
        boolean returnValue = true;
        if (str.length() > 2 && "0x".equals(str.substring(0, 2))) {
            String valueWithoutPrefix = str.substring(2);

            try {
                int pv = Integer.parseInt(valueWithoutPrefix);
                if (pv != 0 && pv != 1) {
                    LogTool.d("The type of boolean can only be True or False, please check the value: " + str);
                    returnValue = false;
                }
            } catch (NumberFormatException nfe) {
                LogTool.d("Number format exception when valid Boolean type. Value: " + str);
                returnValue = false;
            }
        } else {
            if (isDigit(str)) {
                try {
                    int pv = Integer.parseInt(str);
                    if (pv != 0 && pv != 1) {
                        LogTool.d("The type of boolean can only be True or False, please check the value: " + str);
                        returnValue = false;
                    }
                } catch (NumberFormatException nfe) {
                    LogTool.d("Number format exception when valid Boolean type. Value: " + str);
                    returnValue = false;
                }
            } else {
                returnValue = false;
            }
        }

        return returnValue;
    }

    /**
     * Validate whether input str value is the Byte Array type
     *
     * @param str
     * @return boolean
     */
    public static boolean isByteArray(String str) {
        boolean returnValue = true;
        if (CommonUtil.isBlank(str)) {
            return returnValue;
        } else {
            // Replace all of the blank
//            str = str.replaceAll(DLAConstants.SYMBOL_SPACE, "");
        }

//        String[] strArray = str.split(DLAConstants.COMMA);
        String[] strArray = CommonUtil.splitBySeparator(str, ",");
        for (String sa : strArray) {
            if (sa.length() > 2 && "0x".equals(sa.substring(0, 2))) {
                String valueWithoutPrefix = sa.substring(2);
                if (!isHexStr(valueWithoutPrefix) || valueWithoutPrefix.length() > 2) {
                    returnValue = false;
                    break;
                }
            } else {
                // Fixed bug to allow input the escape codes "\n,\fn,\fu,\fr,\ft1,\ft2,\ft3"
                if (!new HashMap<String, Byte[]>().containsKey(sa)
                        && sa.length() > 1) {
                    returnValue = false;
                    break;
                }
            }
        }

        return returnValue;
    }

    /**
     * Validate whether input str value is the Short pointer type
     *
     * @param str
     * @return boolean
     */
    public static boolean isShortArray(String str) {
        boolean returnValue = true;
        if (CommonUtil.isBlank(str)) {
            return returnValue;
        } else {
            // Replace all of the blank
//            str = str.replaceAll(DLAConstants.SYMBOL_SPACE, "");
        }

//        String[] strArray = str.split(DLAConstants.COMMA);
        String[] strArray = CommonUtil.splitBySeparator(str, ",");
        for (String sa : strArray) {
            if (!CommonUtil.isShort(sa)) {
                returnValue = false;
                break;
            }
        }

        return returnValue;
    }

    /**
     * Validate whether input str value is the Long pointer type
     *
     * @param str
     * @return boolean
     */
    public static boolean isLongArray(String str) {
        boolean returnValue = true;
        if (CommonUtil.isBlank(str)) {
            return returnValue;
        } else {
            // Replace all of the blank
//            str = str.replaceAll(DLAConstants.SYMBOL_SPACE, "");
        }

//        String[] strArray = str.split(DLAConstants.COMMA);
        String[] strArray = CommonUtil.splitBySeparator(str, ",");
        for (String sa : strArray) {
            if (!CommonUtil.isLong(sa)) {
                returnValue = false;
                break;
            }
        }

        return returnValue;
    }

    /**
     * Validate whether input str value is the BOOL pointer type
     *
     * @param str
     * @return boolean
     */
    public static boolean isBooleanArray(String str) {
        boolean returnValue = true;
        if (CommonUtil.isBlank(str)) {
            return returnValue;
        } else {
            // Replace all of the blank
//            str = str.replaceAll(DLAConstants.SYMBOL_SPACE, "");
        }

//        String[] strArray = str.split(DLAConstants.COMMA);
        String[] strArray = CommonUtil.splitBySeparator(str, ",");
        for (String sa : strArray) {
            if (!CommonUtil.isBoolean(sa)) {
                returnValue = false;
                break;
            }
        }

        return returnValue;
    }

    /**
     * Validate whether input str value is the Float pointer type
     *
     * @param str
     * @return boolean
     */
    public static boolean isFloatArray(String str) {
        boolean returnValue = true;
        if (CommonUtil.isBlank(str)) {
            return returnValue;
        } else {
            // Replace all of the blank
//            str = str.replaceAll(DLAConstants.SYMBOL_SPACE, "");
        }

//        String[] strArray = str.split(DLAConstants.COMMA);
        String[] strArray = CommonUtil.splitBySeparator(str, ",");
        for (String sa : strArray) {
            if (!CommonUtil.isFloat(sa)) {
                returnValue = false;
                break;
            }
        }

        return returnValue;
    }

    /**
     * Validate whether input value is the Date format type
     *
     * @param sDate
     * @return boolean
     */
    public static boolean isDateFormat(String sDate) {
        boolean returnValue = false;
        if (isBlank(sDate)) {
            return returnValue;
        }

        // Define regular expression
        String datePattern = "(^([1-2][0-9][0-9][0-9])(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$)";

        Pattern pattern = Pattern.compile(datePattern);
        Matcher match = pattern.matcher(sDate);

        if (match.matches()) {
            returnValue = true;
        } else {
            returnValue = false;
            LogTool.d("The format of the date is not correct: " + sDate + ", should be set as yyyyMMddHHmmss");
        }
        return returnValue;
    }

    /**
     * Validate whether input value is the Byte Array type
     *
     * @param str
     * @return boolean
     */
    public static boolean splitAndCheckByteArray(String str) {
        boolean returnValue = true;

        if (CommonUtil.isBlank(str)) {
            return returnValue;
        } else {
            // Replace all of the blank
            // Linux_BUG_128: fixed bug to allow input the blank value.
            // str = str.replaceAll(DLAConstants.SYMBOL_SPACE, "");
        }

//        String[] strByteArray = str.split(DLAConstants.COMMA);
        String[] strByteArray = CommonUtil.splitBySeparator(str, ",");

        for (String strByte : strByteArray) {
            if (!CommonUtil.isByte(strByte)) {
                returnValue = false;
                break;
            }
        }

        return returnValue;
    }

    /**
     * Validate whether input value is the Digit type
     *
     * @param str
     * @return boolean
     */
    public static boolean isDigit(String str) {
        boolean isDigit = true;
        if (isBlank(str)) {
            isDigit = false;
        } else {
            char charArray[] = str.toCharArray();
            for (int j = 0; j < charArray.length; j++) {
                if (!Character.isDigit(charArray[j])) {
                    isDigit = false;
                    break;
                }
            }
        }
        return isDigit;
    }

    /**
     * The format of protocol message should be {Len}{Type}{ID}{Parameter} and
     * the min length could not be less than 8, and return true if the message
     * is valid.
     *
     * @param message
     * @return boolean
     */
    public static boolean isValidProtocolMessage(String message) {
        boolean returnValue = false;

        try {
            if (CommonUtil.isNotBlank(message) && message.length() >= 8) {
                int totalLength = Integer.parseInt(message.substring(0, 4), 16);
                int actualTotalLength = message.substring(4).length();

                if ((totalLength * 2) == actualTotalLength) {
                    returnValue = true;
                } else {
                    LogTool.d("Invalid protocol message:" + message + ", the totalLength is: " + totalLength * 2
                            + ", but the actualTotalLength is: " + actualTotalLength);
                }
            }
        } catch (NumberFormatException e) {
            returnValue = false;
        }
        return returnValue;
    }

    /**
     * Convert to byte primitives
     *
     * @param oBytes
     * @return byte[]
     */
    public static byte[] toBytePrimitives(Byte[] oBytes) {
        byte[] bytes = new byte[oBytes.length];

        for (int i = 0; i < oBytes.length; i++) {
            bytes[i] = oBytes[i];
        }

        return bytes;
    }

    /**
     * Convert to Byte Objects
     *
     * @param bytesPrim
     * @return Byte[]
     */
    public static Byte[] toByteObjects(byte[] bytesPrim) {
        Byte[] bytes = new Byte[bytesPrim.length];

        int i = 0;
        for (byte b : bytesPrim) {
            bytes[i++] = b;
        }
        return bytes;
    }

    // ************************* Others *************************
    /**
     * Get IP address
     *
     * @return String IP address
     */
    public static String getIpAddress() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {

                } else {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        ip = addresses.nextElement();
                        if (ip != null && ip instanceof Inet4Address) {
                            return ip.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            LogTool.e("Can't get the IP address.");
        }
        return "";
    }

    /**
     * Format the str to print as Hex according to the lengthOfLine e.g. str =
     * "11223344001A7EDE03E1" and lengthOfLine = 5, => "11 22 33 44 00" => "1A
     * 7E DE 03 E1"
     *
     * @param str
     * @param lengthOfLine
     * @return String
     */
    public static String printHexString(String str, int lengthOfLine) {
        byte[] b = hexString2HexBytes(str);
        StringBuilder sb = new StringBuilder();

        int byteNumberOfEveryLine;
        int count = 0;
        if (lengthOfLine == 0) {
            byteNumberOfEveryLine = b.length;
        } else {
            byteNumberOfEveryLine = lengthOfLine;
        }

        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase()).append("  ");
            count++;

            if (count >= byteNumberOfEveryLine) {
                sb.append('\n');
                count = 0;
            }
        }
        return sb.toString().trim();
    }

    /**
     * Parse the decimalism int value to hex string by the size. e.g. intValue =
     * "12" and size = "4", then return value = 0x0000000C
     *
     * @param intValue
     * @param size
     * @return hex string value
     */
    public static String parseDecIntToHexStringBySize(String intValue, String size) {
        if (CommonUtil.isBlank(intValue)) {
            return null;
        }

        String paraValue = Integer.toHexString(Integer.parseUnsignedInt(intValue)).toUpperCase();

        String returnStr = paraValue;
        int byteSize = Integer.parseInt(size);
        if (byteSize != 0) {
            for (int i = 0; i < byteSize; i++) {
                paraValue = "00" + paraValue;
            }
            returnStr = paraValue.substring(paraValue.length() - 2 * byteSize);
        }

        return returnStr;
    }

    /**
     * Parse the decimal value to hex string by the size. e.g. longValue = "255"
     * and size = "4", then return value = 0x000000FF
     *
     * @param longValue
     * @param size
     * @return hex string value
     */
    public static String parseDecValueToHexStringBySize(String longValue, String size) {
        if (CommonUtil.isBlank(longValue)) {
            return null;
        }

        String paraValue = Long.toHexString(Long.parseUnsignedLong(longValue)).toUpperCase();

        String returnStr = paraValue;
        int byteSize = Integer.parseInt(size);
        if (byteSize != 0) {
            for (int i = 0; i < byteSize; i++) {
                paraValue = "00" + paraValue;
            }
            returnStr = paraValue.substring(paraValue.length() - 2 * byteSize);
        }

        return returnStr;
    }

    /**
     * Parse hex value to hex string by size. e.g. strValue = "DF01" and size =
     * "4", then return value = 0x0000DF01
     *
     * @param hexValue
     * @param size
     * @return String hex value
     */
    public static String parseHexValueToHexStringBySize(String hexValue, String size) {
        String returnStr = null;
        if (CommonUtil.isBlank(hexValue) || !isHexStr(hexValue)) {
            return returnStr;
        }

        returnStr = hexValue;
        int hexValueLen = hexValue.length();
        int byteSize = Integer.parseInt(size);
        if (byteSize != 0) { // if size = 0, should be array or pointer.
            for (int i = 0; i < byteSize; i++) {
                hexValue = "00" + hexValue;
            }
            returnStr = hexValue.substring(hexValue.length() - 2 * byteSize);
        } else if (hexValueLen == 1) {
            returnStr = "0" + hexValue;
        } else if ((hexValueLen & 1) != 0) { // Check the value is odd or even
            // the value is odd
            returnStr = hexValue.substring(0, hexValueLen - 1) + "0" + hexValue.substring(hexValueLen - 1);

        }

        return returnStr;
    }

    /**
     * Calculate the length of Hex message, and convert to hex String. e.g.
     * "00313233343536373839" = "000A"
     *
     * @param hexMessage
     * @return String
     */
    public static String getHexLengthByHexMessageInfo(String hexMessage) {
        String hexString = "0000";
        if (isBlank(hexMessage)) {
            return hexString;
        }

        String byteLength = Integer.toHexString(hexMessage.length() / 2);
        hexString = "0000" + byteLength.toUpperCase();
        return hexString.substring(hexString.length() - 4);
    }

    /**
     * Calculate the length of Hex TLV, and convert to hex String. e.g. "1A" =
     * "01"
     *
     * @param hexValue
     * @return String
     */
    public static String getHexLengthByHexTLV(String hexValue) {
        String hexLength = "00";
        if (isBlank(hexValue)) {
            return hexLength;
        }

        String byteLength = Integer.toHexString(hexValue.length() / 2);
        hexLength = "00" + byteLength.toUpperCase();
        return hexLength.substring(hexLength.length() - 2);
    }

    /**
     * Time stamp convert to format date,default format is yyyy-MM-dd
     * hh:mm:ss.SSS
     *
     * @param timeStamp time stamp
     * @param format format e.g.YYYY-MM-DD
     * @return
     */
    public static String timeStamp2Date(long timeStamp, String format) {
        if (timeStamp < 0) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(timeStamp));
    }

    /**
     *
     * @param sourceStr
     * @param separator
     * @return
     */
    public static String[] splitBySeparator(String sourceStr, String separator) {
        String[] returnStringArray = {""};
        if (isBlank(sourceStr)) {
        } else if (isBlank(separator)) {
            returnStringArray[0] = sourceStr;
        } else {
            String escapeReplaceCharacter = "@separator";
            String replacedSourceStr = sourceStr.replace("\\\\" + separator, escapeReplaceCharacter);
            String[] spStrArray = replacedSourceStr.split(separator);

            for (int i = 0; i < spStrArray.length; i++) {
                if (spStrArray[i].equals(escapeReplaceCharacter)) {
                    spStrArray[i] = separator;
                }
            }
            returnStringArray = spStrArray;
        }
        return returnStringArray;
    }

    /**
     * Get Resource By Path
     *
     * @param path
     * @return Properties
     */
    public static Properties getResourceByPath(String path) {
        Properties properties = null;
        if (CommonUtil.isBlank(path)) {
            return properties;
        }

        try {
            properties = new Properties();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            properties.load(bufferedReader);

        } catch (IOException ex) {
            // IO Exception
            LogTool.d("IO Exception when load the properties file.");
        }
        return properties;
    }

    public static void logError(String message, Exception e){
        LogTool.e(message);
        for(int i = 0;i < e.getStackTrace().length;i++){
            LogTool.e(e.getStackTrace()[i].toString());
        }
    }

    public static int hexString2byteLength(String hexString){
        return Integer.parseInt(hexString, 16) * 2;
    }

    /**
     * Get File By file path
     *
     * @param path
     * @return File
     */
    public static File getJsonFileByPath(String path) {
        return new File(path);
    }

    /**
     *
     * @return a timestamp
     */
    public static long getTimeStamp(String date){
        return getTimeStamp(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     *
     * @return a timestamp
     */
    public static long getTimeStamp(String dateString, String pattern){
        //set the format of date
        SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());
        // get current system time

        try {
            Date date = df.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     *
     * @return a Date
     */
    public static Date getDate(String date){
        return getDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     *
     * @return a Date
     */
    public static Date getDate(String dateString, String pattern){
        //set the format of date
        SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());
        // get current system time

        try {
            return df.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getRandom(int bound){
        Random seedRandom = new Random(System.currentTimeMillis());
        int seed =  seedRandom.nextInt(Integer.MAX_VALUE);

        Random random = new Random(seed);
        return random.nextInt(bound);
    }

    @NonNull
    public static <T> T checkNotNull(@Nullable T obj) {
        if (obj == null) {
            throw new NullPointerException();
        } else {
            return obj;
        }
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) {
            return true;
        } else {
            if (a != null && b != null) {
                int length = a.length();
                if (length == b.length()) {
                    if (a instanceof String && b instanceof String) {
                        return a.equals(b);
                    }

                    for(int i = 0; i < length; ++i) {
                        if (a.charAt(i) != b.charAt(i)) {
                            return false;
                        }
                    }

                    return true;
                }
            }

            return false;
        }
    }

}
