# Introducing reflection and annotations in java by example

This is a second story in the series, checkout the [master branch](https://github.com/Nnouka/java_reflection_example/tree/master) for a primer on reflections

## What is a java annotation

In real life, annotating a book means marking some sections and commenting or explaining them.

Keeping things short, annotations in java are comments or explanations NOT FOR THE PROGRAMMER, but for the program itself.

## What we will be doing

1. Create three (03) annotations
2. Add the annotations to our POJOs
3. Use our reflective class to get information from our annotations and make our sql better

If you haven't checkout the master branch for the primer on reflections now is the time to do so, because we will be building on it

### Create three (03) annotations

We have created 3 annotations

1. PrimaryKey

```java
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryKey {
    boolean autoIncrement() default true;
}
```

What do you think this will be used for?

Of course, it is used to explain to our program that a field will be the primary key

Furthermore, we can also indicate if this key should be autoIncrement or not, but it is autoIncrement by default in this case

Note.

We have used two in-built java annotations from the java.lang.annotations library.

@Target indicates to java compiler where this annotation should be used, 
 in this case it will be used to annotate a field, nothing more

if you intend to use it otherwise, you would do something like
 @Target({ElementType.FIELD, ElementType.TYPE, ...}) 

in this case, the three dots ... mean you can add more valid ElementTypes (your code will not compile with the 3 dots)

@Retention indicates your intention of when to use this annotation,
 it could be only for source code, or to keep it for the compiled class, or to keep it for the runtime

in this case we intend to keep it around even at runtime

2. SQLColumn

```java
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SQLColumn {
    String name();
    String defaultValue() default "none";
    boolean nullable() default true;
}
```

This will be used to give a custom name to our column,
 very useful if we want a column name different from the field name

We can also set a default value for the column, or indicate if our column will contain null values or not

3. SQLTable

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SQLTable {
    String name();
}
```

Hope you can quickly tell that this will be used in case we want a custom name for our table different from the Class name

### Add the annotations to our POJOs

We have added these annotations to our POJOs they look decorated now

1. entities.Pet
```java
@SQLTable(name = "pets_catalogue")
public class Pet {
    @PrimaryKey
    private long id;
    @SQLColumn(name = "full_name")
    private String name;
    private int age;
    @SQLColumn(name = "pet_dob", nullable = false)
    private Date dateOfBirth;
}
```
2. entities.PetClinicShowCase
```java
@SQLTable(name = "pet_clinical_show")
public class PetClinicShowCase {
    @PrimaryKey
    private Long id;
    private String name;
    private Date schedule;
}
```

### Use our reflective class to get information from our annotations and make our sql better

This is done in "reflections.ReflectEntity" class.

For our table name, we are interested in the @SQLTable annotation

```java
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
```
Notice how we reflect on the class to get all the annotations and specifically we pick out the SQLTable annotation

If its "name" value is not the empty string, we use it as the name of our table. Straight forward, isn't it?

For our columns, we are interested in @PrimaryKey and @SQLColumn

```java
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

                } else if (an instanceof PrimaryKey) { // support PrimaryKey
                    PrimaryKey key = (PrimaryKey) an;
                    typeName.append(key.autoIncrement() ? " AUTO_INCREMENT " : "").append("PRIMARY KEY ");
                }
            }
            fieldNames.put(fName, typeName.toString());
        }
        return fieldNames;
    }
```

# Results
When I ran the code I printed out the resulting sql statement to the console
```gherkin
Executing SQL Statement
CREATE TABLE IF NOT EXISTS pets_catalogue(full_name VARCHAR(255) NULL , prt_id BIGINT AUTO_INCREMENT PRIMARY KEY  NOT NULL , pet_dob DATE NOT NULL , age INTEGER )
Done
Executing SQL Statement
CREATE TABLE IF NOT EXISTS pet_clinical_show(schedule DATE, name VARCHAR(255), id BIGINT AUTO_INCREMENT PRIMARY KEY  )
Done

Process finished with exit code 0
```

Hope this gives you one reason to dive deep into advanced java and learn more about reflections and annotations

Our next series is on Files I/O

To read an entire file and allow manipulations, as one long string,
 as a list of lines or as a list of words

See you there!!!

