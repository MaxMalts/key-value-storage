package org.maxmalts.key_value_storage_database;

import org.hibernate.query.Query;

import java.util.Iterator;

public class NumbersStorage extends AbstractKeyValueStorage<Integer, Double> {
    @Override
    public Double read(Integer key) {
        if (isClosed) {
            throw new RuntimeException();
        }

        return processTransaction(session -> {
            NumbersStorageEntity entity = session.get(NumbersStorageEntity.class, key);
            return entity == null ? null : entity.getValue();
        });
    }

    @Override
    public boolean exists(Integer key) {
        if (isClosed) {
            throw new RuntimeException();
        }

        return processTransaction(session -> (
            session.get(StringsStorageEntity.class, key) != null
        ));
    }

    @Override
    public void write(Integer key, Double value) {
        if (isClosed) {
            throw new RuntimeException();
        }

        NumbersStorageEntity entity = new NumbersStorageEntity();
        entity.setKey(key);
        entity.setValue(value);

        processTransaction(session -> session.merge(entity));
    }

    @Override
    public void delete(Integer key) {
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
    public Iterator<Integer> readKeys() {
        if (isClosed) {
            throw new RuntimeException();
        }

        Iterator<Integer> origIterator = processTransaction(session -> {
            Query<Integer> query = session.createQuery(
                "select key from org.maxmalts.key_value_storage_database.NumbersStorageEntity",
                Integer.class
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
            public Integer next() {
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
                "select count(*) from org.maxmalts.key_value_storage_database.NumbersStorageEntity",
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
