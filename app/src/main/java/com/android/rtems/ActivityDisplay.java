package com.android.rtems;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityDisplay extends AppCompatActivity {

    CardView cv,scv;
    TextView tv,status;
    int progress = 1;
    ProgressBar progressBar;
    TextView percentage;
    float angle = 1.0F;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        progressBar = findViewById(R.id.proressbar_id);
        percentage = findViewById(R.id.percentage_id);

        new Thread(){
            @Override
            public void run() {
                while(progress < 100){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(++progress);
                            percentage.setText(progress+"%");

                            if(progress == 100) {
                                progress = 0;
                                progressBar.setRotation(angle++);
                            }
                        }
                    });

                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        cv = findViewById(R.id.cardid);
        scv = findViewById(R.id.statusCardViewID);
        tv = cv.findViewById(R.id.textid);
        status = findViewById(R.id.status);

        status.setText("GOOD");

        new Thread()
        {
            @Override
            public void run() {
                int i;
                for(i = 1 ; i <= 10 ; i++){
                    final int finalI = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText(String.valueOf(finalI));
                            if(finalI >= 9) {
                                cv.setCardBackgroundColor(getResources().getColor(R.color.red));
                                scv.setCardBackgroundColor(getResources().getColor(R.color.red));
                                status.setText("BAD");
                                status.setTextColor(getResources().getColor(R.color.red));
                            }

                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

     */
}