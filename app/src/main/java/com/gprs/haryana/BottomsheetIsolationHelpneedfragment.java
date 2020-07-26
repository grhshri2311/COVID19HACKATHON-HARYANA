package com.gprs.haryana;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.FileUriExposedException;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


public class BottomsheetIsolationHelpneedfragment extends BottomSheetDialogFragment {


    Button send, profile;
    private String Document_img1 = "";
    private ImageView IDProf, photo;
    Bitmap image1;
    SharedPreferences pref;
    TextInputLayout name, email, rolelayout, address;
    RadioGroup radioGroup;
    EditText dob;
    EditText fixedtime;
    Boolean photouploaded = true, profileuploaded = true;
    private GoogleMap mMap;
    Double lat, lon;
    View view;
    Button location;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MMM:hh:mm:a");
    String currentDateTime = dateFormat.format(new Date());


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                setupFullHeight(bottomSheetDialog);
            }
        });
        return dialog;
    }


    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bottom_sheet_isolationneedhelp, container, false);

        radioGroup = view.findViewById(R.id.radioGroup);

        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        rolelayout = view.findViewById(R.id.rolelayout);
        address = view.findViewById(R.id.address);
        profile = view.findViewById(R.id.profile);
        fixedtime = view.findViewById(R.id.dob);

        IDProf = view.findViewById(R.id.proof);
        send = view.findViewById(R.id.btnSend);
        photo = view.findViewById(R.id.photo);
        pref = getActivity().getSharedPreferences("user", 0);


        fixedtime.setText(currentDateTime);

        location = view.findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), SelectLocation.class), 100, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = view.findViewById(checkedId);


                if (radioButton.getText().equals(getString(R.string.other))) {
                    view.findViewById(R.id.fixedtimelayout).setVisibility(View.VISIBLE);
                } else {
                    view.findViewById(R.id.fixedtimelayout).setVisibility(View.INVISIBLE);
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate()) {
                    send.setBackgroundColor(Color.parseColor("#9CD3FF"));
                    send.setEnabled(false);


                    RadioButton r = view.findViewById(radioGroup.getCheckedRadioButtonId());
                    String type = r.getText().toString();
                    if (type.equals(getString(R.string.other))) {
                        EditText f = view.findViewById(R.id.fixedtime);
                        type = f.getText().toString();
                    }
                    MaterialCollectionHelper m = new MaterialCollectionHelper(name.getEditText().getText().toString(), lat, lon, pref.getString("user", ""), type, dob.getText().toString(), BitMapToString(image1));
                    FirebaseDatabase.getInstance().getReference().child("Isolation").child("needhelp").child(pref.getString("user", "")).setValue(m);
                    Toast.makeText(getActivity(), "Submitted Successfully", Toast.LENGTH_SHORT).show();

                    dismiss();
                }

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Users").child(pref.getString("user", "")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    UserRegistrationHelper u = dataSnapshot.getValue(UserRegistrationHelper.class);
                    name.getEditText().setText(u.getFname());
                    name.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        EditText mobile = view.findViewById(R.id.mobile);
        mobile.setText(pref.getString("user", ""));
        mobile.setEnabled(false);


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        final Calendar myCalendar = Calendar.getInstance();

        dob = view.findViewById(R.id.dob);
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
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        ImageView imageView = view.findViewById(R.id.logo);
        new BottomsheetIsolationHelpneedfragment.SetImage(imageView).execute("https://www.freepngimg.com/thumb/grocery/54018-9-grocery-free-hq-image.png");
        return view;
    }


    private class SetImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public SetImage(ImageView bmImage) {
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

    Boolean camera = false;

    public void requestForCamera() {
        Dexter.withActivity(getActivity()).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                camera = true;
                selectImage();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(getActivity(), "Camera Permission is Required.", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();

            }
        }).check();
    }

    private void selectImage() {
        if (camera) {
            final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Add Photo!");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Take Photo")) {
                        try {
                            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (i.resolveActivity(getActivity().getPackageManager()) != null) {
                                startActivityForResult(i, 3);
                            }
                        } catch (FileUriExposedException e) {

                        }
                    } else if (options[item].equals("Choose from Gallery")) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 4);
                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        } else {
            requestForCamera();
        }
    }

    private Bundle b;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                lat = data.getDoubleExtra("lat", 0);
                lon = data.getDoubleExtra("lon", 0);
                location.setText(String.valueOf(lat) + ',' + lon);
            }

            if (requestCode == 3) {

                b = data.getExtras();
                image1 = (Bitmap) b.get("data");
                photo.setImageBitmap(image1);
                profileuploaded = false;
            } else if (requestCode == 4) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
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


        if (radioGroup.getCheckedRadioButtonId() == -1) {
            TextInputLayout radio = view.findViewById(R.id.radio);
            radio.setError("Select type");
            radioGroup.requestFocus();
            return false;
        } else {
            RadioButton r = view.findViewById(radioGroup.getCheckedRadioButtonId());
            if (r.getText().equals(getString(R.string.other))) {
                EditText type = view.findViewById(R.id.fixedtime);
                if (type.getText().toString().isEmpty()) {
                    TextInputLayout radio = view.findViewById(R.id.radio);
                    radio.setError("Select type");
                    radioGroup.requestFocus();
                    return false;
                } else {
                    TextInputLayout radio = view.findViewById(R.id.radio);
                    radio.setErrorEnabled(false);
                    radio.setError("");
                }
            } else {
                TextInputLayout radio = view.findViewById(R.id.radio);
                radio.setErrorEnabled(false);
                radio.setError("");
            }

        }


        if (lat == null || lon == null) {
            Toast.makeText(getContext(), "Select your location", Toast.LENGTH_SHORT).show();
            return false;

        }

        if (profileuploaded) {
            Toast.makeText(getContext(), "Upload your photo", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}