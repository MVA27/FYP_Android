package com.android.rtems.Threads;

import android.content.Context;
import android.os.SystemClock;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import static com.android.rtems.Constants.Server.domain;
import static com.android.rtems.Constants.Server.folder;
import static com.android.rtems.Constants.Server.protocol;

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
        String link = protocol+"://"+domain+folder+"/set_flags.php";
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

            //TODO : Display Toast
            if(status == 200){
            }
            else if(status == 400){
            }
            else{
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
