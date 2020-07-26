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

class MaterialdistributedAdapter extends ArrayAdapter {
    Activity context;
    ArrayList<String> time, type, image, key;

    public MaterialdistributedAdapter(Activity context, ArrayList<String> time, ArrayList<String> type, ArrayList<String> image, ArrayList<String> key) {
        super(context, R.layout.verificationitem, time);
        this.context = context;
        this.time = time;
        this.type = type;
        this.image = image;
        this.key = key;
    }


    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = null;
        rowView = inflater.inflate(R.layout.materialdistributeditem, null, true);
        TextView name, address;
        ImageView proimg;

        proimg = rowView.findViewById(R.id.proimg);
        name = rowView.findViewById(R.id.name);
        address = rowView.findViewById(R.id.address);

        proimg.setImageBitmap(StringToBitMap(image.get(position)));
        name.setText(type.get(position));
        address.setText(time.get(position));

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
