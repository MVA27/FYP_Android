package com.android.rtems;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.android.rtems.storage.Static;

public class ActivitySettings extends AppCompatActivity {

    Handler handler = new Handler();
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

        handler.post(new Runnable() {
            @Override
            public void run() {

                while(Static.threshold == null){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                temperature.setText("Temperature "+Static.threshold.getTemperature());
                pressure.setText("Pressure "+Static.threshold.getPressure());
                humidity.setText("Humidity "+Static.threshold.getHumidity());
                airQuality.setText("Air Quality "+Static.threshold.getAir_quality());
            }
        });
    }
}