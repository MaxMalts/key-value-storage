package org.maxmalts;

import org.json.*;

import java.io.*;
import java.util.HashMap;

public class StringsStorage extends AbstractKeyValueStorage<String, String> {
    public StringsStorage(String path) {
        try {
            storageFile = new File(path + "/storage.json");
            if (!storageFile.createNewFile()) {
                try (Reader readStream = new FileReader(storageFile)) {

                    JSONObject mapJson = new JSONObject(new JSONTokener(readStream));

                    map = new HashMap<Object, Object>(mapJson.toMap());
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
