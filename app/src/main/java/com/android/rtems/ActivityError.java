package com.android.rtems;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityError extends AppCompatActivity {

    Button close;

    private void initialization() {
        close = findViewById(R.id.id_error_button_close);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ActivityError);
        setContentView(R.layout.activity_error);

        initialization();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });
    }
}