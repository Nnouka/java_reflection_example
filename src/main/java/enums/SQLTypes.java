package enums;

public enum SQLTypes {
    INTEGER("INTEGER"),
    BIGINT("BIGINT"),
    CHAR("CHAR"),
    SMALLINT("SMALLINT"),
    REAL("REAL"),
    DOUBLE("DOUBLE"),
    VARBINARY("VARBINARY"),
    DATE("DATE"),
    TIMESTAMP("TIMESTAMP"),
    VARCHAR("VARCHAR(255)"),
    BOOLEAN("BOOLEAN");

    private String sqlType;

    SQLTypes(String sqlType) {
        this.sqlType = sqlType;
    }

    public String getSqlType() {
        return sqlType;
    }
}
