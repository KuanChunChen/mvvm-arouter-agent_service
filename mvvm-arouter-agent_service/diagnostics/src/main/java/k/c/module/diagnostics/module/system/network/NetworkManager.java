package k.c.module.diagnostics.module.system.network;

import android.provider.Settings;
import android.util.Log;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import k.c.common.lib.CommonLib;

public class NetworkManager {

    public static final int TYPE_IPV4 = 1;
    public static final int TYPE_IPV6 = 2;

    public static String getLocalIpAddress(int type) {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();

                    if (type == TYPE_IPV4) {
                        if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                            return inetAddress.getHostAddress();
                        }
                    } else if (type == TYPE_IPV6) {

                        if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet6Address) {
                            String ipaddress = inetAddress.getHostAddress();
                            return ipaddress;
                        }
                    } else {
                        return "Unavailable";
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("IP Address", ex.toString());
        }
        return null;
    }




    public static String getMacAddress() {
        String strMacAddr = null;
        try {
            // 獲得 IP 位址

            InetAddress ip = getLocalInetAddress();
            byte[] b = NetworkInterface.getByInetAddress(ip).getHardwareAddress();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
                    buffer.append(':');
                }
                String str = Integer.toHexString(b[i] & 0xFF);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            strMacAddr = buffer.toString().toUpperCase();
        } catch (Exception e) {
            Log.d("", "取得 MAC 失敗 == " + e.toString());
            strMacAddr = "Unavailable";
        }
        return strMacAddr;

    }

    private static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            // 列舉
            Enumeration<NetworkInterface> en_netInterface = NetworkInterface.getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {
                // 是否還有元素
                NetworkInterface ni = en_netInterface.nextElement();
                // 得到下一個元素
                Enumeration<InetAddress> en_ip = ni.getInetAddresses();
                // 得到一個 IP 位址的列舉
                while (en_ip.hasMoreElements()) {
                    ip = en_ip.nextElement();
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1)
                        break;
                    else
                        ip = null;
                }
                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ip;
    }

    public static String getBluetoothAddress(){
        final String SECURE_SETTINGS_BLUETOOTH_ADDRESS = "bluetooth_address";
        String macAddress = Settings.Secure.getString(CommonLib.getAppContext().getContentResolver(), SECURE_SETTINGS_BLUETOOTH_ADDRESS);
        return macAddress;
    }

}
