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


public class BottomsheetLabfragment extends BottomSheetDialogFragment {

    ImageView proimg;
    String name, phone, email, role;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.bottom_sheet_lab_layout, container, false);

        proimg = view.findViewById(R.id.proimg);

        name = this.getArguments().getString("name");
        role = this.getArguments().getString("role");
        email = this.getArguments().getString("email");
        phone = this.getArguments().getString("phone");

        view.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr=" + BottomsheetLabfragment.this.getArguments().getString("name")));
                    getContext().startActivity(intent);
                } catch (Exception e) {

                }
            }
        });


        TextView name1, role1, email1, phone1;
        name1 = view.findViewById(R.id.name);
        role1 = view.findViewById(R.id.role);
        phone1 = view.findViewById(R.id.mobile);
        email1 = view.findViewById(R.id.email);

        name1.setText(name);
        role1.setText(role);
        phone1.setText(phone);
        email1.setText(email);


        return view;
    }


}