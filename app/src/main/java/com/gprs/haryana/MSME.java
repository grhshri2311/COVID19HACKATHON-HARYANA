package com.gprs.haryana;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class MSME extends AppCompatActivity {

    ArrayList<String> arrayList;
    ListView listView;

    LinearLayout action, register;
    CustomMSMEAdapter customMSMEAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_msme);

        listView = findViewById(R.id.MSME);
        action = findViewById(R.id.action);
        register = findViewById(R.id.register);

        final ScrollView sv = findViewById(R.id.sv);
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sv.requestDisallowInterceptTouchEvent(true);
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        sv.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MSME.this, pdfViewer.class);
                intent.putExtra("text", "https://drive.google.com/file/d/1cEkR_bTmlz8se71Xtbtk1t4OlhLJYdPA/view?usp=sharing");
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MSME.this).toBundle());
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MSME.this, pdfViewer.class);
                intent.putExtra("text", "https://mkp.gem.gov.in/registration/signup#!/seller");
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MSME.this).toBundle());
            }
        });

        String[] items = {"Ventilators", "Alcohol based hand-rub", "Face shield (eye, nose & mouth protection)", "N95 Masks", "Latex single use gloves (clinical)",
                "Reusable vinyl / rubber gloves (cleaning)", "Eye protection (visor / goggles)", "Protective Gowns / Aprons", "Disposable thermometers",
                "UV tube light for sterilization", "Medical masks (surgical / procedure)", "Detergent / Disinfectant", "Single use towels", "Biohazard bags",
                "Wheel Chair", "Glucometer with strips", "Medicine", "IV Fluid - DNS", "IV Fluid - Dextrose", "20.Hard-frozen Gel Packs", "Sample Collection Kit",
                "Thermocool box / Ice-box", "Stretcher", "Thermal scanners", "Batteries for thermal scanners", "26.BP apparatus", "IV Sets", "IV Cannula",
                "IV Stand", "Ambulance", "First aid", "Medical Waste Incinerator", "ICU Beds", "Cardiac monitors", "Syringe pumps", "Portable x ray machines",
                "Endotracheal tube", "Suction tube", "Oxygen cylinders"};

        arrayList = new ArrayList<>(Arrays.asList(items));

        customMSMEAdapter = new CustomMSMEAdapter(this, arrayList);
        listView.setAdapter(customMSMEAdapter);
    }
}
