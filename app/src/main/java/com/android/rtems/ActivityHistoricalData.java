package com.android.rtems;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.rtems.Threads.FetchHistoricalData;
import com.android.rtems.storage.Static;
import com.android.rtems.storage.UniversalData;

public class ActivityHistoricalData extends AppCompatActivity {

    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical_data);

        tableLayout = findViewById(R.id.id_historical_table_layout);

        new FetchHistoricalData(this,new Handler(),tableLayout).start();
    }
}