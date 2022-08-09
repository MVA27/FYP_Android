package com.android.rtems;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

@Deprecated
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}