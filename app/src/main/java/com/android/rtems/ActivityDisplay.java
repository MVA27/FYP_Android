package com.android.rtems;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.rtems.Threads.FetchParameters;
import com.android.rtems.Threads.FetchThreshold;
import com.android.rtems.storage.Static;

public class ActivityDisplay extends AppCompatActivity {

    ProgressBar progressBar;
    TextView percentage;
    TextView temperature,pressure,humidity,airQuality;
    ImageView optionsButton;
    CardView[] cards = new CardView[4];
    NotificationManagerCompat notificationManagerCompat;
    Notification notification;

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

        notificationManagerCompat = NotificationManagerCompat.from(this);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,ActivityDisplay.class), 0);
        notification = new NotificationCompat.Builder(this, com.android.rtems.Constants.Notification.CHANNEL_HIGH_PRIORITY)
                .setSmallIcon(R.drawable.icon_warning)
                .setContentTitle("WARNING")
                .setContentText("One of the threshold has exceeded")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .build();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ActivityDisplay);
        setContentView(R.layout.activity_display);

        initialization();

        //Fetch Parameters and Thresholds infinitely
        new FetchThreshold().start();
        new FetchParameters(this,new Handler(),progressBar,percentage,temperature,pressure,humidity,airQuality,cards,notificationManagerCompat,notification).start();

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
                                if(Static.user != null) {
                                    if(Static.user.isRoot())  startActivity(new Intent(ActivityDisplay.this,ActivitySettings.class));
                                    else Toast.makeText(ActivityDisplay.this, "Access Denied : Login as root user", Toast.LENGTH_SHORT).show();
                                }
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