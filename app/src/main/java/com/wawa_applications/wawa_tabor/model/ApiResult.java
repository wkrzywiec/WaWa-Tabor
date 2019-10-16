package com.wawa_applications.wawa_tabor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class ApiResult {

    @SerializedName("result")
    @Expose
    private List<Line> linesList = new ArrayList<Line>();

    public void addLine(Line line){

        if (linesList == null) linesList = new ArrayList<Line>();
        linesList.add(line);
    }

    public List<Line> getLinesList() {
        return linesList;
    }

    public void setLinesList(List<Line> linesList) {
        this.linesList = linesList;
    }
}
