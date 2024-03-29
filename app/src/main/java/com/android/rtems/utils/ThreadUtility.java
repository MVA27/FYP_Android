package com.android.rtems.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.Toast;

import com.android.rtems.ActivityError;

import java.io.IOException;
import java.net.HttpURLConnection;

public class ThreadUtility {

    /**
     * usage : { Threads.TruncateTable , Threads.SetThreshold , Threads.RegisterUser , Threads.SetFlags }
     * aim   : To fetch response code from the server and perform necessary action
     */
    public static void responseAction(HttpURLConnection connection, Context context, Handler handler,String success,String failure) throws IOException {

        int status = connection.getResponseCode();
        int seconds = 0;

        //Keep checking response code for 10 seconds
        while(status != 200 && status != 400){

            status = connection.getResponseCode();
            SystemClock.sleep(1000);
            seconds++;

            if(seconds == 10) break;
        }

        if(status == 200) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, success, Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(status == 400){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, failure, Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Connection Issue", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * Customized Intent object that will set flags to destroy all activities in the stack
     */
    public static Intent customizedIntent(Context context){
        Intent intent = new Intent(context, ActivityError.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

}
