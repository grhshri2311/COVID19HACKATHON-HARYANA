package com.gprs.haryana;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class CustomQuoraCommentAdapter extends ArrayAdapter {


    ArrayList<String> name, comment;
    private Activity context;


    public CustomQuoraCommentAdapter(Activity context, ArrayList<String> name, ArrayList<String> comment) {
        super(context, R.layout.quora_comment_item, name);
        this.context = context;
        this.comment = comment;
        this.name = name;
    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = null;

        rowView = inflater.inflate(R.layout.quora_comment_item, null, true);

        final TextView name1 = rowView.findViewById(R.id.name);
        TextView comment1 = rowView.findViewById(R.id.comment);
        TextView date = rowView.findViewById(R.id.date);
        FirebaseDatabase.getInstance().getReference().child("Users").child(name.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    UserRegistrationHelper u = dataSnapshot.getValue(UserRegistrationHelper.class);
                    name1.setText(u.getFname() + ", " + u.getRole());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MMM");
        String currentDateTime = dateFormat.format(new Date());
        name1.setText(name.get(position));
        comment1.setText(comment.get(position));
        date.setText(currentDateTime);


        return rowView;

    }


}
