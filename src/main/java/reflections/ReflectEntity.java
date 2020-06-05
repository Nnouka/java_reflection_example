package reflections;

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

    private String getSQLTypeFromPrimitive(String type) {
        if ("int".equals(type) || "long".equals(type) || "Long".equals(type) || "Integer".equals(type) || "short".equals(type)) {
            return "INTEGER";
        } else if ("boolean".equals(type) || "byte".equals(type)) {
            return "BOOLEAN";
        } else if ("String".equals(type)) {
            return "VARCHAR(255)";
        } else if ("float".equals(type) || "double".equals(type) || "Double".equals(type)) {
            return "DECIMAL";
        }
        throw new RuntimeException("Cannot create SQL mapping for type: " + type);
    }
    public String getDDLString() {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        sb.append(Str.snakeCaseOfCamelCase(getEntityName()))
                .append("(");
        Map<String, String> fieldNames = getFieldNames();
        for (String name: fieldNames.keySet()) {
            sb.append(name).append(" ").append(getSQLTypeFromPrimitive(fieldNames.get(name))).append(", ");
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
