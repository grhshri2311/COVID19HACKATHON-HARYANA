package com.gprs.haryana;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class register extends AppCompatActivity {

    AlertDialog alert;
    ImageView icon;
    TextView signin, welcome;
    Button login;
    Button go;
    static Button role;
    TextInputLayout email;
    TextInputLayout password;
    TextInputLayout mobile;
    TextInputLayout name;
    static TextInputLayout roleL;
    DatabaseReference reference;
    FirebaseDatabase database;
    String email1, password1, phone1, name1;
    ProgressDialog progressDialog;
    private FusedLocationProviderClient fusedLocationClient;
    Location location;
    boolean loc = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        progressDialog = new ProgressDialog(register.this);
        progressDialog.setTitle("Registering");
        progressDialog.setMessage("connecting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        role = findViewById(R.id.role);
        mobile = findViewById(R.id.mobile);
        name = findViewById(R.id.fname);
        login = findViewById(R.id.login);
        icon = findViewById(R.id.icon1);
        signin = findViewById(R.id.signin1);
        welcome = findViewById(R.id.welcome1);
        go = findViewById(R.id.go1);
        email = findViewById(R.id.email1);
        password = findViewById(R.id.password1);
        roleL = findViewById(R.id.roleLayout);

        if (ContextCompat.checkSelfPermission(register.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(register.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(register.this, login.class);
                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(icon, "logo_image");
                pairs[1] = new Pair<View, String>(welcome, "logo_text");
                pairs[2] = new Pair<View, String>(signin, "logo_text1");
                pairs[3] = new Pair<View, String>(email, "email");
                pairs[4] = new Pair<View, String>(password, "pass");
                pairs[5] = new Pair<View, String>(go, "go");
                pairs[6] = new Pair<View, String>(login, "switch");

                //wrap the call in API level 21 or higher
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(register.this, pairs);
                    startActivity(intent, options.toBundle());
                }
                finish();
            }
        });

        role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu rolemenu = new PopupMenu(getApplicationContext(), role);
                rolemenu.getMenuInflater().inflate(R.menu.role, rolemenu.getMenu());
                rolemenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        role.setText(item.getTitle().toString());
                        return true;
                    }
                });
                rolemenu.show();
            }
        });
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate(name, email, mobile, password)) {
                    name1 = name.getEditText().getText().toString();
                    email1 = email.getEditText().getText().toString();
                    phone1 = mobile.getEditText().getText().toString();
                    password1 = password.getEditText().getText().toString();
                    progressDialog.show();
                    verifyemail();
                }
            }
        });

    }


    public boolean validate(TextInputLayout name1, TextInputLayout email1, final TextInputLayout phone1, TextInputLayout password1) {
        String emailPatter = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String passwordPatter = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})";

        if (role.getText().toString().equals(getString(R.string.select_your_role))) {
            roleL.setError("\nRole cannot be empty\n");
            return false;
        } else {
            roleL.setError("");
            roleL.setErrorEnabled(false);
        }

        if (name1.getEditText().getText().toString().isEmpty()) {
            name1.setError("\nName cannot be empty\n");
            return false;
        } else {
            name1.setError("");
            name1.setErrorEnabled(false);
        }


        if (!email1.getEditText().getText().toString().matches(emailPatter) && !email1.getEditText().getText().toString().isEmpty()) {
            email1.setError("\nEnter a valid email\n");
            return false;
        } else {
            email1.setError("");
            email1.setErrorEnabled(false);
        }

        if (password1.getEditText().getText().toString().isEmpty()) {
            password1.setError("\nPassword cannot be empty\n");
            return false;
        } else if (!password1.getEditText().getText().toString().matches(passwordPatter)) {
            password1.setError("\nPassword Must contain atleast\nOne Uppercase ,\nOne Lowercase ,\nOne Number ,\nOne Special character and \nBetween 8 to 16 letter length\n");
            return false;
        } else {
            password1.setError("");
            password1.setErrorEnabled(false);
        }
        if (phone1.getEditText().getText().toString().isEmpty()) {
            phone1.setError("\nMobile cannot be empty\n");
            return false;
        } else {
            phone1.setError("");
            phone1.setErrorEnabled(false);
        }


        return true;
    }


    void checkUser() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference("Users").child(phone1);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserRegistrationHelper helper = dataSnapshot.getValue(UserRegistrationHelper.class);
                if (helper != null) {
                    progressDialog.hide();
                    mobile.setError("Mobile number aldready exists");

                } else {

                    Bundle bundle = new Bundle();
                    bundle.putString("name", name1);
                    bundle.putString("phone", phone1);
                    bundle.putString("email", email1);
                    bundle.putString("password", password1);
                    bundle.putString("role", role.getText().toString());
                    bundle.putDouble("lat", location.getLatitude());
                    bundle.putDouble("lon", location.getLongitude());

                    BottomSheetDialogFragment f = new Bottomsheetregisterfragment();
                    f.setArguments(bundle);
                    f.show(getSupportFragmentManager(), "Dialog");
                    progressDialog.hide();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                progressDialog.hide();
                Toast.makeText(register.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    void verifyemail() {

        if (ContextCompat.checkSelfPermission(register.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(register.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(register.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                ActivityCompat.requestPermissions(register.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        } else if (loc) {

            fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null) {
                            loc = false;
                            location = task.getResult();
                            verifyemail();
                        } else {
                            progressDialog.hide();
                            Toast.makeText(register.this, "Please turn on GPS and accept location permission", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        progressDialog.hide();
                        Toast.makeText(register.this, "No location data found on this device", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            checkUser();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(register.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(register.this, login.class));
                    finish();

                }
            }
        }
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

}
