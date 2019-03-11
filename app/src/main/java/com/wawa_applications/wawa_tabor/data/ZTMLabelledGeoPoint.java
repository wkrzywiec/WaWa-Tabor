package com.wawa_applications.wawa_tabor.data;

import android.location.Location;

import com.wawa_applications.wawa_tabor.data.dto.TransportInfoDTO;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.simplefastpoint.LabelledGeoPoint;

public class ZTMLabelledGeoPoint extends LabelledGeoPoint {

    private TransportInfoDTO transportInfoDTO;

    public ZTMLabelledGeoPoint(double aLatitude, double aLongitude) {
        super(aLatitude, aLongitude);
    }

    public ZTMLabelledGeoPoint(double aLatitude, double aLongitude, TransportInfoDTO aTransportInfoDTO) {
        super(aLatitude, aLongitude);
        this.transportInfoDTO = aTransportInfoDTO;
    }

    public ZTMLabelledGeoPoint(double aLatitude, double aLongitude, double aAltitude) {
        super(aLatitude, aLongitude, aAltitude);
    }

    public ZTMLabelledGeoPoint(double aLatitude, double aLongitude, double aAltitude, String aLabel) {
        super(aLatitude, aLongitude, aAltitude, aLabel);
    }

    public ZTMLabelledGeoPoint(Location aLocation) {
        super(aLocation);
    }

    public ZTMLabelledGeoPoint(GeoPoint aGeopoint) {
        super(aGeopoint);
    }

    public ZTMLabelledGeoPoint(double aLatitude, double aLongitude, String aLabel) {
        super(aLatitude, aLongitude, aLabel);
    }

    public ZTMLabelledGeoPoint(ZTMLabelledGeoPoint aZTMLabelledGeopoint) {
        this(aZTMLabelledGeopoint.getLatitude(), aZTMLabelledGeopoint.getLongitude()
                , aZTMLabelledGeopoint.getAltitude(), aZTMLabelledGeopoint.getLabel());
    }

    public TransportInfoDTO getTransportInfoDTO() {
        return transportInfoDTO;
    }

    public void setTransportInfoDTO(TransportInfoDTO transportInfoDTO) {
        this.transportInfoDTO = transportInfoDTO;
    }
}
