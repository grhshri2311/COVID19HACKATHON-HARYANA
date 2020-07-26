package com.gprs.haryana;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class fragment_done_work extends Fragment {


    AlertDialog alert;
    ArrayList<AssignWorkHelper> assignWorkHelpers;
    ListView list;
    CustomWorkAssignedAdapter c;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ongoing_work, container, false);
        final SharedPreferences pref = getActivity().getSharedPreferences("user", 0);

        assignWorkHelpers = new ArrayList<>();
        c = new CustomWorkAssignedAdapter(getActivity(), assignWorkHelpers, true);
        list = view.findViewById(R.id.list);
        list.setAdapter(c);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                show(position);
            }
        });

        FirebaseDatabase.getInstance().getReference().child("Work").child("volunteer").child(pref.getString("user", "")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    assignWorkHelpers.clear();
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        AssignWorkHelper a = d.getValue(AssignWorkHelper.class);
                        if (!a.getStatus().equals("In Progress"))
                            assignWorkHelpers.add(a);
                    }
                    c.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void show(final int position) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View view = factory.inflate(R.layout.alert_workview, null);
        alertDialog.setView(view);
        ListView memberlist;
        CustomWorkAssignAdapter1 c;
        Button addmember;
        TextInputLayout title, desc, startdate, duedate, contact, comments;
        double lat = 0, lon = 0;
        ArrayList<String> name, role, phone;
        Button address;
        comments = view.findViewById(R.id.comments);

        memberlist = view.findViewById(R.id.memberlist);
        title = view.findViewById(R.id.title);
        desc = view.findViewById(R.id.desc);
        startdate = view.findViewById(R.id.startdate);
        duedate = view.findViewById(R.id.duedate);
        contact = view.findViewById(R.id.contact);
        address = view.findViewById(R.id.address);
        title.getEditText().setText(assignWorkHelpers.get(position).getTitle());
        desc.getEditText().setText(assignWorkHelpers.get(position).getDesc());
        startdate.getEditText().setText(assignWorkHelpers.get(position).getStartdate());
        duedate.getEditText().setText(assignWorkHelpers.get(position).getDuedate());
        contact.getEditText().setText(assignWorkHelpers.get(position).getContact());
        address.setText(String.valueOf(assignWorkHelpers.get(position).getLat()) + ',' + assignWorkHelpers.get(position).getLon());

        if (assignWorkHelpers.get(position).getComment() != null) {
            String comment = "";
            for (int i = 0; i < assignWorkHelpers.get(position).getComment().size(); i++) {
                comment = comment + assignWorkHelpers.get(position).getCommentname().get(i) + " : " + assignWorkHelpers.get(position).getComment().get(i) + '\n';
            }
            comments.getEditText().setText(comment);
        }

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectLocation.class);
                intent.putExtra("lat", assignWorkHelpers.get(position).getLat());
                intent.putExtra("lon", assignWorkHelpers.get(position).getLon());
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            }
        });

        TextView title2 = view.findViewById(R.id.title2);
        title2.setText(assignWorkHelpers.get(position).getLeader());
        name = assignWorkHelpers.get(position).getName();
        role = assignWorkHelpers.get(position).getRole();
        phone = assignWorkHelpers.get(position).getPhone();
        RadioButton radioButton;
        Button button = view.findViewById(R.id.stop);
        button.setEnabled(false);
        button.setText(assignWorkHelpers.get(position).getStatus());
        if (assignWorkHelpers.get(position).getPriority().equals(getContext().getString(R.string.normal))) {
            radioButton = view.findViewById(R.id.radioButton1);
        } else {
            radioButton = view.findViewById(R.id.radioButton2);
        }
        radioButton.setChecked(true);
        c = new CustomWorkAssignAdapter1(getActivity(), name, role, phone, assignWorkHelpers.get(position).getLeader(), true);
        memberlist.setAdapter(c);

        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.hide();
            }
        });

        alert = alertDialog.create();
        alert.setView(view);
        alert.show();
    }

}