package com.extra.cqrs.serializer;

import com.extra.cqrs.fixtures.jsonserializer.SerializableObject;
import static org.assertj.core.api.Assertions.assertThat;

import com.extra.cqrs.fixtures.jsonserializer.SerializableObjectWithDates;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    public void deserializeDateAndTimeWithoutMsPart() throws JSONException, ClassNotFoundException, ParseException {

        final Object object = serializer.deserialize("{\"class\":\"com.extra.cqrs.fixtures.jsonserializer.SerializableObjectWithDates\",\"data\":{\"name\":\"test1\",\"date\":\"2016-01-24T15:42:11+01:00\",\"type\":\"some type\"}}");

        assertThat(object).isInstanceOf(SerializableObjectWithDates.class);

        final SerializableObjectWithDates actual = (SerializableObjectWithDates) object;

        final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd H:m:sX");
        final Date expectedDate = formatter.parse("2016-01-24 15:42:11+01:00");

        assertThat(actual).extracting("name", "date", "type").contains("test1", expectedDate, "some type");

    }

    @Test
    public void serializeDateAndTime() throws JSONException, ClassNotFoundException, ParseException {

        final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd H:m:sXXX");
        final Date date = formatter.parse("2016-01-24 15:42:11+01:00");
        final SerializableObjectWithDates object = new SerializableObjectWithDates("test1", date, "some type");

        final String actual = serializer.serialize(object);

        assertThat(actual).isEqualTo("{\"class\":\"com.extra.cqrs.fixtures.jsonserializer.SerializableObjectWithDates\",\"data\":{\"name\":\"test1\",\"date\":\"2016-01-24T15:42:11+01:00\",\"type\":\"some type\"}}");

    }

}
