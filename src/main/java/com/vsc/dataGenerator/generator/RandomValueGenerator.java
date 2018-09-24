package com.vsc.dataGenerator.generator;

import com.github.javafaker.Faker;
import com.vsc.dataGenerator.model.SchemaConfig;
import com.vsc.dataGenerator.model.ColumnType;
import org.apache.commons.lang3.StringUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class RandomValueGenerator extends Thread implements Generator {

    long originalRowCount;
    private String OUTPUT_FILE_NAME = "outputData";
    int fileCounter;
    int originalColumnCount;
    SchemaConfig schemaConfig;

    @Override
    public void generate(SchemaConfig schemaConfig) {

        this.originalRowCount = schemaConfig.getNumberOfRows();
        this.originalColumnCount = schemaConfig.getColumns().size();
        generateRaw(schemaConfig);
        generateOverLapped(schemaConfig);
        System.out.println("Data Generation is successful......");


    }

    public RandomValueGenerator() {

    }

    public RandomValueGenerator(SchemaConfig schemaConfig) {
        this.schemaConfig = schemaConfig;

    }

    @Override
    public boolean validate() {
        return true;
    }

    private void generateRaw(SchemaConfig schemaConfig) {
        LinkedHashMap<String, ColumnType> columns = schemaConfig.getColumns();
        Set<String> columnNames = columns.keySet();
        long rowCount = schemaConfig.getNumberOfRows();
        String valueSeparator = schemaConfig.getValueSeparator();
        String lineSeparator = schemaConfig.getLineSeparator();
        long maxFileSizeInBytes = schemaConfig.getMaxFileSizeInBytes();
        long rowSizeRunningSum = 0;

        DateFormat onlyDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:SSS");
        DateFormat yearFormat = new SimpleDateFormat("yyyy");

        //DateFormat onlyDateFormat = new SimpleDateFormat("yyyy-MM-dd");


        String genderArray[] = {"Male", "Female"};


        FileWriter fileWriter = null;
        Faker faker = new Faker();

        try {
            String threadName = this.getName();

            fileWriter = new FileWriter(schemaConfig.getOutputPath() + OUTPUT_FILE_NAME + "_" + fileCounter +
                    threadName + ".csv");
            fileWriter.write(StringUtils.join(columnNames, valueSeparator));
            fileWriter.write(lineSeparator);


            for (int i = 0; i < rowCount; i++) {

                List<String> row = new ArrayList<>();

                for (ColumnType column : columns.values()) {

                    ColumnType value = column;
                    Date birthday = faker.date().birthday();

                    switch (value) {

                        case FIRST_NAME:
                            row.add(faker.name().firstName());
                            break;
                        case LAST_NAME:
                            row.add(faker.name().lastName());
                            break;
                        case FULL_NAME:
                            row.add(faker.name().fullName());
                            break;
                        case GENDER:
                            row.add(genderArray[new Random().nextInt(genderArray.length)]);
                            break;
                        case AGE:
                            row.add(faker.random().nextInt(1, 99).toString());
                            break;
                        case SSN:
                            row.add(faker.idNumber().ssnValid());
                            break;
                        case PHONE_NUMBER:
                            row.add(faker.phoneNumber().phoneNumber());
                            break;
                        case CELL_PHONE:
                            row.add(faker.phoneNumber().cellPhone());
                            break;
                        case ADDRESS:
                            row.add(faker.address().fullAddress());
                            break;
                        case INTEGER:
                            row.add(faker.random().nextInt(Generator.min, Generator.max).toString());
                            break;
                        case DOUBLE:
                            row.add(Double.toString(Generator.min + (Generator.max * new Random().nextDouble())));
                            break;
                        case COUNTRY:
                            row.add(faker.address().country());
                        case CITY:
                            row.add(faker.address().cityName());
                            break;
                        case STATE:
                            row.add(faker.address().state());
                            break;
                        case ZIP_CODE:
                            row.add(faker.address().zipCode());
                            break;
                        case E_MAIL:
                            row.add(faker.internet().emailAddress());
                            break;
                        case CREDIT_CARD_NUMBER:
                            row.add(faker.finance().creditCard());
                            break;
                        case CREDIT_CARD_TYPE:
                            row.add(faker.business().creditCardType());
                            break;
                        case DATA_TIME:
                            row.add(dateTimeFormat.format(birthday));
                            break;
                        case DATE:
                            row.add(onlyDateFormat.format(birthday));
                            break;
                        case YEAR:
                            row.add(yearFormat.format(birthday));
                            break;
                        case TIME_STAMP:
                            row.add(Long.toString(birthday.getTime()));
                            break;
                        case UNIVERSITY:
                            row.add(faker.university().name());
                            break;
                        case INDUSTRY:
                            row.add(faker.company().industry());
                            break;
                        case DEPARTMENT:
                            row.add(faker.commerce().department());
                            break;
                        case CURRENT_SYS_TIME:
                            row.add(Long.toString(System.currentTimeMillis()));
                            break;
                        case DISEASE:
                            row.add(faker.medical().diseaseName());
                            break;
                        case MEDICINE:
                            row.add(faker.medical().medicineName());
                            break;
                        case HOSPITAL:
                            row.add(faker.medical().hospitalName());
                            break;
                        case SYMPTOMS:
                            row.add(faker.medical().symptoms());
                            break;
                        default:
                            row.add("");
                            break;

                    }
                }
                String rowString = StringUtils.join(row, valueSeparator);
                rowSizeRunningSum += rowString.getBytes().length;

                fileWriter.write(rowString);
                fileWriter.write(lineSeparator);
                synchronized (schemaConfig) {
                    schemaConfig.setNumberOfRows(schemaConfig.getNumberOfRows() - 1);
                }
                if (rowSizeRunningSum >= maxFileSizeInBytes && maxFileSizeInBytes != 0) {
                    fileCounter++;
                    break;
                }
            }

            fileWriter.close();


            if (schemaConfig.getNumberOfRows() > 0) {
                System.out.println("Going to write next file as file is rolled because of size");
                generateRaw(schemaConfig);
            }

        } catch (IOException | ConcurrentModificationException e) {
            if (e instanceof ConcurrentModificationException) {
                System.out.println("Concurrent Modification Happened No Need To Worry.....");
            } else {
                e.printStackTrace();
            }
        }
        if (fileWriter != null) {
            try {
                fileWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void generateOverLapped(SchemaConfig schemaConfig) {

        SchemaConfig updateSchemaConfig = schemaConfig;

        int overLapFactor = schemaConfig.getOverlapFactor();
        long overlappedRowCount = (originalRowCount * overLapFactor / 100);
        int newOverLappedColumnCount = (schemaConfig.getColumns().keySet().size() * overLapFactor) / 100;
        int runningColumnCount = 0;
        List<ColumnType> columnTypesList = Arrays.asList(ColumnType.values());


        LinkedHashMap<String, ColumnType> columns = schemaConfig.getColumns();

        List<String> columnNames = new ArrayList<>(columns.keySet());

        while (columns.size() > newOverLappedColumnCount) {
            int index = new Random().nextInt(columns.size());
            columns.remove(columnNames.get(index));
            columnNames.remove(index);
            Collections.shuffle(columnNames);
        }

        while (columns.size() < originalColumnCount) {
            runningColumnCount++;
            ColumnType columnType = columnTypesList.get(new Random().nextInt(columnTypesList.size()));
            columns.put(columnType.toString(), columnType);
        }

        updateSchemaConfig.setNumberOfRows(overlappedRowCount);
        updateSchemaConfig.setColumns(columns);
        OUTPUT_FILE_NAME = OUTPUT_FILE_NAME + "_overLapped";
        fileCounter = 0;

        //updateSchemaConfig.setOutputPath(schemaConfig.getOutputPath().split("\\.")[0]+"_overLapped.csv");

        System.out.println("Starting Ovelapped Data generation Now.....");
        generateRaw(updateSchemaConfig);

    }

    @Override
    public void run() {
        generate(schemaConfig);
    }
}
