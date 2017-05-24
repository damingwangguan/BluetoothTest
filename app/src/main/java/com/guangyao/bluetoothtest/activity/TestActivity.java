package com.guangyao.bluetoothtest.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.guangyao.bluetoothtest.R;
import com.guangyao.bluetoothtest.command.CommandManager;

/**
 * Created by liuqiong on 2017/4/22.
 */

public class TestActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        CommandManager manager = CommandManager.getInstance(this);
        manager.FindBracelet();
    }
}
