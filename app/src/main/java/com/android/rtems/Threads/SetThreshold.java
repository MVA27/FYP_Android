package com.android.rtems.Threads;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import com.android.rtems.utils.ThreadUtility;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.android.rtems.Constants.Server.domain;
import static com.android.rtems.Constants.Server.folder;
import static com.android.rtems.Constants.Server.protocol;

public class SetThreshold extends Thread {

    Context context;
    Handler handler;
    ProgressBar progressBar;
    String temperature,pressure,humidity,airQuality;

    public SetThreshold(Context context,Handler handler,ProgressBar progressBar,String temperature,String pressure,String humidity,String airQuality){
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
        handler.post(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        String parameterList = getParameterList();
        String link = protocol+"://"+domain+folder+"/set_threshold.php";

        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.getDoOutput();
            connection.setRequestMethod("POST");

            try(OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())){
                writer.write(parameterList);
                writer.flush();
            }

            ThreadUtility.responseAction(connection,context,handler,"Threshold set successfully","Unable to set threshold");

        }
        catch(IOException e) {
            e.printStackTrace();
        }
        finally{
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private String getParameterList() {
        String parameterList = "";

        if(!temperature.isEmpty()) parameterList = parameterList+"temperature="+temperature+"&";
        if(!pressure.isEmpty()) parameterList = parameterList+"pressure="+pressure+"&";
        if(!humidity.isEmpty()) parameterList = parameterList+"humidity="+humidity+"&";
        if(!airQuality.isEmpty()) parameterList = parameterList+"airquality="+airQuality+"&";

        return parameterList.substring(0,parameterList.length()-1);
    }
}
