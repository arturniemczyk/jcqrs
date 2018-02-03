package com.extra.cqrs.serializer;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonParseException;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class JsonSerializer implements Serializer {

    @Override
    public String serialize(final Object object) {

        if (object == null) {
            throw new IllegalArgumentException();
        }

        final HashMap<String, Object> map = new LinkedHashMap<>();

        map.put("class", object.getClass().getName());
        map.put("data", object);

        return createGson().toJson(map);

    }

    @Override
    public Object deserialize(final String string) throws ClassNotFoundException {

        final JSONObject jsonObject = new JSONObject(string);

        final String className = jsonObject.getString("class");
        final String classData = jsonObject.getJSONObject("data").toString();

        return createGson().fromJson(classData, Class.forName(className));
    }

    private Gson createGson() {
        final GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter());
        builder.serializeNulls();
        return builder.create();
    }

    private static class GsonLocalDateTimeAdapter
            implements com.google.gson.JsonDeserializer<LocalDateTime>, com.google.gson.JsonSerializer<LocalDateTime> {

        @Override
        public LocalDateTime deserialize(
                final JsonElement json,
                final Type typeOfT,
                final JsonDeserializationContext context)

                throws JsonParseException {
                    return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'H:m:s"));
                }

        @Override
        public JsonElement serialize(
                final LocalDateTime dateTime,
                final Type typeOfSrc,
                final JsonSerializationContext context) {
                    return new JsonPrimitive(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'H:m:s")));
                }
    }
}
