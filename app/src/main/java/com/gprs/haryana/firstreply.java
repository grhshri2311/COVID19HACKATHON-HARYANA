package com.gprs.haryana;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class firstreply extends AppCompatActivity {

    String name, phone, email, role;
    double lat, lon;

    private WebView wview;
    private ProgressDialog progressDialog;
    Button respond;
    SharedPreferences.Editor editor;
    float[] res = new float[1];
    TextView name1, phone1, email1, role1, distancce1;
    UserLocationHelper mylocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_firstreply);
        SharedPreferences pref = getSharedPreferences("user", 0);

        Intent intent = getIntent();
        respond = findViewById(R.id.respond);
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        role = intent.getStringExtra("role");
        phone = intent.getStringExtra("phone");
        lat = intent.getDoubleExtra("lat", 0);
        lon = intent.getDoubleExtra("lon", 0);
        name1 = findViewById(R.id.name);
        phone1 = findViewById(R.id.phone);
        email1 = findViewById(R.id.email);
        role1 = findViewById(R.id.role);
        distancce1 = findViewById(R.id.distance);

        name1.setText("Name : " + name);
        phone1.setText("Phone : " + phone + " (Click to call)");
        email1.setText("Email : " + email + " (Click to mail)");
        role1.setText("Role : " + role);


        phone1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                Toast.makeText(firstreply.this, "Connecting to Responder ...", Toast.LENGTH_LONG).show();
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
                startActivity(intent1);

            }
        });

        email1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", email, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "First Responder");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });


        FirebaseDatabase.getInstance().getReference().child("Users").child(pref.getString("user", "")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    mylocation = dataSnapshot.getValue(UserLocationHelper.class);
                    FirebaseDatabase.getInstance().getReference().child("Respond").child("reply").child(mylocation.getPhone()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() == null)
                                alert1();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    Location.distanceBetween(mylocation.getLat(), mylocation.getLon(),
                            lat, lon, res);
                    distancce1.setText(res[0] / 1000 + " KM");
                    String url = "https://maps.google.com/?q=" + lat + "," + lon;
                    wview.loadUrl(url);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        wview = findViewById(R.id.webv);

        wview.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("Please Wait !"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner

        progressDialog.setCancelable(true);
        progressDialog.show();

        WebSettings wsetting = wview.getSettings();
        wsetting.setJavaScriptEnabled(true);
        wsetting.setAllowContentAccess(false);
        wsetting.setSupportZoom(true);


        wview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressDialog.show(); // Display Progress Dialog

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                progressDialog.show(); // Display Progress Dialog
                view.loadUrl(url);

                return true;

            }

            @Override
            public void onPageFinished(WebView view, String url) {

                super.onPageFinished(view, url);
                progressDialog.hide();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                progressDialog.hide();
                handler.proceed(); // Ignore SSL certificate errors
            }

            public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
                progressDialog.hide();
                try {
                    webView.stopLoading();
                } catch (Exception e) {
                }


                webView.loadUrl("about:blank");
                AlertDialog alertDialog = new AlertDialog.Builder(firstreply.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Server not Available ! \nCheck your internet connection and try again.");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Try Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                alertDialog.show();
                super.onReceivedError(webView, errorCode, description, failingUrl);
            }


        });


    }

    @Override
    public void onBackPressed() {
        alert();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseDatabase.getInstance().getReference().child("Respond").child("reply").child(mylocation.getPhone()).removeValue();
    }

    void alert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseDatabase.getInstance().getReference().child("Respond").child("reply").child(mylocation.getPhone()).removeValue();
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    void alert1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Communication Disconnected !");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }

}
