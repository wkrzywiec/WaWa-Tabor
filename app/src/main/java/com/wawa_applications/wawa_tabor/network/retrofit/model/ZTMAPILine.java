package com.wawa_applications.wawa_tabor.network.retrofit.model;

import com.google.gson.annotations.Expose;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ZTMAPILine {

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

    public ZTMAPILine(Double lat, Double lon, String time, String line, String brigade) {
        this.lat = lat;
        this.lon = lon;
        this.time = time;
        this.line = line;
        this.brigade = brigade;
    }
}
