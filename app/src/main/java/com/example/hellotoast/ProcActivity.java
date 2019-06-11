package com.example.hellotoast;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ProcActivity extends AppCompatActivity {

    private ActivityManager activityManager;
    private List<ActivityManager.RunningAppProcessInfo> runningAppProcesses;
    private TextView mProcText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proc);
        mProcText = (TextView) findViewById(R.id.proc_text);
        activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        runningAppProcesses = activityManager.getRunningAppProcesses();
        mProcText.setText(getRunningAppProcessesInfo());
    }

    public String getRunningAppProcessesInfo(){
        String info = "";
        for (int i = 0; i < runningAppProcesses.size(); i++){
            info += getRunningAppProcessInfo(i);
        }
        return info;
    }

    public String getRunningAppProcessInfo(int i){
        String info = "";
        ActivityManager.RunningAppProcessInfo processInfo = runningAppProcesses.get(i);

        info += "Process Name :" + processInfo.processName + "\n";
        info += "Importance :" + processInfo.importance + "\n";
        info += "Pid : " + processInfo.pid + "\n";
        //info += "Launch Activity :" + packageManager.getLaunchIntentForPackage(appInfo.packageName) + "\n";
        info += "Uid :" + processInfo.uid + "\n\n";
        //info += "IsSystem :" + ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) + "\n\n";

        return info;
    }

    public int[] getPidArray(){
        ArrayList<Integer> pidList = new ArrayList<>();
        for (int i = 0; i < runningAppProcesses.size(); i++){
            pidList.add(runningAppProcesses.get(i).pid);
        }
        Integer[] arr = new Integer[pidList.size()];
        arr =  pidList.toArray(arr);

        int[] info = new int[pidList.size()];
        for (int i = 0; i < pidList.size(); i++){
            info[i] = arr[i];
        }
        return info;
    }
}
