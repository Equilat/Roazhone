package com.example.roazhone.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Custom deserializer.
 *
 * @param <T>
 */
public class Deserializer<T> implements JsonDeserializer<T> {
    @Override
    public T deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException {
        // Get the "records" element from the parsed JSON
        JsonArray listRecords = je.getAsJsonObject().getAsJsonArray("records");
        StringBuilder newJson = new StringBuilder("[");
        for (int i = 0; i < listRecords.size(); i++) {
            JsonObject infosParking = (JsonObject) listRecords.get(i);
            newJson.append(infosParking.get("fields").toString());
            if (i != listRecords.size() - 1) {
                newJson.append(",");
            }
        }
        newJson.append("]");
        return new Gson().fromJson(newJson.toString(), type);
    }
}