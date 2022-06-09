package com.android.rtems;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.rtems.Threads.FetchThreshold;

public class ActivitySettings extends AppCompatActivity {

    EditText temperature,pressure,humidity,airQuality;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        temperature = findViewById(R.id.id_settings_edit_text_temperature);
        pressure = findViewById(R.id.id_settings_edit_text_pressure);
        humidity = findViewById(R.id.id_settings_edit_text_humidity);
        airQuality = findViewById(R.id.id_settings_edit_text_airquality);
        progressBar = findViewById(R.id.id_settings_progress_bar);

        new FetchThreshold(new Handler(),temperature,pressure,humidity,airQuality,progressBar).start();
    }
}