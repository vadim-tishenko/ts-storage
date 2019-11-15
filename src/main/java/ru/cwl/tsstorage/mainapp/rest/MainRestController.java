package ru.cwl.tsstorage.mainapp.rest;

import org.springframework.web.bind.annotation.*;
import ru.cwl.tsstorage.mainapp.rest.dto.SensorNumDto;
import ru.cwl.tsstorage.mainapp.rest.dto.TrafficDto;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController

@RequestMapping(path = "/v1/api")
public class MainRestController {
    @GetMapping(path = "/traffic/{from}/{to}/{trId}")
    public List<TrafficDto> getTrafficForTr(@PathVariable long from, @PathVariable long to, @PathVariable long trId) {
        List<TrafficDto> result = Collections.emptyList();
        return result;
    }

    @GetMapping(path = "/sensor-num/{from}/{to}/{trId}/{nNum}")
    public List<SensorNumDto> getSensorNum(@PathVariable long from, @PathVariable long to, @PathVariable long trId,
                                           @PathVariable int nNum) {
        List<SensorNumDto> result = Collections.emptyList();
        return result;
    }

    @GetMapping(path = "/all-sensor-num/{from}/{to}/{trId}")
    public Map<Integer, List<SensorNumDto>> getAllSensorsNum(@PathVariable long from, @PathVariable long to, @PathVariable long trId) {
        Map<Integer, List<SensorNumDto>> result = Collections.emptyMap();
        return result;
    }
}
