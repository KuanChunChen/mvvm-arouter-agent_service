package k.c.common.lib.proxy;

import k.c.common.lib.CommonLib;
import k.c.common.lib.Util.CtmsProxyUtil;
import k.c.common.lib.logTool.LogTool;

public class ProxyManager {

    public static ProxyData getProxyInfo() {


        ProxyData proxyData = new ProxyData();
        String spiltArray[];
        String proxyInfo = null;
        CtmsProxyUtil ctmsProxyUtil = new CtmsProxyUtil();
        int proxyFlag = 0;

        boolean isETHReachable = ctmsProxyUtil.isETHReachable(CommonLib.getAppContext());
        boolean isPPPDReachable = ctmsProxyUtil.isPPPDReachable();
        boolean isWifiReachable = ctmsProxyUtil.isWifiReachable(CommonLib.getAppContext());

        LogTool.d("Proxy status :" + "\r\n"
                + "isETHReachable :" + isETHReachable + "\r\n"
                + "isPPPDReachable :" + isPPPDReachable + "\r\n"
                + "isWifiReachable :" + isWifiReachable + "\r\n");


        if (isETHReachable || isPPPDReachable || isWifiReachable) {
            if (isETHReachable && proxyFlag == 0) {
                proxyInfo = ctmsProxyUtil.getEthProxy();
                proxyFlag = 1;
            }

            if (isPPPDReachable && proxyFlag == 0) {
                proxyInfo = ctmsProxyUtil.getPPPDProxy(CommonLib.getAppContext());
                proxyFlag = 1;
            }

            if (isWifiReachable && proxyFlag == 0) {
                proxyInfo = ctmsProxyUtil.getWifiProxy(CommonLib.getAppContext());
            }
        } else {
            LogTool.d("Proxy reachable not open.");
            return null;
        }

        if (proxyInfo == null || proxyInfo.equals("")) {
            LogTool.d("Get proxy info fail.");
            return null;
        }


        if (proxyInfo.contains(":")){
            spiltArray = proxyInfo.split(":");
            if (spiltArray.length == 2) {
                proxyData.proxyIp = spiltArray[0];
                proxyData.proxyPort = Integer.parseInt(spiltArray[1]);
            }
        }

        return proxyData;
    }
}
