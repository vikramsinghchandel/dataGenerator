package com.vsc.dataGenerator.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.vsc.dataGenerator.model.ColumnType;
import com.vsc.dataGenerator.model.SchemaConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SchemaConfigGenerator {

    private static final String fileName = "..//corejava//wldDataGenerator//src/main/resources//modelSchemaConfig.json";

    public static void main(String args[]){
        generateJSONSchemaConfig();

    }

    public static void generateJSONSchemaConfig(){

        ObjectMapper objectMapper = new ObjectMapper();
        SchemaConfig schema = new SchemaConfig();
        try {

            FileOutputStream out = new FileOutputStream(fileName);
            objectMapper.writeValue(out, schema);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Successfully Generated JSON Configuration");
        }

    }


    public static SchemaConfig getSchemaConfig(String filePath) {

        ObjectMapper objectMapper = new ObjectMapper();
        File configurationFile = new File(filePath);
        SchemaConfig schemaConfig = null;
        try {
            schemaConfig = objectMapper.readValue(configurationFile, SchemaConfig.class);
        } catch (IOException ex) {
            //ex.printStackTrace();
            if(ex instanceof InvalidFormatException){
            System.out.println("Schema Mismatch. Only Following Column Types are valid");
            for(ColumnType columnType: ColumnType.values()){
                System.out.println(columnType.toString());

            }
            }
        }
        return schemaConfig;

    }
}
