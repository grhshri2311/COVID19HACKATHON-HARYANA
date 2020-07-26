package com.gprs.haryana;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class visualize extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_visualize);

        WebView wview = findViewById(R.id.webv1);

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("Please Wait !"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner

        progressDialog.setCancelable(false);
        progressDialog.show();

        WebSettings wsetting = wview.getSettings();
        wsetting.setJavaScriptEnabled(true);
        wsetting.setAllowContentAccess(false);
        wsetting.setSupportZoom(true);
        wsetting.setDomStorageEnabled(true);


        wview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressDialog.show(); // Display Progress Dialog

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;

            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                // Do something with the event here
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

                if (webView.canGoBack()) {
                    webView.goBack();
                }

                webView.loadUrl("about:blank");
                AlertDialog alertDialog = new AlertDialog.Builder(visualize.this).create();
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

        wview.loadUrl("https://www.covid19india.org/");


    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
