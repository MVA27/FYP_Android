package com.android.rtems.Threads;

import android.os.Handler;
import android.util.Log;
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

    public FetchThreshold(){}

    @Override
    public void run() {

        String link = protocol+"://"+subDomain+"."+domain+"."+topLevelDomain+folder+"/fetch_threshold.php";

        while(true) {
            try {
                URL url = new URL(link);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String JSON = br.readLine();

                Gson gson = new Gson();
                Static.threshold = gson.fromJson(JSON,Parameters.class);

                pauseThread();

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void pauseThread() throws InterruptedException{
        for(int sec = 1; sec <= Static.refreshTime ; sec++){
            Thread.sleep(1000);
        }
    }
}
