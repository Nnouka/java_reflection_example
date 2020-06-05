# Introducing reflection in java by example

## What is reflection

In simple terms, it is getting information about a class and runtime

## What we will be doing

1. Create a database connection
2. Create sample POJOs to test our reflective class
3. Create a reflective class that gets information about class name, field names and types
4. Use our reflective class to generate sql statements
5. Execute the generated sql statements on our database

### Create a database connection

For this we need a dependency, mysql connector j.

We will be using maven to help resolve this dependency into our class path.

The maven repository in this case is add like this:
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