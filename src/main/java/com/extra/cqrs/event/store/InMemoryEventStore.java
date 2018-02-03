package com.extra.cqrs.event.store;

import com.extra.cqrs.domain.DomainEvent;
import com.extra.cqrs.domain.DomainEventStream;
import com.extra.cqrs.event.store.exception.ConcurrencyException;
import com.extra.cqrs.event.store.exception.EventStreamNotFound;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryEventStore implements EventStore {

    private final Map<String, List<DomainEvent>> events = new ConcurrentHashMap<>();

    @Override
    public DomainEventStream load(final String id) throws EventStreamNotFound {

        if (events.containsKey(id)) {
            return new DomainEventStream(events.get(id));
        }

        throw new EventStreamNotFound(id);
    }

    @Override
    public void save(final DomainEventStream stream) throws ConcurrencyException {

        for (final DomainEvent event: stream) {

            if (!events.containsKey(event.getId())) {
                events.put(event.getId(), new ArrayList<>());
            }

            final int expectedVersion = events.get(event.getId()).size();

            if (expectedVersion != event.getVersion()) {
                throw new ConcurrencyException(event);
            }

            events.get(event.getId()).add(event);

        }

    }
}
