package ru.cwl.tsstorage.mainapp.simplecsvstorage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TfcSensorCvs {
    long idTr;
    long gmteventtime;
    byte isLocked;
    long idSensor;
    double val;
    long gmtsystime;
    long idSensorTarget;
}
