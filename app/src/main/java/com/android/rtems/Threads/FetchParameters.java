package com.android.rtems.Threads;

import android.app.Notification;
import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationManagerCompat;
import com.android.rtems.R;
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

public class FetchParameters extends Thread {

    Context context;
    Handler handler;
    ProgressBar progressBar;
    TextView percentage;
    TextView temperature,pressure,humidity,airQuality;
    CardView[] cards;
    NotificationManagerCompat notificationManagerCompat;
    Notification notification;

    public FetchParameters(Context context, Handler handler, ProgressBar progressBar,TextView percentage, TextView temperature, TextView pressure, TextView humidity, TextView airQuality, CardView[] cards,NotificationManagerCompat notificationManagerCompat,Notification notification) {
        this.context = context;
        this.handler = handler;
        this.progressBar = progressBar;
        this.percentage = percentage;

        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.airQuality = airQuality;

        this.cards = cards;

        this.notificationManagerCompat = notificationManagerCompat;
        this.notification = notification;
    }

    @Override
    public void run() {

        String link = protocol+"://"+domain+folder+"/fetch_parameters.php";

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
                Static.parameters = gson.fromJson(JSON, Parameters.class);

                //STEP 4 : Display the parameters on the screen (only if the Raspberry pi is not terminated)
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        temperature.setText(String.valueOf(Static.parameters.getTemperature()));
                        pressure.setText(String.valueOf(Static.parameters.getPressure()));
                        humidity.setText(String.valueOf(Static.parameters.getHumidity()));
                        airQuality.setText(String.valueOf(Static.parameters.getAir_quality()));

                        //STEP 5 : Check if any value exceeds threshold
                        trackParameters();
                    }
                });

                //STEP 6 : Pause the thread for few seconds and update progress bar
                pauseThreadUpdateProgress();

            } catch (IOException e) {
                    e.printStackTrace();
            }
        }
    }
    public void trackParameters(){
        if(Static.parameters != null && Static.threshold != null){

            if(Static.parameters.getTemperature() >= Static.threshold.getTemperature()){
                changeCardColor(0,R.color.cardinal_red);
                notificationManagerCompat.notify(com.android.rtems.Constants.Notification.NOTIFICATION_THRESHOLD_ID,notification);
            }
            else{
                changeCardColor(0,R.color.card_background);
            }

            if(Static.parameters.getPressure() >= Static.threshold.getPressure()){
                changeCardColor(1,R.color.cardinal_red);
                notificationManagerCompat.notify(com.android.rtems.Constants.Notification.NOTIFICATION_THRESHOLD_ID,notification);
            }
            else{
                changeCardColor(1,R.color.card_background);
            }

            if(Static.parameters.getHumidity() >= Static.threshold.getHumidity()){
                changeCardColor(2,R.color.cardinal_red);
                notificationManagerCompat.notify(com.android.rtems.Constants.Notification.NOTIFICATION_THRESHOLD_ID,notification);
            }
            else{
                changeCardColor(2,R.color.card_background);
            }

            /**
             * If Raspberry pi is terminated its value in database is set as -1 because if it was 0
             * 0 <= 60 (Threshold) would make the card red
             * Therefore we want some random number to identify this situation
             * Thence -1
             */
            if(Static.parameters.getAir_quality() == -1) { //Raspberry Pi is terminated
                changeCardColor(3,R.color.card_background); // Don't make card red
                airQuality.setText("0.0"); // Don't display -ve value
            }
            else if(Static.parameters.getAir_quality() <= Static.threshold.getAir_quality()){
                changeCardColor(3,R.color.cardinal_red);
                notificationManagerCompat.notify(com.android.rtems.Constants.Notification.NOTIFICATION_THRESHOLD_ID,notification);
            }
            else{
                changeCardColor(3,R.color.card_background);
            }
        }
    }

    public void changeCardColor(int index,int color){
        handler.post(new Runnable() {
            @Override
            public void run() {
                cards[index].setCardBackgroundColor(context.getResources().getColor(color,context.getTheme()));
                cards[index].setRadius(30);
            }
        });
    }

    //TODO : Test Refresh Timer Clock for various test cases
    //Refresh Timer Clock
    public void pauseThreadUpdateProgress(){

        for(int sec = 1; sec <= Static.refreshTime ; sec++){

            int progress = (int)((sec / Static.refreshTime) * 100); //STEP 1 : Calculate Progress

            handler.post(new Runnable() { //STEP 2 : Display Progress
                @Override
                public void run() {
                    progressBar.setProgress(progress);
                    percentage.setText(progress+"%");
                }
            });

            SystemClock.sleep(1000); //STEP 3 : Pause Thread for 1 second
        }

    }

}
