package com.example.hellotoast;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SysActivity extends AppCompatActivity {

    public static final long BEGIN = 1415750400_000L;
    private volatile SimpleArrayMap<String, UsageStats> mStats = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys);
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();

        String arch = System.getProperty("os.arch");
        TextView tv = (TextView) findViewById(R.id.sys_text);


        tv.setText("***** DEVICE Information *****" + "\n");
        tv.append("Model: " + Build.MODEL + "\n");
        tv.append("Board: " + Build.BOARD + "\n");
        tv.append("Brand: " + Build.BRAND + "\n");
        tv.append("Manufacturer: " + Build.MANUFACTURER + "\n");
        tv.append("Device: " + Build.DEVICE + "\n");
        tv.append("Product: " + Build.PRODUCT + "\n");
        tv.append("TAGS: " + Build.TAGS + "\n");
        tv.append("Serial: " + Build.SERIAL + "\n");

        tv.append("\n" + "***** SOC *****" + "\n");
        tv.append("Hardware: " + Build.HARDWARE + "\n");
        tv.append("Number of cores: " + getNumberOfCores() + "\n");
        tv.append("Architecture: " + arch +  "\n");

        tv.append("\n" + "***** OS Information *****" + "\n");
        tv.append("Build release: " + Build.VERSION.RELEASE + "\n");
        tv.append("Incremental release: " + Build.VERSION.INCREMENTAL + "\n");
        tv.append("Base OS: " + Build.VERSION.BASE_OS + "\n");
        tv.append("CODE Name: " + Build.VERSION.CODENAME + "\n");
        tv.append("Security patch: " + Build.VERSION.SECURITY_PATCH + "\n");
        tv.append("Preview SDK: " + Build.VERSION.PREVIEW_SDK_INT + "\n");
        tv.append("SDK/API version: " + Build.VERSION.SDK_INT + "\n");
        tv.append("Display build: " + Build.DISPLAY + "\n");
        tv.append("Finger print: " + Build.FINGERPRINT + "\n") ;
        tv.append("Build ID: " + Build.ID + "\n");

        //SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy 'at' h:mm a");
        //String date = sdf.format(Build.TIME);

        //tv.append("Build Time: " + date + "\n");
        tv.append("Build Type: " + Build.TYPE + "\n");
        tv.append("Build User: " + Build.USER + "\n");
        tv.append("Bootloader: " + Build.BOOTLOADER + "\n");
        tv.append("Kernel version: " + System.getProperty("os.version") + "\n");

        tv.append("\n" + "***** Memory Information *****" + "\n");
        activityManager.getMemoryInfo(memoryInfo);
        tv.append("Available Memory: " + bytesToHuman(memoryInfo.availMem) + "\n");
        tv.append("Total Memory: " + bytesToHuman(memoryInfo.totalMem) + "\n");
        tv.append("Free Memory: " + bytesToHuman(FreeMemory()) + "\n");
        tv.append("Busy Memory: " + bytesToHuman(BusyMemory()) + "\n");
        tv.append("Total Memory: " + bytesToHuman(TotalMemory()) + "\n");
        tv.append("Low Memory (Considered by the system): " + memoryInfo.lowMemory + "\n");

        /*UsageStatsManager manager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> stats = manager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,
                BEGIN, System.currentTimeMillis());
        if (stats == null) {
            return;
        }
        for (UsageStats stat : stats) {
            String packageName = stat.getPackageName();
            int key = mStats.indexOfKey(packageName);
            if (key >= 0) {
                mStats.valueAt(key).add(stat);
                tv.append(packageName + "\n");
            } else {
                mStats.put(packageName, new UsageStats(stat));
            }
        }*/



    }

    public int getNumberOfCores() {
        if(Build.VERSION.SDK_INT >= 17) {
            return Runtime.getRuntime().availableProcessors();
        }else {
            //Code for old SDK values
            return 0;
            //return Runtime.getRuntime().availableProcessors();
        }
    }

    public long TotalMemory()
    {
        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        long   Total  = ( (long) statFs.getBlockCount() * (long) statFs.getBlockSize());
        return Total;
    }

    public long FreeMemory()
    {
        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        long   Free   = (statFs.getAvailableBlocks() * (long) statFs.getBlockSize());
        return Free;
    }

    public long BusyMemory()
    {
        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        long   Total  = ((long) statFs.getBlockCount() * (long) statFs.getBlockSize());
        long   Free   = (statFs.getAvailableBlocks()   * (long) statFs.getBlockSize());
        long   Busy   = Total - Free;
        return Busy;
    }

    /*public static String floatForm (double d)
    {
        return new DecimalFormat("#.##").format(d);
    }*/


    public static String bytesToHuman (long size)
    {
        long Kb = 1  * 1024;
        long Mb = Kb * 1024;
        long Gb = Mb * 1024;
        long Tb = Gb * 1024;
        long Pb = Tb * 1024;
        long Eb = Pb * 1024;

        if (size <  Kb)                 return /*floatForm*/(        size     ) + " byte";
        if (size >= Kb && size < Mb)    return /*floatForm*/((double)size / Kb) + " Kb";
        if (size >= Mb && size < Gb)    return /*floatForm*/((double)size / Mb) + " Mb";
        if (size >= Gb && size < Tb)    return /*floatForm*/((double)size / Gb) + " Gb";
        if (size >= Tb && size < Pb)    return /*floatForm*/((double)size / Tb) + " Tb";
        if (size >= Pb && size < Eb)    return /*floatForm*/((double)size / Pb) + " Pb";
        if (size >= Eb)                 return /*floatForm*/((double)size / Eb) + " Eb";

        return "???";
    }
}
