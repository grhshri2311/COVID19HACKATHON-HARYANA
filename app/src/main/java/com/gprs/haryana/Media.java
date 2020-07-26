package com.gprs.haryana;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.URL;

public class Media extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_media);
        ImageView covid19, symptoms, howspread, prevention, advice1, advice2, advice3, advice4, advice5, protectdo, protectdont, dontspread;
        covid19 = findViewById(R.id.covid19image);
        symptoms = findViewById(R.id.symptoms);
        howspread = findViewById(R.id.howspread);
        prevention = findViewById(R.id.prevention);
        advice1 = findViewById(R.id.advice1);
        advice2 = findViewById(R.id.advice2);
        advice3 = findViewById(R.id.advice3);
        advice4 = findViewById(R.id.advice4);
        advice5 = findViewById(R.id.advice5);
        protectdo = findViewById(R.id.protectdo);
        protectdont = findViewById(R.id.protectdont);
        dontspread = findViewById(R.id.dontspread);

        new Media.SetImage(covid19).execute("https://www.mygov.in/sites/all/themes/mygov/images/covid/covid-share.jpg");
        new Media.SetImage(symptoms).execute("https://www.mygov.in/sites/all/themes/mygov/images/covid/symptoms.jpg");
        new Media.SetImage(howspread).execute("https://www.mygov.in/sites/all/themes/mygov/images/covid/block-one.jpg");
        new Media.SetImage(prevention).execute("https://www.mygov.in/sites/all/themes/mygov/images/covid/block-two.jpg");
        new Media.SetImage(advice1).execute("https://img.youtube.com/vi/K3BqiNq9Xf0/0.jpg");
        advice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Media.this, youtube.class);
                intent.putExtra("ID", "K3BqiNq9Xf0");
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(Media.this).toBundle());

            }
        });
        new Media.SetImage(advice2).execute("https://img.youtube.com/vi/pvG4OiwoR2U/0.jpg");
        advice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Media.this, youtube.class);
                intent.putExtra("ID", "pvG4OiwoR2U");
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(Media.this).toBundle());

            }
        });
        new Media.SetImage(advice3).execute("https://img.youtube.com/vi/CW0H4F8p6hg/0.jpg");
        advice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Media.this, youtube.class);
                intent.putExtra("ID", "CW0H4F8p6hg");
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(Media.this).toBundle());

            }
        });
        new Media.SetImage(advice4).execute("https://img.youtube.com/vi/EJbjyo2xa2o/0.jpg");
        advice4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Media.this, youtube.class);
                intent.putExtra("ID", "EJbjyo2xa2o");
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(Media.this).toBundle());

            }
        });
        new Media.SetImage(advice5).execute("https://img.youtube.com/vi/T25YJ4RJunA/0.jpg");
        advice5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Media.this, youtube.class);
                intent.putExtra("ID", "T25YJ4RJunA");
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(Media.this).toBundle());

            }
        });
        new Media.SetImage(protectdo).execute("https://i.pinimg.com/originals/ba/7d/86/ba7d867a425213f1abb83de22fc5be0a.jpg");
        new Media.SetImage(protectdont).execute("https://www.reviewsontop.com/wp-content/uploads/2020/03/dos-and-donts.png");
        new Media.SetImage(dontspread).execute("https://venngage-wordpress.s3.amazonaws.com/uploads/2020/02/America-Disease-Outbreaks-Infographic-Fact-Sheet.png");

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
}