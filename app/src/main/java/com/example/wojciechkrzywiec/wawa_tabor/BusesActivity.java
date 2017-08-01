package com.example.wojciechkrzywiec.wawa_tabor;


import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.wojciechkrzywiec.wawa_tabor.data.TransportContract;
import com.example.wojciechkrzywiec.wawa_tabor.sync.DataSyncUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;


import static android.content.ContentValues.TAG;

public class BusesActivity extends FragmentActivity implements OnMapReadyCallback,
        LoaderManager.LoaderCallbacks<Cursor> {

    private GoogleMap mMap;

    private static final int ID_LOADER = 88;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buses);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        DataSyncUtils.initialize(this);
        getSupportLoaderManager().initLoader(ID_LOADER, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        LatLngBounds warsaw = new LatLngBounds(new LatLng(52.048272, 20.78179951), new LatLng(52.4175467, 21.18040289));

        /* Add a marker in Sydney and move the camera
        LatLng warsaw = new LatLng(52.229676, 21.012229);
        mMap.addMarker(new MarkerOptions().position(warsaw).title("Witaj w Warszawie!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(warsaw));*/
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(warsaw.getCenter(), 10));

    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

        String selection = TransportContract.TransportEntry.COLUMN_LINE + " = 131";
    switch (loaderId) {
        case ID_LOADER:
            return new CursorLoader(
                    this,
                    TransportContract.TransportEntry.TABLE_URI,
                    null,
                    selection,
                    null,
                    null);
        default:
            throw new RuntimeException("Loader Not Implemented: " + loaderId);
    }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        data.moveToFirst();

        Log.v(TAG,"Wszysttkich autobusów jest: " + String.valueOf(data.getCount()));

        do {
            double lat = data.getDouble(data.getColumnIndex(TransportContract.TransportEntry.COLUMN_LAT));
            double lon = data.getDouble(data.getColumnIndex(TransportContract.TransportEntry.COLUMN_LON));
            String line = data.getString(data.getColumnIndex(TransportContract.TransportEntry.COLUMN_LINE));
            LatLng position = new LatLng(lat, lon);

            mMap.addMarker(new MarkerOptions().position(position).title(line));
        } while (data.moveToNext());

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMap.clear();
    }


}
