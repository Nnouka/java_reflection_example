package reflections;

import enums.SQLTypes;
import utils.SQLType;
import utils.Str;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ReflectEntity {
    private Class<?> tClass;

    public ReflectEntity(Class<?> tClass) {
        this.tClass = tClass;
    }
    public String getEntityName() {
        return tClass.getSimpleName();
    }
    public Map<String, String> getFieldNames() {
        Field[] fields = tClass.getFields();
        Field[] declaredFields = tClass.getDeclaredFields();
        Map<String, String> fieldNames = new HashMap<String, String>();
        for (Field f: fields) {
            fieldNames.put(f.getName(), f.getType().getSimpleName());
        }
        for (Field f: declaredFields) {
            if (!fieldNames.containsKey(f.getName())) fieldNames.put(f.getName(), f.getType().getSimpleName());
        }
        return fieldNames;
    }
    public String getDDLString() {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        sb.append(Str.snakeCaseOfCamelCase(getEntityName()))
                .append("(");
        Map<String, String> fieldNames = getFieldNames();
        for (String name: fieldNames.keySet()) {
            sb.append(Str.snakeCaseOfCamelCase(name)).append(" ")
                    .append(SQLType.of(fieldNames.get(name)).getSqlType()).append(", ");
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
