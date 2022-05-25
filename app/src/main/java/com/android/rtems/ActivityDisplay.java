package com.android.rtems;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.rtems.Threads.FetchParameters;
import com.android.rtems.Threads.RegisterUser;

public class ActivityDisplay extends AppCompatActivity {

    int progress = 1;
    ProgressBar progressBar;
    TextView percentage;
    float angle = 1.0F;

    TextView temperature,pressure,humidity,airQuality;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        progressBar = findViewById(R.id.proressbar_id);
        percentage = findViewById(R.id.percentage_id);

        temperature = findViewById(R.id.id_display_temperature_value);
        pressure = findViewById(R.id.id_display_pressure_value);
        humidity = findViewById(R.id.id_display_humidity_value);
        airQuality = findViewById(R.id.id_display_airquality_value);

        //3rd Review
        //new FetchParameters(this,new Handler(),temperature,pressure,humidity,airQuality).start();

        new Thread(){
            @Override
            public void run() {
                while(progress < 100){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(++progress);
                            percentage.setText(progress+"%");

                            if(progress == 100) {
                                progress = 0;
                                progressBar.setRotation(angle++);
                            }
                        }
                    });

                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();


    }
}