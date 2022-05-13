package com.android.rtems;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.rtems.utils.Validation;

import java.util.function.Consumer;

public class ActivityRegisterUser extends AppCompatActivity {

    EditText firstName,lastName,userName,password,age,phoneNumber;
    Button register;
    Validation validation = new Validation();

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
                if(validation.validateName(firstName.getText().toString().trim())) { // First Name
                    if(validation.validateName(lastName.getText().toString().trim())) { // Last Name
                        if(validation.validatePassword(password.getText().toString().trim())) { // Password
                            if(validation.validateAge(age.getText().toString().trim())) { // Age
                                if(validation.validatePhoneNumber(phoneNumber.getText().toString().trim())) { // Phone Number
                                    Toast.makeText(ActivityRegisterUser.this, "Valid", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ActivityRegisterUser.this, R.string.toast_phone_number, Toast.LENGTH_LONG).show();
                                }
                            } else{
                                Toast.makeText(ActivityRegisterUser.this, R.string.toast_age, Toast.LENGTH_LONG).show();
                            }
                        } else{
                            Toast.makeText(ActivityRegisterUser.this, R.string.toast_password, Toast.LENGTH_LONG).show();
                        }
                    } else{
                        Toast.makeText(ActivityRegisterUser.this, R.string.toast_name, Toast.LENGTH_LONG).show();
                    }
                } else{
                    Toast.makeText(ActivityRegisterUser.this, R.string.toast_name, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}