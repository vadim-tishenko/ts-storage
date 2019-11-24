package ru.cwl.tsstorage.mainapp;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Slf4j
public class ReadLargeCsv {
    public static void main(String[] args) throws FileNotFoundException {
//        String pathname = "C:/dev/tmp/refr2/tfc_sensor_1559001600.csv";
        String pathname = "C:/dev/tmp/refr2/tfc_sensor_1559606400.csv";
//        String pathname = "C:/dev/tmp/refr2/tfc_sensor_1560211200.csv";
        List<TfcSensorCvs2> sensorCvs2List = loadFromFileTfcSensorCvs2s(pathname);

        Map<Integer, Map<Long, Map<Integer, List<TfcSensorCvs2>>>> chunks = groupByChunks(sensorCvs2List);
        saveToDisk(chunks);

    }

    static List<TfcSensorCvs2> loadFromFileTfcSensorCvs2s(String pathname) throws FileNotFoundException {
        InputStream is = new FileInputStream(new File(pathname));
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        log.info("start");
//        long linesCount = br.lines().count();
//        log.info("count {}",linesCount);

        List<TfcSensorCvs2> sensorCvs2List = br.lines()
                .skip(1)
                .map(mapToTfcSensorCvs2)
//                .filter(person -> person.getAge() > 17)
//                .limit(500)
                .collect(toList());
        log.info("first: {}", sensorCvs2List.get(0));
        int size = sensorCvs2List.size();
        log.info("size: {}", size);
        log.info("last: {}", sensorCvs2List.get(size - 1));
        return sensorCvs2List;
    }

    private static void saveToDisk(Map<Integer, Map<Long, Map<Integer, List<TfcSensorCvs2>>>> res) {
        log.info("start saving");
        for (Map.Entry<Integer, Map<Long, Map<Integer, List<TfcSensorCvs2>>>> integerMapEntry : res.entrySet()) {
            int trId = integerMapEntry.getKey();
            log.info("save tr:{}",trId);
            Map<Long, Map<Integer, List<TfcSensorCvs2>>> chunks = integerMapEntry.getValue();
            for (Map.Entry<Long, Map<Integer, List<TfcSensorCvs2>>> longMapEntry : chunks.entrySet()) {
                long chunkId = longMapEntry.getKey();
                Map<Integer, List<TfcSensorCvs2>> vvv = longMapEntry.getValue();
                for (Map.Entry<Integer, List<TfcSensorCvs2>> integerListEntry : vvv.entrySet()) {
                    int sensorId = integerListEntry.getKey();
                    List<TfcSensorCvs2> chunkValue = integerListEntry.getValue();
                    int nNum = calcNNum(trId, sensorId);
                    appendToChunk(trId, chunkId, nNum, chunkValue);
                }
            }
        }
        log.info("finish saving");
    }

    private static void appendToChunk(int trId, long chunkId, int nNum, List<TfcSensorCvs2> chunkValue) {
        log.trace("tr:{} cId:{} n:{} s:{}", trId, chunkId, nNum, chunkValue.size());
        StringBuilder sb = new StringBuilder(chunkValue.size() * (12 + 1 + 6 + 1));
        // convert data to save block;
        for (TfcSensorCvs2 t : chunkValue) {
            sb.append(t.getGmteventtime()).append(',').append(t.getVal()).append('\n');
        }
//        sb.toString();
        // calc file name
        // mkdir, open file for append
        String chunkFileName = nNum + ".csv";
        String ROOT = "C:/dev/tmp/test-storage-2";

        Path pathToChunkDir = Paths.get(ROOT, "raw-sensor", "" + trId, "" + chunkId);
        if (!Files.isDirectory(pathToChunkDir, LinkOption.NOFOLLOW_LINKS)) {
            boolean mkdirRes = pathToChunkDir.toFile().mkdirs();
        }
//        Path path = Paths.get(pathToChunkDir, chunkFileName);
        Path pathToChunk = pathToChunkDir.resolve(chunkFileName);

        // save
        try {
            FileWriter fw = new FileWriter(pathToChunk.toFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(sb.toString());
            bw.close();
            fw.close();
//            Files.write(pathToChunk, sb.toString().getBytes(), StandardOpenOption.WRITE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            log.error("", e);
        }
        // close
    }

    private static int calcNNum(Integer trId, int sensorId) {
        // TODO: 24.11.2019
        return sensorId;
    }

    private static Map<Integer, Map<Long, Map<Integer, List<TfcSensorCvs2>>>> groupByChunks(List<TfcSensorCvs2> sensorCvs2List) {
        log.info("start grouping list: {}", sensorCvs2List.size());
        Map<Integer, Map<Long, Map<Integer, List<TfcSensorCvs2>>>> result = new HashMap<>();
        for (TfcSensorCvs2 t : sensorCvs2List) {
            long chunkId = calcDayChunkId(t.getGmteventtime());
            int idTr = t.getIdTr();
            int sensorId = t.getIdSensor();
            List<TfcSensorCvs2> list = result.computeIfAbsent(idTr, (i) -> new HashMap<>())
                    .computeIfAbsent(chunkId, (c) -> new HashMap<>()).computeIfAbsent(sensorId, (s) -> new ArrayList<>());
            list.add(t);
        }
        log.info("finish grouping(tr) {}", result.size());
        return result;
    }

    static long calcDayChunkId(long time) {
        long SECONDS_IN_DAY = 24L * 60 * 60;
        long result = (time / SECONDS_IN_DAY) * SECONDS_IN_DAY;
        return result;
    }

    //"ID_TR","GMTEVENTTIME","IS_LOCKED","ID_SENSOR","VAL","GMTSYSTIME","ID_SENSOR_TARGET"
    public static Function<String, TfcSensorCvs2> mapToTfcSensorCvs2 = (line) -> {
        String[] p = line.split(",");
        return new TfcSensorCvs2(
                Integer.parseInt(p[0]),
                Long.parseLong(p[1]),
//                Byte.parseByte(p[2]),
                Integer.parseInt(p[3]),
                Float.parseFloat(p[4])
//                ,
//                Long.parseLong(p[5]),
//                Long.parseLong(p[6])
        ); //Person(p[0], Integer.parseInt(p[1]), p[2], p[3]);
    };
}
