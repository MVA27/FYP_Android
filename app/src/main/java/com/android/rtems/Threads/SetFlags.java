package com.android.rtems.Threads;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;

import com.android.rtems.utils.ThreadUtility;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import static com.android.rtems.Constants.Server.domain;
import static com.android.rtems.Constants.Server.folder;
import static com.android.rtems.Constants.Server.protocol;

public class SetFlags extends Thread {

    Context context;
    Handler handler;
    String flag;
    int value;

    public SetFlags(Context context,Handler handler,String flag,int value){
        this.context = context;
        this.handler = handler;
        this.flag = flag;
        this.value = value;
    }

    public String getParameterList(){
        if(flag.equals("sleep")) return "sleep="+value;
        if(flag.equals("terminate")) return "terminate="+value;
        if(flag.equals("sms")) return "sms="+value;
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

            ThreadUtility.responseAction(connection,context,handler,"Flag set"
                    ,"Could not set the flag");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
