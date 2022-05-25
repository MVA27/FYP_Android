package com.android.rtems.Threads;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


import static com.android.rtems.Constants.Server.*;

public class RegisterUser extends Thread {

    Context context;
    Handler handler;
    String firstName,lastName,userName,password,age,phoneNumber;

    public RegisterUser(Context context, Handler handler, String firstName, String lastName, String userName, String password, String age, String phoneNumber) {
        this.context = context;
        this.handler = handler;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.age = age;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void run() {
        String parameter = "firstName="+firstName+"&lastName="+lastName+"&userName="+userName+"&userPassword="+password+"&age="+age+"&phoneNumber="+phoneNumber;
        String link = protocol+"://"+subDomain+"."+domain+"."+topLevelDomain+folder+"/register_user.php";

        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.getDoOutput();
            connection.setRequestMethod("POST");

            try(OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())){
                writer.write(parameter);
                writer.flush();
            }

            int status = connection.getResponseCode();
            int seconds = 0;

            //STEP 3 : Keep checking response code for 10 seconds
            while(status != 200 && status != 400){

                status = connection.getResponseCode();
                Thread.sleep(1000);
                seconds++;

                if(seconds == 10) break; //connection time-out
            }

            if(status == 200) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "User Registered", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else if(status == 400){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "User Registration Failed", Toast.LENGTH_SHORT).show();
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

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
