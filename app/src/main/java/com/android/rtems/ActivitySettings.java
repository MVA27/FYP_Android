package com.android.rtems;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.SwitchCompat;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.rtems.Constants.SharedPreference;
import com.android.rtems.Threads.FetchFlags;
import com.android.rtems.Threads.SetFlags;
import com.android.rtems.Threads.SetThreshold;
import com.android.rtems.Threads.TruncateTable;
import com.android.rtems.storage.Static;
import com.android.rtems.utils.DialogBox;

public class ActivitySettings extends AppCompatActivity {

    Handler handler = new Handler();
    EditText temperature,pressure,humidity,airQuality,refreshTimer,sleepFlag;
    SwitchCompat terminateFlag,smsServiceFlag;
    ProgressBar progressBar;
    Button buttonSet,buttonTruncateParameterData;

    private void initialization(){
        //Threshold
        temperature = findViewById(R.id.id_settings_edit_text_temperature);
        pressure = findViewById(R.id.id_settings_edit_text_pressure);
        humidity = findViewById(R.id.id_settings_edit_text_humidity);
        airQuality = findViewById(R.id.id_settings_edit_text_airquality);
        refreshTimer = findViewById(R.id.id_settings_edit_text_refresh_timer);

        //Flags
        sleepFlag = findViewById(R.id.id_settings_edit_text_sleep_flag);
        terminateFlag = findViewById(R.id.id_settings_switch_terminate_flag);
        smsServiceFlag = findViewById(R.id.id_settings_switch_sms_service_flag);

        //Others
        progressBar = findViewById(R.id.id_settings_progress_bar);
        buttonSet = findViewById(R.id.id_settings_button_set);
        buttonTruncateParameterData = findViewById(R.id.id_settings_button_truncate_parameter_data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ActivitySettings);
        setContentView(R.layout.activity_settings);

        initialization();

        //STEP 1: Fetch current threshold values and provide it as hint
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

        //STEP 2: Fetch current Flags values and provide it as hint
        new FetchFlags(handler,sleepFlag,terminateFlag,smsServiceFlag).start();

        //Terminate Toggle Button
        terminateFlag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked) new SetFlags(ActivitySettings.this,handler,"terminate",1).start();
                else new SetFlags(ActivitySettings.this,handler,"terminate",0).start();
            }
        });

        //sms Service Flag Toggle Button
        smsServiceFlag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked) new SetFlags(ActivitySettings.this,handler,"sms",1).start();
                else new SetFlags(ActivitySettings.this,handler,"sms",0).start();
            }
        });

        //Clear Data Button
        buttonTruncateParameterData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogBox dialogBox = new DialogBox(new TruncateTable(ActivitySettings.this,handler), null,"Clear parameters data permanently?");
                dialogBox.show(getSupportFragmentManager(),"tag");
            }
        });

        //Set Button
        buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Fetch values from EditText
                String temperature = ActivitySettings.this.temperature.getText().toString();
                String pressure = ActivitySettings.this.pressure.getText().toString();
                String humidity = ActivitySettings.this.humidity.getText().toString();
                String airQuality = ActivitySettings.this.airQuality.getText().toString();
                String refreshTimer = ActivitySettings.this.refreshTimer.getText().toString();
                String sleepFlag = ActivitySettings.this.sleepFlag.getText().toString();

                //Parameter values : Even if one input is not empty start thread
                if(!temperature.isEmpty() || !pressure.isEmpty() || !humidity.isEmpty() || !airQuality.isEmpty()){
                    new SetThreshold(ActivitySettings.this,new Handler(),progressBar,temperature,pressure,humidity,airQuality).start();
                }

                //Refresh Timer
                if(!refreshTimer.isEmpty()) {
                    Static.refreshTime = Double.parseDouble(refreshTimer);
                    Toast.makeText(ActivitySettings.this, "Refresh Timer Updated", Toast.LENGTH_SHORT).show();
                    updateRefreshTimer();
                }

                //Sleep Flag
                if(!sleepFlag.isEmpty()) new SetFlags(ActivitySettings.this,handler,getResources().getString(R.string.sleep),Integer.parseInt(sleepFlag)).start();
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