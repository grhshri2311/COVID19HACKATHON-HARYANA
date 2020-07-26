package com.gprs.haryana;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SelectVolunteer extends AppCompatActivity {

    Button role;
    ArrayList<String> namelist, rolelist, placelist;
    ArrayList<UserLocationHelper> volunteer;
    ListView listView;
    VolunteerAdapter adapter;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Boolean select = true;
    int selectedcount = 0;
    AlertDialog alert;
    ArrayList<Integer> number;
    TextView go;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_select_volunteer);

        namelist = new ArrayList<>();
        rolelist = new ArrayList<>();
        placelist = new ArrayList<>();
        listView = findViewById(R.id.volunteers);
        go = findViewById(R.id.go);
        searchView = findViewById(R.id.search);

        number = new ArrayList<>();
        role = findViewById(R.id.role);
        pref = getApplicationContext().getSharedPreferences("user", 0); // 0 - for private mode
        editor = pref.edit();

        volunteer = new ArrayList<>();

        adapter = new VolunteerAdapter(this, namelist, rolelist, placelist);
        listView.setAdapter(adapter);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (TextUtils.isEmpty(query)) {
                    listView.clearTextFilter();
                } else {
                    listView.setFilterText(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        listView.setTextFilterEnabled(true);
        searchView.setIconifiedByDefault(false);


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (view.findViewById(R.id.checked).getVisibility() != View.VISIBLE) {
                    view.findViewById(R.id.checked).setVisibility(View.VISIBLE);
                    selectedcount = selectedcount + 1;
                    go.setVisibility(View.VISIBLE);
                    Toast.makeText(SelectVolunteer.this, selectedcount + " selected", Toast.LENGTH_LONG).show();
                    number.add(position);
                    select = false;
                }
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (select) {
                    show(position, view);
                } else if (view.findViewById(R.id.checked).getVisibility() == View.VISIBLE) {
                    selectedcount = selectedcount - 1;
                    view.findViewById(R.id.checked).setVisibility(View.INVISIBLE);
                    for (int i = 0; i < number.size(); i++) {
                        if (number.get(i) == position) {
                            number.remove(i);
                            break;
                        }
                    }
                } else {
                    selectedcount = selectedcount + 1;
                    number.add(position);
                    view.findViewById(R.id.checked).setVisibility(View.VISIBLE);
                }
                if (selectedcount == 0) {
                    select = true;
                    go.setVisibility(View.GONE);
                }
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendlist();
            }
        });

        role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu rolemenu = new PopupMenu(SelectVolunteer.this.getApplicationContext(), role);
                rolemenu.getMenuInflater().inflate(R.menu.role, rolemenu.getMenu());
                rolemenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(final MenuItem item) {
                        role.setText(item.getTitle().toString());
                        namelist.clear();
                        rolelist.clear();
                        placelist.clear();
                        volunteer.clear();
                        adapter.notifyDataSetChanged();
                        FirebaseDatabase.getInstance().getReference().child("Location")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            final UserLocationHelper userLocationHelper = snapshot.getValue(UserLocationHelper.class);
                                            if (userLocationHelper.getRole().equals(item.getTitle().toString()) && !userLocationHelper.getPhone().equals(pref.getString("user", ""))) {
                                                volunteer.add(userLocationHelper);
                                                namelist.add(userLocationHelper.getFname());
                                                rolelist.add(userLocationHelper.getRole());
                                                placelist.add(location(userLocationHelper.getLat(), userLocationHelper.getLon()));
                                                adapter.notifyDataSetChanged();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                        return true;
                    }
                });
                rolemenu.show();
            }
        });

        FirebaseDatabase.getInstance().getReference().child("Location")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            final UserLocationHelper userLocationHelper = snapshot.getValue(UserLocationHelper.class);
                            if (!userLocationHelper.getPhone().equals(pref.getString("user", ""))) {
                                volunteer.add(userLocationHelper);
                                namelist.add(userLocationHelper.getFname());
                                rolelist.add(userLocationHelper.getRole());
                                placelist.add(location(userLocationHelper.getLat(), userLocationHelper.getLon()));
                            }
                        }
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    private void sendlist() {
        ArrayList<String> name = new ArrayList();
        ArrayList<String> role = new ArrayList();
        ArrayList<String> phone = new ArrayList();

        for (int i = 0; i < number.size(); i++) {
            name.add(volunteer.get(number.get(i)).getFname());
            role.add(volunteer.get(number.get(i)).getRole());
            phone.add(volunteer.get(number.get(i)).getPhone());
        }

        Intent intent = new Intent();
        intent.putStringArrayListExtra("name", name);
        intent.putStringArrayListExtra("role", role);
        intent.putStringArrayListExtra("phone", phone);
        setResult(Activity.RESULT_OK, intent);
        finish();

    }

    private void show(final int id, final View view1) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setMessage(volunteer.get(id).fname);

        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.bottom_sheet_volunteerview_layout, null, true);


        TextView email = view.findViewById(R.id.mapemail);
        TextView phone = view.findViewById(R.id.mapphone);
        TextView role = view.findViewById(R.id.maprole);
        final Button selectvolunteer = view.findViewById(R.id.select);
        TextView name = view.findViewById(R.id.name);

        name.setText(volunteer.get(id).getFname());
        email.setText(volunteer.get(id).email);
        phone.setText(volunteer.get(id).phone);
        role.setText(volunteer.get(id).role);

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + volunteer.get(id).phone));
                Toast.makeText(SelectVolunteer.this, "Connecting to " + volunteer.get(id).fname + " ...", Toast.LENGTH_LONG).show();
                if (SelectVolunteer.this.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent);
                    return;
                }

            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", volunteer.get(id).getEmail(), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Work Assignment");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        selectvolunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedcount = selectedcount + 1;
                go.setVisibility(View.VISIBLE);
                Toast.makeText(SelectVolunteer.this, selectedcount + " selected", Toast.LENGTH_LONG).show();
                number.add(id);
                select = false;
                view1.findViewById(R.id.checked).setVisibility(View.VISIBLE);
                alert.hide();
            }
        });

        alert = builder.create();
        alert.setView(view);
        alert.show();

    }

    String location(double lat, double lon) {


        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5


            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();

            return city + ',' + state;


        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}