package com.gprs.haryana;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUriExposedException;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class IdentityRegistrationForm extends AppCompatActivity {


    Spinner role;
    ArrayList<String> arrayList;
    Button upload, send, profile;
    private String Document_img1 = "";
    private ImageView IDProf, photo;
    Bitmap image, image1;
    SharedPreferences pref;
    TextInputLayout name, email, rolelayout, address;
    RadioGroup radioGroup;
    EditText dob;
    Boolean photouploaded = true, profileuploaded = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_identity_registrationform);
        arrayList = new ArrayList<>();

        upload = findViewById(R.id.upload);
        IDProf = findViewById(R.id.proof);
        send = findViewById(R.id.btnSend);
        photo = findViewById(R.id.photo);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate()) {
                    send.setBackgroundColor(Color.parseColor("#9CD3FF"));
                    send.setEnabled(false);

                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    RadioButton radio = findViewById(selectedId);
                    EditText mobile = findViewById(R.id.mobile);
                    IdentityVerificationHelper i = new IdentityVerificationHelper(name.getEditText().getText().toString(), mobile.getText().toString(), email.getEditText().getText().toString(), role.getSelectedItem().toString(), address.getEditText().getText().toString(), dob.getText().toString(), BitMapToString(image1), BitMapToString(image), radio.getText().toString(), "pending", "");

                    FirebaseDatabase.getInstance().getReference().child("IdentityVerification").child(pref.getString("user", "")).setValue(i).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(IdentityRegistrationForm.this, "Form submitted Successfully !", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(IdentityRegistrationForm.this, "Error submitting form ,Try again later", Toast.LENGTH_LONG).show();
                                send.setBackground(getDrawable(R.drawable.centre_button));
                                send.setEnabled(true);
                            }
                        }
                    });
                }
            }
        });

        arrayList.add(getString(R.string.select_your_role));
        arrayList.add(getString(R.string.volunteers));
        arrayList.add(getString(R.string.healthworker));
        arrayList.add(getString(R.string.counsellor));
        arrayList.add(getString(R.string.sectorworkers));
        arrayList.add(getString(R.string.labour));
        arrayList.add(getString(R.string.stranded_migrant));
        arrayList.add(getString(R.string.orphan));

        radioGroup = findViewById(R.id.radioGroup);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        rolelayout = findViewById(R.id.rolelayout);
        address = findViewById(R.id.address);
        profile = findViewById(R.id.profile);

        pref = getSharedPreferences("user", 0);

        EditText mobile = findViewById(R.id.mobile);
        mobile.setText(pref.getString("user", ""));
        mobile.setEnabled(false);


        role = findViewById(R.id.role);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, R.id.txt_bundle, arrayList);

        role.setAdapter(dataAdapter);
        role.setSelection(0, false);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(true);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(false);
            }
        });

        final Calendar myCalendar = Calendar.getInstance();

        dob = findViewById(R.id.dob);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dob.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
            }

        };

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(IdentityRegistrationForm.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }


    Boolean camera = false;

    public void requestForCamera(final Boolean profile) {
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                camera = true;
                selectImage(profile);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(IdentityRegistrationForm.this, "Camera Permission is Required.", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();

            }
        }).check();
    }

    private void selectImage(final Boolean profile) {
        if (camera) {
            final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Photo!");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Take Photo") && profile) {
                        try {
                            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (i.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(i, 1);
                            }
                        } catch (FileUriExposedException e) {

                        }
                    } else if (options[item].equals("Choose from Gallery") && profile) {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    } else if (options[item].equals("Take Photo")) {
                        try {
                            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (i.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(i, 3);
                            }
                        } catch (FileUriExposedException e) {

                        }
                    } else if (options[item].equals("Choose from Gallery")) {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 4);
                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        } else {
            requestForCamera(profile);
        }
    }

    private Bundle b;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                b = data.getExtras();
                image = (Bitmap) b.get("data");
                IDProf.setImageBitmap(image);
                photouploaded = false;
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                image = (BitmapFactory.decodeFile(picturePath));
                image = getResizedBitmap(image, 400);
                IDProf.setImageBitmap(image);
                BitMapToString(image);
                photouploaded = false;
            }

            if (requestCode == 3) {

                b = data.getExtras();
                image1 = (Bitmap) b.get("data");
                photo.setImageBitmap(image1);
                profileuploaded = false;
            } else if (requestCode == 4) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                image1 = (BitmapFactory.decodeFile(picturePath));
                image1 = getResizedBitmap(image1, 400);
                photo.setImageBitmap(image1);
                BitMapToString(image1);
                profileuploaded = false;
            }

        }
    }

    public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
        return Document_img1;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public boolean validate() {
        String emailPatter = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        if (name.getEditText().getText().toString().isEmpty()) {
            name.setError("\nName cannot be empty\n");
            name.getEditText().requestFocus();
            return false;
        } else {
            name.setError("");
            name.setErrorEnabled(false);

        }


        if (!email.getEditText().getText().toString().matches(emailPatter)) {
            email.setError("\nEnter a valid email\n");
            email.getEditText().requestFocus();
            return false;
        } else {
            email.setError("");
            email.setErrorEnabled(false);
        }

        if (radioGroup.getCheckedRadioButtonId() == -1) {
            TextInputLayout radio = findViewById(R.id.radio);
            radio.setError("Select your gender");
            radioGroup.requestFocus();
            return false;
        } else {
            TextInputLayout radio = findViewById(R.id.radio);
            radio.setErrorEnabled(false);
            radio.setError("");
        }
        if (dob.getText().toString().isEmpty()) {
            TextInputLayout dobl = findViewById(R.id.doblayout);
            dobl.setError("Enter your D/O/B");
            dobl.getEditText().requestFocus();
            return false;

        } else {
            TextInputLayout dobl = findViewById(R.id.doblayout);
            dobl.setError("");
            dobl.setErrorEnabled(false);
        }
        if (address.getEditText().getText().toString().isEmpty()) {
            address.setError("\nAddress cannot be empty\n");
            address.getEditText().requestFocus();
            return false;
        } else {
            address.setError("");
            address.setErrorEnabled(false);
        }

        if (profileuploaded) {
            Toast.makeText(this, "Upload your passport size photo", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (photouploaded) {
            Toast.makeText(this, "Upload your Identity proof", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (role.getSelectedItem().toString().equals(getString(R.string.select_your_role))) {
            rolelayout.setError("\nRole cannot be empty\n");
            rolelayout.requestFocus();
            return false;
        } else {
            rolelayout.setError("");
            rolelayout.setErrorEnabled(false);
        }


        return true;
    }
}