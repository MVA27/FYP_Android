package com.android.rtems.Threads;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.rtems.storage.Static;
import com.android.rtems.storage.User;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import static com.android.rtems.Constants.Server.protocol;
import static com.android.rtems.Constants.Server.domain;
import static com.android.rtems.Constants.Server.folder;

public class VerifyUser extends Thread{

    Intent intent;
    Context context;
    String userName,password;
    Handler handler;
    ProgressBar progressBar;

    public VerifyUser(Context context,Intent intent,String userName,String password,Handler handler,ProgressBar progressBar){
        this.context = context;         //To call startActivity() method from Thread (Non-Activity class)
        this.intent = intent;           //To move to new Activity
        this.userName = userName;
        this.password = password;
        this.handler = handler;         //Handler for ActivityLogin
        this.progressBar = progressBar; //To start and stop progress bar on ActivityLogin
    }

    public void run() {
        //STEP 1 : Start Progress bar
        handler.post(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        //STEP 2 : Start Networking Operation
        try{
            String parameters = "userName="+userName+"&userPassword="+password;
            String link = protocol+"://"+domain+folder+"/verify_user.php";

            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            //POST METHOD
            connection.getDoOutput();
            connection.setRequestMethod("POST");

            try(OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())){
                writer.write(parameters);
                writer.flush();
            }

            int status = connection.getResponseCode();
            int seconds = 0;

            //STEP 3 : Keep checking response code for 10 seconds
            while(status != 200 && status != 400){

                status = connection.getResponseCode();
                SystemClock.sleep(1000);
                seconds++;

                if(seconds == 10) break; //connection time-out
            }

            if(status == 200) {
                //STEP 3.1 : Get Input Stream and read JSON object of user
                try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))){
                    String JSON = br.readLine();
                    Gson gson = new Gson();
                    Static.user = gson.fromJson(JSON, User.class);
                }
                context.startActivity(intent);
            }
            else if(status == 400){
                //STEP 4 : if the status is 400 Display suitable Toast and stop progress bar
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "User Not Registered", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
            else{
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Connection Issue", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
