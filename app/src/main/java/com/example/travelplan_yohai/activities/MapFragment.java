package com.example.travelplan_yohai.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.travelplan_yohai.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment {

    private static final String ARG_LOCATION = "location";
    private String location;

    public static MapFragment newInstance(String location) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            if (location != null) {
                Log.d("MapFragment", "Location received: " + location);
                try {
                    String[] locParts = location.split(",");
                    LatLng latLng = new LatLng(Double.parseDouble(locParts[0]), Double.parseDouble(locParts[1]));
                    googleMap.addMarker(new MarkerOptions().position(latLng).title("Attraction Location"));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12)); // Adjust zoom level as needed
                } catch (NumberFormatException e) {
                    Log.e("MapFragment", "Invalid location format", e);
                }
            } else {
                Log.e("MapFragment", "Location is null");
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            location = getArguments().getString(ARG_LOCATION);
        }
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}
