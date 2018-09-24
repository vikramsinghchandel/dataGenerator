package com.vsc.dataGenerator.main;

import com.vsc.dataGenerator.model.SchemaConfig;
import com.vsc.dataGenerator.generator.RandomValueGenerator;
import com.vsc.dataGenerator.util.SchemaConfigGenerator;

import java.util.ArrayList;
import java.util.List;

public class Main {

    //private static final String CONFIG_FILE_PATH = "/home/waterline/d/workspace_intelliJ/corejava/wldDataGenerator/src/main/resources/modelSchemaConfig.json";
    private static final String CONFIG_FILE_PATH = "/home/waterline/d/workspace_intelliJ/corejava/wldDataGenerator/src/main/resources/SchemaConfig_MEDICAL.json";

    public static void main(String args[]) {

        List<Thread> randomValueGeneratorsThreadList = new ArrayList<>();

        try {
            SchemaConfig schemaConfig = SchemaConfigGenerator.getSchemaConfig(CONFIG_FILE_PATH);

            int numberOfParallelThreads = schemaConfig.getNumberOfParallelThreads();
            if (numberOfParallelThreads == 1) {

                RandomValueGenerator randomValueGenerator = new RandomValueGenerator();
                randomValueGenerator.generate(schemaConfig);
            } else if (numberOfParallelThreads > 1) {

                int perThreadRowCount = (int) (schemaConfig.getNumberOfRows() / schemaConfig.getNumberOfParallelThreads());
                schemaConfig.setNumberOfRows(perThreadRowCount);
                for (int i = 0; i < numberOfParallelThreads; i++) {

                    schemaConfig.setThreadName("Thread "+i);
                    Thread thread = new Thread(new RandomValueGenerator(schemaConfig));
                    thread.setName("Thread "+i);
                    thread.start();
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
