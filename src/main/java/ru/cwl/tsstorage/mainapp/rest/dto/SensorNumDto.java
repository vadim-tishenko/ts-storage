package ru.cwl.tsstorage.mainapp.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class SensorNumDto {
    long time;
    float value;
}
