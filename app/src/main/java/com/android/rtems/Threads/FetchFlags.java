package com.android.rtems.Threads;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.EditText;
import com.android.rtems.storage.Flags;
import androidx.appcompat.widget.SwitchCompat;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.android.rtems.Constants.Server.domain;
import static com.android.rtems.Constants.Server.folder;
import static com.android.rtems.Constants.Server.protocol;
import static com.android.rtems.Constants.Server.subDomain;
import static com.android.rtems.Constants.Server.topLevelDomain;

public class FetchFlags extends Thread {

    Handler handler;
    EditText sleepFlag;
    SwitchCompat terminateFlag;

    public FetchFlags(Handler handler,EditText sleepFlag,SwitchCompat terminateFlag){
        this.handler = handler;
        this.sleepFlag = sleepFlag;
        this.terminateFlag = terminateFlag;
    }

    @Override
    public void run() {

        String link = protocol+"://"+subDomain+"."+domain+"."+topLevelDomain+folder+"/fetch_flags.php";

        try{
            //Connect to Server and fetch JSON object
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = br.readLine();

            Gson gson = new Gson();
            Flags flags = gson.fromJson(line,Flags.class);

            //Change necessary UI components
            handler.post(new Runnable() {
                @Override
                public void run() {

                    //If any one value is null, Don't proceed
                    while(sleepFlag == null || terminateFlag == null || flags == null) SystemClock.sleep(1000);

                    sleepFlag.setHint(Integer.toString(flags.getSleep()));
                    if(flags.getTerminate() == 1) terminateFlag.setChecked(true);
                    else terminateFlag.setChecked(false);
                }
            });

        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
