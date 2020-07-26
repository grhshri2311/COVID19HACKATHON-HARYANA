package com.gprs.haryana;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class fragment_material_distribution extends Fragment {


    View view;
    ListView list;
    MaterialdistributionAdapter m;
    ArrayList<String> time, type, image, key;
    SharedPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_material_distribution, container, false);

        time = new ArrayList<>();
        type = new ArrayList<>();
        image = new ArrayList<>();
        key = new ArrayList<>();

        list = view.findViewById(R.id.list);
        m = new MaterialdistributionAdapter(getActivity(), time, type, image, key);
        list.setAdapter(m);


        pref = getActivity().getSharedPreferences("user", 0); //

        FirebaseDatabase.getInstance().getReference().child("MaterialCollection").child("distribute").child(pref.getString("user", "")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                key.clear();
                time.clear();
                type.clear();
                image.clear();

                if (dataSnapshot != null) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        key.add(d.getKey());
                        MaterialCollectionHelper materialCollectionHelper = d.getValue(MaterialCollectionHelper.class);
                        time.add(materialCollectionHelper.getDatetime());
                        type.add(materialCollectionHelper.getType());
                        image.add(materialCollectionHelper.getImage());
                    }
                    m.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }


}