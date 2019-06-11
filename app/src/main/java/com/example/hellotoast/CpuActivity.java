package com.example.hellotoast;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CpuActivity extends AppCompatActivity {

    private TextView mCpuText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpu);
        mCpuText = (TextView) findViewById(R.id.cpu_text);
        mCpuText.setText(getCpuInfo());
    }

    public void refreshCpu(View view){
        mCpuText.setText(getCpuInfo());
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

    private static final int INSERTION_POINT = 27;

    private static String getCurFrequencyFilePath(int whichCpuCore){
        StringBuilder filePath = new StringBuilder("/sys/devices/system/cpu/cpu/cpufreq/scaling_cur_freq");
        filePath.insert(INSERTION_POINT, whichCpuCore);
        return filePath.toString();
    }

    public static int getCurrentFrequency(int whichCpuCore){

        int curFrequency = -1;
        String cpuCoreCurFreqFilePath = getCurFrequencyFilePath(whichCpuCore);

        if(new File(cpuCoreCurFreqFilePath).exists()){

            try {
                BufferedReader br = new BufferedReader(new FileReader(new File(cpuCoreCurFreqFilePath)));
                String aLine;
                while ((aLine = br.readLine()) != null) {
                    try{
                        curFrequency = Integer.parseInt(aLine);
                    }
                    catch(NumberFormatException e){

                        Log.e("CPU info", e.toString());
                    }
                }
                if (br != null) {
                    br.close();
                }
            }
            catch (IOException e) {
                Log.e("CPU info", e.toString());
            }
        }

        return curFrequency;
    }

    public String getCpuInfo(){
        String resultado = "Frequencia das CPUs:\n";
        int numberOfCores = getNumberOfCores();
        for (int i = 0; i < numberOfCores; i++){
            resultado += String.format("%s %d: %d MHz\n","CPU",i + 1, getCurrentFrequency(i)/1000);
        }
        return resultado;
    }
}
