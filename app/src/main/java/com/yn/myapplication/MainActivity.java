package com.yn.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.yn.libsmartroom.module.proCRE9200Lib;
import com.yn.libsmartroom.module.RFIDDataInterface;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        proCRE9200Lib m_proInstance = new proCRE9200Lib();
        m_proInstance.RegRFIDDataInterface(rfidDataInterface);
        m_proInstance.ModelStart("/dev/ttyS3",115200);
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d("11111111111111","qqqqqqqqqqqqqqqqqqq");
        m_proInstance.ModelStop();  // 2019-6-17
        Log.d("111111111111111111","ssssssssssssssssssssss");

        /*
        m_proInstance.ModelStart("/dev/ttyS3",115200);
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d("11111111111111","qqqqqqqqqqqqqqqqqqq");
        m_proInstance.ModelStop();  // 2019-6-17
        Log.d("111111111111111111","ssssssssssssssssssssss");
*/
    }
    RFIDDataInterface rfidDataInterface = new RFIDDataInterface(){
        public void inputEPCDataInfo(String epc){
            System.out.println("==========="+epc);
        }
    };




}
