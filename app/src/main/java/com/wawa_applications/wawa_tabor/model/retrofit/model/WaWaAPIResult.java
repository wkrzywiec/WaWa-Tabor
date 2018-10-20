package com.wawa_applications.wawa_tabor.model.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class WaWaAPIResult {

    @SerializedName("result")
    @Expose
    private List<WaWaAPILine> linesList = new ArrayList<WaWaAPILine>();

    public void addLine(WaWaAPILine line){

        if (linesList == null) linesList = new ArrayList<WaWaAPILine>();
        linesList.add(line);
    }
}
