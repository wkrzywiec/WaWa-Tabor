package com.wawa_applications.wawa_tabor;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wawa_applications.wawa_tabor.model.TransportInfoDTO;
import com.wawa_applications.wawa_tabor.model.Line;
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

import java.util.List;

public class LinesActivity extends AppCompatActivity {

    private Context mContext;
    private MapView mapView;
    private MyLocationNewOverlay mLocationOverlay;
    private String mDisplayedLine;
    private int lineType;
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

        viewModel.getLineListLiveData().observe(this, list -> {
           list.forEach(item -> Log.d("Dane autobusu: ", item.toString()));
           mapView.getOverlays().clear();
           addLineMarkersOntoMap(list);
        });

        viewModel.getLineNoLiveData().observe(this, line -> {
            Toast toast = Toast.makeText(this, "Pobieranie danych dla lini: " + mDisplayedLine, Toast.LENGTH_LONG);
            toast.show();
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        viewModel.unSubscribeBus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.unSubscribeBus();
    }

    public void setDisplayedLine(View view){
        mDisplayedLine = mLineTextView.getText().toString();
        mDisplayedLine.toUpperCase();
        viewModel.subscribeBus(mDisplayedLine);

        Toast toast = Toast.makeText(this, "Pobieranie danych dla lini: " + mDisplayedLine, Toast.LENGTH_LONG);
        toast.show();
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

    private void addLineMarkersOntoMap(List<Line> lineList) {
        lineList.forEach(
                line -> {

                    TransportInfoDTO infoDTO =
                            TransportInfoDTO.builder()
                                .line(line.getLine())
                                .brigade(line.getBrigade())
                                .time(line.getTime())
                                .build();

                    Marker marker = new Marker(mapView);
                    marker.setPosition(new GeoPoint(line.getLat(), line.getLon()));

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
                }
        );
        mapView.invalidate();
    }
}

