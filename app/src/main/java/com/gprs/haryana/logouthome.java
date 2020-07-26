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
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import com.google.android.material.snackbar.Snackbar;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class logouthome extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 111;
    private static final int REQUEST_INVITE = 10115;
    RelativeLayout selfassess, updates, case_report, helpline, donate, medstore, epass, admission;
    Toolbar toolbar;
    TextView confirm, death;
    BroadcastReceiver br;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Menu toolbarmenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_logouthome);


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


        new logouthome.SetImage(advice1).execute("https://www.cuchd.in/covid-19/img/icon-1.png");
        new logouthome.SetImage(advice2).execute("https://www.cuchd.in/covid-19/img/icon-2.png");
        new logouthome.SetImage(advice3).execute("https://www.cuchd.in/covid-19/img/icon-3.png");
        new logouthome.SetImage(advice4).execute("https://www.cuchd.in/covid-19/img/icon-4.png");
        new logouthome.SetImage(advice5).execute("https://www.cuchd.in/covid-19/img/icon-5.png");
        new logouthome.SetImage(advice6).execute("https://www.cuchd.in/covid-19/img/icon-6.png");

        new logouthome.SetImage(implink1).execute("https://jan-sampark.nic.in/jansampark/images/campaign/2016/01-Jan/Minister/images/mygov-logo.png");
        new logouthome.SetImage(implink2).execute("https://pbs.twimg.com/profile_images/876679325285662721/bhGcfaXx.jpg");
        new logouthome.SetImage(implink3).execute("https://cdn.telanganatoday.com/wp-content/uploads/2020/05/Centre-marks-six-districts-.jpg");
        new logouthome.SetImage(implink4).execute("https://i.pinimg.com/564x/65/30/a5/6530a5e862c58d78b12625850fe1b256.jpg");
        new logouthome.SetImage(implink5).execute("https://tukuz.com/wp-content/uploads/2019/12/national-rural-health-mission-logo-vector.png");

        pref = getApplicationContext().getSharedPreferences("user", 0); // 0 - for private mode
        editor = pref.edit();

        findViewById(R.id.group1).setTag("open");
        findViewById(R.id.group2).setTag("open");
        findViewById(R.id.group3).setTag("open");
        findViewById(R.id.group4).setTag("open");


        findViewById(R.id.group1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findViewById(R.id.group1).getTag().equals("open")) {
                    findViewById(R.id.group1).setTag("close");
                    findViewById(R.id.healthcare).setVisibility(View.VISIBLE);
                    ImageView imageView = findViewById(R.id.healthmenu);
                    imageView.setImageDrawable(getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24));
                } else {
                    findViewById(R.id.group1).setTag("open");
                    findViewById(R.id.healthcare).setVisibility(View.GONE);
                    ImageView imageView = findViewById(R.id.healthmenu);
                    imageView.setImageDrawable(getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24));
                }
            }
        });

        findViewById(R.id.group2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findViewById(R.id.group2).getTag().equals("open")) {
                    findViewById(R.id.group2).setTag("close");
                    findViewById(R.id.disastermanagement).setVisibility(View.VISIBLE);
                    ImageView imageView = findViewById(R.id.disastermanagementmenu);
                    imageView.setImageDrawable(getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24));
                } else {
                    findViewById(R.id.group2).setTag("open");
                    findViewById(R.id.disastermanagement).setVisibility(View.GONE);
                    ImageView imageView = findViewById(R.id.disastermanagementmenu);
                    imageView.setImageDrawable(getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24));
                }
            }
        });

        findViewById(R.id.group3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findViewById(R.id.group3).getTag().equals("open")) {
                    findViewById(R.id.group3).setTag("close");
                    findViewById(R.id.education).setVisibility(View.VISIBLE);
                    ImageView imageView = findViewById(R.id.educationmenu);
                    imageView.setImageDrawable(getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24));
                } else {
                    findViewById(R.id.group3).setTag("open");
                    findViewById(R.id.education).setVisibility(View.GONE);
                    ImageView imageView = findViewById(R.id.educationmenu);
                    imageView.setImageDrawable(getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24));
                }
            }
        });

        findViewById(R.id.group4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findViewById(R.id.group4).getTag().equals("open")) {
                    findViewById(R.id.group4).setTag("close");
                    findViewById(R.id.general).setVisibility(View.VISIBLE);
                    ImageView imageView = findViewById(R.id.generalmenu);
                    imageView.setImageDrawable(getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24));
                } else {
                    findViewById(R.id.group4).setTag("open");
                    findViewById(R.id.general).setVisibility(View.GONE);
                    ImageView imageView = findViewById(R.id.generalmenu);
                    imageView.setImageDrawable(getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24));
                }
            }
        });

        findViewById(R.id.donatehome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(logouthome.this, donate.class), ActivityOptions.makeSceneTransitionAnimation(logouthome.this).toBundle());
            }
        });
        findViewById(R.id.publicadvice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(logouthome.this, stepstofollow.class);
                startActivity(i, ActivityOptions.makeSceneTransitionAnimation(logouthome.this).toBundle());
            }
        });

        findViewById(R.id.media).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(logouthome.this, Media.class);
                startActivity(i, ActivityOptions.makeSceneTransitionAnimation(logouthome.this).toBundle());
            }
        });


        findViewById(R.id.getdirection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(logouthome.this, Labsfortestingandresults.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(logouthome.this).toBundle());
            }
        });


        findViewById(R.id.action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(logouthome.this, pdfViewer.class);
                intent.putExtra("text", "https://drive.google.com/file/d/1cEkR_bTmlz8se71Xtbtk1t4OlhLJYdPA/view?usp=sharing");
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(logouthome.this).toBundle());
            }
        });


        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            startAlarm(false);
            startAlarm(true);
        }


        if (!pref.getString("user", "").equals("")) {
            startActivity(new Intent(logouthome.this, login.class), ActivityOptions.makeSceneTransitionAnimation(logouthome.this).toBundle());
            finish();
        }

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(logouthome.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
            return;
        }
        new notificationHelper(this).createOngoingNotification("COVID19RELIEF", "Stay Safe from COVID-19");


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String currentDateTime = dateFormat.format(new Date()); // Find todays date


        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("chatintrologout", false)) {
            chatbotintro();
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("chatintrologout", true).apply();
        }


        donate = findViewById(R.id.donate);
        helpline = findViewById(R.id.helpline);
        epass = findViewById(R.id.epass);
        selfassess = findViewById(R.id.selfassess);
        confirm = findViewById(R.id.confirm);
        death = findViewById(R.id.death);
        updates = findViewById(R.id.updates);
        medstore = findViewById(R.id.medstore);
        admission = findViewById(R.id.admission);
        case_report = findViewById(R.id.case_report);


        TextView scrolltextview = findViewById(R.id.scrollingtextview);
        scrolltextview.setSelected(true);


        APIextract apIextract = new APIextract(this, confirm, death);


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
                Intent intent = new Intent(logouthome.this, pdfViewer.class);
                intent.putExtra("text", "https://www.mohfw.gov.in/");
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(logouthome.this).toBundle());
            }
        });

        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(logouthome.this, pdfViewer.class);
                intent.putExtra("text", "https://www.nhp.gov.in/disease/communicable-disease/novel-coronavirus-2019-ncov");
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(logouthome.this).toBundle());
            }
        });

        i3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(logouthome.this, pdfViewer.class);
                intent.putExtra("text", "https://www.mohfw.gov.in/");
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(logouthome.this).toBundle());
            }
        });
        i4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(logouthome.this, pdfViewer.class);
                intent.putExtra("text", "https://www.icmr.gov.in/");
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(logouthome.this).toBundle());
            }
        });
        i5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(logouthome.this, pdfViewer.class);
                intent.putExtra("text", "https://nhm.gov.in/");
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(logouthome.this).toBundle());
            }
        });

        LinearLayout faq, faq1;
        faq = findViewById(R.id.faq);
        faq1 = findViewById(R.id.faq1);


        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(logouthome.this, pdfViewer.class);
                intent.putExtra("text", "https://drive.google.com/file/d/1GPoaMCIwbUdd3XDCzHnY_HiP7p2dVJ4x/view?usp=sharing");
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(logouthome.this).toBundle());
            }
        });

        faq1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(logouthome.this, pdfViewer.class);
                intent.putExtra("text", "https://drive.google.com/file/d/1A0mY4oMMoSMY5IeuhtKTWVvTkkdOy7lf/view?usp=sharing");
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(logouthome.this).toBundle());
            }
        });

        epass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(logouthome.this, epass.class), ActivityOptions.makeSceneTransitionAnimation(logouthome.this).toBundle());
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
                Intent intent = new Intent(logouthome.this, Medicalshops.class);
                intent.putExtra("text", "chemists");
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(logouthome.this).toBundle());
            }
        });

        case_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(logouthome.this, cases_report.class), ActivityOptions.makeSceneTransitionAnimation(logouthome.this).toBundle());
            }
        });

        updates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(logouthome.this, news.class), ActivityOptions.makeSceneTransitionAnimation(logouthome.this).toBundle());
            }
        });

        selfassess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(logouthome.this, SelfAssess.class), ActivityOptions.makeSceneTransitionAnimation(logouthome.this).toBundle());
            }
        });


        helpline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(logouthome.this, helpline.class), ActivityOptions.makeSceneTransitionAnimation(logouthome.this).toBundle());
            }
        });
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(logouthome.this, donate.class), ActivityOptions.makeSceneTransitionAnimation(logouthome.this).toBundle());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menulogout, menu);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            Drawable icon = item.getIcon();
            if (icon != null) {
                item.setIcon(applyTint(icon));
            }
        }
        return true;
    }

    Drawable applyTint(Drawable icon) {
        icon.setColorFilter(
                new PorterDuffColorFilter(Color.parseColor("#333333"), PorterDuff.Mode.SRC_IN)
        );
        return icon;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.chatbot) {
            startActivity(new Intent(logouthome.this, Chatbot.class), ActivityOptions.makeSceneTransitionAnimation(logouthome.this).toBundle());

        }
        if (id == R.id.translate) {
            new Bottomsheetlanguagefragment().show(getSupportFragmentManager(), "Dialog");

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
        RelativeLayout constraintLayout=findViewById(R.id.swipe);
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

        myIntent = new Intent(logouthome.this, YourLocationBroadcastReciever.class);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent, 0);


        if (set) {

            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() +
                            1 * 1000, pendingIntent);

        } else if (manager != null) {
            manager.cancel(pendingIntent);
        }

        myIntent = new Intent(logouthome.this, MyNotificationBroadcastReceiver.class);
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
        myIntent = new Intent(logouthome.this, HelpneededBroadcastReceiver.class);
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
                    Toast.makeText(logouthome.this, "Permission granted", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(logouthome.this, home.class));
                } else {
                    Toast.makeText(logouthome.this, "Permission denied", Toast.LENGTH_LONG).show();
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
        startActivity(new Intent(logouthome.this, firstresponder.class), ActivityOptions.makeSceneTransitionAnimation(logouthome.this).toBundle());
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
        startActivity(new Intent(logouthome.this, Chatbot.class), ActivityOptions.makeSceneTransitionAnimation(logouthome.this).toBundle());
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

    private boolean isMyServiceRunning(Class<?> serviceClass) {
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

    public void labs(View view) {
        startActivity(new Intent(logouthome.this, Labsfortestingandresults.class), ActivityOptions.makeSceneTransitionAnimation(logouthome.this).toBundle());
    }

    public void msme(View view) {
        startActivity(new Intent(logouthome.this, MSME.class), ActivityOptions.makeSceneTransitionAnimation(logouthome.this).toBundle());

    }

    public void hosnear(View view) {
        Intent intent = new Intent(this, Medicalshops.class);
        intent.putExtra("text", "Hospitals");
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void login(View view) {
        startActivity(new Intent(logouthome.this, login.class), ActivityOptions.makeSceneTransitionAnimation(logouthome.this).toBundle());
        finish();
    }

    public void onlinecourse(View view) {
        startActivity(new Intent(logouthome.this, course.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void sectorworkers(View view) {
        startActivity(new Intent(logouthome.this, unorganizedsectors.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

    }

    public void publiccare(View view) {
        startActivity(new Intent(this, publichealthcare.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

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

    public void callhelpline(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + "1075"));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


}
