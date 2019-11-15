package ru.cwl.tsstorage.mainapp.simplecsvstorage;

import lombok.Data;

@Data
public class TrafficCvs {
long idTraffic;
long gmteventtime;
byte isLocked;
long idTr;
byte isValidLocation;
String lat;
String lon;
float alt;
int speed;
int heading;
String eventvalue;
String idEvent;
String iostate;
String mileage;
long gmtsystime;
String idDeviceEvent;
long gmtlocationtime;
String odometr;

String unitRecnum;
    
}
