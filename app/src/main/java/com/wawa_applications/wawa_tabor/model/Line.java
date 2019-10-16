package com.wawa_applications.wawa_tabor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Line {

    @Expose
    @SerializedName("Lat")
    private Double lat;

    @Expose
    @SerializedName("Lon")
    private Double lon;

    @Expose
    @SerializedName("Time")
    private String time;

    @Expose
    @SerializedName("Lines")
    private String line;

    @Expose
    @SerializedName("Brigade")
    private String brigade;

    public Line() {};

    public Line(Double lat, Double lon, String time, String line, String brigade) {
        this.lat = lat;
        this.lon = lon;
        this.time = time;
        this.line = line;
        this.brigade = brigade;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getBrigade() {
        return brigade;
    }

    public void setBrigade(String brigade) {
        this.brigade = brigade;
    }
}
