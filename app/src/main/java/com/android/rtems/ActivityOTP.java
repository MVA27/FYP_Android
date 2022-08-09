package com.android.rtems;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.rtems.storage.Static;
import com.android.rtems.storage.User;
import com.android.rtems.utils.Validation;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;

import static com.android.rtems.Constants.Server.domain;
import static com.android.rtems.Constants.Server.folder;
import static com.android.rtems.Constants.Server.protocol;

@Deprecated
public class ActivityOTP extends AppCompatActivity {

    Handler handler = new Handler();

    private int randomNumber = new Random().nextInt(1000);
    private String phoneNumber = null;
    private String uName = null;

    EditText userName;
    Button next;

    TextView message;
    EditText otp;
    Button verify;

    EditText setPassword;
    EditText confirmPassword;
    Button set;

    private void initialize(){
        userName = findViewById(R.id.id_otp_edit_text_user_name);
        next = findViewById(R.id.id_otp_button_next);

        message = findViewById(R.id.id_otp_text_view_message);
        otp = findViewById(R.id.id_otp_edit_text_otp);
        verify = findViewById(R.id.id_otp_button_verify);

        setPassword = findViewById(R.id.id_otp_edit_text_set_password);
        confirmPassword = findViewById(R.id.id_otp_edit_text_confirm_password);
        set = findViewById(R.id.id_otp_button_set);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ActivityOTP);
        setContentView(R.layout.activity_otp);

        initialize();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uName = userName.getText().toString();
                new Thread(){
                    @Override
                    public void run() {

                        String parameter = "type=get&userName="+uName;
                        String link = protocol+"://"+domain+folder+"/otp.php";

                        try {
                            //STEP 1: connect to server
                            URL url = new URL(link);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.getDoOutput();
                            connection.setRequestMethod("POST");

                            //STEP 2: send data to the server i.e user name
                            try(OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())){
                                writer.write(parameter);
                                writer.flush();
                            }

                            //STEP 3 : Keep checking response code for 10 seconds
                            int status = connection.getResponseCode();
                            int seconds = 0;
                            while(status != 200 && status != 400){

                                status = connection.getResponseCode();
                                SystemClock.sleep(1000);
                                seconds++;

                                if(seconds == 10) break; //connection time-out
                            }

                            //If response is 200, go ahead
                            if(status == 200) {
                                //STEP 3.1 : Get Input Stream and read JSON object of user
                                try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))){
                                    String JSON = br.readLine();
                                    Gson gson = new Gson();
                                    Static.user = gson.fromJson(JSON, User.class);
                                    if(Static.user != null) phoneNumber = Static.user.getPhoneNumber();
                                }
                                if(phoneNumber != null) {

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            next.setVisibility(View.GONE);
                                            userName.setVisibility(View.GONE);

                                            message.setVisibility(View.VISIBLE);
                                            verify.setVisibility(View.VISIBLE);
                                            otp.setVisibility(View.VISIBLE);

                                            message.setText("OTP sent to "+phoneNumber);
                                        }
                                    });
                                }
                            }
                        }
                        catch(IOException e) {
                            e.printStackTrace();
                        }

                    }
                }.start();
            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredOTP = otp.getText().toString();
                if(Integer.parseInt(enteredOTP) == randomNumber){
                    otp.setVisibility(View.GONE);
                    verify.setVisibility(View.GONE);
                    message.setVisibility(View.GONE);

                    setPassword.setVisibility(View.VISIBLE);
                    confirmPassword.setVisibility(View.VISIBLE);
                    set.setVisibility(View.VISIBLE);
                }
            }
        });
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String setPasswd = setPassword.getText().toString();
                String confirmPasswd = confirmPassword.getText().toString();

                if(setPasswd.equals(confirmPasswd)){

                    boolean isPasswdValid = new Validation(handler, Toast.makeText(ActivityOTP.this,"",Toast.LENGTH_LONG)).validatePassword(setPasswd);

                    //If password is valid to ahead, else necessary toast will be shown by Validation class
                    if(isPasswdValid) {
                        String parameter = "type=update&userName="+uName+"&password="+setPasswd;
                        String link = protocol+"://"+domain+folder+"/otp.php";

                        new Thread(){
                            @Override
                            public void run() {
                                try {
                                    //STEP 1: connect to server
                                    URL url = new URL(link);
                                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                    connection.getDoOutput();
                                    connection.setRequestMethod("POST");

                                    //STEP 2: send data to the server i.e user name
                                    try(OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())){
                                        writer.write(parameter);
                                        writer.flush();
                                    }

                                    //STEP 3 : Keep checking response code for 10 seconds
                                    int status = connection.getResponseCode();
                                    int seconds = 0;
                                    while(status != 200 && status != 400){

                                        status = connection.getResponseCode();
                                        SystemClock.sleep(1000);
                                        seconds++;

                                        if(seconds == 10) break; //connection time-out
                                    }

                                    //If response is 200, go to login screen again
                                    if(status == 200) {
                                        startActivity(new Intent(ActivityOTP.this,ActivityLogIn.class));
                                        finish();
                                    }
                                }
                                catch(IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
                    else{
                        setPassword.setText("");
                        confirmPassword.setText("");
                    }
                }
                else Toast.makeText(ActivityOTP.this, "Password does not match", Toast.LENGTH_SHORT).show();
            }
        });
    }
}