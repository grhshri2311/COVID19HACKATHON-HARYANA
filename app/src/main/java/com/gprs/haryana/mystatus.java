package com.gprs.haryana;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class mystatus extends AppCompatActivity {

    RelativeLayout profile, viewmap, todo, notification;
    TextView confirm, death, mystatus, mystatus1, mystatus2, todotext;
    ImageView todoimage, profileimg;
    int total = 0, sick = 0;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    UserRegistrationHelper userRegistrationHelper;
    boolean flag = false;
    ViewPager viewPager;
    Adapterviewer adapter;
    List<Modelviewer> models;
    LinearLayout l1, l2;
    SwipeRefreshLayout constraintLayout;

    CountDownTimer c1, c2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mystatus);
        profile = findViewById(R.id.dashprofile);
        constraintLayout=findViewById(R.id.swipe1);
        notification = findViewById(R.id.notification);
        todo = findViewById(R.id.todo);
        viewmap = findViewById(R.id.viewmap);
        mystatus = findViewById(R.id.mystatus);
        mystatus1 = findViewById(R.id.mystatus1);
        mystatus2 = findViewById(R.id.mystatus2);
        profileimg = findViewById(R.id.airplane);
        pref = getSharedPreferences("user", 0); //
        editor = pref.edit();

        todoimage = findViewById(R.id.todoimage);
        todotext = findViewById(R.id.todotext);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String currentDateTime = dateFormat.format(new Date()); // Find todays date


        check();

        models = new ArrayList<>();
        models.add(new Modelviewer("https://ruppels.net/wp-content/uploads/2020/03/first-responders_who-are-first-responders-1024x522.png", "Step 1", ""));
        models.add(new Modelviewer("https://m.hindustantimes.com/rf/image_size_444x250/HT/p2/2018/01/27/Pictures/_d580858c-0378-11e8-8651-33050e64100a.jpg", "Step 2", ""));
        models.add(new Modelviewer("https://www.wearable-technologies.com/wp-content/uploads/2019/01/First-responder-wearables-1.png", "Step 3", ""));
        models.add(new Modelviewer("https://www.ifrc.org/Global/Photos/Secretariat/201409/20140902-first-aid-Main-1.jpg", "Step 4", ""));


        adapter = new Adapterviewer(models, this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(10, 0, 50, 0);

        l1 = findViewById(R.id.linearLayout);
        l2 = findViewById(R.id.linearLayout0);


        c1 = new CountDownTimer(15000, 3000) {
            public void onTick(long l) {
                l2.setVisibility(View.VISIBLE);
                viewPager.setCurrentItem((viewPager.getCurrentItem() + 1) % models.size());
            }

            public void onFinish() {
                l2.setVisibility(View.INVISIBLE);
                c2.start();
            }
        };

        c2 = new CountDownTimer(10000, 1000) {
            public void onTick(long l) {
                l1.setVisibility(View.VISIBLE);
            }

            public void onFinish() {
                l1.setVisibility(View.INVISIBLE);
                c1.start();
            }
        };

        c2.start();


        confirm = findViewById(R.id.confirm);
        death = findViewById(R.id.death);

        APIextract apIextract = new APIextract(this, confirm, death);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mystatus.this, profile.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(profileimg, "profile");


                //wrap the call in API level 21 or higher
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mystatus.this, pairs);
                    startActivity(intent, options.toBundle());
                }

            }
        });
        viewmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pref.getBoolean("verify", false))
                    startActivity(new Intent(mystatus.this, MapsActivity.class), ActivityOptions.makeSceneTransitionAnimation(mystatus.this).toBundle());
                else {
                    Snackbar snackbar = Snackbar
                            .make(constraintLayout, "Please verify your Identity before start using services", Snackbar.LENGTH_LONG)
                            .setDuration(5000)
                            .setAction("Verify", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(mystatus.this, IdentityVerification.class), ActivityOptions.makeSceneTransitionAnimation(mystatus.this).toBundle());
                                }
                            });

                    snackbar.show();
                }
            }
        });

        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe1);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(mystatus.this, ToDoHome.class));

            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mystatus.this, notification.class));
                notification.setBackground(null);
            }
        });
    }

    void check() {

        FirebaseDatabase.getInstance().getReference().child("Location").child(pref.getString("user", "")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final UserLocationHelper userLocationHelper1 = dataSnapshot.getValue(UserLocationHelper.class);
                if (userLocationHelper1 != null) {

                    FirebaseDatabase.getInstance().getReference().child("Assess")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        if (snapshot.getKey().equals(pref.getString("user", ""))) {
                                            continue;
                                        }
                                        final UserSelfAssessHelper userLocationHelper = snapshot.getValue(UserSelfAssessHelper.class);
                                        float[] results = new float[1];

                                        Location.distanceBetween(userLocationHelper.getLat(), userLocationHelper.getLon(),
                                                userLocationHelper1.getLat(), userLocationHelper.getLon(), results);
                                        if (results[0] < 1000) {
                                            total = total + 1;
                                            if (userLocationHelper.getStatus() == 1) {
                                                sick = sick + 1;
                                            }
                                        }


                                    }
                                    if (sick > 0) {
                                        mystatus.setText(R.string.youhavetobesafe);
                                        mystatus.setTextColor(getResources().getColor(R.color.RED));
                                        mystatus1.setText(getString(R.string.Totalassessed) + total);
                                        mystatus2.setText(getString(R.string.FoundSick) + sick + getString(R.string.Within1KMradius));

                                    } else {
                                        mystatus.setText(R.string.youaresafe);
                                        mystatus.setTextColor(getResources().getColor(R.color.GREEN));
                                        mystatus1.setText(getString(R.string.Totalassessed) + total);
                                        mystatus2.setText(getString(R.string.FoundSick) + sick + getString(R.string.Within1KMradius));
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });


                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
