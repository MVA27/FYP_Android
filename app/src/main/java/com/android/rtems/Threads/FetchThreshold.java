package com.android.rtems.Threads;

import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.android.rtems.storage.Parameters;
import com.android.rtems.storage.Static;
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

public class FetchThreshold extends Thread {

    Handler handler;
    EditText temperature,pressure,humidity,airQuality;
    ProgressBar progressBar;

    public FetchThreshold(){}
    public FetchThreshold(Handler handler, EditText temperature, EditText pressure, EditText humidity, EditText airQuality,ProgressBar progressBar) {
        this.handler = handler;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.airQuality = airQuality;
        this.progressBar = progressBar;
    }

    @Override
    public void run() {

        if(progressBar != null) handler.post(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        String link = protocol+"://"+subDomain+"."+domain+"."+topLevelDomain+folder+"/fetch_threshold.php";

        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String JSON = br.readLine();

            Gson gson = new Gson();
            Static.parameters = gson.fromJson(JSON,Parameters.class);;

            if(temperature != null && pressure != null && humidity != null && airQuality != null) handler.post(new Runnable() {
                @Override
                public void run() {
                    temperature.setHint("Temperature "+Static.parameters.getTemperature());
                    pressure.setHint("Pressure "+Static.parameters.getPressure());
                    humidity.setHint("Humidity "+Static.parameters.getHumidity());
                    airQuality.setHint("Air Quality "+Static.parameters.getAir_quality());
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(progressBar != null) handler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

    }
}
