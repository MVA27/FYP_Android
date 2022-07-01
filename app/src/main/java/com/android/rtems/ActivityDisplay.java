package com.android.rtems;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.rtems.Threads.FetchFlags;
import com.android.rtems.Threads.FetchParameters;
import com.android.rtems.Threads.FetchThreshold;

public class ActivityDisplay extends AppCompatActivity {

    ProgressBar progressBar;
    TextView percentage;
    TextView temperature,pressure,humidity,airQuality;
    ImageView optionsButton;
    CardView[] cards = new CardView[4];

    private void initialization(){
        progressBar = findViewById(R.id.id_display_progress_bar);
        percentage = findViewById(R.id.id_display_percentage_text);

        temperature = findViewById(R.id.id_display_temperature_value);
        pressure = findViewById(R.id.id_display_pressure_value);
        humidity = findViewById(R.id.id_display_humidity_value);
        airQuality = findViewById(R.id.id_display_airquality_value);
        optionsButton = findViewById(R.id.id_display_options_button);

        cards[0] = findViewById(R.id.id_display_card_temperature);
        cards[1] = findViewById(R.id.id_display_card_pressure);
        cards[2] = findViewById(R.id.id_display_card_humidity);
        cards[3] = findViewById(R.id.id_display_card_airquality);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        initialization();

        //Fetch Parameters, Thresholds and Flags
        new FetchFlags().start(); //Starts Infinite fetching of flags
        new FetchThreshold().start();
        new FetchParameters(this,new Handler(),progressBar,percentage,temperature,pressure,humidity,airQuality,cards).start();

        //On clicking options button inflate popup button
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenu = new PopupMenu(ActivityDisplay.this,view);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.display_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch(menuItem.getItemId()){
                            case R.id.id_menu_settings:
                                startActivity(new Intent(ActivityDisplay.this,ActivitySettings.class));
                                return true;

                            case R.id.id_menu_historical_data:
                                startActivity(new Intent(ActivityDisplay.this,ActivityHistoricalData.class));
                                return true;

                            default: return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });
    }
}