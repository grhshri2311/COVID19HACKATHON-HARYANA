package com.gprs.haryana;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class forgotpassword extends AppCompatActivity {
    TextInputLayout pass;
    TextInputLayout cpass;
    TextInputLayout mobile;
    Button reset, login;
    private ProgressDialog progressDialog;
    private String verificationCodeBySystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgotpassword);

        mobile = findViewById(R.id.email123);
        pass = findViewById(R.id.password123);
        cpass = findViewById(R.id.mobile123);
        login = findViewById(R.id.login123);
        reset = findViewById(R.id.go123);

        progressDialog = new ProgressDialog(forgotpassword.this);
        progressDialog.setTitle("Logging In");
        progressDialog.setMessage("connecting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(forgotpassword.this, login.class));
                finish();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate(mobile, pass, cpass)) {
                    progressDialog.show();
                    change();

                }
            }
        });


    }

    boolean valid = false;

    void change() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference("Users").child(mobile.getEditText().getText().toString());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserRegistrationHelper helper = dataSnapshot.getValue(UserRegistrationHelper.class);
                if (helper != null) {
                    if (valid) {
                        try {
                            helper.setPass(encrypt(pass.getEditText().getText().toString()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        reference.setValue(helper);
                        progressDialog.hide();
                        Toast.makeText(forgotpassword.this, "Password Changed", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(forgotpassword.this, login.class));
                        finish();

                    } else
                        validatemobile();
                } else {
                    progressDialog.hide();
                    mobile.setError("Mobile number doesn't exists");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                progressDialog.hide();
                Toast.makeText(forgotpassword.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public static boolean validate(TextInputLayout mobile, TextInputLayout pass, final TextInputLayout cpass) {
        String emailPatter = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String passwordPatter = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})";

        if (mobile.getEditText().getText().toString().isEmpty()) {
            mobile.setError("\nMobile cannot be empty\n");
            return false;
        } else {
            mobile.setError("");
            mobile.setErrorEnabled(false);
        }


        if (pass.getEditText().getText().toString().isEmpty()) {
            pass.setError("\nPassword cannot be empty\n");
            return false;
        } else if (!pass.getEditText().getText().toString().matches(passwordPatter)) {
            pass.setError("\nPassword Must contain atleast\nOne Uppercase ,\nOne Lowercase ,\nOne Number ,\nOne Special character and \nBetween 8 to 16 letter length\n");
            return false;
        } else {
            pass.setError("");
            pass.setErrorEnabled(false);
        }
        if (!pass.getEditText().getText().toString().equals(cpass.getEditText().getText().toString())) {
            cpass.setError("\nPassword Mismatched\n");
            return false;
        } else {
            cpass.setError("");
            cpass.setErrorEnabled(false);
        }


        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, login.class));
        finish();
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

    private void validatemobile() {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91 " + mobile.getEditText().getText().toString(),        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);


        verifymobile();
    }

    private void verifymobile() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Verify your Mobile number ");

        LinearLayout lila1 = new LinearLayout(this);
        lila1.setOrientation(LinearLayout.VERTICAL); //1 is for vertical orientation
        final EditText input = new EditText(this);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        input.setLayoutParams(p);
        p.setMargins(100, 100, 70, 0);
        input.setHint("Enter OTP");
        lila1.addView(input);


        builder.setPositiveButton("Verify", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                if (input.getText().toString().equals("")) {
                    Toast.makeText(forgotpassword.this, "Enter valid OTP", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    verifymobile();

                } else {
                    progressDialog.show();
                    dialog.dismiss();
                    verifycode(input.getText().toString());
                }
            }
        });
        builder.setNegativeButton("Change number", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.hide();
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.setView(lila1);
        alert.show();

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

         /*   String code=phoneAuthCredential.getSmsCode();
            if(code!=null){
                verifycode(code);
            }

          */
            progressDialog.hide();
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(forgotpassword.this, "Authentication failed" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void verifycode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, code);
        signinwithcredentials(credential);
    }

    private void signinwithcredentials(final PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(forgotpassword.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    valid = true;
                    change();
                } else
                    Toast.makeText(forgotpassword.this, "Incorrect OTP", Toast.LENGTH_LONG).show();
                verifymobile();
            }
        });
    }
}
