package utils;

import enums.SQLTypes;

import static enums.SQLTypes.*;

public class SQLType {
    public static SQLTypes of(String s){
        switch (s) {
            case "int":
            case "Integer":
                return INTEGER;
            case "boolean":
            case "Boolean":
                return BOOLEAN;
            case "long":
            case "Long":
                return BIGINT;
            case "char":
                return CHAR;
            case "short":
                return SMALLINT;
            case "float":
            case "double":
            case "Double":
                return DOUBLE;
            case "byte[]":
                return VARBINARY;
            case "Date":
                return DATE;
            case "LocalDateTime":
            case "Timestamp":
                return TIMESTAMP;
            case "String":
                return VARCHAR;
            default:
                throw new RuntimeException("cannot create sql type from: " + s);
        }
    }
}
