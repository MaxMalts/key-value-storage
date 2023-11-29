import org.hibernate.Session;
import org.hibernate.Transaction;
import org.maxmalts.key_value_storage_database.HibernateUtil;

import java.util.*;

public class StorageTestUtils {
    public static final ThreadLocal<Calendar> CALENDAR = new ThreadLocal<Calendar>() {
        @Override
        protected Calendar initialValue() {
            return Calendar.getInstance();
        }
    };

    private StorageTestUtils() {
        // Cannot instantiate
    }

    public static void doRunTest(VoidCallback callback) {
        try {
            callback.callback();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("Unexpected Exception", e);
        } finally {
            Transaction tx = null;
            Session session = HibernateUtil.getSessionFactory().openSession();
            try {
                tx = session.beginTransaction();
                session.createQuery("delete from StringsStorageEntity").executeUpdate();
                session.createQuery("delete from NumbersStorageEntity").executeUpdate();
                session.createQuery("delete from StudentsStorageEntity").executeUpdate();
                tx.commit();

            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                }
                throw e;

            } finally {
                session.close();
            }
        }
    }

    @FunctionalInterface
    public interface Callback<T> {
        void callback(T t) throws Exception;
    }

    @FunctionalInterface
    public interface VoidCallback {
        void callback() throws Exception;
    }

    public static Date date(int year, int month, int day) {
        Calendar calendar = CALENDAR.get();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    public static <T> void assertFullyMatch(Iterator<T> iterator, T... items) {
        assertFullyMatch(iterator, new HashSet<T>(Arrays.<T>asList(items)));
    }

    public static <T> void assertFullyMatch(Iterator<T> iterator, Set<T> set) {
        int count = 0;
        while (iterator.hasNext()) {
            T t = iterator.next();
            ++count;
            if (!set.contains(t)) {
                throw new AssertionError("Collections doesn't match");
            }
        }

        if (count != set.size()) {
            throw new AssertionError("Collections doesn't match");
        }
    }

    public static long measureTime(Measureable function) {
        long startTime = System.currentTimeMillis();
        function.doSomething();
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    @FunctionalInterface
    public interface Measureable {
        void doSomething();
    }
}
