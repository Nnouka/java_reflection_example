# Introducing reflection in java by example

## What is reflection

In simple terms, it is getting information about a class at runtime

## What we will be doing

1. Create a database connection
2. Create sample POJOs to test our reflective class
3. Create a reflective class to get information about class name, field names and types
4. Use our reflective class to generate sql statements
5. Execute the generated sql statements on our database

### Create a database connection

For this we need a dependency, mysql connector j.

We will be using maven to help resolve this dependency into our class path.

We added the maven repository in this case like this:
```xml
<dependencies>
        <!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.20</version>
        </dependency>

</dependencies>
```
This gives us access to the necessary jdbc drivers for mysql connection.

We establish the database connection in the connections.DatabaseConnection class. Check it out to see its implementation.

### Create sample POJOs to test our reflective class

We have created two POJOs

1. entities.Pet
2. entities.PetClinicShowCase

### Create a reflective class to get information about class name, field names and types

This is done in reflections.ReflectEntity class.

Note. The key to reflecting the class and its properties is reflections.ReflectEntity.tClass property in the RelectEntiry class

We pass the Class<?> to reflect here
```java
public void of(Class<?> tClass) {
        this.tClass = tClass;
    }
```
We reflect on the class here

```java
public String getEntityName() {
        return tClass.getSimpleName();
    }
```
We reflect on the fields here

```java
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
```
### Use our reflective class to generate sql statements

We do this with the help of two methods
```java
private String getSQLTypeFromPrimitive(String type) {
        if ("int".equals(type) || "long".equals(type) || "Long".equals(type) || "Integer".equals(type) || "short".equals(type)) {
            return "INTEGER";
        } else if ("boolean".equals(type) || "byte".equals(type)) {
            return "BOOLEAN";
        } else if ("String".equals(type)) {
            return "VARCHAR(255)";
        } else if ("float".equals(type) || "double".equals(type) || "Double".equals(type)) {
            return "DECIMAL";
        } else if ("Date".equals(type) || "DateTime".equals(type) || "LocalDateTime".equals(type) || "LocalDate".equals(type)) {
            return "TIMESTAMP";
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
```

Note that this is a simple example to show the power of reflection in java and one of its usages, so don't expect this code to work like
 code from hibernate.org

### Execute the generated sql statements on our database

We execute the sql statements in our Main class
```java
public static void main(String[] args) {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        ReflectEntity reflectEntity = new ReflectEntity(Pet.class);
        databaseConnection.executeUpdate(reflectEntity.getDDLString());
        reflectEntity.of(PetClinicShowCase.class);
        databaseConnection.executeUpdate(reflectEntity.getDDLString());
    }
```
Note that the method call databaseConnection.executeUpdate(reflectEntity.getDDLString()); is built upon the normal 
Statment.executeUpdate(sqlStatement) of the java.sql package. This is how we used it:

```java
public Statement getStatement() {
        try {
            return getConnection().createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public void executeUpdate(String sqlStatement) {
        try {
            System.out.println("Executing SQL Statement");
            System.out.println(sqlStatement);

            getStatement().executeUpdate(sqlStatement);

            System.out.println("Done");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
```

# Results
When I ran the code I printed out the resulting sql statement to the console
```
Executing SQL Statement
CREATE TABLE IF NOT EXISTS pet(name VARCHAR(255), date_of_birth TIMESTAMP, age INTEGER )
Done
Executing SQL Statement
CREATE TABLE IF NOT EXISTS pet_clinic_show_case(schedule TIMESTAMP, name VARCHAR(255), id INTEGER )
Done

Process finished with exit code 0
```

