package com.android.rtems.Threads;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.TextView;

import com.android.rtems.storage.Parameters;
import com.android.rtems.storage.Static;
import com.android.rtems.utils.ThreadUtility;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import static com.android.rtems.Constants.Server.domain;
import static com.android.rtems.Constants.Server.folder;
import static com.android.rtems.Constants.Server.protocol;

public class FetchThreshold extends Thread {

    Context context;
    Handler handler;
    TextView thresholdTemperature,thresholdPressure,thresholdHumidity,thresholdAirQuality;

    public FetchThreshold(){}

    public FetchThreshold(Context context, Handler handler, TextView thresholdTemperature, TextView thresholdPressure, TextView thresholdHumidity, TextView thresholdAirQuality){
        this.context = context;
        this.handler = handler;
        this.thresholdTemperature = thresholdTemperature;
        this.thresholdPressure = thresholdPressure;
        this.thresholdHumidity = thresholdHumidity;
        this.thresholdAirQuality = thresholdAirQuality;
    }

    @Override
    public void run() {
        String link = protocol+"://"+domain+folder+"/fetch_threshold.php";

        while(true) {
            try {
                //STEP 1: connect to the server
                URL url = new URL(link);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                //STEP 2: fetch the JSON object
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String JSON = br.readLine();

                //STEP 3: convert the JSON object to Java object and save it in a static variable
                Gson gson = new Gson();
                Static.threshold = gson.fromJson(JSON,Parameters.class);

                //STEP 4: update the threshold value on ActivityDisplay UI
                if(Static.threshold != null) handler.post(new Runnable() {
                        @Override
                        public void run() {
                            thresholdTemperature.setText("Threshold : "+Static.threshold.getTemperature());
                            thresholdPressure.setText("Threshold : "+Static.threshold.getPressure());
                            thresholdHumidity.setText("Threshold : "+Static.threshold.getHumidity());
                            thresholdAirQuality.setText("Threshold : "+Static.threshold.getAir_quality());
                        }
                    });

                //STEP 5: wait for few seconds before repeating the process
                for(int sec = 1; sec <= Static.refreshTime ; sec++) SystemClock.sleep(1000);

            }
            catch (Exception e) {break;}
        }

        //If the loop breaks, that means error occurred. Hence show ActivityError
        context.startActivity(ThreadUtility.customizedIntent(context));
    }
}
