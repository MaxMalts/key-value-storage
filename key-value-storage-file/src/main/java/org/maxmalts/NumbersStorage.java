package org.maxmalts;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.Iterator;

public class NumbersStorage extends AbstractKeyValueStorage<Integer, Double> {
    public NumbersStorage(String path) {
        try {
            storageFile = new File(path + "/storage.json");
            if (!storageFile.createNewFile()) {
                try (Reader readStream = new FileReader(storageFile)) {
                    JSONObject mapJson = new JSONObject(new JSONTokener(readStream));

                    assert map.isEmpty();
                    for (Iterator<String> it = mapJson.keys(); it.hasNext();) {
                        String key = it.next();
                        map.put(Integer.valueOf(key), mapJson.getDouble(key));
                    }
                }
            }

        } catch (Exception e) {
            throw new MalformedDataException(e);
        }
    }

    @Override
    public void flush() {
        if (isClosed) {
            throw new RuntimeException();
        }

        try (Writer writeStream = new FileWriter(storageFile, false)) {
            JSONObject mapJson = new JSONObject(map);
            mapJson.write(writeStream);

        } catch (Exception e) {
            throw new RuntimeException("Unable to write to file.");
        }
    }
}
