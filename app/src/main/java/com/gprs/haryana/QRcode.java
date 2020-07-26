package com.gprs.haryana;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

import static android.graphics.Color.WHITE;

public class QRcode extends AppCompatActivity {

    private TextView title;
    ImageView imageViewBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_q_rcode);

        Button buttonScan = (Button) findViewById(R.id.buttonScan);
        title = (TextView) findViewById(R.id.title);
        imageViewBitmap = findViewById(R.id.qr);


        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  qrScan.initiateScan();
                startActivity(new Intent(getApplicationContext(), Scanner.class));
                finish();
            }
        });


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                GenerateClick();
            }
        }, 1000);
    }

    public void GenerateClick() {
        title.setText("Loading QR Code...");
        try {
            //setting size of qr code
            int width = 800, height = 800;
            int smallestDimension = Math.min(width, height);


            //setting parameters for qr code
            String charset = "UTF-8";
            Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            CreateQRCode(charset, hintMap, smallestDimension, smallestDimension);

        } catch (Exception ex) {
            Log.e("QrGenerate", ex.getMessage());
        }
    }

    public void CreateQRCode(final String charset, final Map hintMap, final int qrCodeheight, final int qrCodewidth) {
        final SharedPreferences pref;
        final SharedPreferences.Editor editor;

        pref = getApplicationContext().getSharedPreferences("user", 0); // 0 - for private mode
        editor = pref.edit();

        FirebaseDatabase.getInstance().getReference().child("Users").child(pref.getString("user", "")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserRegistrationHelper userRegistrationHelper = dataSnapshot.getValue(UserRegistrationHelper.class);
                if (userRegistrationHelper != null) {
                    String qrCodeData;
                    qrCodeData = "{\n" +
                            "   \"name\":" + "\"" + userRegistrationHelper.getFname() + "\"" + ",\n" +
                            "   \"role\":" + "\"" + userRegistrationHelper.getRole() + "\"" + ",\n" +
                            "   \"phone\":" + "\"" + userRegistrationHelper.getPhone() + "\"" + ",\n" +
                            "   \"place\" :" + "\"" + getApplicationContext().getSharedPreferences("user", 0).getString("city", "") + ' ' + getApplicationContext().getSharedPreferences("user", 0).getString("state", "") + "\"" + ",\n" +
                            "   \"status\" :" + "\"" + getApplicationContext().getSharedPreferences("user", 0).getString("status", "") + "\"" + "\n" +
                            "  }";
                    try {
                        //generating qr code.
                        BitMatrix matrix = new MultiFormatWriter().encode(new String(qrCodeData.getBytes(charset), charset),
                                BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
                        //converting bitmatrix to bitmap

                        int width = matrix.getWidth();
                        int height = matrix.getHeight();
                        int[] pixels = new int[width * height];
                        // All are 0, or black, by default
                        for (int y = 0; y < height; y++) {
                            int offset = y * width;
                            for (int x = 0; x < width; x++) {
                                //for black and white
                                //pixels[offset + x] = matrix.get(x, y) ? BLACK : WHITE;
                                //for custom color
                                pixels[offset + x] = matrix.get(x, y) ?
                                        ResourcesCompat.getColor(getResources(), R.color.GREEN, null) : WHITE;
                            }
                        }
                        //creating bitmap
                        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

                        //getting the logo
                        Bitmap overlay = BitmapFactory.decodeResource(getResources(), R.drawable.qricon);
                        //setting bitmap to image view
                        imageViewBitmap.setImageBitmap(mergeBitmaps(overlay, bitmap));
                        title.setText("Scan to know Status");

                    } catch (Exception er) {
                        Log.e("QrGenerate", er.getMessage());
                    }
                } else {
                    title.setText("Check Your Internet Connection");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                title.setText("Check Your Internet Connection");
            }
        });


    }

    public Bitmap mergeBitmaps(Bitmap overlay, Bitmap bitmap) {

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        Bitmap combined = Bitmap.createBitmap(width, height, bitmap.getConfig());
        Canvas canvas = new Canvas(combined);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        canvas.drawBitmap(bitmap, new Matrix(), null);

        int centreX = (canvasWidth - overlay.getWidth()) / 2;
        int centreY = (canvasHeight - overlay.getHeight()) / 2;
        canvas.drawBitmap(overlay, centreX, centreY, null);

        return combined;
    }


}