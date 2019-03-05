package com.wawa_applications.wawa_tabor.network.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ZTMAPIResult {

    @SerializedName("result")
    @Expose
    private List<ZTMAPILine> linesList = new ArrayList<ZTMAPILine>();

    public void addLine(ZTMAPILine line){

        if (linesList == null) linesList = new ArrayList<ZTMAPILine>();
        linesList.add(line);
    }
}
