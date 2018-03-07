package com.extra.cqrs.event.handling;

import com.extra.cqrs.domain.DomainEvent;
import com.extra.cqrs.domain.DomainEventListener;
import com.extra.cqrs.domain.DomainEventStream;

import java.util.ArrayList;
import java.util.List;

public class InMemoryEventBus implements EventBus {

    private final List<DomainEventListener> listeners = new ArrayList<>();

    public void register(final DomainEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void publish(final DomainEventStream stream) {
        for (final DomainEventListener listener: listeners) {
            for (final DomainEvent event: stream) {
                listener.handle(event);
            }
        }
    }

}
