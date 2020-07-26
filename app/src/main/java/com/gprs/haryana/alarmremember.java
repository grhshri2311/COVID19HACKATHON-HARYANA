package com.gprs.haryana;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class alarmremember extends AppCompatActivity {

    TextView textView, textView1, textView2, textView3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_alarmremember);


        Intent intent = getIntent();

        textView = findViewById(R.id.textView15);
        textView1 = findViewById(R.id.textView16);
        textView2 = findViewById(R.id.name);
        textView3 = findViewById(R.id.textView17);


        textView2.setText(intent.getStringExtra("name"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        String currentDateTime = dateFormat.format(new Date());
        textView.setText(currentDateTime);
        dateFormat = new SimpleDateFormat("dd:MMMM:E:a");
        currentDateTime = dateFormat.format(new Date());

        textView1.setText(currentDateTime);


        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}
