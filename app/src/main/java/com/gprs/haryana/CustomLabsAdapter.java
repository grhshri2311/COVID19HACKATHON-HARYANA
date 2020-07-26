package com.gprs.haryana;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


class CustomLabsAdapter extends ArrayAdapter {
    private final ArrayList<Pair<String, Pair<String, String>>> labs;
    private Activity context;


    public CustomLabsAdapter(Activity context, ArrayList<Pair<String, Pair<String, String>>> labs) {
        super(context, R.layout.labsitem, labs);
        this.context = context;
        this.labs = labs;

    }

    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = null;

        rowView = inflater.inflate(R.layout.labsitem, null, true);
        TextView name = rowView.findViewById(R.id.name);
        TextView type = rowView.findViewById(R.id.type);
        TextView type1 = rowView.findViewById(R.id.type1);
        TextView getdirection = rowView.findViewById(R.id.getdirection);

        name.setText(labs.get(position).first);
        type.setText(labs.get(position).second.first);
        type1.setText(labs.get(position).second.second);

        getdirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr=" + labs.get(position).first));
                    context.startActivity(intent);
                } catch (Exception e) {

                }
            }
        });


        return rowView;

    }


}


