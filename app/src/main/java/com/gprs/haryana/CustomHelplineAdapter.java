package com.gprs.haryana;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


class CustomHelplineAdapter extends ArrayAdapter {

    ArrayList<String> state, number;
    private Activity context;


    public CustomHelplineAdapter(Activity context, ArrayList<String> state, ArrayList<String> number) {
        super(context, R.layout.helplineitem, state);
        this.context = context;
        this.state = state;
        this.number = number;
    }

    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = null;

        rowView = inflater.inflate(R.layout.helplineitem, null, true);
        final TextView state1 = rowView.findViewById(R.id.label);
        final TextView number1 = rowView.findViewById(R.id.number);
        state1.setText(state.get(position));
        number1.setText(number.get(position));

        rowView.findViewById(R.id.call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + number.get(position)));
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
            }
        });
        return rowView;
    }


}


