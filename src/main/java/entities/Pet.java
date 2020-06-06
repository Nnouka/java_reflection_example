package entities;

import annotations.PrimaryKey;
import annotations.SQLColumn;
import annotations.SQLTable;

import java.util.Date;

@SQLTable(name = "pets_catalogue")
public class Pet {
    @PrimaryKey
    @SQLColumn(name = "pet_id", nullable = false)
    private long id;
    @SQLColumn(name = "full_name")
    private String name;
    private int age;
    @SQLColumn(name = "pet_dob", nullable = false)
    private Date dateOfBirth;
}
