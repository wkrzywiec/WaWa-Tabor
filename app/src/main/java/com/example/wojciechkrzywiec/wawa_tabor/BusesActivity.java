package com.example.wojciechkrzywiec.wawa_tabor;



import android.database.Cursor;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.wojciechkrzywiec.wawa_tabor.data.TransportContract;
import com.example.wojciechkrzywiec.wawa_tabor.sync.DataSyncUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;


import static android.content.ContentValues.TAG;

public class BusesActivity extends AppCompatActivity implements OnMapReadyCallback,
        LoaderManager.LoaderCallbacks<Cursor> {

    private GoogleMap mMap;

    private static final int ID_LOADER = 88;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buses);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        DataSyncUtils.initialize(this);
        getSupportLoaderManager().initLoader(ID_LOADER, null, this);

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
        mMap.clear();
        LatLngBounds warsaw = new LatLngBounds(new LatLng(52.048272, 20.78179951), new LatLng(52.4175467, 21.18040289));
        mMap.setBuildingsEnabled(false);
        mMap.setInfoWindowAdapter(new WaWaTaborInfoWindow(this));
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(warsaw.getCenter(), 10));


    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

        String selection = TransportContract.TransportEntry.COLUMN_LINE + " = 522";
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
        mMap.clear();
        data.moveToFirst();

        Log.v(TAG,"Wszysttkich autobusów jest: " + String.valueOf(data.getCount()));

        do {
            double lat = data.getDouble(data.getColumnIndex(TransportContract.TransportEntry.COLUMN_LAT));
            double lon = data.getDouble(data.getColumnIndex(TransportContract.TransportEntry.COLUMN_LON));
            String line =
                    data.getString(data.getColumnIndex(TransportContract.TransportEntry.COLUMN_LINE));
            String busDetails =  data.getString(data.getColumnIndex(TransportContract.TransportEntry.COLUMN_BRIGADE))
                    + "," + data.getString(data.getColumnIndex(TransportContract.TransportEntry.COLUMN_TIME));
            LatLng position = new LatLng(lat, lon);

            mMap.addMarker(new MarkerOptions().position(position).title(line).snippet(busDetails));
        } while (data.moveToNext());

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMap.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

