package org.maxmalts.key_value_storage_database;

import org.hibernate.query.Query;

import java.util.Iterator;

public class StringsStorage extends AbstractKeyValueStorage<String, String> {
    @Override
    public String read(String key) {
        if (isClosed) {
            throw new RuntimeException();
        }

        return processTransaction(session -> {
            StringsStorageEntity entity = session.get(StringsStorageEntity.class, key);
            return entity == null ? null : entity.getValue();
        });
    }

    @Override
    public boolean exists(String key) {
        if (isClosed) {
            throw new RuntimeException();
        }

        return processTransaction(session -> (
            session.get(StringsStorageEntity.class, key) != null
        ));
    }

    @Override
    public void write(String key, String value) {
        if (isClosed) {
            throw new RuntimeException();
        }

        StringsStorageEntity entity = new StringsStorageEntity();
        entity.setKey(key);
        entity.setValue(value);

        processTransaction(session -> session.merge(entity));
    }

    @Override
    public void delete(String key) {
        if (isClosed) {
            throw new RuntimeException();
        }
        inactivateIterators();

        processTransaction(session -> {
            StringsStorageEntity entity = session.load(StringsStorageEntity.class, key);
            session.delete(entity);
            return null;
        });
    }

    @Override
    public Iterator<String> readKeys() {
        if (isClosed) {
            throw new RuntimeException();
        }

        Iterator<String> origIterator = processTransaction(session -> {
            Query<String> query = session.createQuery(
                "select key from org.maxmalts.key_value_storage_database.StringsStorageEntity",
                String.class
            );
            return query.list().iterator();
        });

        return new AbstractStorageIterator() {
            @Override
            public boolean hasNext() {
                throwIfObsolete();
                return origIterator.hasNext();
            }

            @Override
            public String next() {
                throwIfObsolete();
                return origIterator.next();
            }
        };
    }

    @Override
    public int size() {
        if (isClosed) {
            throw new RuntimeException();
        }
        return processTransaction(session -> {
            Query<Long> query = session.createQuery(
                "select count(*) from org.maxmalts.key_value_storage_database.StringsStorageEntity",
                Long.class
            );
            return query.uniqueResult().intValue();
        });
    }

    @Override
    public void flush() {
        if (isClosed) {
            throw new RuntimeException();
        }
    }
}
