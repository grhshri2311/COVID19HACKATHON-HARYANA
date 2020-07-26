package com.gprs.haryana;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class Bottomsheetpubliccarefragment extends BottomSheetDialogFragment {

    ImageView proimg;
    String name, type, district;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.bottom_sheet_publiccare_layout, container, false);

        proimg = view.findViewById(R.id.proimg);

        name = this.getArguments().getString("name");
        type = this.getArguments().getString("type");
        district = this.getArguments().getString("district");

        view.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr=" + Bottomsheetpubliccarefragment.this.getArguments().getString("name")));
                    getContext().startActivity(intent);
                } catch (Exception e) {

                }
            }
        });


        TextView name1, type1, district1;
        name1 = view.findViewById(R.id.name);
        type1 = view.findViewById(R.id.type);
        district1 = view.findViewById(R.id.district);

        name1.setText(name);
        type1.setText(type);
        district1.setText(district);
        return view;
    }


}