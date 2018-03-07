package com.extra.cqrs.domain;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class DomainEventStreamTest {

    @Test
    public void streamIsTraversableUsingForeach() {
        final ArrayList<DomainEvent> messages = new ArrayList<>();

        messages.add(new DomainEvent("dd75a6ca-d51d-11e7-9986-b8ca3a8bacac", 0, new Object(), LocalDateTime.now()));
        messages.add(new DomainEvent("dd75a6ca-d51d-11e7-9986-b8ca3a8bacac", 1, new Object(), LocalDateTime.now()));

        final DomainEventStream stream = new DomainEventStream(messages);

        final ArrayList<DomainEvent> traversed = new ArrayList<>();

        for (final DomainEvent message: stream) {
            traversed.add(message);
        }

        assertThat(messages).isEqualTo(traversed);
    }

    @Test
    public void streamIsCountable() {
        final ArrayList<DomainEvent> messages = new ArrayList<>();

        messages.add(new DomainEvent("dd75a6ca-d51d-11e7-9986-b8ca3a8bacac", 0, new Object(), LocalDateTime.now()));
        messages.add(new DomainEvent("dd75a6ca-d51d-11e7-9986-b8ca3a8bacac", 1, new Object(), LocalDateTime.now()));

        final DomainEventStream stream = new DomainEventStream(messages);

        assertThat(stream.count()).isEqualTo(2);
    }
}
