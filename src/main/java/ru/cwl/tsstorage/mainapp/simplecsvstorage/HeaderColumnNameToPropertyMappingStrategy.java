package ru.cwl.tsstorage.mainapp.simplecsvstorage;

import com.google.common.base.CaseFormat;
import com.opencsv.CSVReader;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.IOException;

public class HeaderColumnNameToPropertyMappingStrategy<T> extends HeaderColumnNameMappingStrategy<T> {

    public HeaderColumnNameToPropertyMappingStrategy(Class<T> aClass) {
        setType(aClass);
    }

    private String[] fieldNames =null;

    @Override
    public String getColumnName(int col) {
        return fieldNames[col];
    }

    @Override
    public void captureHeader(CSVReader reader) throws IOException, CsvRequiredFieldEmptyException {
        super.captureHeader(reader);
        synchronized (this) {
            if (fieldNames == null) {

                final int maxIndex = headerIndex.findMaxIndex() + 1;

                fieldNames = new String[maxIndex];
                for (int i = 0; i < maxIndex; i++) {
                    String name = headerIndex.getByPosition(i);
                    name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name);
                    fieldNames[i] = name;
                }
            }
        }
    }

    @Override
    public boolean isAnnotationDriven() {
        return false;
    }
}

