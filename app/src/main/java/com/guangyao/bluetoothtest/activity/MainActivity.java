package com.guangyao.bluetoothtest.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.guangyao.bluetoothtest.App;
import com.guangyao.bluetoothtest.service.BluetoothLeService;
import com.guangyao.bluetoothtest.R;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_SEARCH = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.hello);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TestActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("zgy", "onResume");
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());


    }

    private IntentFilter makeGattUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        if (!App.mConnected) {
            menu.findItem(R.id.disconnect_ble).setVisible(false);

        } else {
            menu.findItem(R.id.disconnect_ble).setVisible(true);

        }

        if (!App.isConnecting) {
            menu.findItem(R.id.menu_refresh).setActionView(
                    null);
        } else {
            menu.findItem(R.id.menu_refresh).setActionView(
                    R.layout.actionbar_indeterminate_progress);
        }


        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_ble:
                Intent intent = new Intent(MainActivity.this, DeviceScanActivity.class);
                startActivityForResult(intent, REQUEST_SEARCH);
                break;
            case R.id.disconnect_ble:

                App.mBluetoothLeService.disconnect();

                Log.i("zgy", "disconnect_ble");
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SEARCH && resultCode == RESULT_OK) {
            String address = data.getStringExtra("address");
            if (!TextUtils.isEmpty(address)) {
                Log.i("zgy", address);

                App.mBluetoothLeService.connect(address);
                App.isConnecting = true;
                invalidateOptionsMenu();
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    //接收蓝牙状态改变的广播
    private BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                App.mConnected = true;
                App.isConnecting = false;

                //todo 更改界面ui
                invalidateOptionsMenu();//更新菜单栏

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                App.mConnected = false;
                //todo 更改界面ui

                invalidateOptionsMenu();//更新菜单栏
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
//                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
//                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
                Log.i("zgy", "接收到的数据：" + intent.getStringExtra(BluetoothLeService.EXTRA_DATA));

            }
        }
    };


}
