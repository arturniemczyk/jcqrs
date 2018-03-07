package com.extra.cqrs;

import com.extra.cqrs.domain.AggregateRoot;
import com.extra.cqrs.domain.DomainEventStream;
import com.extra.cqrs.event.handling.EventBus;
import com.extra.cqrs.event.store.EventStore;
import com.extra.cqrs.event.store.exception.ConcurrencyException;
import com.extra.cqrs.event.store.exception.EventStreamNotFound;

import java.lang.reflect.InvocationTargetException;

public class AggregateRootRepository {

    private final EventStore store;

    private final EventBus bus;

    public AggregateRootRepository(final EventStore store, final EventBus bus) {
        this.store = store;
        this.bus = bus;
    }

    public <T extends AggregateRoot> T find(final String id, final Class<T> clazz) {

        T aggregateRoot = null;

        try {
            final DomainEventStream stream = store.load(id);
            if (stream.count() > 0) {
                try {
                    aggregateRoot = clazz.getConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
                aggregateRoot.loadFromStream(stream);
            }


        } catch (EventStreamNotFound streamNotFound) {
            return null;
        }

        return aggregateRoot;
    }

    public <T extends AggregateRoot> void save(final T aggregateRoot) throws ConcurrencyException {

        if (aggregateRoot.getAggregateRootId() == null) {
            throw new IllegalArgumentException(String.format("Aggregate root is missing it's id (\" %s\").", aggregateRoot.getClass().getName()));
        }

        final DomainEventStream stream = aggregateRoot.getUncommittedChanges();
        aggregateRoot.markChangesAsCommitted();
        store.save(stream);
        bus.publish(stream);

    }

}
