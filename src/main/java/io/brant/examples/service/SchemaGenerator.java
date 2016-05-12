package io.brant.examples.service;

import io.brant.examples.ColumnDefinition;
import io.brant.examples.annotation.ColumnPosition;
import org.apache.commons.io.IOUtils;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.springframework.boot.orm.jpa.hibernate.SpringNamingStrategy;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
public class SchemaGenerator extends SchemaGeneratorBase {

    public void createSchema() throws IOException, ClassNotFoundException {
        SchemaExport schemaExport = getSchemaExport();
        String scriptOutputPath = System.getProperty("java.io.tmpdir") + "/schema.sql";
        schemaExport.setOutputFile(scriptOutputPath);
        schemaExport.create(false, true);

        List<String> DDLs = IOUtils.readLines(new FileInputStream(scriptOutputPath), "UTF-8");
        List<String> convertedDDLs = new ArrayList<>();

        for (String DDL : DDLs) {
            if (DDL.toLowerCase().startsWith("create table")) {
                convertedDDLs.add(convert(DDL));
            } else {
                convertedDDLs.add(DDL);
            }
        }

        for (String convertedDDL : convertedDDLs) {
            System.out.println(convertedDDL);
            jdbcTemplate.execute(convertedDDL);
        }
    }

    private String convert(String ddl) throws ClassNotFoundException {
        StringBuilder convertedDDL = new StringBuilder();

        int startColumnBody = ddl.indexOf('(');
        int endColumnBody = ddl.lastIndexOf(')');

        String tableName = ddl.substring("create table ".length(), startColumnBody).trim();
        String columnBody = ddl.substring(startColumnBody + 1, endColumnBody);

        List<ColumnDefinition> columnDefinitions = Arrays.stream(columnBody.split(", ")).map(ColumnDefinition::new).collect(toList());

        ClassMetadata classMetadata = getClassMetaData(tableName);

        Class<?> clazz = Class.forName(classMetadata.getEntityName());
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            setPosition(field, columnDefinitions);
        }

        convertedDDL
                .append("create table")
                .append(" ")
                .append(tableName)
                .append(" ")
                .append("(");

        StringJoiner columns = new StringJoiner(", ");

        columnDefinitions
                .stream()
                .sorted(
                        Comparator.comparingInt(ColumnDefinition::getPosition)
                                .thenComparing(ColumnDefinition::getColumnName))
                .forEach(entityField -> {
                    columns.add(entityField.getColumnDefinition());
                });

        convertedDDL.append(columns.toString());

        convertedDDL.append(")");

        return convertedDDL.toString();
    }

    public void setPosition(Field field, List<ColumnDefinition> columnDefinitions) {
        String name = field.getName();
        String columnName;
        int position = Integer.MAX_VALUE - 10;

        Column column = field.getAnnotation(Column.class);
        ColumnPosition columnPosition = field.getAnnotation(ColumnPosition.class);

        if (column != null && !"".equals(column.name())) {
            columnName = column.name();
        } else {
            columnName = new SpringNamingStrategy().columnName(name);
        }

        if (columnPosition != null && columnPosition.value() > 0) {
            position = columnPosition.value();
        }

        for (ColumnDefinition columnDefinition : columnDefinitions) {
            if (columnDefinition.getColumnName().toLowerCase().equals(columnName.toLowerCase())) {
                columnDefinition.setPosition(position);
            }
        }
    }
}
