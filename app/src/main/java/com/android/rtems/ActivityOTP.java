package com.android.rtems;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.rtems.storage.Static;
import com.android.rtems.storage.User;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import static com.android.rtems.Constants.Server.domain;
import static com.android.rtems.Constants.Server.folder;
import static com.android.rtems.Constants.Server.protocol;

public class ActivityOTP extends AppCompatActivity {

    Handler handler = new Handler();
    private int randomNumber = new Random().nextInt(1000);
    private String phoneNumber = null;

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


        Log.d("testr", "onCreate: "+randomNumber);
        //compile 'com.vonage:client:[6.1.0,7.0.0)'
//        VonageClient client = VonageClient.builder().apiKey("bbbd316d").apiSecret("10ZOlPuwnL7A69oQ").build();
//        TextMessage message = new TextMessage("Vonage APIs",
//                "918830274551",
//                "A text message sent using the Vonage SMS API"
//        );
//
//        SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);
//
//        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
//            System.out.println("Message sent successfully.");
//        } else {
//            System.out.println("Message failed with error: " + response.getMessages().get(0).getErrorText());
//        }

        initialize();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(){
                    @Override
                    public void run() {
                        String parameter = "userName="+userName.getText().toString();
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
                                    Log.d("testr", "onCreate: "+Static.user);
                                    Log.d("testr", "onCreate: "+phoneNumber);
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
                String confirmPasswd = setPassword.getText().toString();
                if(setPasswd.equals(confirmPasswd)){
                    //TODO : Update database with new password

                }
            }
        });
    }
}