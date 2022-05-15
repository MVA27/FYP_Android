package com.android.rtems;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.rtems.utils.Validation;


public class ActivityRegisterUser extends AppCompatActivity {

    EditText firstName,lastName,userName,password,age,phoneNumber;
    Button register;
    Validation validation;

    private void validate(){
        validation = new Validation(new Handler(),Toast.makeText(this,"",Toast.LENGTH_LONG));
        if(validation.validateName(firstName.getText().toString().trim())) { // First Name
            if(validation.validateName(lastName.getText().toString().trim())) { // Last Name
                if(validation.validateUserName(userName.getText().toString().trim())){  // User Name
                    if(validation.validatePassword(password.getText().toString().trim())) { // Password
                        if(validation.validateAge(age.getText().toString().trim())) { // Age
                            if(validation.validatePhoneNumber(phoneNumber.getText().toString().trim())) { // Phone Number
                                Toast.makeText(ActivityRegisterUser.this, "Valid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        firstName = findViewById(R.id.id_register_first_name);
        lastName = findViewById(R.id.id_register_last_name);
        userName = findViewById(R.id.id_register_user_name);
        password = findViewById(R.id.id_register_password);
        age = findViewById(R.id.id_register_age);
        phoneNumber = findViewById(R.id.id_register_phone_number);
        register = findViewById(R.id.id_register_register_button);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validate();

            }
        });
    }
}