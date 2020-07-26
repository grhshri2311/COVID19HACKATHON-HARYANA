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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class help extends AppCompatActivity {

    String name, phone, email, role;
    double lat, lon;

    private WebView wview;
    private ProgressDialog progressDialog;
    Button respond,reject;
    private SharedPreferences pref;
    SharedPreferences.Editor editor;
    UserLocationHelper u;
    float[] res = new float[1];
    TextView name1, phone1, email1, role1, distancce1;
    UserLocationHelper mylocation;
    TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_help);
        pref = getSharedPreferences("user", 0);
        respond = findViewById(R.id.respond);
        reject = findViewById(R.id.reject);
        name1 = findViewById(R.id.name);
        phone1 = findViewById(R.id.phone);
        email1 = findViewById(R.id.email);
        role1 = findViewById(R.id.role);
        distancce1 = findViewById(R.id.distance);
        t = findViewById(R.id.textView32);
        wview = findViewById(R.id.webv);


        FirebaseDatabase.getInstance().getReference().child("Respond").child("Help").child(pref.getString("user", "")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    u = dataSnapshot.getValue(UserLocationHelper.class);

                    name = u.getFname();
                    email = u.getEmail();
                    role = u.getRole();
                    phone = u.getPhone();
                    lat = u.getLat();
                    lon = u.getLon();


                    name1.setText("Name : " + name);
                    phone1.setText("Phone : " + phone + " (Click to call)");
                    email1.setText("Email : " + email + " (Click to mail)");
                    role1.setText("Role : " + role);

                    reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseDatabase.getInstance().getReference().child("Respond").child("Help").child(pref.getString("user", "")).removeValue();
                            alert2();
                        }

                    });

                    respond.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mylocation != null) {
                                FirebaseDatabase.getInstance().getReference().child("Respond").child("Help").child(mylocation.getPhone()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue() != null) {
                                            FirebaseDatabase.getInstance().getReference().child("Respond").child("reply").child(phone).setValue(mylocation).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isComplete()) {
                                                        FirebaseDatabase.getInstance().getReference().child("Respond").child("reply").child(phone).addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.getValue() == null)
                                                                    alert1();
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                            respond.setText("Responding");
                                            reject.setVisibility(View.GONE);
                                            t.setText("Stay Connected !");
                                            t.setTextColor(getResources().getColor(R.color.GREEN));
                                            respond.setBackgroundColor(getResources().getColor(R.color.GREEN));

                                        } else {
                                            t.setText("Aldready got responder ,Thank you for your contribution !");
                                            t.setTextColor(getResources().getColor(R.color.GREEN));
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                        }
                    });
                    phone1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                            Toast.makeText(help.this, "Connecting to victim ...", Toast.LENGTH_LONG).show();
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

                    progressDialog = new ProgressDialog(help.this);

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
                            AlertDialog alertDialog = new AlertDialog.Builder(help.this).create();
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


                } else {
                    t.setText("Already Got help !");
                    t.setTextColor(getResources().getColor(R.color.GREEN));
                    respond.setVisibility(View.INVISIBLE);
                    name1.setVisibility(View.INVISIBLE);
                    phone1.setVisibility(View.INVISIBLE);
                    email1.setVisibility(View.INVISIBLE);
                    role1.setVisibility(View.INVISIBLE);
                    distancce1.setVisibility(View.INVISIBLE);
                    wview.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
        if (phone != null)
            FirebaseDatabase.getInstance().getReference().child("Respond").child("reply").child(phone).removeValue();
    }

    void alert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (phone != null)
                    FirebaseDatabase.getInstance().getReference().child("Respond").child("reply").child(phone).removeValue();
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
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }

    void alert2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Request Rejected !");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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
