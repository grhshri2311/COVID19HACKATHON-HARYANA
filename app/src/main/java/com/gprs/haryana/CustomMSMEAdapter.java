package com.gprs.haryana;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


class CustomMSMEAdapter extends ArrayAdapter {
    private final ArrayList<String> labs;
    private Activity context;


    public CustomMSMEAdapter(Activity context, ArrayList<String> MSME) {
        super(context, R.layout.msmeitem, MSME);
        this.context = context;
        this.labs = MSME;

    }

    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = null;

        rowView = inflater.inflate(R.layout.msmeitem, null, true);
        TextView name = rowView.findViewById(R.id.name);
        ImageView logo = rowView.findViewById(R.id.logo);
        name.setText(labs.get(position));

        if (position % 2 == 0) {
            logo.setBackground(context.getResources().getDrawable(R.drawable.textviewbackground1));
        }
        TextView getdirection = rowView.findViewById(R.id.getdirection);

        getdirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, pdfViewer.class);
                intent.putExtra("text", "https://mkp.gem.gov.in/search?q=" + labs.get(position));
                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context).toBundle());
            }
        });


        return rowView;

    }


}


