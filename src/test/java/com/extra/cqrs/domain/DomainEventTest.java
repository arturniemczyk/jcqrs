package com.extra.cqrs.domain;

import com.extra.cqrs.fixtures.domain.event.DomainEventPayload;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class DomainEventTest {

    @Test
    public void createNewInstance() throws ParseException {

        final DomainEventPayload payload = new DomainEventPayload();

        final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd H:m:sXXX");
        final Date createdAt = formatter.parse("2016-01-24 15:42:11+01:00");

        final DomainEvent event = new DomainEvent("376fd59c-d519-11e7-8af0-b8ca3a8bacac", 0, payload, createdAt);

        assertThat(event)
                .extracting("id", "version", "createdAt")
                .contains("376fd59c-d519-11e7-8af0-b8ca3a8bacac", 0, createdAt);

        assertThat(event.getType()).isEqualTo(DomainEventPayload.class.getName());
    }

}
