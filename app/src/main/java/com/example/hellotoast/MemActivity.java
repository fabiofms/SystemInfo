package com.example.hellotoast;

import android.app.ActivityManager;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

public class MemActivity extends AppCompatActivity {

    private ActivityManager activityManager;
    private ActivityManager.MemoryInfo memoryInfo;
    private TextView mMemText;
    StorageStatsManager storageStatsManager;
    StorageManager storageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mem);
        mMemText = (TextView) findViewById(R.id.mem_text);

        activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        memoryInfo = new ActivityManager.MemoryInfo();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            storageStatsManager = (StorageStatsManager) getSystemService(Context.STORAGE_STATS_SERVICE);
        }
        storageManager = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
        mMemText.setText(getMemInfo());
    }

    public void refreshMem(View view){
        mMemText.setText(getMemInfo());
    }

    public String getMemInfo(){
        activityManager.getMemoryInfo(memoryInfo);
        String info = "";
        info = info + String.format("%-20s: ","Avaliable Memory") + bytesToHuman(memoryInfo.availMem) + "\n";
        info = info + String.format("%-20s: ","Total Memory") + bytesToHuman(memoryInfo.totalMem) + "\n";
        info += String.format("%-20s: %d%s\n","Memory Usage",(100-memoryInfo.availMem*100/memoryInfo.totalMem),"%");
        info += String.format("%-20s: %s\n\n","Low Memory",memoryInfo.lowMemory ? "Sim":"Não");
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            info += getStorageInfo();
        }*/
        return info;
    }

    public static String floatForm (double d)
    {
        return new DecimalFormat("#.##").format(d);
    }


    public static String bytesToHuman (long size)
    {
        long Kb = 1  * 1024;
        long Mb = Kb * 1024;
        long Gb = Mb * 1024;
        long Tb = Gb * 1024;
        long Pb = Tb * 1024;
        long Eb = Pb * 1024;

        if (size <  Kb)                 return floatForm(        size     ) + " byte";
        if (size >= Kb && size < Mb)    return floatForm((double)size / Kb) + " Kb";
        if (size >= Mb && size < Gb)    return floatForm((double)size / Mb) + " Mb";
        if (size >= Gb && size < Tb)    return floatForm((double)size / Gb) + " Gb";
        if (size >= Tb && size < Pb)    return floatForm((double)size / Tb) + " Tb";
        if (size >= Pb && size < Eb)    return floatForm((double)size / Pb) + " Pb";
        if (size >= Eb)                 return floatForm((double)size / Eb) + " Eb";

        return "???";
    }

    /*  Método que retorna informação de armazenamento do dispositivo. Não usado.   */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getStorageInfo() {
        String info = "";
        if (storageManager == null || storageStatsManager == null) {
            return info;
        }
        List<StorageVolume> storageVolumes = storageManager.getStorageVolumes();
        for (StorageVolume storageVolume : storageVolumes) {
            final String uuidStr = storageVolume.getUuid();
            final UUID uuid = uuidStr == null ? StorageManager.UUID_DEFAULT : UUID.fromString(uuidStr);
            try {
                info+=("Type: " +  storageVolume.getDescription(this)+"\n");
                info+=("Free Storage Space:\t\t" + Formatter.formatShortFileSize(this, storageStatsManager.getFreeBytes(uuid))+"\n");
                info+=("Total Storage Space:\t\t" + Formatter.formatShortFileSize(this, storageStatsManager.getTotalBytes(uuid))+"\n");

            } catch (Exception e) {
                // IGNORED
            }
        }
        return info;
    }
}
