package com.extra.cqrs.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;

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

        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'H:m:sXXX");
        final Gson gson = gsonBuilder.create();
        return gson.toJson(map);

    }

    @Override
    public Object deserialize(final String string) throws ClassNotFoundException {

        final JSONObject jsonObject = new JSONObject(string);

        final String className = jsonObject.getString("class");
        final String classData = jsonObject.getJSONObject("data").toString();

        return new Gson().fromJson(classData, Class.forName(className));
    }

}
