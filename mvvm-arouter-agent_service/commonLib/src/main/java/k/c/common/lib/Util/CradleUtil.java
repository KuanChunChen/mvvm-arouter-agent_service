package k.c.common.lib.Util;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import javax.net.SocketFactory;

public class CradleUtil {
    private final String TAG = "MachineUtil";

    private Socket socket = null;
    public final String pppdServer = "192.168.99.9";
    public final String WiFiServer = "192.168.88.8";
    private Context mContext;
    private WifiManager mWifiManager;

    public CradleUtil(Context mcontext) {
        this.mContext = mcontext;
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * 跟Cradle要Cradle的版本號碼
     * BIOS:VR0028
     * SULD:VRF028
     * KERNEL:VR0032
     * ROOTFS:VRC419
     * MainAP:VR0013
     * ConfigAP:VR0014
     * SerialAP:VR0016
     * UldAP:VR0015
     * @return String: 回傳範例如上
     */
    public String getCradleVersion() {
        try {
            SocketWrite("getversion");
            String buff = SocketRead();
            Log.d(TAG, "Version:" + buff);
            return buff;
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } catch (InterruptedException e) {
            Log.e(TAG, e.toString());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }
    /**
     * 建立Socket
     * @param ip: 連線的IP
     * @param port: 連線的PORT
     * @param Timeout:連線逾時時間, ms
     */
    private void Socket(String ip,int port,int Timeout) throws IOException {

        socket = SocketFactory.getDefault().createSocket();
        SocketAddress remoteaddr = new InetSocketAddress(ip,port);
        socket.connect(remoteaddr, Timeout);

    }
    /**
     * Socket傳送資料
     * @param text: 傳送String格式資料
     */
    private void SocketWrite(String text) throws Exception {
        if (isPPP0()==true){
            Socket(pppdServer,6000,5000);
        }else{
            Socket(WiFiServer,6000,5000);
        }
        OutputStream out = new DataOutputStream(socket.getOutputStream());
        byte[] tmp = text.getBytes();
        Byte bytes = (byte)0x0d;
        byte[] buff = new byte[tmp.length+1];
        for (int i=0;i<tmp.length;i++){
            buff[i]=tmp[i];
        }
        buff[tmp.length]=bytes;
        out.write(buff,0,buff.length);
        out.flush();
    }

    /**
     * Socket讀取資料
     * @return String: 回傳byte ASCII轉String的值
     */
    private String SocketRead() throws InterruptedException, IOException {
        InputStream in = new DataInputStream(socket.getInputStream());
        int available = in.available();
        long start = System.currentTimeMillis(),end;
        while (available>2){
            end = System.currentTimeMillis();
            if ((end-start)>=10000){
                break;
            }
            Thread.sleep(500);
            available = in.available();
        }
        Thread.sleep(1000);
        available = in.available();
        byte[] buff = new byte[available];
        in.read(buff,0,available);
        SockClose();
        return ByteToStringASCII(buff);
    }

    /**
     * 判斷是否為ppp0連線
     * @return boolean: 回傳結果
     */
    public boolean isPPP0() {
        String PPP0_Info = command("ifconfig", "ppp0");
        Log.e(TAG, PPP0_Info);
        int index = PPP0_Info.indexOf("addr:");
        if (index > 0) {
            return true;
        }
        return false;
    }

    /**
     * Socket 關閉
     */
    private void SockClose() throws IOException {
        if (socket!=null){
            socket.close();
        }
    }

    private String ByteToStringASCII(byte[] tmp){
        String aChar="";
        for (int i=0;i<tmp.length;i++){
            if (tmp[i]==(byte)0x0A){
                aChar+="\r\n";
            }else{
                aChar+= new Character((char)tmp[i]).toString();
            }
        }
        return aChar;
    }

    public static String command(String command, String command1) {
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
