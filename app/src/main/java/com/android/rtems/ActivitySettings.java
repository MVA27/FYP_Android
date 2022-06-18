package com.android.rtems;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.rtems.Threads.SetThreshold;
import com.android.rtems.storage.Static;

public class ActivitySettings extends AppCompatActivity {

    Handler handler = new Handler();
    EditText temperature,pressure,humidity,airQuality,refreshTimer;
    ProgressBar progressBar;
    Button buttonSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        temperature = findViewById(R.id.id_settings_edit_text_temperature);
        pressure = findViewById(R.id.id_settings_edit_text_pressure);
        humidity = findViewById(R.id.id_settings_edit_text_humidity);
        airQuality = findViewById(R.id.id_settings_edit_text_airquality);
        refreshTimer = findViewById(R.id.id_settings_edit_text_refreshtimer);
        progressBar = findViewById(R.id.id_settings_progress_bar);
        buttonSet = findViewById(R.id.id_settings_button_set);

        //Fetch current threshold values and provide it as hint
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

                temperature.setHint(String.valueOf(Static.threshold.getTemperature()));
                pressure.setHint(String.valueOf(Static.threshold.getPressure()));
                humidity.setHint(String.valueOf(Static.threshold.getHumidity()));
                airQuality.setHint(String.valueOf(Static.threshold.getAir_quality()));
                refreshTimer.setHint(String.valueOf(Static.refreshTime));
            }
        });

        buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temperature = ActivitySettings.this.temperature.getText().toString();
                String pressure = ActivitySettings.this.pressure.getText().toString();
                String humidity = ActivitySettings.this.humidity.getText().toString();
                String airQuality = ActivitySettings.this.airQuality.getText().toString();
                String refreshTimer = ActivitySettings.this.refreshTimer.getText().toString();

                //TODO : Show necessary Toasts

                if(!refreshTimer.isEmpty()) Static.refreshTime = Double.parseDouble(refreshTimer);

                //Even if one input is not empty start thread
                if(!temperature.isEmpty() || !pressure.isEmpty() || !humidity.isEmpty() || !airQuality.isEmpty()){
                    new SetThreshold(ActivitySettings.this,new Handler(),progressBar,temperature,pressure,humidity,airQuality).start();
                }
            }
        });
    }
}