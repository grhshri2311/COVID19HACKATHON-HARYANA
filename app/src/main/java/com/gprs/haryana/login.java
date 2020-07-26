package com.gprs.haryana;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class login extends AppCompatActivity {

    ImageView icon;
    TextView signin, welcome;
    Button register, go, fp;
    ImageView language;
    TextInputLayout email, password;
    UserRegistrationHelper helper;
    private ProgressDialog progressDialog;

    private SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onStart() {
        super.onStart();


        pref = getApplicationContext().getSharedPreferences("user", 0); // 0 - for private mode

        if (!pref.getString("user", "").equals("")) {
            startActivity(new Intent(login.this, bio.class));
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        pref = getApplicationContext().getSharedPreferences("language", 0); // 0 - for private mode
        editor = pref.edit();

        language = findViewById(R.id.language);
        progressDialog = new ProgressDialog(login.this);
        progressDialog.setTitle("Logging In");
        progressDialog.setMessage("connecting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        register = findViewById(R.id.register);
        icon = findViewById(R.id.icon2);
        signin = findViewById(R.id.signin);
        welcome = findViewById(R.id.welcome);
        go = findViewById(R.id.post);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        fp = findViewById(R.id.fp);


        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Bottomsheetlanguagefragment().show(getSupportFragmentManager(), "Dialog");

            }
        });


        fp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this, forgotpassword.class));
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, register.class);
                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(icon, "logo_image");
                pairs[1] = new Pair<View, String>(welcome, "logo_text");
                pairs[2] = new Pair<View, String>(signin, "logo_text1");
                pairs[3] = new Pair<View, String>(email, "email");
                pairs[4] = new Pair<View, String>(password, "pass");
                pairs[5] = new Pair<View, String>(go, "go");
                pairs[6] = new Pair<View, String>(register, "switch");

                //wrap the call in API level 21 or higher
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(login.this, pairs);
                    startActivity(intent, options.toBundle());
                }
                finish();
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate(email, password)) {
                    progressDialog.show();


                    checkUser(email.getEditText().getText().toString(), password.getEditText().getText().toString());


                }
            }
        });
    }

    private boolean validate(TextInputLayout mobile, TextInputLayout password) {
        if (mobile.getEditText().getText().toString().isEmpty()) {
            mobile.setError("\nMobile cannot be empty\n");
            return false;
        } else {
            mobile.setError("");
            mobile.setErrorEnabled(false);
        }
        if (password.getEditText().getText().toString().isEmpty()) {
            password.setError("\nPassword cannot be empty\n");
            return false;
        } else {
            password.setError("");
            password.setErrorEnabled(false);
        }
        return true;
    }

    void checkUser(final String phone1, final String pass) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference("Users").child(phone1);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserRegistrationHelper helper = dataSnapshot.getValue(UserRegistrationHelper.class);
                if (helper != null) {
                    try {
                        if (helper.getPass().equals(encrypt(pass))) {
                            SharedPreferences pref;
                            SharedPreferences.Editor editor;

                            pref = getApplicationContext().getSharedPreferences("user", 0); // 0 - for private mode
                            editor = pref.edit();

                            editor.putString("user", phone1);
                            editor.apply();

                            startActivity(new Intent(login.this, home.class), ActivityOptions.makeSceneTransitionAnimation(login.this).toBundle());
                            finish();
                        } else {
                            progressDialog.hide();
                            password.setError("Invalid Password");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    progressDialog.hide();
                    email.setError("Mobile number doesn't exists");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                progressDialog.hide();
                Toast.makeText(login.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Exit1();
    }

    public void Exit1() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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

    private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";

    public static String encrypt(String value) throws Exception {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedByteValue = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
        String encryptedValue64 = Base64.encodeToString(encryptedByteValue, Base64.DEFAULT);
        return encryptedValue64;

    }

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        return key;
    }


}
