package ru.cwl.tsstorage.mainapp.simplecsvstorage;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class DataLoader {

    final String srcPath = "C:\\dev\\tmp\\refr\\";

//    @PostConstruct
    public void loadFromDisk() throws FileNotFoundException {


        List<TrafficCvs> result = loadTraffic();

        List<TfcSensorCvs> result2 = getTfcSensorCvs();

        List<TfcSensorCvs> resultAll = loadTfcSensorCvs();


        System.out.println(result2.size());

        System.out.println(resultAll.size());

    }

    List<TfcSensorCvs> getTfcSensorCvs() throws FileNotFoundException {
        final String fileName = srcPath + "TFC_SENSOR_21007.tsv";

        final Class aClass = TfcSensorCvs.class;

        return loadTsv(fileName, aClass);
    }

    List<TfcSensorCvs> loadTfcSensorCvs() throws FileNotFoundException {
        String inFiles[] = {
                "TFC_SENSOR_3.tsv",
                "TFC_SENSOR_21007.tsv",
                "TFC_SENSOR_111007.tsv",
                "TFC_SENSOR_112007.tsv",
                "TFC_SENSOR_113007.tsv",
                "TFC_SENSOR_115007.tsv",
                "TFC_SENSOR_905007.tsv",
                "TFC_SENSOR_906007.tsv",
                "TFC_SENSOR_945007.tsv"
        };
        List<TfcSensorCvs> resultAll = new ArrayList<>();

        for (String inFile : inFiles) {
            List<TfcSensorCvs> result3 = loadTsv(srcPath + inFile, TfcSensorCvs.class);
            resultAll.addAll(result3);
        }
        return resultAll;
    }

    List<TrafficCvs> loadTraffic() throws FileNotFoundException {
        return loadTsv(srcPath + "TRAFFIC.tsv", TrafficCvs.class);
    }

    private <T> List<T> loadTsv(String fileName, Class<T> aClass) throws FileNotFoundException {
        HeaderColumnNameToPropertyMappingStrategy<T> mappingStrategy = new HeaderColumnNameToPropertyMappingStrategy<T>(aClass);

        CsvToBean<T> tsvToBean = new CsvToBeanBuilder<T>(new FileReader(fileName))
                .withSeparator('\t')
                .withMappingStrategy(mappingStrategy)
                .withType(aClass).build();

        return tsvToBean.parse();
    }


}
