package com.android.rtems.Threads;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.rtems.R;
import com.android.rtems.storage.Static;
import com.android.rtems.storage.UniversalData;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.BiConsumer;

import static com.android.rtems.Constants.Server.domain;
import static com.android.rtems.Constants.Server.folder;
import static com.android.rtems.Constants.Server.protocol;

/**
 * This thread fetches historical data
 * At the same time constructs and displays table data
 * */
public class FetchHistoricalData extends Thread {

    Context context;
    Handler handler;
    TableLayout tableLayout;

    public FetchHistoricalData(Context context,Handler handler,TableLayout tableLayout){
        this.context = context;
        this.handler = handler;
        this.tableLayout = tableLayout;
    }

    public void initializeTable() {

        //Method Specific Utility : Created to add columns to each row of Table
        BiConsumer<TableRow,String> initColumn = (row, content)->{
            TextView textView = new TextView(context);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setText(content);
            row.addView(textView);
        };

        handler.post(new Runnable() {
            @Override
            public void run() {

                while(Static.universalData == null) SystemClock.sleep(1000);

                for(UniversalData value : Static.universalData){

                    TableRow row = new TableRow(context);

                    //Date
                    initColumn.accept(row,value.getDay()+"-"+value.getMonth()+"-"+value.getYear());

                    //Time
                    initColumn.accept(row,value.getHours()+":"+value.getMinutes()+":"+value.getSeconds());

                    //Parameters
                    initColumn.accept(row,String.valueOf(value.getTemperature()));
                    initColumn.accept(row,String.valueOf(value.getPressure()));
                    initColumn.accept(row,String.valueOf(value.getHumidity()));
                    initColumn.accept(row,String.valueOf(value.getAir_quality()));

                    if(value.getStatus() == 1) row.setBackgroundColor(context.getColor(R.color.red));
                    tableLayout.addView(row);
                }
            }
        });
    }

    @Override
    public void run() {
        String link = protocol+"://"+domain+folder+"/fetch_historical_data.php";

        //If Data is not already loaded, then connect to server and load
        if(Static.universalData == null) {
            try {
                URL url = new URL(link);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder JSONArray = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    JSONArray.append(line);
                }

                Gson gson = new Gson();
                Static.universalData = gson.fromJson(JSONArray.toString(), UniversalData[].class);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        initializeTable();
    }
}
