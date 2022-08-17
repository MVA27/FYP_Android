package com.android.rtems.Threads;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import com.android.rtems.utils.ThreadUtility;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import static com.android.rtems.Constants.Server.domain;
import static com.android.rtems.Constants.Server.folder;
import static com.android.rtems.Constants.Server.protocol;

public class RegisterUser extends Thread {

    Context context;
    Handler handler;
    ProgressBar progressBar;
    String firstName,lastName,userName,password,age,phoneNumber;

    public RegisterUser(Context context, Handler handler, ProgressBar progressBar, String firstName, String lastName, String userName, String password, String age, String phoneNumber) {
        this.context = context;
        this.handler = handler;
        this.progressBar = progressBar;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.age = age;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void run() {

        handler.post(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        String parameter = "firstName="+firstName+"&lastName="+lastName+"&userName="+userName+"&userPassword="+password+"&age="+age+"&phoneNumber="+phoneNumber;
        String link = protocol+"://"+domain+folder+"/register_user.php";

        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.getDoOutput();
            connection.setRequestMethod("POST");

            try(OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())){
                writer.write(parameter);
                writer.flush();
            }

            ThreadUtility.responseAction(connection,context,handler,"User Registered","User Registration Failed");

        }
        catch(IOException e) {
            context.startActivity(ThreadUtility.customizedIntent(context));
        }
        finally{
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }
}
