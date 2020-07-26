package com.gprs.haryana;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class fragment_quora_home extends Fragment {


    private View root;
    ListView listView;
    CustomQuoraHomeItemAdapter customAdapter;
    ArrayList<QuoraHelper> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_quora_home, container, false);
        arrayList = new ArrayList<>();


        listView = root.findViewById(R.id.quoralist);

        customAdapter = new CustomQuoraHomeItemAdapter(getActivity(), arrayList);
        listView.setAdapter(customAdapter);


        TextView textView = root.findViewById(R.id.empty);
        listView.setEmptyView(textView);

        customAdapter.notifyDataSetChanged();

        FirebaseDatabase.getInstance().getReference().child("Quora").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    arrayList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        QuoraHelper quoraHelper = snapshot.getValue(QuoraHelper.class);
                        arrayList.add(quoraHelper);
                        Collections.sort(arrayList, new fragment_quora_home.comp());
                        customAdapter.notifyDataSetChanged();
                    }


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return root;
    }

    class comp implements Comparator<QuoraHelper> {

        @Override
        public int compare(QuoraHelper o1, QuoraHelper o2) {
            return -(o1.getTime().compareTo(o2.getTime()));
        }
    }
}