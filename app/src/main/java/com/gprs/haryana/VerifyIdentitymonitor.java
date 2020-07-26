package com.gprs.haryana;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VerifyIdentitymonitor extends AppCompatActivity {

    AlertDialog alert;
    ListView listView;
    VerificationAdapter verificationAdapter;
    ArrayList<IdentityVerificationHelper> i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_verify_identitymonitor);
        listView = findViewById(R.id.list);
        i = new ArrayList<>();
        verificationAdapter = new VerificationAdapter(i, this);
        listView.setAdapter(verificationAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view_selected(position);
            }
        });

        TextView textView=findViewById(R.id.empty);
        listView.setEmptyView(textView);

        FirebaseDatabase.getInstance().getReference().child("IdentityVerification").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        IdentityVerificationHelper ii = data.getValue(IdentityVerificationHelper.class);
                        if (ii.getStatus().equals("pending")) {
                            i.add(ii);
                        }
                    }
                    verificationAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void view_selected(final int position) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        builder.setCancelable(true);


        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.verifyidentificationitem, null, true);
        TextView name, email, phone, dob, gender, role, address;
        ImageView proimg, proof;

        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        phone = view.findViewById(R.id.phone);
        dob = view.findViewById(R.id.dob);
        gender = view.findViewById(R.id.gender);
        role = view.findViewById(R.id.role);
        address = view.findViewById(R.id.address);
        proimg = view.findViewById(R.id.proimg);
        proof = view.findViewById(R.id.proof);

        name.setText("Name : " + i.get(position).getName());
        email.setText("Email : " + i.get(position).getEmail());
        phone.setText("Phone : " + i.get(position).getPhone());
        dob.setText("D/O/B : " + i.get(position).getDob());
        gender.setText("Gender : " + i.get(position).getGender());
        role.setText("Role : " + i.get(position).getRole());
        address.setText("Address : " + i.get(position).getAddress());

        proimg.setImageBitmap(StringToBitMap(i.get(position).getPassphoto()));
        proof.setImageBitmap(StringToBitMap(i.get(position).getProof()));

        view.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(VerifyIdentitymonitor.this).create();
                alertDialog.setTitle("Confirm");
                alertDialog.setMessage("Accept verification request of " + i.get(position).getName());
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                i.get(position).setStatus("accepted");
                                FirebaseDatabase.getInstance().getReference().child("IdentityVerification").child(i.get(position).getPhone()).setValue(i.get(position));
                                FirebaseDatabase.getInstance().getReference().child("Users").child(i.get(position).getPhone()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot != null) {
                                            UserRegistrationHelper u = dataSnapshot.getValue(UserRegistrationHelper.class);
                                            u.setVerify(true);
                                            FirebaseDatabase.getInstance().getReference().child("Users").child(i.get(position).getPhone()).setValue(u);
                                            i.remove(position);
                                            verificationAdapter.notifyDataSetChanged();
                                            alert.hide();
                                            Toast.makeText(VerifyIdentitymonitor.this, "Request Accepted", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

        view.findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(VerifyIdentitymonitor.this).create();
                alertDialog.setTitle("Confirm");
                alertDialog.setMessage("Reject verification request of " + i.get(position).getName());
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                i.get(position).setStatus("rejected");
                                FirebaseDatabase.getInstance().getReference().child("IdentityVerification").child(i.get(position).getPhone()).setValue(i.get(position));
                                i.remove(position);
                                verificationAdapter.notifyDataSetChanged();
                                alert.hide();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.hide();
            }
        });
        alert = builder.create();
        alert.setView(view);
        alert.show();
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

}

