import org.junit.Assert;
import org.junit.Test;
import org.maxmalts.key_value_storage_database.KeyValueStorage;
import org.maxmalts.key_value_storage_database.Student;
import org.maxmalts.key_value_storage_database.StudentKey;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.concurrent.Callable;

import static org.junit.Assert.*;

/**
 * Оснастка для функционального тестирования {@link KeyValueStorage}.
 * Для запуска нужно завести конкретный класс-потомок и определить соответствующие фабричные методы.
 */
public abstract class AbstractSingleStorageTest extends KeyValueStorageFactories {

    public static final AnnotationConfigApplicationContext CONTEXT =
        new AnnotationConfigApplicationContext(SpringConfig.class);

    public static final StudentKey KEY_1 = CONTEXT.getBean("studentKey1", StudentKey.class);
    public static final Student VALUE_1 = CONTEXT.getBean("student1", Student.class);

    public static final StudentKey KEY_2 = CONTEXT.getBean("studentKey2", StudentKey.class);
    public static final Student VALUE_2 = CONTEXT.getBean("student2", Student.class);

    public static final StudentKey KEY_3 = CONTEXT.getBean("studentKey3", StudentKey.class);
    public static final Student VALUE_3 = CONTEXT.getBean("student3", Student.class);

    @Test
    public void testReadWrite() {
        StorageTestUtils.doRunTest(() -> doWithStrings(storage -> {
            storage.write("foo", "bar");
            Assert.assertEquals("bar", storage.read("foo"));
            Assert.assertEquals(1, storage.size());
            StorageTestUtils.assertFullyMatch(storage.readKeys(), "foo");
        }));
    }

    @Test
    public void testPersistence() {
        StorageTestUtils.doRunTest(() -> {
            doWithPojo(storage -> storage.write(KEY_1, VALUE_1));
            doWithPojo(storage -> {
                Assert.assertEquals(VALUE_1, storage.read(KEY_1));
                Assert.assertEquals(1, storage.size());
                StorageTestUtils.assertFullyMatch(storage.readKeys(), KEY_1);
            });
        });

        StorageTestUtils.doRunTest(() -> doWithPojo(storage -> assertNull(storage.read(KEY_1))));
    }

    @Test
    public void testMissingKey() {
        StorageTestUtils.doRunTest(() -> doWithNumbers(storage -> {
            storage.write(4, 3.0);
            assertEquals((Object) storage.read(4), 3.0);
            assertNull(storage.read(5));
            Assert.assertEquals(1, storage.size());
            StorageTestUtils.assertFullyMatch(storage.readKeys(), 4);
        }));
    }

    @Test
    public void testMultipleModifications() {
        StorageTestUtils.doRunTest(() -> {
            doWithStrings(storage -> {
                storage.write("foo", "bar");
                storage.write("bar", "foo");
                storage.write("yammy", "nooo");
                Assert.assertEquals("bar", storage.read("foo"));
                Assert.assertEquals("foo", storage.read("bar"));
                Assert.assertEquals("nooo", storage.read("yammy"));
                assertTrue(storage.exists("foo"));
                Assert.assertEquals(3, storage.size());
                StorageTestUtils.assertFullyMatch(storage.readKeys(), "bar", "foo", "yammy");
            });
            doWithStrings(storage -> {
                Assert.assertEquals("bar", storage.read("foo"));
                Assert.assertEquals("foo", storage.read("bar"));
                Assert.assertEquals("nooo", storage.read("yammy"));
                assertTrue(storage.exists("bar"));
                assertFalse(storage.exists("yep"));
                Assert.assertEquals(3, storage.size());
                StorageTestUtils.assertFullyMatch(storage.readKeys(), "bar", "foo", "yammy");
            });
            doWithStrings(storage -> {
                storage.delete("bar");
                storage.write("yammy", "yeahs");
                assertFalse(storage.exists("bar"));
                assertFalse(storage.exists("yep"));
                Assert.assertEquals(2, storage.size());
                StorageTestUtils.assertFullyMatch(storage.readKeys(), "foo", "yammy");
            });
            doWithStrings(storage -> {
                Assert.assertEquals("bar", storage.read("foo"));
                assertNull(storage.read("bar"));
                Assert.assertEquals("yeahs", storage.read("yammy"));
                Assert.assertEquals(2, storage.size());
                StorageTestUtils.assertFullyMatch(storage.readKeys(), "foo", "yammy");
            });
        });
    }
//
//    This test is not suited for database storage
//
//    @Test
//    public void testPersistAndCopy() {
//        StorageTestUtils.doRunTest(path1 -> {
//            doWithPojo(path1, storage -> {
//                storage.write(KEY_1, VALUE_1);
//                storage.write(KEY_2, VALUE_2);
//            });
//            StorageTestUtils.doRunTest(path2 -> {
//                File from = new File(path1);
//                String path2ext = path2 + File.separator + "trololo/";
//                File to = new File(path2ext);
//                FileUtils.copyDirectory(from, to);
//                doWithPojo(path2ext, storage -> {
//                    Assert.assertEquals(VALUE_1, storage.read(KEY_1));
//                    Assert.assertEquals(VALUE_2, storage.read(KEY_2));
//                    Assert.assertEquals(2, storage.size());
//                    StorageTestUtils.assertFullyMatch(storage.readKeys(), KEY_1, KEY_2);
//                });
//            });
//        });
//    }

    @Test
    public void testNonEquality() {
        StorageTestUtils.doRunTest(() -> assertNotSame(doWithPojo(null), doWithPojo(null)));
        StorageTestUtils.doRunTest(() -> assertNotSame(doWithStrings(null), doWithStrings(null)));
        StorageTestUtils.doRunTest(() -> assertNotSame(doWithNumbers(null), doWithNumbers(null)));
    }

    @Test(expected = ConcurrentModificationException.class)
    public void testIteratorWithConcurrentKeysModification() {
        StorageTestUtils.doRunTest(() -> doWithPojo(storage -> {
            storage.write(KEY_1, VALUE_1);
            storage.write(KEY_2, VALUE_2);
            storage.write(KEY_3, VALUE_3);
            Iterator<StudentKey> iterator = storage.readKeys();
            assertTrue(iterator.hasNext());
            assertTrue(Arrays.asList(KEY_1, KEY_2, KEY_3).contains(iterator.next()));
            assertTrue(iterator.hasNext());
            assertTrue(Arrays.asList(KEY_1, KEY_2, KEY_3).contains(iterator.next()));
            storage.delete(KEY_2);
            iterator.hasNext();
            iterator.next();
        }));
    }

    @Test
    public void testIteratorWithConcurrentValuesModification() {
        StorageTestUtils.doRunTest(() -> doWithPojo(storage -> {
            storage.write(KEY_1, VALUE_1);
            storage.write(KEY_2, VALUE_2);
            storage.write(KEY_3, VALUE_3);
            Iterator<StudentKey> iterator = storage.readKeys();
            assertTrue(iterator.hasNext());
            assertTrue(Arrays.asList(KEY_1, KEY_2, KEY_3).contains(iterator.next()));
            assertTrue(iterator.hasNext());
            assertTrue(Arrays.asList(KEY_1, KEY_2, KEY_3).contains(iterator.next()));
            storage.write(KEY_3, VALUE_2);
            assertTrue(iterator.hasNext());
            assertTrue(Arrays.asList(KEY_1, KEY_2, KEY_3).contains(iterator.next()));
        }));
    }

    @Test(expected = Exception.class)
    public void testDoNotWriteInClosedState() {
        StorageTestUtils.doRunTest(() -> doWithNumbers(storage -> {
            assertNull(storage.read(4));
            storage.write(4, 5.0);
            Assert.assertEquals((Object) 5.0, storage.read(4));
            storage.close();
            storage.write(3, 5.0);
            throw new AssertionError("Storage should not accept writes in closed state");
        }));
    }

    @Test(expected = Exception.class)
    public void testDoNotReadInClosedState() {
        StorageTestUtils.doRunTest(() -> doWithStrings(storage -> {
            assertNull(storage.read("trololo"));
            storage.write("trololo", "yarr");
            Assert.assertEquals("yarr", storage.read("trololo"));
            storage.close();
            storage.readKeys();
            throw new AssertionError("Storage should not allow read anything in closed state");
        }));
    }

    protected final <K extends Comparable<K>, V> KeyValueStorage<K, V> storageCallback(
            StorageTestUtils.Callback<KeyValueStorage<K, V>> callback,
            Callable<KeyValueStorage<K, V>> builder) throws Exception {
        KeyValueStorage<K, V> storage = builder.call();
        try {
            if (callback != null) {
                callback.callback(storage);
            }
        } finally {
            storage.close();
        }
        return storage;
    }

    protected final KeyValueStorage<String, String> doWithStrings(
            StorageTestUtils.Callback<KeyValueStorage<String, String>> callback) throws Exception {
        return storageCallback(callback, this::buildStringsStorage);
    }

    protected final KeyValueStorage<Integer, Double> doWithNumbers(
            StorageTestUtils.Callback<KeyValueStorage<Integer, Double>> callback) throws Exception {
        return storageCallback(callback, this::buildNumbersStorage);
    }

    protected final KeyValueStorage<StudentKey, Student> doWithPojo(
            StorageTestUtils.Callback<KeyValueStorage<StudentKey, Student>> callback) throws Exception {
        return storageCallback(callback, this::buildPojoStorage);
    }
}