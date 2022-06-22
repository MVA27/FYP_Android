package com.android.rtems;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.rtems.Threads.FetchParameters;
import com.android.rtems.Threads.FetchThreshold;

public class ActivityDisplay extends AppCompatActivity {

    ProgressBar progressBar;
    TextView percentage;
    TextView temperature,pressure,humidity,airQuality;
    ImageView settingsButton;
    CardView[] cards = new CardView[4];

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

        cards[0] = findViewById(R.id.id_display_card_temperature);
        cards[1] = findViewById(R.id.id_display_card_pressure);
        cards[2] = findViewById(R.id.id_display_card_humidity);
        cards[3] = findViewById(R.id.id_display_card_airquality);

        //Fetch Parameters and Thresholds
        new FetchParameters(this,new Handler(),progressBar,percentage,temperature,pressure,humidity,airQuality,cards).start();
        new FetchThreshold().start();

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityDisplay.this,ActivitySettings.class));
                //startActivity(new Intent(ActivityDisplay.this,ActivityHistoricalData.class));
                finish();
            }
        });
    }
}