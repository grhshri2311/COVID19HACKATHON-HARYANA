package com.gprs.haryana;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class Labsfortestingandresults extends AppCompatActivity {

    CardView mapview, listview;
    TextView maptext, listtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_labsfortestingandresults);
        final ViewPager viewPager = findViewById(R.id.view_pager);

        listview = findViewById(R.id.listview);
        listtext = findViewById(R.id.listtext);
        maptext = findViewById(R.id.maptext);
        mapview = findViewById(R.id.mapview);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listview.setCardBackgroundColor(Color.parseColor("#3F51B5"));
                listtext.setTextColor(Color.parseColor("#ffffff"));
                maptext.setTextColor(Color.parseColor("#000000"));
                mapview.setCardBackgroundColor(Color.parseColor("#ffffff"));
                viewPager.setCurrentItem(1, true);
            }
        });
        mapview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listview.setCardBackgroundColor(Color.parseColor("#ffffff"));
                listtext.setTextColor(Color.parseColor("#000000"));
                maptext.setTextColor(Color.parseColor("#ffffff"));
                mapview.setCardBackgroundColor(Color.parseColor("#3F51B5"));
                viewPager.setCurrentItem(0, true);
            }
        });


        viewPager.setEnabled(false);
        viewPager.setAdapter(new Labsfortestingandresults.MyPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    listview.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    listtext.setTextColor(Color.parseColor("#000000"));
                    maptext.setTextColor(Color.parseColor("#ffffff"));
                    mapview.setCardBackgroundColor(Color.parseColor("#3F51B5"));
                } else {
                    listview.setCardBackgroundColor(Color.parseColor("#3F51B5"));
                    listtext.setTextColor(Color.parseColor("#ffffff"));
                    maptext.setTextColor(Color.parseColor("#000000"));
                    mapview.setCardBackgroundColor(Color.parseColor("#ffffff"));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {

                case 0:
                    return new fragment_labsfortestmaptview();
                case 1:
                    return new fragment_labsfortestlistview();
                default:
                    return new fragment_labsfortestmaptview();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
