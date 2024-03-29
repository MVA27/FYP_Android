package com.android.rtems.Threads;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.EditText;
import com.android.rtems.storage.Flags;
import androidx.appcompat.widget.SwitchCompat;
import com.android.rtems.storage.Static;
import com.android.rtems.utils.ThreadUtility;
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

    Context context;
    Handler handler;
    EditText sleepFlag;
    SwitchCompat terminateFlag,smsServiceFlag;

    //When Parametrized constructor is called, This thread will fetch values only once and stop
    public FetchFlags(Context context, Handler handler,EditText sleepFlag,SwitchCompat terminateFlag,SwitchCompat smsServiceFlag){
        this.context = context;
        this.handler = handler;
        this.sleepFlag = sleepFlag;
        this.terminateFlag = terminateFlag;
        this.smsServiceFlag = smsServiceFlag;
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

                    if (Static.flags.getSmsService() == 1) smsServiceFlag.setChecked(true);
                    else smsServiceFlag.setChecked(false);
                }
            });
        }
        catch (Exception e) {
            context.startActivity(ThreadUtility.customizedIntent(context));
        }
    }
}
