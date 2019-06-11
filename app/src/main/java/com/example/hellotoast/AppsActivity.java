package com.example.hellotoast;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.List;

public class AppsActivity extends AppCompatActivity {

    private PackageManager packageManager;
    private List<ApplicationInfo> installedApplications;
    private TextView mAppText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps);
        mAppText = (TextView) findViewById(R.id.app_text);

        packageManager = getPackageManager();

        installedApplications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        mAppText.setText(getInstalledApplicationsInfo());
    }

    public String getInstalledApplicationsInfo(){
        String info = "";
        for (int i = 0; i < installedApplications.size(); i++){
            if ((installedApplications.get(i).flags & ApplicationInfo.FLAG_SYSTEM) == 0)
                info += getInstalledApplicationInfo(i);
        }
        return info;
    }

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


}
