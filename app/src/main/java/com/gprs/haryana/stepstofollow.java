package com.gprs.haryana;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class stepstofollow extends AppCompatActivity {

    ViewPager viewPager;
    Adapterviewer adapter;
    List<Modelviewer> models;
    Integer[] colors = null;
    Button btOrder;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepstofollow);

        btOrder = findViewById(R.id.btnOrder);
        btOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        models = new ArrayList<>();
        models.add(new Modelviewer("https://www.who.int/images/default-source/health-topics/coronavirus/risk-communications/general-public/protect-yourself/blue-1.tmb-1920v.png?sfvrsn=3d15aa1c_5", "Step 1", ""));
        models.add(new Modelviewer("https://www.who.int/images/default-source/health-topics/coronavirus/risk-communications/general-public/protect-yourself/blue-2.tmb-1920v.png?sfvrsn=2bc43de1_5", "Step 2", ""));
        models.add(new Modelviewer("https://www.who.int/images/default-source/health-topics/coronavirus/risk-communications/general-public/protect-yourself/blue-3.tmb-1920v.png?sfvrsn=b1ef6d45_5", "Step 3", ""));
        models.add(new Modelviewer("https://www.who.int/images/default-source/health-topics/coronavirus/social-media-squares/be-ready-social-3.tmb-1920v.jpg?sfvrsn=1706a18f_30", "Step 4", ""));
        models.add(new Modelviewer("https://www.who.int/images/default-source/health-topics/coronavirus/social-media-squares/be-ready-social-2.tmb-1920v.jpg?sfvrsn=28a6f92d_5", "Step 5", ""));

        models.add(new Modelviewer("https://www.who.int/images/default-source/health-topics/coronavirus/social-media-squares/be-ready-social-1.tmb-1920v.jpg?sfvrsn=c81745a7_5", "Step 6", ""));
        models.add(new Modelviewer("https://www.who.int/images/default-source/health-topics/coronavirus/social-media-squares/be-smart-if-you-develop.tmb-1920v.jpg?sfvrsn=1486258a_26", "Step 7", ""));
        models.add(new Modelviewer("https://www.who.int/images/default-source/health-topics/coronavirus/social-media-squares/be-smart-inform.tmb-1920v.jpg?sfvrsn=f6dbe358_30", "Step 8", ""));
        models.add(new Modelviewer("https://www.who.int/images/default-source/health-topics/coronavirus/social-media-squares/be-safe.tmb-1920v.jpg?sfvrsn=1f6e4aef_30", "Step 9", ""));
        models.add(new Modelviewer("https://www.who.int/images/default-source/health-topics/coronavirus/social-media-squares/be-safe.tmb-1920v.jpg?sfvrsn=1f6e4aef_30", "Step 10", ""));

        adapter = new Adapterviewer(models, this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(10, 0, 50, 0);

        Integer[] colors_temp = {
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4),
                getResources().getColor(R.color.color5)
        };

        colors = colors_temp;

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position % colors.length < (adapter.getCount() - 1) && position < (colors.length - 1)) {
                    viewPager.setBackgroundColor(

                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position + 1]
                            )
                    );
                } else {
                    viewPager.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
