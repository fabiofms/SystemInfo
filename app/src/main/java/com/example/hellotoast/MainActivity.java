package com.example.hellotoast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*  Métodos que chamam as outras atividades */

    public void launchAppsActivity(View view) {
        Intent intent = new Intent(this, AppsActivity.class);
        startActivity(intent);
    }

    public void launchCpuActivity(View view) {
        Intent intent = new Intent(this, CpuActivity.class);
        startActivity(intent);
    }

    public void launchMemActivity(View view) {
        Intent intent = new Intent(this, MemActivity.class);
        startActivity(intent);
    }

    public void launchSysActivity(View view) {
        Intent intent = new Intent(this, SysActivity.class);
        startActivity(intent);
    }
}
