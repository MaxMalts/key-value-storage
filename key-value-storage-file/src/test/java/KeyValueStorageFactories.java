import org.maxmalts.Student;
import org.maxmalts.StudentKey;
import org.maxmalts.KeyValueStorage;
import org.maxmalts.MalformedDataException;

public abstract class KeyValueStorageFactories {
    protected abstract KeyValueStorage<String, String> buildStringsStorage(String path) throws MalformedDataException;

    protected abstract KeyValueStorage<Integer, Double> buildNumbersStorage(String path) throws MalformedDataException;

    protected abstract KeyValueStorage<StudentKey, Student> buildPojoStorage(String path) throws MalformedDataException;
}
