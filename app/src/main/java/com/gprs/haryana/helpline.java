package com.gprs.haryana;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

import static com.gprs.haryana.home.whatsapp;


public class helpline extends AppCompatActivity {

    CustomHelplineAdapter c;
    ListView listView;
    ArrayList<String> state, number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_helpline);

        state = new ArrayList<>(Arrays.asList(getString(R.string.andhrapradesh),
                getString(R.string.arunachalpradesh),
                getString(R.string.assam),
                getString(R.string.Bihar),
                getString(R.string.Chhattisgarh),
                getString(R.string.Goa),
                getString(R.string.Gujarat),
                getString(R.string.Haryana),
                getString(R.string.Himachalpradesh),
                getString(R.string.Jharkhand),
                getString(R.string.Karnataka),
                getString(R.string.Kerala),
                getString(R.string.Madhyapradesh),
                getString(R.string.Maharashtra),
                getString(R.string.Manipur),
                getString(R.string.Meghalaya),
                getString(R.string.Mizoram),
                getString(R.string.Nagaland),
                getString(R.string.Odisha),
                getString(R.string.Punjab),
                getString(R.string.Rajasthan),
                getString(R.string.Sikkim),
                getString(R.string.Tamilnadu),
                getString(R.string.Telangana),
                getString(R.string.Tripura),
                getString(R.string.Uttarpradesh),
                getString(R.string.Uttarakhand),
                getString(R.string.Westbengal),
                getString(R.string.AndamanandNicobarIsland),
                getString(R.string.Chandigarh),
                getString(R.string.DadraNagarHaveliDamanandDiu),
                getString(R.string.Delhi),
                getString(R.string.JammuandKashmir),
                getString(R.string.Ladakh),
                getString(R.string.Lakshadweep),
                getString(R.string.Puducherry)));

        number = new ArrayList<>(Arrays.asList("0866-2410978",
                "104",
                "0612-2217681",
                "104",
                "0771-282113",
                "104",
                "079-23251900",
                "0172-2545938",
                "104",
                "104",
                "080-46848600 ",
                "0471-2552056",
                "0755-2411180",
                "022-22024535",
                "1800-345-3818",
                "108",
                "102",
                "0370-2291122",
                "0674-2534177",
                "104",
                "0141-2225000",
                "104",
                "044-29510500",
                "104",
                "0381-2315879",
                "0522-2237515",
                "104",
                "1800-313-444222",
                "03192-232102",
                "0172-2752038",
                "104",
                "011-22307145",
                "0191-2549676",
                "01982-256462",
                "104",
                "104"));

        listView = findViewById(R.id.statelist);
        c = new CustomHelplineAdapter(this, state, number);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                listView.setAdapter(c);
                listView.getLayoutParams().height = 300 * state.size();
            }
        }, 500);
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        findViewById(R.id.translate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Bottomsheetlanguagefragment().show(getSupportFragmentManager(), "Dialog");
            }
        });
        findViewById(R.id.title).setFocusable(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void call1(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + "1075"));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void call2(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + "+91-11-23978043"));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void call3(View view) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "ncov2019@gmail.com", null));
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    public void call4(View view) {
        try {
            whatsapp(helpline.this, "919013151515");
        } catch (IllegalStateException e) {
            Toast.makeText(helpline.this, "You have no whatsapp", Toast.LENGTH_LONG).show();
        }
    }
}
