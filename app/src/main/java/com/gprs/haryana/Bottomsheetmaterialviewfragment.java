package com.gprs.haryana;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class Bottomsheetmaterialviewfragment extends BottomSheetDialogFragment {

    ImageView img;
    String proimg;
    String name, phone, timedate, type;
    Double lat, lon;
    TextView name1, phone1, type1, time1;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.bottom_sheet_materialview_layout, container, false);

        img = view.findViewById(R.id.proimg);

        name = this.getArguments().getString("name");
        type = this.getArguments().getString("type");
        timedate = this.getArguments().getString("time");
        phone = this.getArguments().getString("phone");
        proimg = this.getArguments().getString("image");
        lat = this.getArguments().getDouble("lat");
        lon = this.getArguments().getDouble("lon");

        name1 = view.findViewById(R.id.name);
        name1.setText(name);
        phone1 = view.findViewById(R.id.phone);
        phone1.setText(phone);
        type1 = view.findViewById(R.id.type);
        type1.setText(type);
        time1 = view.findViewById(R.id.time);
        time1.setText(timedate);

        phone1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                if (getActivity().checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
                Toast.makeText(getActivity(), "Connecting to Helpline ...", Toast.LENGTH_LONG).show();

            }
        });

        img.setImageBitmap(StringToBitMap(proimg));

        view.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr=" + lat + "," + lon));
                    getContext().startActivity(intent);
                } catch (Exception e) {

                }
            }
        });

        return view;
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