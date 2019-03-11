package com.wawa_applications.wawa_tabor.data.dto;

public class TransportInfoDTO {

    private String line;
    private String brigade;
    private String time;

    public TransportInfoDTO(String line, String brigade, String time) {
        this.line = line;
        this.brigade = brigade;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
