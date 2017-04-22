package com.guangyao.bluetoothtest.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.guangyao.bluetoothtest.R;

/**
 * Created by liuqiong on 2017/4/22.
 */

public class TestActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }
}
