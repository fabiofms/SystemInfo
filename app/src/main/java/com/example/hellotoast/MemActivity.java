package com.example.hellotoast;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MemActivity extends AppCompatActivity {

    private ActivityManager activityManager;
    private ActivityManager.MemoryInfo memoryInfo;
    private TextView mMemText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mem);
        mMemText = (TextView) findViewById(R.id.mem_text);

        activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        memoryInfo = new ActivityManager.MemoryInfo();
        mMemText.setText(getMemInfo());
    }

    public void refreshMem(View view){
        mMemText.setText(getMemInfo());
    }

    public String getMemInfo(){
        activityManager.getMemoryInfo(memoryInfo);
        String info = "";
        info += String.format("%-20s: %d\n","Avaliable Memory",memoryInfo.availMem);
        info += String.format("%-20s: %d\n","Total Memory",memoryInfo.totalMem);
        info += String.format("%-20s: %d%s\n","Memory Usage",(100-memoryInfo.availMem*100/memoryInfo.totalMem),"%");
        info += String.format("%-20s: %s\n","Low Memory",memoryInfo.lowMemory ? "Sim":"Não");
        return info;
    }
}
