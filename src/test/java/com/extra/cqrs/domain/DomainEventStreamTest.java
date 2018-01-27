package com.extra.cqrs.domain;

import org.junit.Test;

import java.util.Date;
import java.util.LinkedList;

import static org.assertj.core.api.Assertions.assertThat;

public class DomainEventStreamTest {

    @Test
    public void streamIsTraversableUsingForeach() {
        final LinkedList<DomainEvent> messages = new LinkedList<>();

        messages.add(new DomainEvent("dd75a6ca-d51d-11e7-9986-b8ca3a8bacac", 0, new Object(), new Date()));
        messages.add(new DomainEvent("dd75a6ca-d51d-11e7-9986-b8ca3a8bacac", 1, new Object(), new Date()));

        final DomainEventStream stream = new DomainEventStream(messages);

        final LinkedList<DomainEvent> traversed = new LinkedList<>();

        for (final DomainEvent message: stream) {
            traversed.add(message);
        }

        assertThat(messages).isEqualTo(traversed);
    }
}
