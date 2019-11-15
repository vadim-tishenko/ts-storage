package ru.cwl.tsstorage.mainapp.rest.dto;

import lombok.Data;

@Data
public class TrafficDto {
    long time;
    float lat;
    float lon;
    int speed;
    int heading;
    int alt;
}
