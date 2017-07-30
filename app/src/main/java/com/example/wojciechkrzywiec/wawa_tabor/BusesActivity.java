package com.example.wojciechkrzywiec.wawa_tabor;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


import com.example.wojciechkrzywiec.wawa_tabor.data.NetworkUtils;
import com.example.wojciechkrzywiec.wawa_tabor.data.OpenTransportJSONUtils;
import com.example.wojciechkrzywiec.wawa_tabor.sync.DataSyncUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.URL;

import static android.content.ContentValues.TAG;

public class BusesActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buses);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //DataSyncUtils.initialize(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        DataSyncUtils.initialize(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DataSyncUtils.cancelScheduledJob();
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

        // Add a marker in Sydney and move the camera
        LatLng warsaw = new LatLng(52.229676, 21.012229);
        mMap.addMarker(new MarkerOptions().position(warsaw).title("Witaj w Warszawie!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(warsaw));
    }


}
