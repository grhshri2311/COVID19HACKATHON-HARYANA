package com.gprs.haryana;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.core.view.ViewCompat;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class Bottomsheetlogoutfragment extends BottomSheetDialogFragment {

    ImageView proimg;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.bottom_sheet_logout_layout, container, false);

        proimg = view.findViewById(R.id.proimg);

        view.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.cancel(101);
                mNotificationManager.cancel(33);
                SharedPreferences pref;
                SharedPreferences.Editor editor;


                pref = getActivity().getApplicationContext().getSharedPreferences("user", 0); // 0 - for private mode
                editor = pref.edit();

                editor.clear();
                editor.apply();
                PreferenceManager.getDefaultSharedPreferences(getContext()).edit().clear().apply();
                if (isMyServiceRunning(VictimAlertForegroundNotification.class))
                    stopService(VictimAlertForegroundNotification.class);
                startActivity(new Intent(getContext(), logouthome.class));
                getActivity().finish();
            }
        });
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("user", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(pref.getString("user", ""));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    UserRegistrationHelper helper = dataSnapshot.getValue(UserRegistrationHelper.class);
                    TextView name, role, place, phone;
                    name = view.findViewById(R.id.name);
                    role = view.findViewById(R.id.role);
                    place = view.findViewById(R.id.place);
                    phone = view.findViewById(R.id.mobile);

                    name.setText(helper.getFname());
                    role.setText(helper.getRole());
                    place.setText(getActivity().getApplicationContext().getSharedPreferences("user", 0).getString("city", "") + ',' + getActivity().getApplicationContext().getSharedPreferences("user", 0).getString("state", ""));
                    phone.setText(helper.getPhone());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (mStorageRef.child(pref.getString("user", "")) != null) {
            StorageReference sr = mStorageRef.child("proImg").child(pref.getString("user", "") + ".jpg");
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