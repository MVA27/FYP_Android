package com.android.rtems.Threads;

import android.content.Context;
import android.os.Handler;
import com.android.rtems.utils.ThreadUtility;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.android.rtems.Constants.Server.domain;
import static com.android.rtems.Constants.Server.folder;
import static com.android.rtems.Constants.Server.protocol;

public class TruncateTable extends Thread {

    Context context;
    Handler handler;

    public TruncateTable(Context context,Handler handler){
        this.context = context;
        this.handler = handler;
    }

    @Override
    public void run() {
        String link = protocol+"://"+domain+folder+"/truncate_table.php";
        String parameters = "tableName=parameters";

        try{
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            //POST METHOD
            connection.getDoOutput();
            connection.setRequestMethod("POST");

            try(OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())){
                writer.write(parameters);
                writer.flush();
            }

            ThreadUtility.responseAction(connection,context,handler,"Data Cleared","Unable to clear data");
        }
        catch(IOException e) {
            context.startActivity(ThreadUtility.customizedIntent(context));
        }

    }
}
