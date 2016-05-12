package io.brant.examples.service;

import io.brant.examples.ColumnOrderHbm2DDL;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ColumnOrderHbm2DDL.class)
public class SchemaGeneratorTest {

    @Autowired
    private SchemaGenerator schemaGenerator;

    @Test
    public void createSchema() throws IOException, ClassNotFoundException {
        schemaGenerator.createSchema();
    }

}