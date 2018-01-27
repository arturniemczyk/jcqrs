package com.extra.cqrs.fixtures.jsonserializer;

public class SerializableObject {

    private final String name;

    private final String date;

    private final String type;

    public SerializableObject(final String name, final String date, final String type) {
        this.name = name;
        this.date = date;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }
}
