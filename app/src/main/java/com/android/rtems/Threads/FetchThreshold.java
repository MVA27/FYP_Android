package com.android.rtems.Threads;

import android.os.SystemClock;
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

public class FetchThreshold extends Thread {

    public FetchThreshold(){}

    @Override
    public void run() {
        String link = protocol+"://"+domain+folder+"/fetch_threshold.php";

        while(true) {
            try {
                URL url = new URL(link);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String JSON = br.readLine();

                Gson gson = new Gson();
                Static.threshold = gson.fromJson(JSON,Parameters.class);

                for(int sec = 1; sec <= Static.refreshTime ; sec++) SystemClock.sleep(1000);
            } catch (IOException e) {
                    e.printStackTrace();
            }
        }
    }
}
