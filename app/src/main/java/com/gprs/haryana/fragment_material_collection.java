package com.gprs.haryana;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class fragment_material_collection extends Fragment implements OnMapReadyCallback {

    private UiSettings mUiSettings;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    MapView mapView;

    View view;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_material_collection, container, false);

        return view;
    }

    ArrayList<String> fname, phone, type, datetime, image;
    ArrayList<Double> lat, lon;


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        fname = new ArrayList<>();
        phone = new ArrayList<>();
        type = new ArrayList<>();
        datetime = new ArrayList<>();
        image = new ArrayList<>();
        lat = new ArrayList<>();
        lon = new ArrayList<>();


        mUiSettings = mMap.getUiSettings();

        // Keep the UI Settings state in sync with the checkboxes.
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mUiSettings.setScrollGesturesEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setTiltGesturesEnabled(true);
        mUiSettings.setRotateGesturesEnabled(true);


        FirebaseDatabase.getInstance().getReference().child("MaterialCollection").child("distribute").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    mMap.clear();
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        for (DataSnapshot d1 : d.getChildren()) {
                            MaterialCollectionHelper m = d1.getValue(MaterialCollectionHelper.class);
                            if (m != null) {
                                fname.add(m.getFname());
                                phone.add(m.getPhone());
                                datetime.add(m.getDatetime());
                                type.add(m.getType());
                                image.add(m.getImage());
                                lat.add(m.getLat());
                                lon.add(m.getLon());
                                LatLng latLng = new LatLng(m.getLat(), m.getLon());
                                Marker marker = mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(m.getFname())
                                        .snippet(m.getType())
                                        .icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_place_black_24dp)));
                                marker.showInfoWindow();
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                CustomInfoWindow customInfoWindow = new CustomInfoWindow(getActivity());
                                mMap.setInfoWindowAdapter(customInfoWindow);
                                marker.showInfoWindow();
                                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick(Marker marker) {
                                        view(marker.getTitle());
                                    }
                                });
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void view(String id) {

        int i;
        for (i = 0; i < fname.size(); i++) {
            if (fname.get(i).equals(id))
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putString("name", fname.get(i));
        bundle.putString("phone", phone.get(i));
        bundle.putString("type", type.get(i));
        bundle.putString("image", image.get(i));
        bundle.putString("time", datetime.get(i));
        bundle.putDouble("lat", lat.get(i));
        bundle.putDouble("lon", lon.get(i));

        BottomSheetDialogFragment f = new Bottomsheetmaterialviewfragment();
        f.setArguments(bundle);
        f.show(getActivity().getSupportFragmentManager(), "Dialog");


    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}