package entities;

import annotations.PrimaryKey;
import annotations.SQLColumn;
import annotations.SQLTable;

import java.util.Date;

@SQLTable(name = "pets_catalogue")
public class Pet {
    @PrimaryKey
    private long id;
    @SQLColumn(name = "full_name")
    private String name;
    private int age;
    private Date dateOfBirth;
}
