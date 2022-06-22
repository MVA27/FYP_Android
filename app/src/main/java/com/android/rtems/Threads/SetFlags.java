package com.android.rtems.Threads;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import static com.android.rtems.Constants.Server.domain;
import static com.android.rtems.Constants.Server.folder;
import static com.android.rtems.Constants.Server.protocol;
import static com.android.rtems.Constants.Server.subDomain;
import static com.android.rtems.Constants.Server.topLevelDomain;

public class SetFlags extends Thread {

    Context context;
    String flag;
    int value;

    public SetFlags(Context context,String flag,int value){
        this.context = context;
        this.flag = flag;
        this.value = value;
    }

    public String getParameterList(){
        if(flag.equals("sleep")) return "sleep="+value;
        if(flag.equals("terminate")) return "terminate="+value;
        return "";
    }

    @Override
    public void run() {
        String link = protocol+"://"+subDomain+"."+domain+"."+topLevelDomain+folder+"/set_flags.php";
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.getDoOutput();
            connection.setRequestMethod("POST");

            String parameterList = getParameterList();

            try(OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())){
                writer.write(parameterList);
                writer.flush();
            }

            int status = connection.getResponseCode();
            int seconds = 0;

            while(status != 200 && status != 400){

                status = connection.getResponseCode();
                SystemClock.sleep(1000);
                seconds++;

                if(seconds == 10) break; //connection time-out
            }

            if(status == 200){
                Log.d("testsf", "run: success");
            }
            else if(status == 400){
                Log.d("testsf", "run: failed");
            }
            else{
                Log.d("testsf", "run: unexpected failure");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
