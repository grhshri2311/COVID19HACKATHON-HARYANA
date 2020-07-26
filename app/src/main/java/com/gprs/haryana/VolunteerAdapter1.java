package com.gprs.haryana;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

class VolunteerAdapter1 extends ArrayAdapter {

    private final ArrayList<String> work;
    private ArrayList<String> name, role, place, comment, status;
    private Activity context;


    public VolunteerAdapter1(Activity context, ArrayList name, ArrayList role, ArrayList place, ArrayList<String> work, ArrayList<String> comment, ArrayList<String> status) {
        super(context, R.layout.volunteers, name);
        this.context = context;
        this.name = name;
        this.role = role;
        this.place = place;
        this.work = work;
        this.status = status;
        this.comment = comment;

    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = null;

        rowView = inflater.inflate(R.layout.volunteers1, null, true);
        //this code gets references to objects in the listview_row.xml file
        TextView nameTextField = rowView.findViewById(R.id.name);
        TextView infoTextField = rowView.findViewById(R.id.role);
        TextView textView = rowView.findViewById(R.id.place);
        TextView work1 = rowView.findViewById(R.id.work);
        TextView status1 = rowView.findViewById(R.id.status);
        TextView comment1 = rowView.findViewById(R.id.comment);

        RelativeLayout relativeLayout = rowView.findViewById(R.id.notice);

        if (status.get(position).equals("pending")) {
            relativeLayout.setBackground(rowView.getResources().getDrawable(R.drawable.textviewbackground));
        } else if (status.get(position).equals("done")) {
            relativeLayout.setBackground(rowView.getResources().getDrawable(R.drawable.textviewbackground1));
        } else {
            relativeLayout.setBackground(rowView.getResources().getDrawable(R.drawable.textviewbackground2));
        }

        nameTextField.setText(name.get(position));
        infoTextField.setText(role.get(position));
        textView.setText(place.get(position));
        work1.setText(work.get(position));
        status1.setText("Status : " + status.get(position));
        if (!comment.get(position).isEmpty())
            comment1.setText("Comments : " + comment.get(position));


        return rowView;

    }


}
