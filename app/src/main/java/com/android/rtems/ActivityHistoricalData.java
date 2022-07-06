package com.android.rtems;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.rtems.Threads.FetchHistoricalData;
import com.android.rtems.storage.Static;
import com.android.rtems.storage.UniversalData;

public class ActivityHistoricalData extends AppCompatActivity {

    TableLayout tableLayout;

    private void initialization(){
        tableLayout = findViewById(R.id.id_historical_table_layout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ActivityHistoricalData);
        setContentView(R.layout.activity_historical_data);

        initialization();

        new FetchHistoricalData(this,new Handler(),tableLayout).start();
    }
}