package br.csi.vinicius.appinternetc;

import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;

public class ActionHelper {

    private Context context;
    private WifiManager wifiManager;
    private BluetoothAdapter bluetoothAdapter;
    private LocationManager locationManager;
    private ConnectivityManager dataManager;
    private TelephonyManager telephonyManager;
    private AudioManager aManager;

    public enum Actions {
        NONE, WIFI_ON, WIFI_OFF, BLUETOOTH_ON, BLUETOOTH_OFF, ENABLE_NETWORK, DISABLE_NETWORK, ENABLE_GPS, DISABLE_GPS, NAO_PERTURBE, ATIVAR_SONS, VIBRACAO,
    }

    public ActionHelper(Context context) {

        this.context = context;

        this.wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        dataManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        aManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

    }

    public void normal(){
        aManager.setRingerMode(aManager.RINGER_MODE_NORMAL);
    }
    public void silencioso(){
        aManager.setRingerMode(aManager.RINGER_MODE_SILENT);
    }
    public void vibracao(){
        aManager.setRingerMode(aManager.RINGER_MODE_VIBRATE);
    }

    public void enableWifi() {
            wifiManager.setWifiEnabled(true);
    }


    public void disableWifi() {

            wifiManager.setWifiEnabled(false);

    }

    public void enableGps() {

        try {

            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        } catch (Exception e) {
            Log.e("enableNetwork", e.getMessage(), e);
        }


    }

    public void disableGps() {

        try {

            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        } catch (Exception e) {
            Log.e("enableNetwork", e.getMessage(), e);
        }

    }

    public void enableNetwork() {

        try {

            Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        } catch (Exception e) {
            Log.e("enableNetwork", e.getMessage(), e);
        }


    }

    public void disableNetwork() {
        /*
        try {

            Method getMobileDataDisabledMethod = telephonyManager.getClass().getDeclaredMethod("getDataDisabled");
            getMobileDataDisabledMethod.setAccessible(true);
            getMobileDataDisabledMethod.invoke(telephonyManager, true);

        } catch (Exception e) {
            Log.e("disableNetwork", e.getMessage(), e);
        }
        */
        try {

            Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        } catch (Exception e) {
            Log.e("enableNetwork", e.getMessage(), e);
        }

    }

    public void enableBluetooth() {
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
    }

    public void disableBluetooth() {
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.disable();
        }
    }

    public void navigateToUrl(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }

    public void startApp(String packageName) {
        Intent intent = new Intent(packageName);

        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {

            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + packageName));
            context.startActivity(intent);

        }

    }

}
