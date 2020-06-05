import connections.DatabaseConnection;
import entities.Pet;
import entities.PetClinicShowCase;
import reflections.ReflectEntity;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        ReflectEntity reflectEntity = new ReflectEntity(Pet.class);
        databaseConnection.executeUpdate(reflectEntity.getDDLString());
        reflectEntity.of(PetClinicShowCase.class);
        databaseConnection.executeUpdate(reflectEntity.getDDLString());
    }
}
