package com.extra.cqrs.fixtures.jsonserializer;

import java.time.LocalDateTime;

public class SerializableObjectWithDates {

    private final String name;

    private final LocalDateTime dateTime;

    private final String type;

    public SerializableObjectWithDates(final String name, final LocalDateTime dateTime, final String type) {
        this.name = name;
        this.dateTime = dateTime;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getType() {
        return type;
    }
}
