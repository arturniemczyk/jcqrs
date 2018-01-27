package com.extra.cqrs.fixtures.jsonserializer;

import java.util.Date;

public class SerializableObjectWithDates {

    private final String name;

    private final Date date;

    private final String type;

    public SerializableObjectWithDates(final String name, final Date date, final String type) {
        this.name = name;
        this.date = new Date(date.getTime());
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return new Date(date.getTime());
    }

    public String getType() {
        return type;
    }
}
