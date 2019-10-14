package com.wawa_applications.wawa_tabor.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TransportInfoDTO {

    private String line;
    private String brigade;
    private String time;
}
