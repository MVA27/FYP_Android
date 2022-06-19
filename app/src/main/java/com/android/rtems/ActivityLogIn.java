package com.android.rtems;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.rtems.Threads.FetchHistoricalData;
import com.android.rtems.Threads.FetchThreshold;
import com.android.rtems.Threads.VerifyUser;
import com.android.rtems.utils.Validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityLogIn extends AppCompatActivity {

    Button buttonLogIn;
    ImageView imageViewRegisterUser;
    EditText userName,password;
    ProgressBar progressBar;
    Handler handler = new Handler();
    Validation validation;

    private void validate(){
        //Inheritance performed to customize a method
        validation = new Validation(handler,Toast.makeText(this,"",Toast.LENGTH_LONG)){

            int maxLength = 30;
            int minLength = 1;

            @Override
            public boolean validateUserName(String target) {

                if(target.length() > maxLength) return error(R.string.toast_user_name_length_max);
                if(target.length() < minLength) return error(R.string.toast_user_name_length_min);;

                Pattern pattern = Pattern.compile("[^a-zA-Z]");
                Matcher matcher = pattern.matcher(target);
                if(matcher.find()) return error(R.string.toast_user_name);

                return true;
            }
        };

        String un = userName.getText().toString().trim();
        String pswd = password.getText().toString().trim();

        //STEP 1 : Perform Validation using Regular Expressions
        if(validation.validateUserName(un)){
            if(validation.validatePassword(pswd)){

                //STEP 2 : Make Intent object and pass it to the thread
                Intent intent = new Intent(ActivityLogIn.this, ActivityDisplay.class);
                new VerifyUser(ActivityLogIn.this,intent,un,pswd,handler,progressBar).start();

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonLogIn = findViewById(R.id.id_login_log_in_button);
        imageViewRegisterUser = findViewById(R.id.id_login_register_user_button);
        userName = findViewById(R.id.id_login_user_name);
        password = findViewById(R.id.id_login_password);
        progressBar = findViewById(R.id.id_login_progress_bar);

        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

        imageViewRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityLogIn.this,ActivityRegisterUser.class);
                startActivity(intent);
                finish();
            }
        });
    }
}