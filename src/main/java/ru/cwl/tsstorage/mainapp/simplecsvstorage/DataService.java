package ru.cwl.tsstorage.mainapp.simplecsvstorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataService {
    private final DataLoader dataLoader;

    @PostConstruct
    public void load() throws FileNotFoundException {
        List<TrafficCvs> traffic = dataLoader.loadTraffic();
        log.info("traffic {}", traffic.size());
        List<TfcSensorCvs> tfcSensor = dataLoader.loadTfcSensorCvs();
        log.info("sensor {}", tfcSensor.size());
    }
}
