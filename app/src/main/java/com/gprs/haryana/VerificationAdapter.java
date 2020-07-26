package com.gprs.haryana;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class VerificationAdapter extends ArrayAdapter {
    Activity context;
    ArrayList<IdentityVerificationHelper> i;

    public VerificationAdapter(ArrayList<IdentityVerificationHelper> i, Activity context) {
        super(context, R.layout.verificationitem, i);
        this.context = context;
        this.i = i;
    }


    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = null;
        rowView = inflater.inflate(R.layout.verificationitem, null, true);
        TextView name, role, address;
        ImageView proimg;

        proimg = rowView.findViewById(R.id.proimg);
        name = rowView.findViewById(R.id.name);
        role = rowView.findViewById(R.id.role);
        address = rowView.findViewById(R.id.address);

        proimg.setImageBitmap(StringToBitMap(i.get(position).getPassphoto()));
        name.setText(i.get(position).getName());
        role.setText(i.get(position).getRole());
        address.setText(i.get(position).getAddress());
        return rowView;

    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


}
