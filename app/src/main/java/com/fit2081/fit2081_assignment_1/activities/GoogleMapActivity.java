package com.fit2081.fit2081_assignment_1.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.fit2081.fit2081_assignment_1.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.fit2081.fit2081_assignment_1.databinding.ActivityGoogleMapBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GoogleMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityGoogleMapBinding binding;
    private String location;
    private Geocoder geocoder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGoogleMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Receive intent
        location = getIntent().getExtras().getString("location", "Australia");
        Log.d("Google map", "Location Name: " + location);

        // Instantiate GeoCoder
        geocoder = new Geocoder(this, Locale.getDefault());
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        findCountryMoveCamera();

        mMap.setOnMapClickListener(point -> {
            try {
                List<Address> addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
                if (!addresses.isEmpty()) {
                    String countryName = addresses.get(0).getCountryName();
                    Toast.makeText(GoogleMapActivity.this, String.format("Country: %s", countryName), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GoogleMapActivity.this, "No country found", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void findCountryMoveCamera() {
        // getFromLocationName method works for API 33 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            /**
             * countryToFocus: String value, any string we want to search
             * maxResults: how many results to return if search was successful
             * successCallback method: if results are found, this method will be executed
             *                          runs in a background thread
             */
            geocoder.getFromLocationName(location, 1, addresses -> {
                // if there are results, this condition would return true
                if (!addresses.isEmpty()) {
                    // run on UI thread as the user interface will update once set map location
                    runOnUiThread(() -> {
                        // define new LatLng variable using the first address from list of addresses
                        LatLng newAddressLocation = new LatLng(
                                addresses.get(0).getLatitude(),
                                addresses.get(0).getLongitude()
                        );

                        // repositions the camera according to newAddressLocation
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(newAddressLocation));

                        // Add a new Marker
                        Marker marker = mMap.addMarker(
                                new MarkerOptions()
                                        .position(newAddressLocation)
                                        .title(location)
                        );
                        marker.showInfoWindow();

                        // set zoom level to 8.5f or any number between range of 2.0 to 21.0
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f));
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Category address not found", Toast.LENGTH_SHORT).show();
                    });
//                    Toast.makeText(this, "Category address not found", Toast.LENGTH_SHORT).show();
                    Log.d("Google map", "No country found");
                }
            });
        }
    }
}