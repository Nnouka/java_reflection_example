package reflections;

import annotations.PrimaryKey;
import annotations.SQLColumn;
import annotations.SQLTable;
import enums.SQLTypes;
import utils.SQLType;
import utils.Str;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.*;

public class ReflectEntity {
    private Class<?> tClass;

    public ReflectEntity(Class<?> tClass) {
        this.tClass = tClass;
    }
    public String getEntityName() {
        String name = tClass.getSimpleName();
        Annotation[] annotations = tClass.getAnnotations();
        for (Annotation an: annotations) {
            // support SQLTable
            if (an instanceof SQLTable) {
                SQLTable sqlTable = (SQLTable) an;
                String tName = sqlTable.name();
                if (!tName.trim().isEmpty()) name = sqlTable.name();
                // remember to take off this break statement if you add another annotation support
                break;
            }
        }
        return name;
    }
    public Map<String, String> getFieldNames() {
        Field[] fields = tClass.getFields();
        Field[] declaredFields = tClass.getDeclaredFields();
        // we use set here to ensure that in case were we have public fields in the superclass and subclass
        // we will eliminate the duplicate fields gotten from fields and declaredFields
        Set<Field> fieldSet = new HashSet<>(Arrays.asList(fields));
        fieldSet.addAll(Arrays.asList(declaredFields));
        Map<String, String> fieldNames = new HashMap<>();
        for (Field f: fieldSet) {
            String fName = Str.snakeCaseOfCamelCase(f.getName());
            StringBuilder typeName = new StringBuilder(SQLType.of(f.getType().getSimpleName()).getSqlType());
            Annotation[] annotations = f.getAnnotations();
            for (Annotation an: annotations) {
                // support SQLColumn
                if (an instanceof SQLColumn) {
                    SQLColumn sqlColumn = (SQLColumn) an;
                    String cName = sqlColumn.name();
                    String nullable = sqlColumn.nullable() ? " NULL " : " NOT NULL ";
                    typeName.append(nullable);
                    String defaultValue = sqlColumn.defaultValue();
                    if (!cName.trim().isEmpty()) fName = sqlColumn.name();
                    if (!defaultValue.equals("none")) typeName.append(" DEFAULT ").append(defaultValue);

                } else if (an instanceof PrimaryKey) {
                    PrimaryKey key = (PrimaryKey) an;
                    typeName.append(key.autoIncrement() ? " AUTO_INCREMENT " : "").append("PRIMARY KEY ");
                    // remember to take off this break statement if you add another annotation support
                    break;
                }
            }
            fieldNames.put(fName, typeName.toString());
        }
        return fieldNames;
    }
    public String getDDLString() {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        sb.append(Str.snakeCaseOfCamelCase(getEntityName()))
                .append("(");
        Map<String, String> fieldNames = getFieldNames();
        for (String name: fieldNames.keySet()) {
            sb.append(name).append(" ")
                    .append(fieldNames.get(name)).append(", ");
        }
        if (fieldNames.isEmpty()) {
            sb.append("id INTEGER AUTO_INCREMENT PRIMARY KEY");
        }
        if (sb.lastIndexOf(",") > -1) sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(")");
        return sb.toString();
    }
    public void of(Class<?> tClass) {
        this.tClass = tClass;
    }
}
