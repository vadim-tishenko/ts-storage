package ru.cwl.tsstorage.mainapp.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class TrafficDto {
    long time;
    float lat;
    float lon;
    int speed;
    int heading;
}
