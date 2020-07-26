package com.gprs.haryana;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class Bottomsheetadmissionfragment extends BottomSheetDialogFragment {

    TextView hos, test, ord;
    String urlgo;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_hospital, container, false);


        hos = view.findViewById(R.id.hos);
        test = view.findViewById(R.id.test);
        ord = view.findViewById(R.id.ord);

        hos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlgo = "https://ors.gov.in/copp/welcome.jsp?NICSecurityORS=KVJF-IIQJ-X44U-BM3H-SPSG-SF4I-YIQL-DIRP";
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(urlgo));
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "You don't have browser installed", Toast.LENGTH_LONG).show();
                }
            }
        });

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlgo = "https://ors.gov.in/copp/welcome.jsp?NICSecurityORS=KVJF-IIQJ-X44U-BM3H-SPSG-SF4I-YIQL-DIRP";
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(urlgo));
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "You don't have browser installed", Toast.LENGTH_LONG).show();
                }
            }
        });

        ord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlgo = "https://ors.gov.in/copp/welcome.jsp?NICSecurityORS=KVJF-IIQJ-X44U-BM3H-SPSG-SF4I-YIQL-DIRP";
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(urlgo));
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "You don't have browser installed", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }


}