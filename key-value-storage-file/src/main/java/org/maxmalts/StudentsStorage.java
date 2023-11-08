package org.maxmalts;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONWriter;

import java.io.*;
import java.util.Date;
public class StudentsStorage extends AbstractKeyValueStorage<StudentKey, Student> {
    private final String groupIdKey = "groupId";
    private final String nameKey = "name";
    private final String hometownKey = "hometown";
    private final String birthDateKey = "birthDate";
    private final String hasDormitoryKey = "hasDormitory";
    private final String averageScoreKey = "averageScore";

    public StudentsStorage(String path) {
        try {
            storageFile = new File(path + "/storage.json");
            if (!storageFile.createNewFile()) {
                try (Reader readStream = new FileReader(storageFile)) {
                    JSONArray mapJson = new JSONArray(new JSONTokener(readStream));

                    for (Object item : mapJson) {
                        JSONObject keyJson = ((JSONArray) item).getJSONObject(0);
                        JSONObject valueJson = ((JSONArray) item).getJSONObject(1);

                        int groupId = keyJson.getInt(groupIdKey);
                        String name = keyJson.getString(nameKey);

                        StudentKey key = new StudentKey(groupId, name);
                        Student value = new Student(
                            groupId,
                            name,
                            valueJson.getString(hometownKey),
                            new Date(valueJson.getLong(birthDateKey)),
                            valueJson.getBoolean(hasDormitoryKey),
                            valueJson.getDouble(averageScoreKey)
                        );

                        map.put(key, value);
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
            JSONWriter jsonWriter = new JSONWriter(writeStream).array();

            map.forEach((Object keyObj, Object valueObj) -> {
                StudentKey key = (StudentKey) keyObj;
                Student value = (Student) valueObj;

                jsonWriter
                    .array()
                        .object()
                            .key(groupIdKey)
                            .value(key.getGroupId())
                            .key(nameKey)
                            .value(key.getName())
                        .endObject()
                        .object()
                            .key(groupIdKey)
                            .value(key.getGroupId())
                            .key(nameKey)
                            .value(key.getName())
                            .key(hometownKey)
                            .value(value.getHometown())
                            .key(birthDateKey)
                            .value(value.getBirthDate().getTime())
                            .key(hasDormitoryKey)
                            .value(value.isHasDormitory())
                            .key(averageScoreKey)
                            .value(value.getAverageScore())
                        .endObject()
                    .endArray();
            });
            jsonWriter.endArray();

        } catch (Exception e) {
            throw new RuntimeException("Unable to write to file.");
        }
    }
}
