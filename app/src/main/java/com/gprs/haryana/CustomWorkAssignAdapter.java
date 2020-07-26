package com.gprs.haryana;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;


class CustomWorkAssignAdapter extends ArrayAdapter {

    private final ArrayList<String> name, role, phone;
    private final ArrayAdapter<String> dataAdapter;
    private final Spinner spinner;
    private Activity context;


    public CustomWorkAssignAdapter(Activity context, ArrayList<String> name, ArrayList<String> role, ArrayList<String> phone, ArrayAdapter<String> dataAdapter, Spinner spinner) {
        super(context, R.layout.workassignitem, name);
        this.context = context;
        this.name = name;
        this.role = role;
        this.phone = phone;
        this.dataAdapter = dataAdapter;
        this.spinner = spinner;


    }

    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = null;

        rowView = inflater.inflate(R.layout.workassignitem, null, true);
        final TextView name1 = rowView.findViewById(R.id.name);
        TextView role1 = rowView.findViewById(R.id.role);

        if (spinner.getSelectedItem().toString().equals(name.get(position)))
            rowView.findViewById(R.id.leader).setVisibility(View.VISIBLE);
        else
            rowView.findViewById(R.id.leader).setVisibility(View.INVISIBLE);

        name1.setText(name.get(position));
        role1.setText(role.get(position));

        rowView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm");
                builder.setCancelable(true);
                builder.setMessage("Remove " + name.get(position));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        name.remove(position);
                        phone.remove(position);
                        role.remove(position);
                        notifyDataSetChanged();
                        dataAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        return rowView;
    }


}


