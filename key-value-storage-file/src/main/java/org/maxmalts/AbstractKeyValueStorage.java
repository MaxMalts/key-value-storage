package org.maxmalts;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

abstract class AbstractKeyValueStorage<T, V> implements KeyValueStorage<T, V> {
    protected HashMap<Object, Object> map = new HashMap<Object, Object>();
    protected File storageFile;
    protected boolean isClosed = false;

    @Override
    public V read(T key) {
        if (isClosed) {
            throw new RuntimeException();
        }
        return (V) map.get(key);
    }

    @Override
    public boolean exists(T key) {
        if (isClosed) {
            throw new RuntimeException();
        }
        return map.containsKey(key);
    }

    @Override
    public void write(T key, V value) {
        if (isClosed) {
            throw new RuntimeException();
        }
        map.put(key, value);
    }

    @Override
    public void delete(T key) {
        if (isClosed) {
            throw new RuntimeException();
        }
        map.remove(key);
    }

    @Override
    public Iterator<T> readKeys() {
        if (isClosed) {
            throw new RuntimeException();
        }

        Iterator<Object> origIterator = map.keySet().iterator();
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return origIterator.hasNext();
            }

            @Override
            public T next() {
                return (T) origIterator.next();
            }
        };
    }

    @Override
    public int size() {
        if (isClosed) {
            throw new RuntimeException();
        }
        return map.size();
    }

    @Override
    public void close() throws IOException {
        if (isClosed) {
            throw new RuntimeException();
        }

        flush();
        isClosed = true;
    }
}
