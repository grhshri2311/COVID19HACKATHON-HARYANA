package com.gprs.haryana;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


class CustomPubliccareAdapter extends ArrayAdapter {
    private ArrayList<String> state,link;
    private Activity context;


    public CustomPubliccareAdapter(Activity context, ArrayList<String> state, ArrayList<String> link) {
        super(context, R.layout.publiccareitem, state);
        this.context = context;
        this.state = state;
        this.link = link;
    }

    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = null;

        rowView = inflater.inflate(R.layout.publiccareitem, null, true);


            TextView name1 = rowView.findViewById(R.id.name);

            TextView getdirection = rowView.findViewById(R.id.getdirection);

            name1.setText((String) state.get(position));

            getdirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, pdfViewer.class);
                    intent.putExtra("text", link.get(position));
                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context).toBundle());
                }
            });

        return rowView;

    }


}


