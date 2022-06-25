package com.android.rtems;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.rtems.Threads.RegisterUser;
import com.android.rtems.utils.Validation;

public class ActivityRegisterUser extends AppCompatActivity {

    EditText firstName,lastName,userName,password,age,phoneNumber;
    ProgressBar progressBar;
    Button register;
    Validation validation;
    Handler handler = new Handler();

    private boolean validate(){

        validation = new Validation(new Handler(),Toast.makeText(this,"",Toast.LENGTH_LONG));
        if(validation.validateName(firstName.getText().toString().trim())) { // First Name
            if(validation.validateName(lastName.getText().toString().trim())) { // Last Name
                if(validation.validateUserName(userName.getText().toString().trim())){  // User Name
                    if(validation.validatePassword(password.getText().toString().trim())) { // Password
                        if(validation.validateAge(age.getText().toString().trim())) { // Age
                            if(validation.validatePhoneNumber(phoneNumber.getText().toString().trim())) { // Phone Number
                                return true;
                            } else return false;
                        }else return false;
                    }else return false;
                }else return false;
            }else return false;
        }else return false;

    }

    private void initialization(){
        firstName = findViewById(R.id.id_register_first_name);
        lastName = findViewById(R.id.id_register_last_name);
        userName = findViewById(R.id.id_register_user_name);
        password = findViewById(R.id.id_register_password);
        age = findViewById(R.id.id_register_age);
        phoneNumber = findViewById(R.id.id_register_phone_number);
        progressBar = findViewById(R.id.id_register_progress_bar);
        register = findViewById(R.id.id_register_register_button);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        initialization();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate()){
                    new RegisterUser(ActivityRegisterUser.this,
                            handler,
                            progressBar,
                            firstName.getText().toString(),
                            lastName.getText().toString(),
                            userName.getText().toString(),
                            password.getText().toString(),
                            age.getText().toString(),
                            phoneNumber.getText().toString()
                    ).start();
                }
            }
        });
    }
}