package com.gprs.haryana;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


class CustomWorkAssignAdapter1 extends ArrayAdapter {

    private final ArrayList<String> name, role, phone;
    private final String leader;
    private Activity context;
    private Boolean done;


    public CustomWorkAssignAdapter1(Activity context, ArrayList<String> name, ArrayList<String> role, ArrayList<String> phone, String leader, Boolean done) {
        super(context, R.layout.workassignitem, name);
        this.context = context;
        this.name = name;
        this.role = role;
        this.phone = phone;
        this.leader = leader;
        this.done = done;
    }

    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = null;

        rowView = inflater.inflate(R.layout.workassignitem, null, true);
        final TextView name1 = rowView.findViewById(R.id.name);
        TextView role1 = rowView.findViewById(R.id.role);


        if (leader.equals(name.get(position)))
            rowView.findViewById(R.id.leader).setVisibility(View.VISIBLE);
        else
            rowView.findViewById(R.id.leader).setVisibility(View.INVISIBLE);

        name1.setText(name.get(position));
        role1.setText(role.get(position));


        ImageView image = rowView.findViewById(R.id.delete);
        if (done) {
            image.setVisibility(View.INVISIBLE);
        } else {
            image.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_cached_24));
        }

        return rowView;
    }
}


