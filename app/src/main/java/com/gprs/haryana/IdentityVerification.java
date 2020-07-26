package com.gprs.haryana;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.net.URL;

public class IdentityVerification extends AppCompatActivity {

    TextView title, status;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_identity_verification);
        title = findViewById(R.id.title);
        status = findViewById(R.id.status);
        imageView = findViewById(R.id.statusimg);
        ImageView logo = findViewById(R.id.logo);
        new IdentityVerification.DownloadImageTask(logo)
                .execute("https://dynamicbusiness.com.au/wp-content/uploads/2017/11/IDCheck.png");

        final SharedPreferences pref = getSharedPreferences("user", 0);
        FirebaseDatabase.getInstance().getReference().child("IdentityVerification").child(pref.getString("user", "")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    IdentityVerificationHelper i = dataSnapshot.getValue(IdentityVerificationHelper.class);
                    if (i.getStatus().equals("accepted")) {
                        title.setText("Form Approved");
                        status.setText("Verification Success");
                        findViewById(R.id.accepted).setVisibility(View.VISIBLE);
                        new IdentityVerification.DownloadImageTask(imageView)
                                .execute("https://titlexchange.com.au/wp-content/uploads/2017/07/identity-verification.jpg");
                        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        });
                    }
                    if (i.getStatus().equals("pending")) {
                        title.setText("Form submitted");
                        status.setText("Verification pending");
                        findViewById(R.id.pending).setVisibility(View.VISIBLE);
                        new IdentityVerification.DownloadImageTask(imageView)
                                .execute("https://www.learnpedia.in/public/outerAssets/images/error-icon.png");
                    } else if (i.getStatus().equals("rejected")) {
                        title.setText("Form Rejected");
                        status.setText("Verification failed");
                        new IdentityVerification.DownloadImageTask(imageView)
                                .execute("https://www.biospectrumasia.com/uploads/articles/rejected-9105.jpg");
                        findViewById(R.id.rejected).setVisibility(View.VISIBLE);
                        TextView comment = findViewById(R.id.comment);
                        comment.setText(i.getComment());
                        findViewById(R.id.btnreSend).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FirebaseDatabase.getInstance().getReference().child("IdentityVerification").child(pref.getString("user", "")).removeValue();
                                startActivityForResult(new Intent(IdentityVerification.this, IdentityRegistrationForm.class), 0);
                            }
                        });
                    }
                } else {

                    title.setText("Not verified");
                    status.setText("Not submitted");
                    new IdentityVerification.DownloadImageTask(imageView)
                            .execute("https://www.learnpedia.in/public/outerAssets/images/error-icon.png");
                    findViewById(R.id.notsubmitted).setVisibility(View.VISIBLE);
                    findViewById(R.id.btnSend).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivityForResult(new Intent(IdentityVerification.this, IdentityRegistrationForm.class), 0);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
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
            bmImage.setImageBitmap(result);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}