package com.gprs.haryana;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Isolation extends AppCompatActivity implements OnMapReadyCallback {

    AlertDialog alert;
    ViewPager viewPager;
    Adapterviewer1 adapter;
    List<Modelviewer> models;
    Integer[] colors = null;
    TextView viewmore, viewmore1;
    MapView mapView;
    private UiSettings mUiSettings;
    private GoogleMap mMap;


    ArrayList<String> fname, phone, type, datetime, image;
    ArrayList<Double> lat, lon;

    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_isolation);

        viewmore = findViewById(R.id.viewmore);
        viewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Isolation.this, pdfViewer.class);
                intent.putExtra("text", "https://drive.google.com/file/d/1OyzDh6IXDVn4DIpcVwL3SnhAy2y6fHYx/view?usp=sharing");
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(Isolation.this).toBundle());
            }
        });

        viewmore1 = findViewById(R.id.viewmore1);
        viewmore1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Isolation.this, pdfViewer.class);
                intent.putExtra("text", "https://www.mohfw.gov.in/pdf/RevisedguidelinesforHomeIsolationofverymildpresymptomaticCOVID19cases10May2020.pdf");
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(Isolation.this).toBundle());
            }
        });


        final ScrollView sv = findViewById(R.id.sv);


        final Button needhelp = findViewById(R.id.btnNeedHelp);
        final SharedPreferences pref = getSharedPreferences("user", 0);
        FirebaseDatabase.getInstance().getReference().child("Isolation").child("needhelp").child(pref.getString("user", "")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    needhelp.setText(R.string.helprequest);
                    needhelp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog alertDialog = new AlertDialog.Builder(Isolation.this).create();
                            alertDialog.setTitle("Confirm");
                            alertDialog.setMessage("Remove material request");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            FirebaseDatabase.getInstance().getReference().child("Isolation").child("needhelp").child(pref.getString("user", "")).removeValue();
                                            Toast.makeText(Isolation.this, "Request removed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                    });
                } else {
                    needhelp.setText(R.string.material_request);
                    needhelp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new BottomsheetIsolationHelpneedfragment().show(getSupportFragmentManager(), "Dialog");
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        findViewById(R.id.dohelp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(Isolation.this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
                builder.setCancelable(false);

                LayoutInflater inflater = getLayoutInflater();
                final View view = inflater.inflate(R.layout.fragment_orphanhelpview, null, true);

                mapView = (MapView) view.findViewById(R.id.map);
                mapView.onCreate(savedInstanceState);
                mapView.onResume();
                mapView.getMapAsync(Isolation.this);


                view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.hide();
                    }
                });
                alert = builder.create();
                alert.setView(view);
                alert.show();

            }
        });


        models = new ArrayList<>();
        models.add(new Modelviewer("https://www.who.int/images/default-source/wpro/health-topic/covid-19/slide2dafa71754b7f4b0a839aa0b1e1778ef8.jpg", "", ""));
        models.add(new Modelviewer("https://www.who.int/images/default-source/wpro/health-topic/covid-19/slide3a4c37eccf1af40e79a65657c29a2559b.jpg", "", ""));
        models.add(new Modelviewer("https://www.who.int/images/default-source/wpro/health-topic/covid-19/slide46968ed41452248eb988d17326fdf17af.jpg", "", ""));


        adapter = new Adapterviewer1(models, this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(10, 0, 50, 0);

        Integer[] colors_temp = {getResources().getColor(R.color.white)};

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
        new CountDownTimer(5000, 5000) {
            public void onTick(long l) {
            }

            public void onFinish() {
                viewPager.setCurrentItem((viewPager.getCurrentItem() + 1) % models.size());
                start();
            }
        }.start();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        fname = new ArrayList<>();
        phone = new ArrayList<>();
        type = new ArrayList<>();
        datetime = new ArrayList<>();
        image = new ArrayList<>();
        lat = new ArrayList<>();
        lon = new ArrayList<>();


        mUiSettings = mMap.getUiSettings();

        // Keep the UI Settings state in sync with the checkboxes.
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(Isolation.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Isolation.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mUiSettings.setScrollGesturesEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setTiltGesturesEnabled(true);
        mUiSettings.setRotateGesturesEnabled(true);


        FirebaseDatabase.getInstance().getReference().child("Isolation").child("needhelp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    mMap.clear();
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        MaterialCollectionHelper m = d.getValue(MaterialCollectionHelper.class);
                        if (m != null) {
                            fname.add(m.getFname());
                            phone.add(m.getPhone());
                            datetime.add(m.getDatetime());
                            type.add(m.getType());
                            image.add(m.getImage());
                            lat.add(m.getLat());
                            lon.add(m.getLon());
                            LatLng latLng = new LatLng(m.getLat(), m.getLon());
                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title(m.getFname())
                                    .snippet(m.getType())
                                    .icon(bitmapDescriptorFromVector(Isolation.this, R.drawable.ic_place_black_24dp)));
                            marker.showInfoWindow();
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            CustomInfoWindow customInfoWindow = new CustomInfoWindow(Isolation.this);
                            mMap.setInfoWindowAdapter(customInfoWindow);
                            marker.showInfoWindow();
                            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                @Override
                                public void onInfoWindowClick(Marker marker) {
                                    view(marker.getTitle());
                                }
                            });
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void view(String id) {

        int i;
        for (i = 0; i < fname.size(); i++) {
            if (fname.get(i).equals(id))
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putString("name", fname.get(i));
        bundle.putString("phone", phone.get(i));
        bundle.putString("type", type.get(i));
        bundle.putString("image", image.get(i));
        bundle.putString("time", datetime.get(i));
        bundle.putDouble("lat", lat.get(i));
        bundle.putDouble("lon", lon.get(i));

        BottomSheetDialogFragment f = new Bottomsheetmaterialviewfragment();
        f.setArguments(bundle);
        f.show(getSupportFragmentManager(), "Dialog");


    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}