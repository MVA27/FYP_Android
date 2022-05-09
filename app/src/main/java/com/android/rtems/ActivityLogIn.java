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
import com.android.rtems.Threads.VerifyUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityLogIn extends AppCompatActivity {

    Button buttonLogIn;
    ImageView imageViewRegisterUser;
    EditText userName,password;
    ProgressBar progressBar;
    Handler handler = new Handler();

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
                //STEP 1 : Check if input fields are empty
                if(userName.getText().length() != 0){
                    if(password.getText().length() != 0){

                        //STEP 2 : Perform Validation using Regular Expressions
                        if(validateInput(userName.getText(),password.getText())){

                            //STEP 3 : Make Intent object and pass it to the thread
                            Intent intent = new Intent(ActivityLogIn.this, ActivityDisplay.class);
                            new VerifyUser(ActivityLogIn.this,intent,userName.getText(),password.getText(),handler,progressBar).start();
                        }
                    }
                    else{
                        Toast.makeText(ActivityLogIn.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(ActivityLogIn.this, "Enter User Name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageViewRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityLogIn.this,ActivityRegisterUser.class);
                startActivity(intent);
            }
        });
    }

    public boolean validateInput(Editable userName,Editable password){

        String userNameRegx = "[a-zA-Z]+"; //TODO : implement proper regular expression
        Pattern pattern = Pattern.compile(userNameRegx);
        Matcher matcher = pattern.matcher(userName.toString());
        if(!matcher.find()){
            Toast.makeText(this, "Invalid User Name", Toast.LENGTH_SHORT).show();
            return false;
        }

        String passwordRegx = "[a-zA-Z0-9]+"; //TODO : implement proper regular expression
        pattern = Pattern.compile(passwordRegx);
        matcher = pattern.matcher(password.toString());
        if(!matcher.find()){
            Toast.makeText(this, "Invalid Password", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}