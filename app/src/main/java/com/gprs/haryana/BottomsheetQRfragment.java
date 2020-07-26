package com.gprs.haryana;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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


public class BottomsheetQRfragment extends BottomSheetDialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.scanresult, container, false);

        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        TextView title1 = view.findViewById(R.id.title);

        TextView name1 = view.findViewById(R.id.name);
        name1.setText("Name : " + this.getArguments().getString("name"));
        TextView role1 = view.findViewById(R.id.role);
        role1.setText(this.getArguments().getString("role"));
        TextView phone1 = view.findViewById(R.id.phone);
        phone1.setText(this.getArguments().getString("phone"));
        TextView place1 = view.findViewById(R.id.place);
        place1.setText(this.getArguments().getString("place"));

        if (this.getArguments().getString("title").equals("normal")) {
            title1.setTextColor(Color.GREEN);
            title1.setText(this.getArguments().getString("name") + " is Safe !");
        } else if (this.getArguments().getString("title").equals("victim")) {
            title1.setTextColor(Color.RED);
            title1.setText(this.getArguments().getString("name") + " is found Victim !");
        }
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        final ImageView proimg = view.findViewById(R.id.proimg);

        if (mStorageRef.child(this.getArguments().getString("phone")) != null) {
            StorageReference sr = mStorageRef.child("proImg").child(this.getArguments().getString("phone") + ".jpg");
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
        return view;
    }

}