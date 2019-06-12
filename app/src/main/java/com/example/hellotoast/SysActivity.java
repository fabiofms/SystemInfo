package com.example.hellotoast;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SysActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys);

        String arch = System.getProperty("os.arch");
        TextView tv = (TextView) findViewById(R.id.sys_text);


        tv.setText("\t\t***** DEVICE Information *****" + "\n");
        tv.append("\t\tModel: " + Build.MODEL + "\n");
        tv.append("\t\tBoard: " + Build.BOARD + "\n");
        tv.append("\t\tBrand: " + Build.BRAND + "\n");
        tv.append("\t\tManufacturer: " + Build.MANUFACTURER + "\n");
        tv.append("\t\tDevice: " + Build.DEVICE + "\n");
        tv.append("\t\tProduct: " + Build.PRODUCT + "\n");
        tv.append("\t\tSerial: " + Build.SERIAL + "\n");
        tv.append("\t\tHardware: " + Build.HARDWARE + "\n");
        tv.append("\t\tNumber of cores: " + getNumberOfCores() + "\n");
        tv.append("\t\tArchitecture: " + arch +  "\n");
        tv.append("\t\tBuild release: " + Build.VERSION.RELEASE + "\n");
        tv.append("\t\tIncremental release: " + Build.VERSION.INCREMENTAL + "\n");
        tv.append("\t\tBase OS: " + Build.VERSION.BASE_OS + "\n");
        tv.append("\t\tSDK/API version: " + Build.VERSION.SDK_INT + "\n");
        tv.append("\t\tKernel version: " + System.getProperty("os.version") + "\n");

    }

    public int getNumberOfCores() {
        if(Build.VERSION.SDK_INT >= 17) {
            return Runtime.getRuntime().availableProcessors();
        }else {
            return 0;
        }
    }


}