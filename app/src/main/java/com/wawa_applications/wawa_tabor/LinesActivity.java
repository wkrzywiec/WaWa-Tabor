package com.wawa_applications.wawa_tabor;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wawa_applications.wawa_tabor.R;
import com.wawa_applications.wawa_tabor.data.TransportContract;
import com.wawa_applications.wawa_tabor.pref.WaWaTaborInfoWindow;
import com.wawa_applications.wawa_tabor.sync.DataSyncUtils;


import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import static android.content.ContentValues.TAG;

public class LinesActivity extends AppCompatActivity {

    private MapView map = null;
    private MyLocationNewOverlay mLocationOverlay = null;
    private LocationManager locationManager;
    private String mDisplayedLine;

    private static final int ID_LOADER = 88;
    private int lineType;
    private boolean isDataSyncStarted = false;

    private EditText mLineTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lineType = getIntent().getIntExtra(getString(R.string.line_type), 1);
        if (lineType == 1) {
            setContentView(R.layout.activity_buses);
        } else {
            setContentView(R.layout.activity_trams);
            setTitle(R.string.title_activity_trams);
        }

        // map fragment
        Context context = getApplicationContext();
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay(map);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        map.getOverlays().add(mScaleBarOverlay);

        IMapController mapController = map.getController();
        mapController.setZoom(11);
        GeoPoint startPoint = new GeoPoint(52.2287235, 21.0188457);
        mapController.setCenter(startPoint);

        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context),map);
        this.mLocationOverlay.enableMyLocation();
        map.getOverlays().add(this.mLocationOverlay);

        mLineTextView = (EditText) findViewById(R.id.edit_query);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        mLineTextView.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                if (actionID == EditorInfo.IME_ACTION_DONE) {
                    setDisplayedLine(textView);
                    return true;
                }
                return false;
            }
        });
    }

    public void onResume(){

        super.onResume();
        map.onResume();
    }

    @Override
    protected void onPause() {

        super.onPause();
        map.onPause();
        if (isDataSyncStarted)
            DataSyncUtils.cancelScheduledJob();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        getContentResolver().delete(
                TransportContract.TransportEntry.TABLE_URI,
                null,
                null
        );
    }


    /*@Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();

        LatLngBounds warsaw = new LatLngBounds(new LatLng(52.048272, 20.78179951), new LatLng(52.4175467, 21.18040289));
        mMap.setBuildingsEnabled(false);

        mMap.setInfoWindowAdapter(new WaWaTaborInfoWindow(this));
        setMapStyle();
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(warsaw.getCenter(), 10));

    }*/

    public void setDisplayedLine(View view){
        mDisplayedLine = mLineTextView.getText().toString();

        mDisplayedLine.toUpperCase();

        if(isDataSyncStarted){
            Log.v(TAG, "Job has finished? " + String.valueOf(DataSyncUtils.cancelScheduledJob()));
        }

        isDataSyncStarted = true;

        DataSyncUtils.initialize(this, lineType, mDisplayedLine);

        //getSupportLoaderManager().restartLoader(ID_LOADER, null, this);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        Toast toast = Toast.makeText(this, "Pobieranie danych dla lini: " + mDisplayedLine, Toast.LENGTH_LONG);
        toast.show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

/*
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

        String selection = TransportContract.TransportEntry.COLUMN_LINE + "= '" + mDisplayedLine + "' ";

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

        if (data.getCount() != 0){
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
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMap.clear();
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_settings:
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public boolean onMyLocationButtonClick() {

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this, "Brak sygnału GPS. Sprawdź, czy masz go włączonego.", Toast.LENGTH_LONG).show();
        }
        return false;
    }*/

    private void setMapStyle(){

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        String key = getResources().getString(R.string.map_style_key);
        String defaultValue = getResources().getString(R.string.map_style_value_default);

        String userMapStyle = sharedPref.getString(key, defaultValue);

        /*if (userMapStyle.equals(getResources().getString(R.string.map_style_value_black_and_white))){
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_black_and_white));

            } else if (userMapStyle.equals(getResources().getString(R.string.map_style_value_red_alert))){
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_red_alert));

            } else if (userMapStyle.equals(getResources().getString(R.string.map_style_value_retro))) {
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_retro));

            } else if (userMapStyle.equals(getResources().getString(R.string.map_style_value_night))) {
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_night));

            } else if (userMapStyle.equals(getResources().getString(R.string.map_style_value_greyscale))) {
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_greyscale));

            } else if (userMapStyle.equals(getResources().getString(R.string.map_style_value_toned))) {
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_toned));

            } else {
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_default));
            }*/
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } /*else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }*/
    }

}

