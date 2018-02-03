package com.extra.cqrs.serializer;

import com.extra.cqrs.fixtures.jsonserializer.SerializableObject;
import static org.assertj.core.api.Assertions.assertThat;

import com.extra.cqrs.fixtures.jsonserializer.SerializableObjectWithDates;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class JsonSerializerTest {

    private JsonSerializer serializer;

    @Before
    public void setUp() throws Exception {
        serializer = new JsonSerializer();
    }

    @Test(expected = IllegalArgumentException.class)
    public void serializationOfNullThrowsException() throws JSONException {
        serializer.serialize(null);
    }

    @Test
    public void serializeSerializableObject() throws JSONException {

        final SerializableObject object = new SerializableObject("test1", "12-12-2012", "some type");
        final String actual = serializer.serialize(object);

        assertThat(actual).isEqualTo("{\"class\":\"com.extra.cqrs.fixtures.jsonserializer.SerializableObject\",\"data\":{\"name\":\"test1\",\"date\":\"12-12-2012\",\"type\":\"some type\"}}");
    }

    @Test
    public void deserializeDeserializableString() throws ClassNotFoundException, JSONException {

        final Object object = serializer.deserialize("{\"class\":\"com.extra.cqrs.fixtures.jsonserializer.SerializableObject\",\"data\":{\"name\":\"test1\",\"date\":\"12-12-2012\",\"type\":\"some type\"}}");

        assertThat(object).isInstanceOf(SerializableObject.class);

        final SerializableObject actual = (SerializableObject) object;

        assertThat(actual).extracting("name", "date", "type").contains("test1", "12-12-2012", "some type");
    }

    @Test
    public void serializeWithNullProperty() throws JSONException {

        final SerializableObject object = new SerializableObject("test1", null, "some type");

        final String actual = serializer.serialize(object);

        assertThat(actual).isEqualTo("{\"class\":\"com.extra.cqrs.fixtures.jsonserializer.SerializableObject\",\"data\":{\"name\":\"test1\",\"date\":null,\"type\":\"some type\"}}");

    }

    @Test
    public void deserializeWithNullProperty() throws ClassNotFoundException, JSONException {

        final Object object = serializer.deserialize("{\"class\":\"com.extra.cqrs.fixtures.jsonserializer.SerializableObject\",\"data\":{\"name\":\"test1\",\"type\":\"some type\"}}");

        assertThat(object).isInstanceOf(SerializableObject.class);

        final SerializableObject actual = (SerializableObject) object;

        assertThat(actual).extracting("name", "date", "type").contains("test1", null, "some type");
    }

    @Test
    public void deserializeObjectWithDateTime() throws JSONException, ClassNotFoundException, ParseException {

        final Object object = serializer.deserialize("{\"class\":\"com.extra.cqrs.fixtures.jsonserializer.SerializableObjectWithDates\",\"data\":{\"name\":\"test1\",\"dateTime\":\"2016-01-24T15:42:11\",\"type\":\"some type\"}}");

        assertThat(object).isInstanceOf(SerializableObjectWithDates.class);

        final SerializableObjectWithDates actual = (SerializableObjectWithDates) object;

        final LocalDateTime dateTime = LocalDateTime.parse("2016-01-24T15:42:11", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'H:m:s"));

        assertThat(actual).extracting("name", "dateTime", "type").contains("test1", dateTime, "some type");

    }

    @Test
    public void serializeObjectWithDateTime() throws JSONException, ClassNotFoundException, ParseException {

        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Warsaw"));

        final LocalDateTime dateTime = LocalDateTime.parse("2016-01-24T15:42:11", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'H:m:s"));
        final SerializableObjectWithDates object = new SerializableObjectWithDates("test1", dateTime, "some type");

        final String actual = serializer.serialize(object);

        assertThat(actual).isEqualTo("{\"class\":\"com.extra.cqrs.fixtures.jsonserializer.SerializableObjectWithDates\",\"data\":{\"name\":\"test1\",\"dateTime\":\"2016-01-24T15:42:11\",\"type\":\"some type\"}}");

    }

}
