package com.gprs.haryana;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


class CustomWorkAssignedAdapter extends ArrayAdapter {


    private final ArrayList<AssignWorkHelper> c;
    private final Boolean done;
    private Activity context;


    public CustomWorkAssignedAdapter(Activity context, ArrayList<AssignWorkHelper> assignWorkHelpers, Boolean done) {
        super(context, R.layout.workassignitem, assignWorkHelpers);
        this.context = context;
        this.c = assignWorkHelpers;
        this.done = done;
    }

    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = null;

        rowView = inflater.inflate(R.layout.workviewitem, null, true);
        TextView title = rowView.findViewById(R.id.title);
        TextView desc = rowView.findViewById(R.id.desc);
        TextView due = rowView.findViewById(R.id.due);

        title.setText(c.get(position).getTitle());
        desc.setText(c.get(position).getDesc());
        due.setText(c.get(position).getDuedate());

        ImageView image = rowView.findViewById(R.id.status);
        if (done) {
            image.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_check_24));
        } else {
            image.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_cached_24));
        }

        return rowView;
    }


}


