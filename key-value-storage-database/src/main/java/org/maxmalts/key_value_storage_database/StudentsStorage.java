package org.maxmalts.key_value_storage_database;

import org.hibernate.query.Query;

import java.util.Date;
import java.util.Iterator;

public class StudentsStorage extends AbstractKeyValueStorage<StudentKey, Student> {
    @Override
    public Student read(StudentKey key) {
        if (isClosed) {
            throw new RuntimeException();
        }

        return processTransaction(session -> {
            StudentsStorageEntity entity =
                session.get(StudentsStorageEntity.class, getEntityPk(key));
            return entity == null ? null : new Student(
                entity.getGroupId(),
                entity.getName(),
                entity.getHomeTown(),
                new Date(entity.getBirthDate().getTime()),
                entity.getHasDormitory(),
                entity.getAverageScore()
            );
        });
    }

    @Override
    public boolean exists(StudentKey key) {
        if (isClosed) {
            throw new RuntimeException();
        }

        return processTransaction(session -> (
            session.get(StudentsStorageEntity.class, getEntityPk(key)) != null
        ));
    }

    @Override
    public void write(StudentKey key, Student value) {
        if (isClosed) {
            throw new RuntimeException();
        }

        StudentsStorageEntity entity = new StudentsStorageEntity();
        entity.setGroupId(key.getGroupId());
        entity.setName(key.getName());
        entity.setHomeTown(value.getHometown());
        entity.setBirthDate(new java.sql.Date(value.getBirthDate().getTime()));
        entity.setHasDormitory(value.isHasDormitory());
        entity.setAverageScore(value.getAverageScore());

        processTransaction(session -> session.merge(entity));
    }

    @Override
    public void delete(StudentKey key) {
        if (isClosed) {
            throw new RuntimeException();
        }
        inactivateIterators();

        processTransaction(session -> {
            StudentsStorageEntity entity =
                session.load(StudentsStorageEntity.class, getEntityPk(key));
            session.delete(entity);
            return null;
        });
    }

    @Override
    public Iterator<StudentKey> readKeys() {
        if (isClosed) {
            throw new RuntimeException();
        }

        Iterator<Object[]> origIterator = processTransaction(session -> {
            Query<Object[]> query = session.createQuery(
                "select groupId, name from org.maxmalts.key_value_storage_database.StudentsStorageEntity",
                Object[].class
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
            public StudentKey next() {
                throwIfObsolete();
                Object[] nextEntry = origIterator.next();
                return new StudentKey((int) nextEntry[0], (String) nextEntry[1]);
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
                "select count(*) from org.maxmalts.key_value_storage_database.StudentsStorageEntity",
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

    private StudentsStorageEntityPK getEntityPk(StudentKey key) {
        StudentsStorageEntityPK entityPk = new StudentsStorageEntityPK();
        entityPk.setName(key.getName());
        entityPk.setGroupId(key.getGroupId());

        return entityPk;
    }
}
