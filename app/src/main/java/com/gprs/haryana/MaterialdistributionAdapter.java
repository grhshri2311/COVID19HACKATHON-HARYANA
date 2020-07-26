package com.gprs.haryana;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class MaterialdistributionAdapter extends ArrayAdapter {
    Activity context;
    ArrayList<String> time, type, image, key;

    public MaterialdistributionAdapter(Activity context, ArrayList<String> time, ArrayList<String> type, ArrayList<String> image, ArrayList<String> key) {
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
        rowView = inflater.inflate(R.layout.materialdistributionitem, null, true);
        TextView name, address;
        ImageView proimg;

        proimg = rowView.findViewById(R.id.proimg);
        name = rowView.findViewById(R.id.name);
        address = rowView.findViewById(R.id.address);

        proimg.setImageBitmap(StringToBitMap(image.get(position)));
        name.setText(type.get(position));
        address.setText(time.get(position));

        Button button = rowView.findViewById(R.id.stop);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Confirm");
                alertDialog.setMessage("Stop Distributing " + type.get(position));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                final String currentDateTime = dateFormat.format(new Date()); // Find todays date
                                SharedPreferences pref = context.getSharedPreferences("user", 0);
                                MaterialCollectionHelper mm = new MaterialCollectionHelper(type.get(position), time.get(position), image.get(position));
                                FirebaseDatabase.getInstance().getReference().child("MaterialCollection").child("distributed").child(pref.getString("user", "")).child(currentDateTime).setValue(mm);
                                FirebaseDatabase.getInstance().getReference().child("MaterialCollection").child("distribute").child(pref.getString("user", "")).child(key.get(position)).removeValue();
                                Toast.makeText(context, "Distribution Stopped", Toast.LENGTH_LONG).show();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                alertDialog.show();
            }
        });
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
