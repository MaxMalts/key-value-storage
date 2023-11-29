package org.maxmalts.key_value_storage_database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.function.Function;

abstract class AbstractKeyValueStorage<K, V> implements KeyValueStorage<K, V> {
    public abstract class AbstractStorageIterator implements Iterator<K> {
        private boolean isObsolete = false;

        AbstractStorageIterator() {
            registerIterator(this);
        }

        protected void throwIfObsolete() {
            if (isObsolete) {
                throw new ConcurrentModificationException();
            }
        }

        protected boolean getIsObsolete() {
            return isObsolete;
        }

        private void inactivate() {
            isObsolete = true;
        }
    }

    private ArrayList<AbstractStorageIterator> activeIterators = new ArrayList<>();

    protected boolean isClosed = false;

    @Override
    public void close() throws IOException {
        if (isClosed) {
            throw new RuntimeException();
        }
        inactivateIterators();

        flush();
        isClosed = true;
    }

    protected <T> T processTransaction(Function<Session, T> action) {
        Transaction tx = null;
        T result;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            result = action.apply(session);
            tx.commit();
            return result;

        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;

        } finally {
            session.close();
        }
    }

    protected void registerIterator(AbstractStorageIterator it) {
        activeIterators.add(it);
    }

    protected void inactivateIterators() {
        activeIterators.forEach(it -> it.inactivate());
        activeIterators.clear();
    }
}
