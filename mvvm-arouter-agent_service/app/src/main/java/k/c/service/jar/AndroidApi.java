package k.c.service.jar;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.Calendar;
import java.util.TimeZone;

import k.c.service.ACTMSAPI;

public class AndroidApi extends BaseAidlApi {

    private ACTMSAPI actmsapi = null;
    private static final String ACAMSAPI_PACKAGE = "castles.ctms.service";
    private static final String ACAMSAPI_ACTION = "castles.android.CTMSAPI";

    public AndroidApi(Context context){
        Intent intent = new Intent();
        intent.setPackage(ACAMSAPI_PACKAGE).setAction(ACAMSAPI_ACTION);
        context.bindService(intent, serviceconnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceconnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            actmsapi = ACTMSAPI.Stub.asInterface(service);
        }

        public void onServiceDisconnected(ComponentName className) {
            actmsapi = null;

        }
    };

    @Override
    protected String getAllConfig() {

        String configuration = null;
        if (actmsapi != null) {
            try {
                configuration = actmsapi.getAllConfig();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return configuration;
    }

    @Override
    protected void setAllConfig(String config) {

        if (actmsapi != null) {
            try {
                actmsapi.setAllConfig(config);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected String getConfig(byte configType) {

        String configuration = null;
        if (actmsapi != null) {

            try {
                configuration = actmsapi.getConfig(configType);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return configuration;
    }

    @Override
    protected void setConfig(byte configType, String configContent) {

        if (actmsapi != null) {
            try {
                actmsapi.setConfig(configType, configContent);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void setBootConnectEnable(byte openType) {

        if (actmsapi != null) {
            try {
                actmsapi.setBootConnectEnable(openType);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected int getBootConnectStatus() {

        int bootConnectStatus = -1;
        if (actmsapi != null) {
            try {
                bootConnectStatus = actmsapi.getBootConnectStatus();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return bootConnectStatus;
    }

    @Override
    protected void setCTMSEnable(byte openType) {

        if (actmsapi != null) {
            try {
                actmsapi.setCTMSEnable(openType);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void setCM_Mode(byte openType) {

        if (actmsapi != null) {
            try {
                actmsapi.setCM_Mode(openType);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected String getUpdateList() {

        String updateList = "";
        if (actmsapi != null) {
            try {
                updateList = actmsapi.getUpdateList();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return updateList;
    }

    @Override
    protected int getCTMSStatus() {

        int CTMSStatus = -1;
        if (actmsapi != null) {

            try {
                CTMSStatus = actmsapi.getCTMSStatus();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return CTMSStatus;
    }

    @Override
    protected Calendar getTrigger() {

        Calendar triggercalendar = Calendar.getInstance(TimeZone.getDefault());
        if (actmsapi != null) {
            try {
                long unixTime = actmsapi.getTrigger();
                triggercalendar.setTimeInMillis(unixTime * 1000L);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return triggercalendar;
    }

    @Override
    protected Calendar getActiveTime() {

        Calendar activeTimeCalendar = Calendar.getInstance(TimeZone.getDefault());
        if (actmsapi != null) {
            try {
                long unixTime = actmsapi.getActiveTime();
                activeTimeCalendar.setTimeInMillis(unixTime * 1000L);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return activeTimeCalendar;
    }

    @Override
    protected Calendar getConnectTime() {

        Calendar connectTimeCalendar = Calendar.getInstance(TimeZone.getDefault());
        if (actmsapi != null) {
            try {
                long unixTime = actmsapi.getConnectTime();
                connectTimeCalendar.setTimeInMillis(unixTime * 1000L);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return connectTimeCalendar;
    }

    @Override
    protected Calendar getLeaveTime() {

        Calendar leaveTimeCalendar = Calendar.getInstance(TimeZone.getDefault());
        if (actmsapi != null) {
            try {
                long unixTime = actmsapi.getLeaveTime();
                leaveTimeCalendar.setTimeInMillis(unixTime * 1000L);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return leaveTimeCalendar;
    }

    @Override
    protected int ResetFolder() {

        int returnValue = 0;
        if (actmsapi != null) {
            try {
                returnValue = actmsapi.ResetFolder();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return returnValue;
    }

    @Override
    protected int UpdateActive() {

        int returnValue = 0;
        if (actmsapi != null) {
            try {
                returnValue = actmsapi.UpdateActive();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return returnValue;
    }

    @Override
    protected int UpdateImmediately() {

        int returnValue = 0;
        if (actmsapi != null) {
            try {
                returnValue = actmsapi.UpdateImmediately();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return returnValue;
    }

    @Override
    protected int resetSetting() {

        int returnValue = 0;
        if (actmsapi != null) {
            try {
                returnValue = actmsapi.resetSetting();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return returnValue;
    }

    @Override
    protected boolean isServiceAlive() {

        boolean isAlive = false;
        if (actmsapi != null) {
            isAlive = true;
        }
        return isAlive;
    }
}
