package com.vsc.dataGenerator.model;

import lombok.Data;

import java.util.LinkedHashMap;

@Data
public class SchemaConfig implements Cloneable {

    //<ColumnName, Column Type>
    LinkedHashMap<String, ColumnType> columns;
    long numberOfRows;
    int overlapFactor;
    String valueSeparator;
    String lineSeparator;
    String outputPath;
    // A zero value is treated as to create 1 single file
    long maxFileSizeInBytes;
    int numberOfParallelThreads;
    String threadName;

    public SchemaConfig() {

        this.columns = new LinkedHashMap<>();
        columns.put("firstName", ColumnType.FIRST_NAME);
        columns.put("lastName", ColumnType.LAST_NAME);
        columns.put("gender", ColumnType.GENDER);
        columns.put("age", ColumnType.AGE);
        columns.put("salary", ColumnType.INTEGER);
        columns.put("phoneNumber", ColumnType.PHONE_NUMBER);
        columns.put("address", ColumnType.ADDRESS);
        columns.put("dateOfBirth", ColumnType.DATE);
        columns.put("yearOfBirth", ColumnType.YEAR);
        columns.put("ssn", ColumnType.SSN);
        columns.put("bankBalance", ColumnType.DOUBLE);
        columns.put("creditCardNumber",ColumnType.CREDIT_CARD_NUMBER);
        columns.put("email",ColumnType.E_MAIL);

        this.numberOfRows = 500000;
        this.overlapFactor = 25;
        this.valueSeparator = "\t";
        this.lineSeparator = "\n";
        this.outputPath = "/home/waterline/d/workspace_intelliJ/corejava/wldDataGenerator/src/main/resources/";
        this.maxFileSizeInBytes = 20000000;
        this.numberOfParallelThreads = 1;
    }


}
