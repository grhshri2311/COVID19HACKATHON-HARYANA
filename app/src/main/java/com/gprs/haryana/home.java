package com.gprs.haryana;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class home extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 111;
    RelativeLayout mystatus, selfassess, dashmap, updates, case_report, helpline, donate, scan, medstore, epass, admission, quora;
    Toolbar toolbar;
    TextView confirm, death;
    BroadcastReceiver br;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = getApplicationContext().getSharedPreferences("user", 0); // 0 - for private mode
        editor = pref.edit();

        FirebaseDatabase.getInstance().getReference().child("Assess").child(pref.getString("user", "")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue()!=null) {
                    UserSelfAssessHelper u=dataSnapshot.getValue(UserSelfAssessHelper.class);
                    Integer status = u.getStatus();
                    if (status != null && status == 1) {
                        editor.putString("status", "victim");
                        editor.apply();
                        if (!isMyServiceRunning(VictimAlertForegroundNotification.class)) {
                            startService(VictimAlertForegroundNotification.class);
                        }
                    } else {
                        editor.putString("status", "normal");
                        editor.commit();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("Users").child(pref.getString("user", "")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserRegistrationHelper userRegistrationHelper = dataSnapshot.getValue(UserRegistrationHelper.class);
                if (userRegistrationHelper != null) {
                    editor.putBoolean("verify", userRegistrationHelper.getVerify());
                    editor.putString("role", userRegistrationHelper.getRole());
                    editor.commit();
                } else {
                    editor.putString("role", "not defined");
                    editor.commit();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        constraintLayout = findViewById(R.id.layout);

        ImageView implink1, implink2, implink3, implink4, implink5, advice1, advice2, advice3, advice4, advice5, advice6;
        implink1 = findViewById(R.id.implink1);
        implink2 = findViewById(R.id.implink2);
        implink3 = findViewById(R.id.implink3);
        implink4 = findViewById(R.id.implink4);
        implink5 = findViewById(R.id.implink5);
        advice1 = findViewById(R.id.advice1);
        advice2 = findViewById(R.id.advice2);
        advice3 = findViewById(R.id.advice3);
        advice4 = findViewById(R.id.advice4);
        advice5 = findViewById(R.id.advice5);
        advice6 = findViewById(R.id.advice6);


        new home.SetImage(advice1).execute("https://www.cuchd.in/covid-19/img/icon-1.png");
        new home.SetImage(advice2).execute("https://www.cuchd.in/covid-19/img/icon-2.png");
        new home.SetImage(advice3).execute("https://www.cuchd.in/covid-19/img/icon-3.png");
        new home.SetImage(advice4).execute("https://www.cuchd.in/covid-19/img/icon-4.png");
        new home.SetImage(advice5).execute("https://www.cuchd.in/covid-19/img/icon-5.png");
        new home.SetImage(advice6).execute("https://www.cuchd.in/covid-19/img/icon-6.png");
        new home.SetImage(implink1).execute("https://jan-sampark.nic.in/jansampark/images/campaign/2016/01-Jan/Minister/images/mygov-logo.png");
        new home.SetImage(implink2).execute("https://pbs.twimg.com/profile_images/876679325285662721/bhGcfaXx.jpg");
        new home.SetImage(implink3).execute("https://cdn.telanganatoday.com/wp-content/uploads/2020/05/Centre-marks-six-districts-.jpg");
        new home.SetImage(implink4).execute("https://i.pinimg.com/564x/65/30/a5/6530a5e862c58d78b12625850fe1b256.jpg");
        new home.SetImage(implink5).execute("https://tukuz.com/wp-content/uploads/2019/12/national-rural-health-mission-logo-vector.png");


        findViewById(R.id.group1).setTag("open");
        findViewById(R.id.group2).setTag("open");
        findViewById(R.id.group3).setTag("open");
        findViewById(R.id.group4).setTag("open");
        findViewById(R.id.group5).setTag("open");


        findViewById(R.id.group1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findViewById(R.id.group1).getTag().equals("open")) {
                    findViewById(R.id.group1).setTag("close");
                    findViewById(R.id.general).setVisibility(View.VISIBLE);
                    ImageView imageView = findViewById(R.id.generalmenu);
                    imageView.setImageDrawable(getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24));
                } else {
                    findViewById(R.id.group1).setTag("open");
                    findViewById(R.id.general).setVisibility(View.GONE);
                    ImageView imageView = findViewById(R.id.generalmenu);
                    imageView.setImageDrawable(getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24));
                }
            }
        });

        findViewById(R.id.group2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findViewById(R.id.group2).getTag().equals("open")) {
                    findViewById(R.id.group2).setTag("close");
                    findViewById(R.id.healthcare).setVisibility(View.VISIBLE);
                    ImageView imageView = findViewById(R.id.healthmenu);
                    imageView.setImageDrawable(getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24));
                } else {
                    findViewById(R.id.group2).setTag("open");
                    findViewById(R.id.healthcare).setVisibility(View.GONE);
                    ImageView imageView = findViewById(R.id.healthmenu);
                    imageView.setImageDrawable(getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24));
                }
            }
        });

        findViewById(R.id.group3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findViewById(R.id.group3).getTag().equals("open")) {
                    findViewById(R.id.group3).setTag("close");
                    findViewById(R.id.disastermanagement).setVisibility(View.VISIBLE);
                    ImageView imageView = findViewById(R.id.disastermanagementmenu);
                    imageView.setImageDrawable(getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24));
                } else {
                    findViewById(R.id.group3).setTag("open");
                    findViewById(R.id.disastermanagement).setVisibility(View.GONE);
                    ImageView imageView = findViewById(R.id.disastermanagementmenu);
                    imageView.setImageDrawable(getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24));
                }
            }
        });

        findViewById(R.id.group4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findViewById(R.id.group4).getTag().equals("open")) {
                    findViewById(R.id.group4).setTag("close");
                    findViewById(R.id.skill).setVisibility(View.VISIBLE);
                    ImageView imageView = findViewById(R.id.skillmenumenu);
                    imageView.setImageDrawable(getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24));
                } else {
                    findViewById(R.id.group4).setTag("open");
                    findViewById(R.id.skill).setVisibility(View.GONE);
                    ImageView imageView = findViewById(R.id.skillmenumenu);
                    imageView.setImageDrawable(getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24));
                }
            }
        });

        findViewById(R.id.group5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findViewById(R.id.group5).getTag().equals("open")) {
                    findViewById(R.id.group5).setTag("close");
                    findViewById(R.id.education).setVisibility(View.VISIBLE);
                    ImageView imageView = findViewById(R.id.educationmenu);
                    imageView.setImageDrawable(getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24));
                } else {
                    findViewById(R.id.group5).setTag("open");
                    findViewById(R.id.education).setVisibility(View.GONE);
                    ImageView imageView = findViewById(R.id.educationmenu);
                    imageView.setImageDrawable(getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24));
                }
            }
        });


        findViewById(R.id.publicadvice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(home.this, stepstofollow.class);
                startActivity(i, ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });

        findViewById(R.id.media).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(home.this, Media.class);
                startActivity(i, ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });


        br = new InternetBroadcastReciever();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(br, filter);
        pref = getApplicationContext().getSharedPreferences("user", 0); // 0 - for private mode
        editor = pref.edit();



        findViewById(R.id.donatehome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this, donate.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });

        findViewById(R.id.getdirection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, Labsfortestingandresults.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });


        findViewById(R.id.action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, pdfViewer.class);
                intent.putExtra("text", "https://drive.google.com/file/d/1cEkR_bTmlz8se71Xtbtk1t4OlhLJYdPA/view?usp=sharing");
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            startAlarm(false);
            startAlarm(true);
        }


        if (pref.getString("user", "").equals("")) {
            startActivity(new Intent(home.this, logouthome.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            finish();
        }

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(home.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
            return;
        }
        new notificationHelper(this).createOngoingNotification("COVID19RELIEF", "Stay Safe from COVID-19");


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String currentDateTime = dateFormat.format(new Date()); // Find todays date


        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("menuintro", false)) {
            findViewById(R.id.rellayout).setVisibility(View.VISIBLE);
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("menuintro", true).apply();
        }


        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.rellayout).setVisibility(View.INVISIBLE);
            }
        });


        if (!android.preference.PreferenceManager.getDefaultSharedPreferences(this).getString("today1", "").equals(currentDateTime)) {
            FirebaseDatabase.getInstance().getReference().child("IdentityVerification").child(pref.getString("user", "")).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot == null || dataSnapshot.getValue() == null) {
                        Snackbar snackbar = Snackbar
                                .make(constraintLayout, "Please verify your Identity before start using services", Snackbar.LENGTH_LONG)
                                .setDuration(5000)
                                .setAction("Verify", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startActivity(new Intent(home.this, IdentityVerification.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
                                    }
                                });

                        snackbar.show();
                    } else {
                        IdentityVerificationHelper i = dataSnapshot.getValue(IdentityVerificationHelper.class);
                        if (i.getStatus().equals("rejected")) {
                            Snackbar snackbar = Snackbar
                                    .make(constraintLayout, "Please verify your Identity before start using services", Snackbar.LENGTH_LONG)
                                    .setDuration(5000)
                                    .setAction("Verify", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent(home.this, IdentityVerification.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
                                        }
                                    });

                            snackbar.show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        quora = findViewById(R.id.quora);
        donate = findViewById(R.id.donate);
        helpline = findViewById(R.id.helpline);
        mystatus = findViewById(R.id.mystatus);
        scan = findViewById(R.id.scan);
        dashmap = findViewById(R.id.dashmap);
        epass = findViewById(R.id.epass);

        selfassess = findViewById(R.id.selfassess);
        confirm = findViewById(R.id.confirm);
        death = findViewById(R.id.death);
        updates = findViewById(R.id.updates);
        medstore = findViewById(R.id.medstore);
        admission = findViewById(R.id.admission);


        TextView scrolltextview = findViewById(R.id.scrollingtextview);
        scrolltextview.setSelected(true);


        APIextract apIextract = new APIextract(this, confirm, death);


        case_report = findViewById(R.id.case_report);
        setToolBar();


        ImageView i1, i2, i3, i4, i5;
        i1 = findViewById(R.id.implink1);
        i2 = findViewById(R.id.implink2);
        i3 = findViewById(R.id.implink3);
        i4 = findViewById(R.id.implink4);
        i5 = findViewById(R.id.implink5);
        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, pdfViewer.class);
                intent.putExtra("text", "https://www.mohfw.gov.in/");
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });

        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, pdfViewer.class);
                intent.putExtra("text", "https://www.nhp.gov.in/disease/communicable-disease/novel-coronavirus-2019-ncov");
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });

        i3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, pdfViewer.class);
                intent.putExtra("text", "https://www.mohfw.gov.in/");
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });
        i4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, pdfViewer.class);
                intent.putExtra("text", "https://www.icmr.gov.in/");
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });
        i5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, pdfViewer.class);
                intent.putExtra("text", "https://nhm.gov.in/");
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });

        LinearLayout faq, faq1;
        faq = findViewById(R.id.faq);
        faq1 = findViewById(R.id.faq1);


        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, pdfViewer.class);
                intent.putExtra("text", "https://drive.google.com/file/d/1GPoaMCIwbUdd3XDCzHnY_HiP7p2dVJ4x/view?usp=sharing");
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });

        faq1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, pdfViewer.class);
                intent.putExtra("text", "https://drive.google.com/file/d/1A0mY4oMMoSMY5IeuhtKTWVvTkkdOy7lf/view?usp=sharing");
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });
        quora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this, Quora.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });
        epass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this, epass.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });

        admission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Bottomsheetadmissionfragment().show(getSupportFragmentManager(), "Dialog");
            }
        });

        medstore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, Medicalshops.class);
                intent.putExtra("text", "chemists");
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });


        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pref.getString("status", "").equals("victim"))
                    startActivity(new Intent(home.this, victimalert.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
                else {

                    Snackbar snackbar = Snackbar
                            .make(constraintLayout, "You are found victim \nYou can't use this festure!You are found victim \nYou can't use this festure!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

        dashmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pref.getBoolean("verify", false))
                    startActivity(new Intent(home.this, MapsActivity.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
                else {
                    Snackbar snackbar = Snackbar
                            .make(constraintLayout, "Please verify your Identity before start using services", Snackbar.LENGTH_LONG)
                            .setDuration(5000)
                            .setAction("Verify", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(home.this, IdentityVerification.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
                                }
                            });

                    snackbar.show();
                }
            }
        });

        case_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this, cases_report.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });

        updates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this, news.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });

        selfassess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this, SelfAssess.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });


        helpline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this, helpline.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this, donate.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });
        mystatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView cur = findViewById(R.id.textView);
                TextView confirm = findViewById(R.id.confirm);
                TextView death = findViewById(R.id.death);
                ImageView icon = findViewById(R.id.imageView);
                CardView l1 = findViewById(R.id.group1);
                CardView l2 = findViewById(R.id.group2);
                CardView l3 = findViewById(R.id.group3);

                mystatus.setBackground(getDrawable(R.drawable.customborder_home));

                Intent intent = new Intent(home.this, mystatus.class);
                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(icon, "icon");
                pairs[1] = new Pair<View, String>(cur, "currrentstatus");
                pairs[2] = new Pair<View, String>(confirm, "active");
                pairs[3] = new Pair<View, String>(death, "death");
                pairs[4] = new Pair<View, String>(l1, "linear1");
                pairs[5] = new Pair<View, String>(l2, "linear2");
                pairs[6] = new Pair<View, String>(l3, "linear3");

                //wrap the call in API level 21 or higher
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(home.this, pairs);
                    startActivity(intent, options.toBundle());
                }
            }

        });

        FirebaseDatabase.getInstance().getReference().child("Location").child(pref.getString("user", "")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    UserLocationHelper u = dataSnapshot.getValue(UserLocationHelper.class);

                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(home.this, Locale.getDefault());

                    try {
                        addresses = geocoder.getFromLocation(u.getLat(), u.getLon(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5


                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();

                        editor.putString("city", city);
                        editor.putString("state", state);
                        editor.apply();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (!isMyServiceRunning(AlarmForegroundNotification.class)) {
            startService(AlarmForegroundNotification.class);
        }

        if (!isMyServiceRunning(HelpneededBroadcastReceiver.class)) {
            startService(HelpneededBroadcastReceiver.class);
        }

        sendBroadcast(new Intent(this, Restarter.class).setAction("Help"));


    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        final SharedPreferences pref;
        final SharedPreferences.Editor editor;

        pref = getApplicationContext().getSharedPreferences("user", 0); // 0 - for private mode

        FirebaseDatabase.getInstance().getReference().child("Notification").child(pref.getString("user", "")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    Long count1 = dataSnapshot.getChildrenCount();
                    if (count1 >= 1) {
                        menu.getItem(1).setIcon(R.drawable.ic_notifications_red_24dp);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.identity) {
            startActivity(new Intent(home.this, IdentityVerification.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
        }

        if (id == R.id.logoutmenu) {
            startAlarm(false);
            new Bottomsheetlogoutfragment().show(getSupportFragmentManager(), "Dialog");
        }
        if (id == R.id.settings) {
            startActivity(new Intent(home.this, SettingsActivity.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
        }

        if (id == R.id.nav_qr) {
            startActivity(new Intent(home.this, QRcode.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
        }
        if (id == R.id.chatbot) {
            startActivity(new Intent(home.this, Chatbot.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());

        }
        if (id == R.id.translate) {
            new Bottomsheetlanguagefragment().show(getSupportFragmentManager(), "Dialog");
        }
        if (id == R.id.notify) {
            item.setIcon(R.drawable.ic_baseline_notifications_none_24);
            startActivity(new Intent(home.this, notification.class));
        }
        if (id == R.id.share) {
            share();
            final String appPackageName = BuildConfig.APPLICATION_ID;
            final String appName = getString(R.string.app_name);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareBodyText = "https://play.google.com/store/apps/details?id=" +
                    appPackageName;
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, appName);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(shareIntent, getString(R.string
                    .share_with)));
        }
        return true;
    }

    private void setToolBar() {
        androidx.appcompat.widget.Toolbar tb = findViewById(R.id.hometoolbar);
        setSupportActionBar(tb);
    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Snackbar snackbar = Snackbar
                .make(constraintLayout, "Please click BACK again to exit", Snackbar.LENGTH_LONG);
        snackbar.show();


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    void startAlarm(boolean set) {

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent;


        // SET TIME HERE
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());


        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = null;

        // SET TIME HERE
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        myIntent = new Intent(home.this, YourLocationBroadcastReciever.class);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent, 0);


        if (set) {

            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() +
                            1 * 1000, pendingIntent);

        } else if (manager != null) {
            manager.cancel(pendingIntent);
        }

        myIntent = new Intent(home.this, MyNotificationBroadcastReceiver.class);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent, 0);


        if (set) {

            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() +
                            1 * 1000, pendingIntent1);

        } else {
            if (manager != null) {
                manager.cancel((pendingIntent1));
            }
        }
        myIntent = new Intent(home.this, HelpneededBroadcastReceiver.class);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent, 0);


        if (set) {

            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() +
                            1 * 1000, pendingIntent2);

        } else {
            if (manager != null) {
                manager.cancel((pendingIntent2));
            }

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(home.this, "Permission granted", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(home.this, home.class));
                } else {
                    Toast.makeText(home.this, "Permission denied", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    public void alarm(View view) {
        startActivity(new Intent(this, Alarm.class));
    }

    public void firstresponder(View view) {
        if (pref.getBoolean("verify", false))
            startActivity(new Intent(home.this, firstresponder.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
        else {
            Snackbar snackbar = Snackbar
                    .make(constraintLayout, "Please verify your Identity before start using services", Snackbar.LENGTH_LONG)
                    .setDuration(5000)
                    .setAction("Verify", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(home.this, IdentityVerification.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
                        }
                    });

            snackbar.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(br);
        } catch (Exception e) {

        }
    }

    public void chatbot(View view) {
        startActivity(new Intent(home.this, Chatbot.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
    }

    public void whatsapp(View view) {
        try {
            whatsapp(this, "919013151515");
        } catch (IllegalStateException e) {
            Toast.makeText(this, "You have no whatsapp", Toast.LENGTH_LONG).show();
        }
    }

    public static void whatsapp(Activity activity, String phone) {
        String formattedNumber = (phone);
        try {
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Press 1 for latest updates");
            sendIntent.putExtra("jid", formattedNumber + "@s.whatsapp.net");
            sendIntent.setPackage("com.whatsapp");
            activity.startActivity(sendIntent);
        } catch (Exception e) {
            Toast.makeText(activity, "You don't have Whatsapp" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void chatbotintro() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        final AlertDialog alert = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_chatbotintro, null, true);
        Button button = view.findViewById(R.id.btn_get_started);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.hide();

            }
        });
        alert.setView(view);
        alert.show();

    }

    public void startService(Class<?> serviceClass) {
        Intent serviceIntent = new Intent(this, serviceClass);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void stopService(Class<?> serviceClass) {
        Intent serviceIntent = new Intent(this, serviceClass);
        stopService(serviceIntent);

    }

    public void assign(View view) {
        startActivity(new Intent(home.this, WorkAssignHome.class));
    }

    public void labs(View view) {
        startActivity(new Intent(home.this, Labsfortestingandresults.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
    }

    public void msme(View view) {
        startActivity(new Intent(home.this, MSME.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());

    }

    public void hosnear(View view) {
        Intent intent = new Intent(this, Medicalshops.class);
        intent.putExtra("text", "Hospitals");
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void onlinecourse(View view) {
        startActivity(new Intent(home.this, course.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
    }

    public void sectorworkers(View view) {
        if (pref.getBoolean("verify", false))
            startActivity(new Intent(home.this, unorganizedsectors.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
        else {
            Snackbar snackbar = Snackbar
                    .make(constraintLayout, "Please verify your Identity before start using services", Snackbar.LENGTH_LONG)
                    .setDuration(5000)
                    .setAction("Verify", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(home.this, IdentityVerification.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
                        }
                    });

            snackbar.show();
        }
    }

    public void publiccare(View view) {
        startActivity(new Intent(home.this, publichealthcare.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());

    }

    private void share() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Share");


        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_share, null, true);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final String appPackageName = BuildConfig.APPLICATION_ID;

        String shareBodyText = "https://play.google.com/store/apps/details?id=" +
                appPackageName;

        TextView textView = view.findViewById(R.id.text_share);
        textView.setText(shareBodyText);

        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.setView(view);
        alert.show();


    }

    public void counselling(View view) {
        if (pref.getBoolean("verify", false))
            startActivity(new Intent(home.this, DoCounselling.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
        else {
            Snackbar snackbar = Snackbar
                    .make(constraintLayout, "Please verify your Identity before start using services", Snackbar.LENGTH_LONG)
                    .setDuration(5000)
                    .setAction("Verify", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(home.this, IdentityVerification.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
                        }
                    });

            snackbar.show();
        }
    }

    public void materialcollection(View view) {
        if (pref.getBoolean("verify", false))
            startActivity(new Intent(home.this, MaterialCollection.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
        else {
            Snackbar snackbar = Snackbar
                    .make(constraintLayout, "Please verify your Identity before start using services", Snackbar.LENGTH_LONG)
                    .setDuration(5000)
                    .setAction("Verify", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(home.this, IdentityVerification.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
                        }
                    });

            snackbar.show();
        }
    }

    public void isolation(View view) {
        if (pref.getBoolean("verify", false))
            startActivity(new Intent(home.this, Isolation.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
        else {
            Snackbar snackbar = Snackbar
                    .make(constraintLayout, "Please verify your Identity before start using services", Snackbar.LENGTH_LONG)
                    .setDuration(5000)
                    .setAction("Verify", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(home.this, IdentityVerification.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
                        }
                    });

            snackbar.show();
        }
    }

    public void orphan(View view) {
        if (pref.getBoolean("verify", false))
            startActivity(new Intent(home.this, OrphanAndVulnerable.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
        else {
            Snackbar snackbar = Snackbar
                    .make(constraintLayout, "Please verify your Identity before start using services", Snackbar.LENGTH_LONG)
                    .setDuration(5000)
                    .setAction("Verify", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(home.this, IdentityVerification.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
                        }
                    });

            snackbar.show();
        }
    }

    public void callhelpline(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + "1075"));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void todo(View view) {
        if (pref.getBoolean("verify", false))
            startActivity(new Intent(home.this, ToDoHome.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
        else {
            Snackbar snackbar = Snackbar
                    .make(constraintLayout, "Please verify your Identity before start using services", Snackbar.LENGTH_LONG)
                    .setDuration(5000)
                    .setAction("Verify", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(home.this, IdentityVerification.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
                        }
                    });

            snackbar.show();
        }
    }

    public void takecounselling(View view) {
        if (pref.getBoolean("verify", false))
            startActivity(new Intent(home.this, Counselling.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
        else {
            Snackbar snackbar = Snackbar
                    .make(constraintLayout, "Please verify your Identity before start using services", Snackbar.LENGTH_LONG)
                    .setDuration(5000)
                    .setAction("Verify", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(home.this, IdentityVerification.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
                        }
                    });

            snackbar.show();
        }
    }

    public void doverification(View view) {
        if (pref.getString("role", "").equals(getString(R.string.monitors)))
            startActivity(new Intent(home.this, VerifyIdentitymonitor.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
        else {
            Snackbar snackbar = Snackbar
                    .make(constraintLayout, "Only Monitors can use this feature", Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }

    public void verification(View view) {
        startActivity(new Intent(home.this, IdentityVerification.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
    }


    class SetImage extends AsyncTask<String, Void, Bitmap> {

        ImageView imageView;

        public SetImage(ImageView bmImage) {
            this.imageView = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);

        }
    }


}
