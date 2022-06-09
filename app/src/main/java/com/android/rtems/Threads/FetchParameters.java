package com.android.rtems.Threads;

import android.content.Context;
import android.os.Handler;
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

    Context context;
    Handler handler;
    ProgressBar progressBar;
    TextView temperature,pressure,humidity,airQuality;

    public FetchParameters(Context context, Handler handler, ProgressBar progressBar, TextView temperature, TextView pressure, TextView humidity, TextView airQuality) {
        this.context = context;
        this.handler = handler;
        this.progressBar = progressBar;

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
                URL url = new URL(link);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String JSON = br.readLine();

                Gson gson = new Gson();
                Parameters parameters = gson.fromJson(JSON, Parameters.class);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        temperature.setText(String.valueOf(parameters.getTemperature()));
                        pressure.setText(String.valueOf(parameters.getPressure()));
                        humidity.setText(String.valueOf(parameters.getHumidity()));
                        airQuality.setText(String.valueOf(parameters.getAir_quality()));
                    }
                });

                Thread.sleep(10000);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
