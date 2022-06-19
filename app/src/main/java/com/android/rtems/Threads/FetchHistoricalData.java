package com.android.rtems.Threads;

import android.util.Log;

import com.android.rtems.storage.Parameters;
import com.android.rtems.storage.Static;
import com.android.rtems.storage.UniversalData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static com.android.rtems.Constants.Server.domain;
import static com.android.rtems.Constants.Server.folder;
import static com.android.rtems.Constants.Server.protocol;
import static com.android.rtems.Constants.Server.subDomain;
import static com.android.rtems.Constants.Server.topLevelDomain;

public class FetchHistoricalData extends Thread {
    @Override
    public void run() {
        String link = protocol+"://"+subDomain+"."+domain+"."+topLevelDomain+folder+"/fetch_historical_data.php";

        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            StringBuilder JSONArray = new StringBuilder();

            while((line = br.readLine()) != null){
                JSONArray.append(line);
            }

            Gson gson = new GsonBuilder().setLenient().create();
            UniversalData[] universalData = gson.fromJson(JSONArray.toString(),UniversalData[].class);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
