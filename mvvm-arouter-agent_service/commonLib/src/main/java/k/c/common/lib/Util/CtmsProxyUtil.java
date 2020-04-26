package k.c.common.lib.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.EthernetManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.ProxyInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.SystemProperties;
import android.provider.Settings;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Method;
import java.util.List;

public class CtmsProxyUtil {

    private final String TAG = "CtProxy 0.0.3";

    private boolean isNetWorkAvailable(String ipStr) {
        Runtime runtime = Runtime.getRuntime();
        try {
            String ping = "/system/bin/ping -c 1 " + ipStr;
            Process pingProcess = runtime.exec(ping);
            int exitCode = pingProcess.waitFor();
            return (exitCode == 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isETHReachable(Context context) {
        String ETHERNET_SERVICE = "ethernet";
        EthernetManager mEthernetManager = (EthernetManager) context.getSystemService(ETHERNET_SERVICE);
        if (!mEthernetManager.isAvailable()) {
            return false;
        }

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }

        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo == null) {
            return false;
        }

        Network net[] = manager.getAllNetworks();
        for (int i = 0; i < net.length; i++) {
            if (manager.getNetworkInfo(net[i]).getType() == ConnectivityManager.TYPE_ETHERNET) {
                if (!manager.getNetworkInfo(net[i]).isConnected() || !manager.getNetworkInfo(net[i]).isAvailable()) {
                    return false;
                }
                return true;
            }
        }

        return false;
    }

    public boolean isWifiReachable(Context ctx) {
        ConnectivityManager connManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifi.isConnected();
    }

    public boolean isPPPDReachable() {
        String PPPD_ADDR = "192.168.99.9";
        if (!SystemProperties.getBoolean("sys.ppp.usb.status", false)) return false;
        if (!isNetWorkAvailable(PPPD_ADDR)) return false;
        return true;
    }

    public String getPPPDProxy(Context context) {
        return Settings.Global.getString(context.getContentResolver(), Settings.Global.HTTP_PROXY);
    }

    public String  getEthProxy() {
        return SystemProperties.get("persist.sys.eth_proxy", null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public String getWifiProxy(Context context) {
        try {
            WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            List<WifiConfiguration> list = mWifiManager.getConfiguredNetworks();
            if (list.size() == 0) return "";
            WifiConfiguration wifiConfiguration = list.get(0);
            ProxyInfo proxyInfo = getWifiProxyInfo(wifiConfiguration);
            return proxyInfo.getHost() + ":" + proxyInfo.getPort();
        } catch (Exception e) {
            return "";
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private ProxyInfo getWifiProxyInfo(WifiConfiguration config) throws Exception {
        ProxyInfo proxyInfo = null;

        if (config != null) {
            Class wifiConfigClass = Class.forName("android.net.wifi.WifiConfiguration");
            Method getHttpProxy = wifiConfigClass.getDeclaredMethod("getHttpProxy");
            getHttpProxy.setAccessible(true);
            proxyInfo = (ProxyInfo) getHttpProxy.invoke(config);
        } else {
            throw new Exception("err WifiConfiguration = null");
        }

        return proxyInfo;
    }
}
