import org.maxmalts.key_value_storage_database.*;

public class SingleFileStorageTest extends AbstractSingleStorageTest {
    @Override
    protected KeyValueStorage<String, String> buildStringsStorage() throws MalformedDataException {
        return new StringsStorage();
    }

    @Override
    protected KeyValueStorage<Integer, Double> buildNumbersStorage() throws MalformedDataException {
        return new NumbersStorage();
    }

    @Override
    protected KeyValueStorage<StudentKey, Student> buildPojoStorage() throws MalformedDataException {
        return new StudentsStorage();
    }
}
