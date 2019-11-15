package ru.cwl.tsstorage.mainapp.simplecsvstorage;

import lombok.Data;

@Data
public class TfcSensorCvs {
    long idTr;
    long gmteventtime;
    byte isLocked;
    long idSensor;
    double val;
    long gmtsystime;
    long idSensorTarget;
}
