package ru.cwl.tsstorage.mainapp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TfcSensorCvs2 {
    int idTr;
    long gmteventtime;
//    byte isLocked;
    int idSensor;
    float val;
//    long gmtsystime;
//    long idSensorTarget;
}
