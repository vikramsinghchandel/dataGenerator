package com.vsc.dataGenerator.generator;

import com.vsc.dataGenerator.model.SchemaConfig;

public interface Generator {

    int min = 0;
    int max = 100000;

    void generate(SchemaConfig schemaConfig);
    boolean validate();

}
