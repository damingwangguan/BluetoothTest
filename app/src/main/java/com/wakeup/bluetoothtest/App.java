package com.wakeup.bluetoothtest;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.wakeup.bluetoothtest.service.BluetoothLeService;

/**
 * Created by liuqiong on 2017/4/22.
 */

public class App extends Application {

    public static BluetoothLeService mBluetoothLeService;
    public static boolean mConnected = false;
    public static boolean isConnecting = false;


    @Override
    public void onCreate() {
        super.onCreate();
        bindBleService();

    }
    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e("zgy", "Unable to initialize Bluetooth");

            }
            // Automatically connects to the device upon successful start-up initialization.
//            mBluetoothLeService.connect(mDeviceAddress);
            Log.d("zgy", "onServiceConnected");

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };
    private void bindBleService() {
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }







}
