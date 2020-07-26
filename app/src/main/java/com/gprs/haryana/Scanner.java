package com.gprs.haryana;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONObject;

public class Scanner extends AppCompatActivity {
    CodeScanner codeScanner;
    CodeScannerView scannView;
    String name, title, role, phone, place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_scanner);
        scannView = findViewById(R.id.scannerView);
        codeScanner = new CodeScanner(this, scannView);

        findViewById(R.id.generate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Scanner.this, QRcode.class), ActivityOptions.makeSceneTransitionAnimation(Scanner.this).toBundle());
                finish();
            }
        });
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                        toneGen1.startTone(ToneGenerator.TONE_CDMA_CONFIRM, 150);

                        try {
                            JSONObject object = new JSONObject(result.getText());
                            title = object.optString("status");
                            name = object.optString("name");
                            role = "Role : " + object.optString("role");
                            phone = "Phone : " + object.optString("phone");
                            place = "Place : " + object.optString("place");
                            show();

                        } catch (Exception e) {
                            findViewById(R.id.rellayout).setVisibility(View.VISIBLE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    findViewById(R.id.rellayout).setVisibility(View.INVISIBLE);
                                    codeScanner.startPreview();
                                }
                            }, 3000);
                        }

                    }
                });

            }
        });


        scannView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestForCamera();

    }

    public void requestForCamera() {
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                codeScanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(Scanner.this, "Camera Permission is Required.", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();

            }
        }).check();
    }

    private void show() {
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("phone", phone);
        bundle.putString("place", place);
        bundle.putString("role", role);
        bundle.putString("title", title);

        BottomSheetDialogFragment f = new BottomsheetQRfragment();
        f.setArguments(bundle);
        f.show(getSupportFragmentManager(), "Dialog");
    }


}
