package com.android.rtems.Threads;

import android.os.Handler;
import android.os.SystemClock;
import android.widget.EditText;
import com.android.rtems.storage.Flags;
import androidx.appcompat.widget.SwitchCompat;
import com.android.rtems.storage.Static;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.android.rtems.Constants.Server.domain;
import static com.android.rtems.Constants.Server.folder;
import static com.android.rtems.Constants.Server.protocol;

public class FetchFlags extends Thread {

    Handler handler;
    EditText sleepFlag;
    SwitchCompat terminateFlag;

    //When Parametrized constructor is called, This thread will fetch values only once and stop
    public FetchFlags(Handler handler,EditText sleepFlag,SwitchCompat terminateFlag){
        this.handler = handler;
        this.sleepFlag = sleepFlag;
        this.terminateFlag = terminateFlag;
    }

    @Override
    public void run() {

        String link = protocol+"://"+domain+folder+"/fetch_flags.php";

        try {
            //Connect to Server and fetch JSON object
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = br.readLine();

            Gson gson = new Gson();
            Static.flags = gson.fromJson(line, Flags.class);

            //Change necessary UI components
            handler.post(new Runnable() {
                @Override
                public void run() {

                    //If any one value is null, Don't proceed (Check is necessary as it threw an error)
                    while (sleepFlag == null || terminateFlag == null || Static.flags == null)
                        SystemClock.sleep(1000);

                    sleepFlag.setHint(Integer.toString(Static.flags.getSleep()));
                    if (Static.flags.getTerminate() == 1) terminateFlag.setChecked(true);
                    else terminateFlag.setChecked(false);
                }
            });
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
