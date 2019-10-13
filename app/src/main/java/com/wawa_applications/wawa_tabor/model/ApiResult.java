package com.wawa_applications.wawa_tabor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ApiResult {

    @SerializedName("result")
    @Expose
    private List<LineInfo> linesList = new ArrayList<LineInfo>();

    public void addLine(LineInfo line){

        if (linesList == null) linesList = new ArrayList<LineInfo>();
        linesList.add(line);
    }
}
