package ru.cwl.tsstorage.mainapp.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.cwl.tsstorage.mainapp.rest.dto.SensorNumDto;
import ru.cwl.tsstorage.mainapp.rest.dto.TrafficDto;
import ru.cwl.tsstorage.mainapp.simplecsvstorage.RamDataService;
import ru.cwl.tsstorage.mainapp.simplecsvstorage.TfcSensorCvs;
import ru.cwl.tsstorage.mainapp.simplecsvstorage.TrafficCvs;

import java.util.*;

@RestController

@RequestMapping(path = "/v1/api")
@RequiredArgsConstructor
public class MainRestController {
    private final RamDataService dataService;

    @GetMapping(path = "/traffic/{trId}/{from}/{to}")
    public List<TrafficDto> getTrafficForTr(@PathVariable long from, @PathVariable long to, @PathVariable long trId) {
        List<TrafficCvs> res = dataService.getTraffic(trId, from, to);
        return map(res);
    }

    private List<TrafficDto> map(List<TrafficCvs> res) {

        List<TrafficDto> result = new ArrayList<>(res.size());
        for (TrafficCvs t : res) {
            TrafficDto aa = TrafficDto.of(t.getGmteventtime(), t.getLat(),t.getLon(), t.getSpeed(), t.getHeading());
            result.add(aa);
        }
        return result;
    }

    private List<SensorNumDto> map2(List<TfcSensorCvs> res1) {
        List<SensorNumDto> result = new ArrayList<>(res1.size());
        for (TfcSensorCvs s : res1) {
            result.add(SensorNumDto.of(s.getGmteventtime(), (float) s.getVal()));
        }
        return result;
    }

    private Map<Integer, List<SensorNumDto>> map3(Map<Integer, List<TfcSensorCvs>> res1) {
        Map<Integer, List<SensorNumDto>> result = new HashMap<>();
        for (Map.Entry<Integer, List<TfcSensorCvs>> entry : res1.entrySet()) {
            result.put(entry.getKey(),map2(entry.getValue()));
        }
        return result;
    }

    @GetMapping(path = "/sensor-num/{trId}/{nNum}/{from}/{to}")
    public List<SensorNumDto> getSensorNum(@PathVariable long trId,
                                           @PathVariable int nNum, @PathVariable long from, @PathVariable long to) {
        List<TfcSensorCvs> res1 = dataService.getSensor(trId, nNum, from, to);
        return map2(res1);
    }


    @GetMapping(path = "/all-sensor-num/{trId}/{from}/{to}")
    public Map<Integer, List<SensorNumDto>> getAllSensorsNum(@PathVariable long from, @PathVariable long to, @PathVariable long trId) {
        Map<Integer, List<TfcSensorCvs>> res1 = dataService.getAllSensor(trId, from, to);
        return map3(res1);
    }


}
