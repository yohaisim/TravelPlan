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
            if (location != null && !location.trim().isEmpty()) {
                Log.d("MapFragment", "Location received: " + location);
                try {
                    String[] locParts = location.split(",");
                    if (locParts.length == 2) {
                        double lat = Double.parseDouble(locParts[0].trim());
                        double lng = Double.parseDouble(locParts[1].trim());
                        LatLng latLng = new LatLng(lat, lng);
                        googleMap.addMarker(new MarkerOptions().position(latLng).title("Attraction Location"));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15)); // Zoom level 15 for better view
                        Log.d("MapFragment", "Map updated with location: " + lat + ", " + lng);
                    } else {
                        Log.e("MapFragment", "Invalid location format: not enough parts");
                    }
                } catch (NumberFormatException e) {
                    Log.e("MapFragment", "Invalid location format: cannot parse to double", e);
                }
            } else {
                Log.e("MapFragment", "Location is null or empty");
                // Set default location to Disneyland Paris
                LatLng defaultLocation = new LatLng(48.8673893, 2.7810181);
                googleMap.addMarker(new MarkerOptions().position(defaultLocation).title("Default Location"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15));
                Log.d("MapFragment", "Map updated with default location");
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
