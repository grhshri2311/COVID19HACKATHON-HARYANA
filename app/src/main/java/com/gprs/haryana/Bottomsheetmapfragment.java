package com.gprs.haryana;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class Bottomsheetmapfragment extends BottomSheetDialogFragment {

    ImageView proimg;
    String name, phone, email, role;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.bottom_sheet_map_layout, container, false);

        proimg = view.findViewById(R.id.proimg);

        name = this.getArguments().getString("name");
        role = this.getArguments().getString("role");
        email = this.getArguments().getString("email");
        phone = this.getArguments().getString("phone");

        view.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

        TextView name1, role1, email1, phone1;
        name1 = view.findViewById(R.id.name);
        role1 = view.findViewById(R.id.role);
        phone1 = view.findViewById(R.id.mobile);
        email1 = view.findViewById(R.id.email);

        name1.setText(name);
        role1.setText(role);
        phone1.setText(phone);
        email1.setText(email);

        if (phone != null) {
            if (mStorageRef.child(phone) != null) {
                StorageReference sr = mStorageRef.child("proImg").child(phone + ".jpg");
                sr.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        proimg.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }
        }

        return view;
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void stopService(Class<?> serviceClass) {
        Intent serviceIntent = new Intent(getContext(), serviceClass);
        getActivity().stopService(serviceIntent);

    }
}