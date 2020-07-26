package com.gprs.haryana;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class CustomSelfAssessAdapter extends ArrayAdapter {


    ArrayList<String> text;
    private final ArrayList<Integer> toggle;
    private Activity context;


    public CustomSelfAssessAdapter(Activity context, ArrayList<Integer> togle, ArrayList<String> text) {
        super(context, R.layout.their_message, text);
        this.context = context;
        this.toggle = togle;
        this.text = text;

    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = null;
        if (toggle.get(position) == 0) {
            rowView = inflater.inflate(R.layout.their_message, null, true);
            //this code gets references to objects in the listview_row.xml file
            TextView nameTextField = rowView.findViewById(R.id.name);
            TextView infoTextField = rowView.findViewById(R.id.message_body);
            ImageView imageView = rowView.findViewById(R.id.avatar);


            //this code sets the values of the objects to values from the arrays
            nameTextField.setText("Covid19Relief");
            infoTextField.setText(text.get(position));
            imageView.setImageResource(R.drawable.collective_intelligence_icon_use);

        } else {
            rowView = inflater.inflate(R.layout.my_message, null, true);

            //this code gets references to objects in the listview_row.xml file
            TextView nameTextField = rowView.findViewById(R.id.message_body1);
            nameTextField.setText(text.get(position));
        }
        return rowView;

    }
}
