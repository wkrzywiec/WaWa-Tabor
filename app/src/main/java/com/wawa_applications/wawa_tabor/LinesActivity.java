package com.wawa_applications.wawa_tabor;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wawa_applications.wawa_tabor.data.TransportContract;
import com.wawa_applications.wawa_tabor.data.dto.TransportInfoDTO;
import com.wawa_applications.wawa_tabor.network.retrofit.model.ZTMAPILine;
import com.wawa_applications.wawa_tabor.sync.DataSyncUtils;
import com.wawa_applications.wawa_tabor.viewmodel.LinesViewModel;


import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.stream.Collectors;

import static android.content.ContentValues.TAG;

public class LinesActivity extends AppCompatActivity implements  LoaderManager.LoaderCallbacks<Cursor> {

    private Context mContext = null;
    private MapView mapView = null;
    private MyLocationNewOverlay mLocationOverlay = null;

    private String mDisplayedLine;

    private static final int ID_LOADER = 88;
    private int lineType;
    private boolean isDataSyncStarted = false;

    private EditText mLineTextView;
    private LinesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mContext = getApplicationContext();
        Configuration.getInstance().load(mContext, PreferenceManager.getDefaultSharedPreferences(mContext));

        lineType = getIntent().getIntExtra(getString(R.string.line_type), 1);
        if (lineType == 1) {
            setContentView(R.layout.activity_buses);
        } else {
            setContentView(R.layout.activity_trams);
            setTitle(R.string.title_activity_trams);
        }

        mapView = (MapView) findViewById(R.id.map);
        this.onCreatePrepareMap();

        mLineTextView = (EditText) findViewById(R.id.edit_query);
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

        viewModel = ViewModelProviders.of(this).get(LinesViewModel.class);
        viewModel.getLineNo().observe(this, line -> {
            viewModel.subscribeBus(line);

            Toast toast = Toast.makeText(this, "Pobieranie danych dla lini: " + mDisplayedLine, Toast.LENGTH_LONG);
            toast.show();
        });

        viewModel.getTransportList().observe(this, transportList -> {
            Log.d("ZTM API call: ", transportList.stream().map(ZTMAPILine::toString).collect(Collectors.joining("||")));
        });
    }

    public void onResume(){

        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {

        super.onPause();
        mapView.onPause();

        viewModel.unSubscribeBus();
        if (isDataSyncStarted)
            DataSyncUtils.cancelScheduledJob();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        viewModel.unSubscribeBus();
        getContentResolver().delete(
                TransportContract.TransportEntry.TABLE_URI,
                null,
                null
        );
    }

    public void setDisplayedLine(View view){
        mDisplayedLine = mLineTextView.getText().toString();

        mDisplayedLine.toUpperCase();

        if(isDataSyncStarted){
            Log.v(TAG, "Job has finished? " + String.valueOf(DataSyncUtils.cancelScheduledJob()));
        }

        isDataSyncStarted = true;

        DataSyncUtils.initialize(this, lineType, mDisplayedLine);

        getSupportLoaderManager().restartLoader(ID_LOADER, null, this);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        Toast toast = Toast.makeText(this, "Pobieranie danych dla lini: " + mDisplayedLine, Toast.LENGTH_LONG);
        toast.show();

    }

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

            mapView.getOverlays().clear();
            data.moveToFirst();

            do {

                double lat = data.getDouble(data.getColumnIndex(TransportContract.TransportEntry.COLUMN_LAT));
                double lon = data.getDouble(data.getColumnIndex(TransportContract.TransportEntry.COLUMN_LON));

                TransportInfoDTO infoDTO = new TransportInfoDTO(
                            data.getString(data.getColumnIndex(TransportContract.TransportEntry.COLUMN_LINE)),
                            data.getString(data.getColumnIndex(TransportContract.TransportEntry.COLUMN_BRIGADE)),
                            data.getString(data.getColumnIndex(TransportContract.TransportEntry.COLUMN_TIME)));


                Marker marker = new Marker(mapView);
                marker.setPosition(new GeoPoint(lat, lon));

                Drawable markerIcon;
                if (lineType == 1) {
                    markerIcon = this.getResources().getDrawable(R.drawable.ic_bus);
                } else {
                    markerIcon = this.getResources().getDrawable(R.drawable.ic_tram);
                }
                marker.setIcon(markerIcon);
                marker.setTitle(infoDTO.getLine());
                marker.setSnippet("Brygada: " + infoDTO.getBrigade());
                marker.setSubDescription("Czas: " +infoDTO.getTime());

                marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {

                    @Override
                    public boolean onMarkerClick(Marker marker, MapView mapView) {
                        marker.showInfoWindow();
                        return true;
                    }
                });

                mapView.getOverlays().add(marker);


            } while (data.moveToNext());
            mapView.invalidate();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mapView.getOverlays().clear();
    }

    private void onCreatePrepareMap() {

        mapView.setTileSource(TileSourceFactory.MAPNIK);

        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        this.setMapZoom();
        this.setMapScaleBar();
        this.setMyLocation();
    }

    private void setMapZoom() {
        IMapController mapController = mapView.getController();
        mapController.setZoom(11);
        GeoPoint startPoint = new GeoPoint(52.2287235, 21.0188457);
        mapController.setCenter(startPoint);
    }

    private void setMapScaleBar() {
        final DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay(mapView);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        mapView.getOverlays().add(mScaleBarOverlay);
    }

    private void setMyLocation() {
        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(mContext), mapView);
        this.mLocationOverlay.enableMyLocation();
        mapView.getOverlays().add(this.mLocationOverlay);
    }

}

