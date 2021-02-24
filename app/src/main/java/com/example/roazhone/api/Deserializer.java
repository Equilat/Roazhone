package com.example.roazhone.api;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
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
        JsonElement content = je.getAsJsonObject().get("records").getAsJsonArray();
        // Deserialize it. New instance of Gson to avoid infinite recursion to this deserializer
        return new Gson().fromJson(content, type);
    }
}