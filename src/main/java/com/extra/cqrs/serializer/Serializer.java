package com.extra.cqrs.serializer;

interface Serializer {

    String serialize(Object object);

    Object deserialize(String string) throws ClassNotFoundException;
}
