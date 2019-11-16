package ru.cwl.tsstorage.mainapp.simplecsvstorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RamDataService {
    private final DataLoader dataLoader;

    private Map<Long, TreeMap<Long, TrafficCvs>> trafficStore = new HashMap<>();
    private Map<Long, Map<Integer, TreeMap<Long,TfcSensorCvs>>> sensorNumStore = new HashMap<>();


    @PostConstruct
    public void load() throws FileNotFoundException {
        loadTraffic();
        loadSensors();
    }

    private void loadSensors() throws FileNotFoundException {
        List<TfcSensorCvs> tfcSensor = dataLoader.loadTfcSensorCvs();
        log.info("sensor {}", tfcSensor.size());
        for (TfcSensorCvs s : tfcSensor) {
            long key = s.getIdTr();
            int nNum=(int)s.getIdSensor();
            long time=s.getGmteventtime();
            Map<Integer, TreeMap<Long,TfcSensorCvs>> res = sensorNumStore.computeIfAbsent(key, k -> new TreeMap<>());
            TreeMap<Long, TfcSensorCvs> m1 = res.computeIfAbsent(nNum, k -> new TreeMap<>());
            m1.put(time,s);
        }
        log.info("sz {}",sensorNumStore.size());
        log.info("{}",sensorNumStore.keySet());
    }

    private void loadTraffic() throws FileNotFoundException {
        List<TrafficCvs> traffic = dataLoader.loadTraffic();
        for (TrafficCvs trafficCvs : traffic) {
            if (trafficCvs == null) {
                log.warn("1");
                continue;
            }
            long key = trafficCvs.getIdTr();
            TreeMap<Long, TrafficCvs> res = trafficStore.computeIfAbsent(key, k -> new TreeMap<>());
            res.put(trafficCvs.getGmteventtime(), trafficCvs);
        }
        log.info("traffic loaded {} {}", traffic.size(), trafficStore.size());
        log.info("traffic trId {}", trafficStore.keySet());
    }

    public List<TrafficCvs> getTraffic(long trId, long from, long to) {
        TreeMap<Long, TrafficCvs> map = trafficStore.get(trId);
        if (map == null) return Collections.emptyList();
        return new ArrayList<>(map.subMap(from,to).values());
    }

    public List<TfcSensorCvs> getSensor(long trId, int nNum,long from, long to){
        Map<Integer, TreeMap<Long, TfcSensorCvs>> map = sensorNumStore.get(trId);
        if (map == null) return Collections.emptyList();
        TreeMap<Long, TfcSensorCvs> res = map.get(nNum);
        if(res==null) return Collections.emptyList();
        return new ArrayList<>(res.subMap(from,to).values());
    }

    public Map<Integer,List<TfcSensorCvs>> getAllSensor(long trId, long from, long to){
        Map<Integer, TreeMap<Long, TfcSensorCvs>> map = sensorNumStore.get(trId);
        if (map == null) return Collections.emptyMap();
        Set<Integer> allNNums = map.keySet();

        Map<Integer, List<TfcSensorCvs>> result=new HashMap<>();
        for (Integer nNum : allNNums) {
            TreeMap<Long, TfcSensorCvs> res = map.get(nNum);
            if(res==null) continue;
            result.put(nNum,new ArrayList<>(res.subMap(from,to).values()));
        }
        return result;
    }
}
