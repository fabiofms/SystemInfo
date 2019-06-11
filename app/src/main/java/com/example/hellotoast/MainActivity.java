package com.example.hellotoast;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.health.HealthStats;
import android.os.health.SystemHealthManager;
import android.os.health.UidHealthStats;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import static java.lang.Runtime.getRuntime;

public class MainActivity extends AppCompatActivity {

    private int mCount = 0;
    private SystemHealthManager systemHealthManager;
    private HealthStats myUidStats;
    private UsageStatsManager mUsageStatsManager;
    private ActivityManager activityManager;
    private ProcessBuilder processBuilder;
    private String Holder = "";
    private String[] DATA = {"/system/bin/cat", "/proc/cpuinfo"};
    private InputStream inputStream;
    private Process process ;
    private byte[] byteArry ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        systemHealthManager = (SystemHealthManager) getSystemService(Context.SYSTEM_HEALTH_SERVICE);
        mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        byteArry = new byte[2048];
        processBuilder = new ProcessBuilder(DATA);
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

    public void showToast(View view) {
        Toast toast = Toast.makeText(this,R.string.toast_message, Toast.LENGTH_SHORT);
        toast.show();
        try {
            // process le o arquivo cpuinfo
            process = processBuilder.start();
            inputStream = process.getInputStream();
            while(inputStream.read(byteArry) != -1){
                Holder = Holder + new String(byteArry);
            }
            inputStream.close();
            //mShowCPU.setText(Holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void countUp(View view) {
        myUidStats = systemHealthManager.takeMyUidSnapshot();
        String debug = "MainActivity";
        Log.d(debug,Long.toString(myUidStats.getMeasurement(UidHealthStats.MEASUREMENT_REALTIME_BATTERY_MS)));
        printProcesses();
        //printProcessesName();
        printUsageStatistics(UsageStatsManager.INTERVAL_DAILY);
        printApplications();
        //JSONObject cpuInfo = getCpuInfo();
        mCount++;
        //long valor = systemHealthManager.takeMyUidSnapshot().getMeasurement(UidHealthStats.MEASUREMENT_USER_CPU_TIME_MS) - myUidStats.getMeasurement(UidHealthStats.MEASUREMENT_USER_CPU_TIME_MS);
        //String msg = String.format("%d",valor);
    }

    public String printProcesses(){
        ProcessBuilder pb =new ProcessBuilder();
        Runtime runtime = getRuntime();
        Process process = null;
        try{
            process = runtime.exec("top -n 1");//Shell.SH.run("ps");
        }
        catch(IOException ex){
            System.out.println (ex.toString());
        }
        InputStream in = process.getInputStream();
        String msg = null;
        Charset charset = StandardCharsets.UTF_8;
        try (Scanner scanner = new Scanner(in, charset.name())) {
            msg = scanner.useDelimiter("\\A").next();
        }
        return msg;
    }

    public void printProcessesName() {

        int pid = android.os.Process.myPid();

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {

            Log.d("ActivityManager",appProcess.processName + "\n");
            if (appProcess.pid == pid) {
                //return appProcess.processName;
            }
        }
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        Log.d("Avaliable Memory", Long.toString(memoryInfo.availMem*100/memoryInfo.totalMem) + "% \n");
        //Log.d("Total Memory", Long.toString(memoryInfo.totalMem) + "\n");
    }

    public void printUsageStatistics(int intervalType) {

        // Get the app statistics since one year ago from the current time.

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        List<UsageStats> queryUsageStats = mUsageStatsManager.queryUsageStats(intervalType, cal.getTimeInMillis(),System.currentTimeMillis());

        if (queryUsageStats.size() == 0) {

            Log.i("UsageStats", "The user may not allow the access to apps usage. ");

            for(UsageStats usageStats: queryUsageStats){
                Log.d("UsageStats",usageStats.getPackageName());
            }

        }

    }

    public void printApplications(){
        final PackageManager pm = getPackageManager();
//get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            Log.d("Applications", "Installed package :" + packageInfo.packageName);
            Log.d("Applications", "Source dir : " + packageInfo.sourceDir);
            Log.d("Applications", "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
            Log.d("Applications", "Uid :" + packageInfo.uid);
            Log.d("Applications", "Process Name :" + packageInfo.processName);
            Log.d("Applications", "Label :" + packageInfo.loadLabel(pm) + "\n");
            Log.d("Applications", "IsSystem :" + ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) + "\n");
        }
    }



}
