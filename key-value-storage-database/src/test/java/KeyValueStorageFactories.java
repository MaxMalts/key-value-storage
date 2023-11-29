import org.maxmalts.key_value_storage_database.KeyValueStorage;
import org.maxmalts.key_value_storage_database.MalformedDataException;
import org.maxmalts.key_value_storage_database.Student;
import org.maxmalts.key_value_storage_database.StudentKey;

public abstract class KeyValueStorageFactories {
    protected abstract KeyValueStorage<String, String> buildStringsStorage() throws MalformedDataException;

    protected abstract KeyValueStorage<Integer, Double> buildNumbersStorage() throws MalformedDataException;

    protected abstract KeyValueStorage<StudentKey, Student> buildPojoStorage() throws MalformedDataException;
}
