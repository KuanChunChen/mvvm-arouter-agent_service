package k.c.module.diagnostics.module.system.location;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import k.c.common.lib.CommonLib;
import k.c.common.lib.logTool.LogTool;

import static android.content.Context.LOCATION_SERVICE;

public class GpsManager implements LocationListener {

    private LocationManager locationManager;
    private Location location;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 0 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 1 * 1; // 1 second

    // Flag for GPS status
    boolean isGPSEnabled = false;

    // Flag for network status
    boolean isNetworkEnabled = false;

    int locationMode;

    public GpsManager() {
        init();
    }

    public void init(){
        locationManager = (LocationManager) CommonLib.getAppContext().getSystemService(LOCATION_SERVICE);
        if(locationManager == null){
            LogTool.d("Can not get locationManager");
        }
        getLocationUpdates();
    }

    public int checkLocationMode(){
        if (isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            return 1;
        }else if(isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            return 2;
        }
        return 0;
    }

    public void getLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(CommonLib.getAppContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(CommonLib.getAppContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            LogTool.d("permission is null");
            return;
        }
        switch (locationMode = checkLocationMode()){
            case 1:
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                break;
            case 2:
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                break;
            default:
                break;
        }
    }

    public Location getLastLocation() {
        if (ActivityCompat.checkSelfPermission(CommonLib.getAppContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(CommonLib.getAppContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            LogTool.d("permission is null");
        }
        switch (locationMode){
            case 1:
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                break;
            case 2:
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            default:
                break;
        }
        return location;
    }

    @Override
    public void onLocationChanged(Location location) {
        LogTool.d("onLocationChanged, location = %s : %s", location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle bundle) {
        LogTool.d("onStatusChanged provider=%s,status=%d", provider, status);
    }

    @Override
    public void onProviderEnabled(String s) {
        getLocationUpdates();
        LogTool.d("onProviderEnabled = %s", s);
    }

    @Override
    public void onProviderDisabled(String s) {
        getLocationUpdates();
        LogTool.d("onProviderDisabled = %s", s);
    }
}
