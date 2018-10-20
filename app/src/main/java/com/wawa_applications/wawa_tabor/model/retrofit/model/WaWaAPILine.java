package com.wawa_applications.wawa_tabor.model.retrofit.model;

import com.google.gson.annotations.Expose;

import lombok.Data;

@Data
public class WaWaAPILine {

    @Expose
    private Double lat;
    @Expose
    private Double lon;
    @Expose
    private String time;
    @Expose
    private String line;
    @Expose
    private String brigade;

    public WaWaAPILine(Double lat, Double lon, String time, String line, String brigade) {
        this.lat = lat;
        this.lon = lon;
        this.time = time;
        this.line = line;
        this.brigade = brigade;
    }
}
