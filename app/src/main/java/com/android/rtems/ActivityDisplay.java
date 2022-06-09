package com.android.rtems;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.rtems.Threads.FetchParameters;

public class ActivityDisplay extends AppCompatActivity {

    int progress = 1;
    ProgressBar progressBar;
    TextView percentage;
    float angle = 1.0F;

    TextView temperature,pressure,humidity,airQuality;
    ImageView settingsButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        progressBar = findViewById(R.id.id_display_progress_bar);
        percentage = findViewById(R.id.id_display_percentage_text);

        temperature = findViewById(R.id.id_display_temperature_value);
        pressure = findViewById(R.id.id_display_pressure_value);
        humidity = findViewById(R.id.id_display_humidity_value);
        airQuality = findViewById(R.id.id_display_airquality_value);
        settingsButton = findViewById(R.id.id_display_settings_button);


        //3rd Review
        new FetchParameters(this,new Handler(),progressBar,temperature,pressure,humidity,airQuality).start();

        //
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityDisplay.this,ActivitySettings.class));
            }
        });

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