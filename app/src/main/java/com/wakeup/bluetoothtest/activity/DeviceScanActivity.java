package com.wakeup.bluetoothtest.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.wakeup.bluetoothtest.R;
import com.wakeup.bluetoothtest.adapter.LeDeviceListAdapter;

import java.util.ArrayList;


/**
 * Created by liuqiong on 2017/4/21.
 */

public class DeviceScanActivity extends AppCompatActivity {
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter bluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    private Context context;
    private ListView listView;
    private ArrayList<BluetoothDevice> bluetoothDevices;
    private Handler mHandler;
    private boolean mScanning;
    private static final long SCAN_PERIOD = 10000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        context=this;
        mHandler = new Handler();

        getSupportActionBar().setTitle("设备列表");
        listView = (ListView) findViewById(R.id.listview);

        BluetoothManager bluetoothManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter ==null) {
            Toast.makeText(this,"不支持蓝牙",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BluetoothDevice device = mLeDeviceListAdapter.getDevice(i);
                if (device==null) {
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("address",device.getAddress());
                setResult(RESULT_OK,intent);
                if (mScanning) {
                    bluetoothAdapter.stopLeScan(mLeScanCallback);
                    mScanning = false;
                }

                finish();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!bluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(bluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent,REQUEST_ENABLE_BT);
        }

        bluetoothDevices = new ArrayList<>();
        mLeDeviceListAdapter = new LeDeviceListAdapter(context, bluetoothDevices);
        listView.setAdapter(mLeDeviceListAdapter);




        scanLeDevice(true);


    }
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    bluetoothAdapter.stopLeScan(mLeScanCallback);
                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            bluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            bluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }


    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
        mLeDeviceListAdapter.clear();
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback=new BluetoothAdapter.LeScanCallback(){

        @Override
        public void onLeScan(final BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLeDeviceListAdapter.addDevice(bluetoothDevice);
                    mLeDeviceListAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.device_list,menu);

        if (!mScanning) {
            menu.findItem(R.id.stop).setVisible(false);
            menu.findItem(R.id.re_search).setVisible(true);
            menu.findItem(R.id.menu_refresh).setActionView(null);
        } else {
            menu.findItem(R.id.stop).setVisible(true);
            menu.findItem(R.id.re_search).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(
                    R.layout.actionbar_indeterminate_progress);
        }

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.re_search:
                if (!bluetoothAdapter.isEnabled()) {
                    Intent intent = new Intent(bluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent,REQUEST_ENABLE_BT);
                }

                mLeDeviceListAdapter.clear();
                scanLeDevice(true);
                break;

            case R.id.sort:
                break;
            case R.id.stop:
                scanLeDevice(false);

                break;
        }

        return super.onOptionsItemSelected(item);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_ENABLE_BT&&resultCode== Activity.RESULT_CANCELED){
            finish();
            return;
        }
    }
}
