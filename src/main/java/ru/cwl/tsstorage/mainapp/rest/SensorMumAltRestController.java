package ru.cwl.tsstorage.mainapp.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cwl.tsstorage.mainapp.simplecsvstorage.RamDataService;
import ru.cwl.tsstorage.mainapp.simplecsvstorage.TfcSensorCvs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController

@RequestMapping(path = "/v1/api")
@RequiredArgsConstructor
public class SensorMumAltRestController {
    private final RamDataService dataService;

    @GetMapping(path = "/sensor-num2/{trId}/{nNum}/{from}/{to}")
    public Map<Long,Float> getSensorNum2(@PathVariable long trId,
                                            @PathVariable int nNum, @PathVariable long from, @PathVariable long to) {
        List<TfcSensorCvs> res1 = dataService.getSensor(trId, nNum, from, to);
        return map2(res1);
    }

    private Map<Long, Float> map2(List<TfcSensorCvs> res1) {
        Map<Long, Float> result=new HashMap<>();
        for (TfcSensorCvs s : res1) {
            result.put(s.getGmteventtime(), (float) s.getVal());
        }
        return result;
    }


    @GetMapping(path = "/all-sensor-num2/{trId}/{from}/{to}")
    public Map<Integer, Map<Long,Float>> getAllSensorsNum2(@PathVariable long from, @PathVariable long to, @PathVariable long trId) {
        Map<Integer, List<TfcSensorCvs>> res1 = dataService.getAllSensor(trId, from, to);
        return map3(res1);
    }

    private Map<Integer, Map<Long, Float>> map3(Map<Integer, List<TfcSensorCvs>> res1) {
        Map<Integer, Map<Long, Float>> result=new HashMap<>();
        for (Map.Entry<Integer, List<TfcSensorCvs>> e : res1.entrySet()) {
            result.put(e.getKey(),map2(e.getValue()));
        }
        return result;
    }
}
