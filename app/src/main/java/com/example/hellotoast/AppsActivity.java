package com.example.hellotoast;

import android.app.usage.StorageStatsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AppsActivity extends AppCompatActivity {

    private PackageManager packageManager;
    private List<ApplicationInfo> installedApplications;
    private TextView mAppText;
    UsageStatsManager mUsageStatsManager;
    StorageStatsManager storageStatsManager;
    StorageManager storageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps);
        mAppText = (TextView) findViewById(R.id.app_text);

        packageManager = getPackageManager();

        /*  Installed Applications. Não usado*/
        installedApplications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        mAppText.setText(getInstalledApplicationsInfo());

        /*  Usage Stats */
        mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        getUsageStatistics(UsageStatsManager.INTERVAL_BEST);

        /*  Storage Stats. Não usado   */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            storageStatsManager = (StorageStatsManager) getSystemService(Context.STORAGE_STATS_SERVICE);
        }
        storageManager = (StorageManager) getSystemService(Context.STORAGE_SERVICE);

        mAppText.setText(getUsageStatsInfo(getUsageStatistics(UsageStatsManager.INTERVAL_BEST)));

    }

    /*  Método que fornece lista dos aplicativos instalados no dispositivo. Não utilizado no momento    */
    public String getInstalledApplicationsInfo(){
        String info = "";
        for (int i = 0; i < installedApplications.size(); i++){
            if ((installedApplications.get(i).flags & ApplicationInfo.FLAG_SYSTEM) == 0)
                info += getInstalledApplicationInfo(i);
        }

        return info;
    }

    /*  Método que retorna informação de aplicativo instalado no dispositivo. Não utilizado no momento    */
    public String getInstalledApplicationInfo(int i){
        String info = "";
        ApplicationInfo appInfo = installedApplications.get(i);

        info += "Label :" + appInfo.loadLabel(packageManager) + "\n";
        info += "Installed package :" + appInfo.packageName + "\n";
        info += "Source dir : " + appInfo.sourceDir + "\n";
        //info += "Launch Activity :" + packageManager.getLaunchIntentForPackage(appInfo.packageName) + "\n";
        info += "Uid :" + appInfo.uid + "\n";
        info += "Process Name :" + appInfo.processName + "\n\n";
        //info += "IsSystem :" + ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) + "\n\n";

        return info;
    }

    public List<UsageStats> getUsageStatistics(int intervalType) {
        // Get the app statistics since one year ago from the current time.
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);

        List<UsageStats> queryUsageStats = mUsageStatsManager.queryUsageStats(intervalType, cal.getTimeInMillis(), System.currentTimeMillis());

        if (queryUsageStats.size() == 0) {
            //Log.i(TAG, "The user may not allow the access to apps usage. ");
            Toast.makeText(this,"Requer permissão de uso.",Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
        return queryUsageStats;
    }

    public String getUsageStatsInfo(List<UsageStats> queryUsageStats){
        String info = "";
        boolean system;
        PackageInfo packageInfo = null;
        HashMap<String, UsageStats> UsageStatsMap = new HashMap<>();

        /*  Eliminar duplicados por meio de HashMap*/
        for (UsageStats usageStats : queryUsageStats) {
            //system = false;
            try {
                packageInfo = packageManager.getPackageInfo(usageStats.getPackageName(),0);
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0){
                    //system = true;
                    CharSequence label = packageInfo.applicationInfo.loadLabel(packageManager);
                    if(UsageStatsMap.containsKey(label)){
                        if(UsageStatsMap.get(label).getLastTimeUsed() < usageStats.getLastTimeUsed()){
                            UsageStatsMap.remove(label);
                            UsageStatsMap.put(label.toString(),usageStats);
                        }
                    }
                    else UsageStatsMap.put(label.toString(),usageStats);
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        /*  Retornar para ArrayList */
        Collection<UsageStats> values = UsageStatsMap.values();
            //Creating an ArrayList of values
        ArrayList<UsageStats> UsageStatsList = new ArrayList<>(values);
        /*  Ordenar o ArrayList */
        Comparator<UsageStats> compareByTime = new Comparator<UsageStats>() {
            @Override
            public int compare(UsageStats o1, UsageStats o2) {
                return (int)(o1.getLastTimeUsed() - o2.getLastTimeUsed());
            }
        };

        Collections.sort(UsageStatsList, compareByTime.reversed());

        for(UsageStats usageStats:UsageStatsList)
            try {
                packageInfo = packageManager.getPackageInfo(usageStats.getPackageName(),0);
                CharSequence label = packageInfo.applicationInfo.loadLabel(packageManager);
                info += label + "\n";
                //info += "Package Name :" + usageStats.getPackageName() + "\n";
                info += "Last time used :" + (usageStats.getLastTimeUsed() > 0 ? DateFormat.getDateInstance().format(new Date(usageStats.getLastTimeUsed())): "") + "\n\n";
            }catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        return info;
    }

    /*  Código a ser finalizado para obter dados de armazenamento dos aplicativos */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<StorageVolume> getStorageVolumes() {
        if (storageManager == null || storageStatsManager == null) {
            return new ArrayList<StorageVolume>();
        }
        List<StorageVolume> storageVolumes = storageManager.getStorageVolumes();
        for (StorageVolume storageVolume : storageVolumes) {
            final String uuidStr = storageVolume.getUuid();
            final UUID uuid = uuidStr == null ? StorageManager.UUID_DEFAULT : UUID.fromString(uuidStr);
        }
        return storageVolumes;
    }
}
