package k.c.service.jar;

import android.binder.aidl.ICTMSAPI;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

import static k.c.service.constants.Constants.OPTION.OPTION_ENABLE;


public class KernelApi extends BaseAidlApi {

    private static final String TAG = "Ctms API";
    private ICTMSAPI ictmsapi = null;
    private static final String KERNEL_SERVICE_NAME = "android.binder.ctms";

    public KernelApi(){
        IBinder binder = null;
        Log.i(TAG, "getCtmsAPIService");
        try {
            //android.os.ServiceManager is hide class, we can not invoke them from SDK. So we have to use reflect to invoke these classes.
            Object object = new Object();
            Method getService = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
            Object obj = getService.invoke(object, new Object[]{new String(KERNEL_SERVICE_NAME)});
            binder = (IBinder) obj;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        if (binder != null) {
            ictmsapi = ICTMSAPI.Stub.asInterface(binder);
            Log.i(TAG, "Find Ctms binder");
        } else{
            ictmsapi = null;
            Log.e(TAG, "Service is null.");
        }
    }

    @Override
    protected String getAllConfig() {

        String configuration = null;
        if (ictmsapi != null) {
            int[] filesize = new int[1];
            byte[] baOutBuff;
            try {
                if (this.ictmsapi.getFileSize(filesize) == 0) {
                    baOutBuff = new byte[filesize[0]];
                    if (this.ictmsapi.getAllConfig(baOutBuff) == 0) {
                        configuration = new String(baOutBuff);
                    } else {
                        Log.d("ERR", "Read error!");
                        configuration = "No file";
                    }
                } else {
                    Log.d("ERR", "Read error!");
                    configuration = "Can't get file size!";
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        }
        return configuration;
    }

    @Override
    protected void setAllConfig(String config) {

        if (ictmsapi != null) {
            try {
                int size = config.length();
                byte[] WriteIn = config.getBytes();
                this.ictmsapi.setAllConfig(size, WriteIn);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    @Override
    protected String getConfig(byte configType) {

        String configuration = null;
        if (ictmsapi != null) {
            byte[] baOutBuff = new byte[1024];
            int[] ConfigSize = new int[1];
            byte[] baType = new byte[1];
            baType[0] = configType;

            try {

                if (this.ictmsapi.getConfig(baType, ConfigSize, baOutBuff) == 0) {
                    byte[] baConfig = Arrays.copyOf(baOutBuff, ConfigSize[0]);
                    configuration = new String(baConfig);



                } else {
                    configuration = "get config fail.";
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        return configuration;
    }

    @Override
    protected void setConfig(byte configType, String configContent) {

        if (ictmsapi != null) {
            try {
                byte[] baType = new byte[1];
                baType[0] = configType;
                this.ictmsapi.setConfig(baType, configContent);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void setBootConnectEnable(byte openType) {

        if (ictmsapi != null) {
            byte[] baType = new byte[1];
            baType[0] = openType;
            try {
                this.ictmsapi.setBootConnect(baType);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected int getBootConnectStatus() {

        int bootConnectStatus = -1;
        if (ictmsapi != null) {
            int[] dataswitch = new int[1];

            try {
                if (this.ictmsapi.getBootConnectStatus(dataswitch) == 0) {
                    bootConnectStatus = dataswitch[0];
                }

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return bootConnectStatus;
    }

    @Override
    protected void setCTMSEnable(byte openType) {

        if (ictmsapi != null) {
            byte[] baType = new byte[1];
            baType[0] = openType;
            try {
                this.ictmsapi.setCTMSEnable(baType);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void setCM_Mode(byte openType) {

        if (ictmsapi != null) {
            byte[] baType = new byte[1];
            baType[0] = openType;
            try {
                this.ictmsapi.setCMMode(baType);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected String getUpdateList() {

        String updatList = null;
        if (ictmsapi != null) {
            byte[] baOutBuff = new byte[4096];
            int[] dataSize = new int[1];
            try {
                if (this.ictmsapi.getUpdateList(dataSize, baOutBuff) == 0) {
                    if(dataSize[0] != 0){
                        byte[] baUpdateInfo = Arrays.copyOf(baOutBuff, dataSize[0]);
                        updatList = new String(baUpdateInfo);
                    }
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return updatList;
    }

    @Override
    protected int getCTMSStatus() {
        int CTMSStatus = -1;
        if (ictmsapi != null) {
            int[] dataswitch = new int[1];
            try {
                if (this.ictmsapi.getCTMSEnable(dataswitch) == 0) {
                    CTMSStatus = dataswitch[0];
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return CTMSStatus;
    }

    @Override
    protected Calendar getTrigger() {

        Calendar triggercalendar = Calendar.getInstance(TimeZone.getDefault());
        if (ictmsapi != null) {
            int[] datearray = new int[7];
            byte[] ba_unix_time = new byte[16];
            try {
                this.ictmsapi.getTrigger(ba_unix_time, datearray);
                byte[] ba_String_time = Arrays.copyOf(ba_unix_time, 10);
                String unix_time = new String(ba_String_time);
                Long lutime = Long.parseLong(unix_time);
                triggercalendar.setTimeInMillis((lutime*1000));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return triggercalendar;
    }

    @Override
    protected Calendar getActiveTime() {

        Calendar activeTimeCalendar = Calendar.getInstance(TimeZone.getDefault());
        if (ictmsapi != null) {
            int[] datearray = new int[7];
            byte[] ba_unix_time = new byte[16];
            try {
                this.ictmsapi.getActive(ba_unix_time, datearray);
                byte[] ba_String_time = Arrays.copyOf(ba_unix_time, 10);
                String unix_time = new String(ba_String_time);
                Long lutime = Long.parseLong(unix_time);
                activeTimeCalendar.setTimeInMillis((lutime*1000));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return activeTimeCalendar;
    }

    @Override
    protected Calendar getConnectTime() {

        Calendar connectTimeCalendar = Calendar.getInstance(TimeZone.getDefault());
        if (ictmsapi != null) {
            byte[] ba_unix_time = new byte[16];
            try {
                this.ictmsapi.getConnectTime(ba_unix_time);
                byte[] ba_String_time = Arrays.copyOf(ba_unix_time, 10);
                String unix_time = new String(ba_String_time);
                Long lutime = Long.parseLong(unix_time);
                connectTimeCalendar.setTimeInMillis((lutime*1000));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return connectTimeCalendar;
    }

    @Override
    protected Calendar getLeaveTime() {

        Calendar leaveTimeCalendar = Calendar.getInstance(TimeZone.getDefault());
        if (ictmsapi != null) {
            byte[] ba_unix_time = new byte[16];
            try {
                this.ictmsapi.getLeaveTime(ba_unix_time);
                byte[] ba_String_time = Arrays.copyOf(ba_unix_time, 10);
                String unix_time = new String(ba_String_time);
                Long lutime = Long.parseLong(unix_time);
                leaveTimeCalendar.setTimeInMillis((lutime*1000));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return leaveTimeCalendar;
    }

    @Override
    protected int ResetFolder() {

        int returnValue = 0;
        if (ictmsapi != null) {
            byte[] baType = new byte[1];
            baType[0] = OPTION_ENABLE;
            try {
                this.ictmsapi.setClear(baType);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        return returnValue;
    }

    @Override
    protected int UpdateActive() {

        int returnValue = 0;
        if (ictmsapi != null) {
            byte[] baType = new byte[1];
            baType[0] = OPTION_ENABLE;
            try {
                this.ictmsapi.setActiveImmediately(baType);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return returnValue;
    }

    @Override
    protected int UpdateImmediately() {

        int returnValue = 0;
        if (ictmsapi != null) {
            byte[] baType = new byte[1];
            baType[0] = OPTION_ENABLE;
            try {
                this.ictmsapi.setTriggerImmediately(baType);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return returnValue;
    }

    @Override
    protected int resetSetting() {
        int returnValue = 0;
        return returnValue;
    }

    @Override
    protected boolean isServiceAlive() {
        boolean isAlive = false;
        if (ictmsapi != null) {
            isAlive = true;
        }
        return isAlive;
    }
}
