package com.extra.cqrs.domain;

import java.util.Iterator;
import java.util.List;

public class DomainEventStream implements Iterable<DomainEvent> {

    private final List<DomainEvent> messages;

    public DomainEventStream(final List<DomainEvent> messages) {
        this.messages = messages;
    }

    public void append(final DomainEvent message) {
        messages.add(message);
    }

    public int count() {
        return messages.size();
    }

    @Override
    public Iterator<DomainEvent> iterator() {
        return messages.iterator();
    }
}
