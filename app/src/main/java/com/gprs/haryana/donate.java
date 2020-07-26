package com.gprs.haryana;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class donate extends AppCompatActivity {

    private WebView wview;
    private ProgressDialog progressDialog;
    TextView HaryanaUPI;
    ImageView PM, Haryana;
    String urlgo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_donate);
        wview = findViewById(R.id.webv);
        PM = findViewById(R.id.PM);
        Haryana = findViewById(R.id.Haryana);
        HaryanaUPI = findViewById(R.id.HaryanaUPI);

        PM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://www.pmcares.gov.in/en?should_open_safari=true"));
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(donate.this, "You don't have browser installed", Toast.LENGTH_LONG).show();
                }

            }
        });

        HaryanaUPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UPI();
            }
        });

        Haryana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://www.onlinesbi.com/sbicollect/icollecthome.htm?corpID=1952558"));
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(donate.this, "You don't have browser installed", Toast.LENGTH_LONG).show();
                }

            }
        });
        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("Please Wait !"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner

        progressDialog.setCancelable(true);


        WebSettings wsetting = wview.getSettings();
        wsetting.setJavaScriptEnabled(true);
        wsetting.setSupportZoom(true);
        wsetting.setDomStorageEnabled(true);
        wsetting.setAppCacheEnabled(true);
        String newUA = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1";
        wview.getSettings().setUserAgentString(newUA);


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
                try {
                    webView.stopLoading();
                } catch (Exception e) {
                }

                if (webView.canGoBack()) {
                    webView.goBack();
                }
                progressDialog.hide();
                webView.loadUrl("about:blank");
                AlertDialog alertDialog = new AlertDialog.Builder(donate.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Server not Available ! \nCheck your internet connection and try again.");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Try Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(getIntent());
                    }
                });

                alertDialog.show();
                super.onReceivedError(webView, errorCode, description, failingUrl);
            }


        });


    }

    void load() {
        progressDialog.show();
        PM.setVisibility(View.INVISIBLE);
        Haryana.setVisibility(View.INVISIBLE);
        HaryanaUPI.setVisibility(View.INVISIBLE);
        wview.loadUrl(urlgo);
    }


    @Override
    public void onBackPressed() {
        if (wview.canGoBack()) {
            wview.goBack();
        } else {
            finish();
        }
    }

    public void UPI() {
        Long tsLong = System.currentTimeMillis() / 1000;
        String transaction_ref_id = tsLong.toString() + "UPI"; // This is your Transaction Ref id - Here we used as a timestamp -

        String sOrderId = tsLong + "UPI";// This is your order id - Here we used as a timestamp -

        Log.e("TR Reference ID==>", "" + transaction_ref_id);
        Uri myAction = Uri.parse("upi://pay?pa=" + "hrycoronarelieffund@sbi" + "&pn=" + "payee" + "&mc=" + "&tid=" + transaction_ref_id + "&tr=" + transaction_ref_id + "&tn=Donation%20to%20Haryana%20COVIDRELIEFFUND%20" + "&mam=null&cu=INR");


        PackageManager packageManager = getPackageManager();
        //Intent intent = packageManager.getLaunchIntentForPackage("com.mgs.induspsp"); // Comment line - if you want to open specific application then you can pass that package name For example if you want to open Bhim app then pass Bhim app package name -
        Intent intent = new Intent();

        if (intent != null) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(myAction);
            // startActivity(intent);
            Intent chooser = Intent.createChooser(intent, "Pay with...");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivityForResult(chooser, 1, null);
            }

        }
    }


// For onActivityResult -

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        try {
            Log.e("UPI RESULT REQUEST CODE-->", "" + requestCode);
            Log.e("UPI RESULT RESULT CODE-->", "" + resultCode);
            Log.e("UPI RESULT DATA-->", "" + data);


            if (resultCode == -1) {

                // 200 Success

            } else {
                // 400 Failed
            }


            donate.this.finish();


        } catch (Exception e) {
            Log.e("Error in UPI onActivityResult->", "" + e.getMessage());
        }
    }

}


