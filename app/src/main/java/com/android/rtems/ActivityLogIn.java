package com.android.rtems;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityLogIn extends AppCompatActivity {

    Button buttonLogIn;
    ImageView imageViewRegisterUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonLogIn = findViewById(R.id.id_log_in_button);
        imageViewRegisterUser = findViewById(R.id.id_login_register_user_button);

        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLogIn.this,ActivityDisplay.class);
                startActivity(intent);
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
}