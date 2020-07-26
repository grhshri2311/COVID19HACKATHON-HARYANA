package com.gprs.haryana;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AssignWork extends AppCompatActivity {

    ListView memberlist;
    CustomWorkAssignAdapter c;
    Button addmember;
    TextInputLayout title, desc, startdate, duedate, contact;
    private double lat = 0, lon = 0;
    ArrayList<String> name, role, phone;
    Button address;
    ArrayAdapter<String> dataAdapter;
    AssignWorkHelper assignWorkHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_assign_work);

        memberlist = findViewById(R.id.memberlist);
        addmember = findViewById(R.id.addmember);
        title = findViewById(R.id.title);
        desc = findViewById(R.id.desc);
        startdate = findViewById(R.id.startdate);
        duedate = findViewById(R.id.duedate);
        contact = findViewById(R.id.contact);
        address = findViewById(R.id.address);

        name = new ArrayList<>();
        role = new ArrayList<>();
        phone = new ArrayList<>();

        dataAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, R.id.txt_bundle, name);

        final Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                c.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        c = new CustomWorkAssignAdapter(this, name, role, phone, dataAdapter, spinner);
        memberlist.setAdapter(c);

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AssignWork.this, SelectLocation.class), 100, ActivityOptions.makeSceneTransitionAnimation(AssignWork.this).toBundle());

            }
        });

        addmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AssignWork.this, SelectVolunteer.class), 200, ActivityOptions.makeSceneTransitionAnimation(AssignWork.this).toBundle());
                c.notifyDataSetChanged();
            }
        });

        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    final SharedPreferences pref = getSharedPreferences("user", 0);
                    FirebaseDatabase.getInstance().getReference().child("Users").child(pref.getString("user", "")).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MMM:hh:mm:ss");
                                String currentDateTime = dateFormat.format(new Date());
                                UserRegistrationHelper u = dataSnapshot.getValue(UserRegistrationHelper.class);
                                RadioGroup radioGroup = findViewById(R.id.radioGroup);
                                RadioButton radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                                assignWorkHelper = new AssignWorkHelper(u.getFname(), u.getPhone(), u.getRole(), title.getEditText().getText().toString(), desc.getEditText().getText().toString(), startdate.getEditText().getText().toString(), duedate.getEditText().getText().toString(), contact.getEditText().getText().toString(), radioButton.getText().toString(), spinner.getSelectedItem().toString(), name, role, phone, lat, lon, "In Progress", currentDateTime);
                                assignWorkHelper.setComment(new ArrayList<String>());
                                assignWorkHelper.setCommentname(new ArrayList<String>());

                                FirebaseDatabase.getInstance().getReference().child("Work").child("monitor").child(pref.getString("user", "")).child(currentDateTime).setValue(assignWorkHelper);
                                for (int i = 0; i < phone.size(); i++) {
                                    FirebaseDatabase.getInstance().getReference().child("Work").child("volunteer").child(phone.get(i)).child(currentDateTime).setValue(assignWorkHelper);
                                    FirebaseDatabase.getInstance().getReference().child("Notification").child(phone.get(i)).child("Work").child(currentDateTime).setValue("You are assigned for work");
                                }

                                Toast.makeText(AssignWork.this, "Work Assigned successfully", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    assignWorkHelper = new AssignWorkHelper();
                }
            }
        });
    }

    private boolean validate() {

        if (title.getEditText().getText().toString().isEmpty()) {
            title.setError("\nTitle cannot be empty\n");
            title.getEditText().requestFocus();
            return false;
        } else {
            title.setError("");
            title.setErrorEnabled(false);

        }

        if (desc.getEditText().getText().toString().isEmpty()) {
            desc.setError("Descrition cannot be empty");
            desc.getEditText().requestFocus();
            return false;

        } else {
            desc.setError("");
            desc.setErrorEnabled(false);
        }


        if (contact.getEditText().getText().toString().isEmpty()) {
            contact.setError("Contact cannot be empty");
            contact.getEditText().requestFocus();
            return false;

        } else {
            contact.setError("");
            contact.setErrorEnabled(false);
        }
        if (startdate.getEditText().getText().toString().isEmpty()) {
            startdate.setError("Start Date cannot be empty");
            startdate.getEditText().requestFocus();
            return false;

        } else {
            startdate.setError("");
            startdate.setErrorEnabled(false);
        }
        if (duedate.getEditText().getText().toString().isEmpty()) {
            duedate.setError("Due Date cannot be empty");
            duedate.getEditText().requestFocus();
            return false;

        } else {
            duedate.setError("");
            duedate.setErrorEnabled(false);
        }
  /*      if(lat==0 ||lon==0) {
            Toast.makeText(this, "Place cannot be empty", Toast.LENGTH_SHORT).show();return false;
        }


   */
        if (phone.size() == 0) {
            Toast.makeText(this, "Select atleast one member", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                lat = data.getDoubleExtra("lat", 0);
                lon = data.getDoubleExtra("lon", 0);
                address.setText(String.valueOf(lat) + ',' + lon);
            } else if (requestCode == 200) {
                name.addAll(data.getStringArrayListExtra("name"));
                role.addAll(data.getStringArrayListExtra("role"));
                phone.addAll(data.getStringArrayListExtra("phone"));

                c.notifyDataSetChanged();
                dataAdapter.notifyDataSetChanged();
            }
        }
    }
}