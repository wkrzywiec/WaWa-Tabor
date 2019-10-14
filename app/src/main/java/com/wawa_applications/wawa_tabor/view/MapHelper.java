package com.wawa_applications.wawa_tabor.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import com.wawa_applications.wawa_tabor.model.Line;
import com.wawa_applications.wawa_tabor.model.TransportInfoDTO;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.List;

public class MapHelper {

    private MapView mapView;
    private MyLocationNewOverlay mLocationOverlay;
    Context context;

    public MapHelper(MapView mapView, Context context){
        this.mapView = mapView;
        this.context = context;
    }

    public void onCreatePrepareMap() {
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        this.setMapZoom();
        this.setMapScaleBar();
        this.setMyLocation();
    }

    public void addLineMarkersOntoMap(List<Line> lineList, Drawable markerIcon) {
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

    private void setMapZoom() {
        IMapController mapController = mapView.getController();
        mapController.setZoom(11);
        GeoPoint startPoint = new GeoPoint(52.2287235, 21.0188457);
        mapController.setCenter(startPoint);
    }

    private void setMapScaleBar() {
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay(mapView);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        mapView.getOverlays().add(mScaleBarOverlay);
    }

    private void setMyLocation() {
        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context), mapView);
        this.mLocationOverlay.enableMyLocation();
        mapView.getOverlays().add(this.mLocationOverlay);
    }
}
