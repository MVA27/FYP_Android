package com.android.rtems.Threads;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.rtems.storage.Parameters;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.android.rtems.Constants.Server.domain;
import static com.android.rtems.Constants.Server.folder;
import static com.android.rtems.Constants.Server.protocol;
import static com.android.rtems.Constants.Server.subDomain;
import static com.android.rtems.Constants.Server.topLevelDomain;

public class FetchParameters extends Thread {

    double refreshTime = 10.0D; // in seconds
    Context context;
    Handler handler;
    ProgressBar progressBar;
    TextView percentage;
    TextView temperature,pressure,humidity,airQuality;

    public FetchParameters(Context context, Handler handler, ProgressBar progressBar,TextView percentage, TextView temperature, TextView pressure, TextView humidity, TextView airQuality) {
        this.context = context;
        this.handler = handler;
        this.progressBar = progressBar;
        this.percentage = percentage;

        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.airQuality = airQuality;
    }

    @Override
    public void run() {
        String link = protocol+"://"+subDomain+"."+domain+"."+topLevelDomain+folder+"/fetch_parameters.php";

        while(true) {
            try {
                //STEP 1 : Connect to the server
                URL url = new URL(link);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                //STEP 2 : Fetch the JSON object
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String JSON = br.readLine();

                //STEP 3 : Convert JSON object to Java object
                Gson gson = new Gson();
                Parameters parameters = gson.fromJson(JSON, Parameters.class);

                //STEP 4 : Display the parameters on the screen
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        temperature.setText(String.valueOf(parameters.getTemperature()));
                        pressure.setText(String.valueOf(parameters.getPressure()));
                        humidity.setText(String.valueOf(parameters.getHumidity()));
                        airQuality.setText(String.valueOf(parameters.getAir_quality()));
                    }
                });

                //STEP 5 : Pause the thread for few seconds and update progress bar
                pauseThread();

            }
            catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //Refresh Timer Clock
    public void pauseThread() throws InterruptedException{

        for(int sec = 1 ; sec <= refreshTime ; sec++){

            int progress = (int)((sec / refreshTime) * 100);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setProgress(progress);
                    percentage.setText(progress+"%");
                }
            });

            Thread.sleep(1000);
        }

    }
}
