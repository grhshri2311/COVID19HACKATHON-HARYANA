package com.gprs.haryana;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class firstresponder extends AppCompatActivity {

    ViewPager viewPager;
    Adapterviewer adapter;
    List<Modelviewer> models;
    Integer[] colors = null;
    AlertDialog alert;
    ArrayList<UserLocationHelper> arrayList;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    UserLocationHelper mylocation;
    TextView scan;
    int num = 0;
    boolean search = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_firstresponder);
        pref = getSharedPreferences("user", 0); //


        FirebaseDatabase.getInstance().getReference().child("Users").child(pref.getString("user", "")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    mylocation = dataSnapshot.getValue(UserLocationHelper.class);
                    if (mylocation != null)
                        get();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        arrayList = new ArrayList<>();
        models = new ArrayList<>();
        models.add(new Modelviewer("https://ruppels.net/wp-content/uploads/2020/03/first-responders_who-are-first-responders-1024x522.png", "Step 1", ""));
        models.add(new Modelviewer("https://m.hindustantimes.com/rf/image_size_444x250/HT/p2/2018/01/27/Pictures/_d580858c-0378-11e8-8651-33050e64100a.jpg", "Step 2", ""));
        models.add(new Modelviewer("https://www.wearable-technologies.com/wp-content/uploads/2019/01/First-responder-wearables-1.png", "Step 3", ""));
        models.add(new Modelviewer("https://www.ifrc.org/Global/Photos/Secretariat/201409/20140902-first-aid-Main-1.jpg", "Step 4", ""));


        adapter = new Adapterviewer(models, this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(10, 0, 50, 0);


        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe);
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
        new CountDownTimer(5000, 5000) {
            public void onTick(long l) {
            }

            public void onFinish() {
                viewPager.setCurrentItem((viewPager.getCurrentItem() + 1) % models.size());
                start();
            }
        }.start();

    }

    public void fire(View view) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "101"));
        Toast.makeText(firstresponder.this, "Connecting to Fire Service ...", Toast.LENGTH_LONG).show();
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }

    public void ambulance(View view) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "108"));
        Toast.makeText(firstresponder.this, "Connecting to Ambulance ...", Toast.LENGTH_LONG).show();
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }

    public void police(View view) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "100"));
        Toast.makeText(firstresponder.this, "Connecting to Police ...", Toast.LENGTH_LONG).show();
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }

    public void hospital(View view) {
        Intent intent = new Intent(this, Medicalshops.class);
        intent.putExtra("text", "Hospitals");
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(firstresponder.this).toBundle());
    }

    public void pharmachy(View view) {
        Intent intent = new Intent(this, Medicalshops.class);
        intent.putExtra("text", "Pharmacies");
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(firstresponder.this).toBundle());
    }

    public void firstaid(View view) {
        startActivity(new Intent(this, firstaidguidance.class), ActivityOptions.makeSceneTransitionAnimation(firstresponder.this).toBundle());
    }


    public void helpline(View view) {
        startActivity(new Intent(this, helpline.class), ActivityOptions.makeSceneTransitionAnimation(firstresponder.this).toBundle());
    }


    public void alert(View view) {
        if (mylocation != null)
            show();
    }

    private void show() {
        num = 0;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Searching for Users");

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.finduser, null, true);


        Button stop = view.findViewById(R.id.stop);
        scan = view.findViewById(R.id.scan);


        new CountDownTimer(5000, 5000) {
            public void onTick(long l) {
            }

            public void onFinish() {
                int i = 5;
                while (num < arrayList.size() && i >= 0 && num >= 0) {
                    i = i - 1;
                    if (num >= 0) {
                        FirebaseDatabase.getInstance().getReference().child("Respond").child("Help").child(arrayList.get(num).getPhone()).setValue(mylocation);
                        num = num + 1;
                    } else
                        break;

                }
                if (num >= 0) {
                    float[] res = new float[1];
                    try {
                        Location.distanceBetween(mylocation.getLat(), mylocation.getLon(),
                                arrayList.get(num - 1).getLat(), arrayList.get(num - 1).getLon(), res);
                        scan.setText("Scanning within " + res[0] / 1000 + " KM radius...");
                        if (num < arrayList.size() && search)
                            start();
                    } catch (ArrayIndexOutOfBoundsException e) {

                    }
                }

            }
        }.start();

        FirebaseDatabase.getInstance().getReference().child("Respond").child("reply").child(mylocation.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    search = false;
                    alert.hide();
                    num = num - 1;
                    while (num >= 0) {
                        FirebaseDatabase.getInstance().getReference().child("Respond").child("Help").child(arrayList.get(num).getPhone()).removeValue();
                        num = num - 1;
                    }
                    UserLocationHelper userLocationHelper = dataSnapshot.getValue(UserLocationHelper.class);
                    Intent intent = new Intent(firstresponder.this, firstreply.class);
                    intent.putExtra("name", userLocationHelper.getFname());
                    intent.putExtra("role", userLocationHelper.getRole());
                    intent.putExtra("lat", userLocationHelper.getLat());
                    intent.putExtra("lon", userLocationHelper.getLon());
                    intent.putExtra("phone", userLocationHelper.getPhone());
                    intent.putExtra("email", userLocationHelper.getEmail());
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(firstresponder.this).toBundle());
                    finish();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = num - 1;
                while (num >= 0) {
                    FirebaseDatabase.getInstance().getReference().child("Respond").child("Help").child(arrayList.get(num).getPhone()).removeValue();
                    num = num - 1;
                }
                alert.hide();
            }
        });


        alert = builder.create();
        alert.setView(view);
        alert.show();

    }

    void get() {

        FirebaseDatabase.getInstance().getReference().child("Location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        UserLocationHelper userLocationHelper1 = data.getValue(UserLocationHelper.class);
                        if (!mylocation.getPhone().equals(userLocationHelper1.getPhone()))
                            arrayList.add(userLocationHelper1);
                    }
                    Collections.sort(arrayList, new sortarray());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    class sortarray implements Comparator<UserLocationHelper> {

        float[] res = new float[1];
        float[] res1 = new float[1];

        @Override
        public int compare(UserLocationHelper o1, UserLocationHelper o2) {
            Location.distanceBetween(mylocation.getLat(), mylocation.getLon(),
                    o1.getLat(), o1.getLon(), res);
            Location.distanceBetween(mylocation.getLat(), mylocation.getLon(),
                    o2.getLat(), o2.getLon(), res1);
            return (int) (res[0] - res1[0]);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        num = num - 1;
        while (num >= 0) {
            FirebaseDatabase.getInstance().getReference().child("Respond").child("Help").child(arrayList.get(num).getPhone()).removeValue();
            num = num - 1;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        num = num - 1;
        while (num >= 0) {
            FirebaseDatabase.getInstance().getReference().child("Respond").child("Help").child(arrayList.get(num).getPhone()).removeValue();
            num = num - 1;
        }
    }
}
