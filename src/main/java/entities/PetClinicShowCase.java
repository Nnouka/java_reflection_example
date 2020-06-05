package entities;

import annotations.PrimaryKey;
import annotations.SQLTable;

import java.util.Date;
@SQLTable(name = "pet_clinical_show")
public class PetClinicShowCase {
    @PrimaryKey
    private Long id;
    private String name;
    private Date schedule;
}
