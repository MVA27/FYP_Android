package com.android.rtems.utils;

import com.android.rtems.Constants.Notification;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

/**
 *  This Class is called before starting any activity
 **/
public class Initializer extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            //Create Channel
            NotificationChannel channelHighPriority = new NotificationChannel(
              Notification.CHANNEL_HIGH_PRIORITY,
              "Channel High Priority",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channelHighPriority.setDescription("Parameter Threshold Has Exceeded");

            //Register The Channel
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channelHighPriority);
        }
    }
}
