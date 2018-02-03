package com.extra.cqrs.event.store;

import com.extra.cqrs.domain.DomainEventStream;
import com.extra.cqrs.event.store.exception.ConcurrencyException;
import com.extra.cqrs.event.store.exception.EventStreamNotFound;

public interface EventStore {

    DomainEventStream load(String id) throws EventStreamNotFound;


    void save(DomainEventStream stream) throws ConcurrencyException;

}
