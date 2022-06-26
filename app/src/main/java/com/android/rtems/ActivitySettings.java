package com.android.rtems;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.rtems.Constants.SharedPreference;
import com.android.rtems.Threads.FetchFlags;
import com.android.rtems.Threads.SetFlags;
import com.android.rtems.Threads.SetThreshold;
import com.android.rtems.storage.Static;

public class ActivitySettings extends AppCompatActivity {

    Handler handler = new Handler();
    EditText temperature,pressure,humidity,airQuality,refreshTimer,sleepFlag;
    SwitchCompat terminateFlag;
    ProgressBar progressBar;
    Button buttonSet;

    private void initialization(){
        //Threshold
        temperature = findViewById(R.id.id_settings_edit_text_temperature);
        pressure = findViewById(R.id.id_settings_edit_text_pressure);
        humidity = findViewById(R.id.id_settings_edit_text_humidity);
        airQuality = findViewById(R.id.id_settings_edit_text_airquality);
        refreshTimer = findViewById(R.id.id_settings_edit_text_refresh_timer);

        //Flags
        sleepFlag = findViewById(R.id.id_settings_edit_text_sleep_flag);
        terminateFlag = findViewById(R.id.id_settings_edit_text_terminate_flag);

        //Others
        progressBar = findViewById(R.id.id_settings_progress_bar);
        buttonSet = findViewById(R.id.id_settings_button_set);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initialization();

        //TODO : if possible move this in the background thread i.e FetchThreshold.java
        //Fetch current threshold values and provide it as hint
        handler.post(new Runnable() {
            @Override
            public void run() {

                while(Static.threshold == null) SystemClock.sleep(1000);

                temperature.setHint(String.valueOf(Static.threshold.getTemperature()));
                pressure.setHint(String.valueOf(Static.threshold.getPressure()));
                humidity.setHint(String.valueOf(Static.threshold.getHumidity()));
                airQuality.setHint(String.valueOf(Static.threshold.getAir_quality()));
                refreshTimer.setHint(String.valueOf(Static.refreshTime));
            }
        });

        //Fetch current Flags values and provide it as hint
        new FetchFlags(handler,sleepFlag,terminateFlag).start();

        //Set Button
        buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temperature = ActivitySettings.this.temperature.getText().toString();
                String pressure = ActivitySettings.this.pressure.getText().toString();
                String humidity = ActivitySettings.this.humidity.getText().toString();
                String airQuality = ActivitySettings.this.airQuality.getText().toString();
                String refreshTimer = ActivitySettings.this.refreshTimer.getText().toString();

                String sleepFlag = ActivitySettings.this.sleepFlag.getText().toString();

                //TODO : Show necessary Toasts

                if(!refreshTimer.isEmpty()) {
                    Static.refreshTime = Double.parseDouble(refreshTimer);
                    updateRefreshTimer();
                }

                //Even if one input is not empty start thread
                if(!temperature.isEmpty() || !pressure.isEmpty() || !humidity.isEmpty() || !airQuality.isEmpty()){
                    new SetThreshold(ActivitySettings.this,new Handler(),progressBar,temperature,pressure,humidity,airQuality).start();
                }

                if(!sleepFlag.isEmpty()) new SetFlags(ActivitySettings.this,getResources().getString(R.string.sleep),Integer.parseInt(sleepFlag)).start();
            }
        });

        //Terminate Toggle Button
        terminateFlag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked) new SetFlags(ActivitySettings.this,"terminate",1).start();
                else new SetFlags(ActivitySettings.this,"terminate",0).start();
            }
        });
    }

    //Saves the value of Static.refreshTimer in a SP
    private void updateRefreshTimer() {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreference.NAME_REFRESH_TIMER,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(SharedPreference.KEY_REFRESH_TIMER,(float) Static.refreshTime);
        editor.apply();
    }
}