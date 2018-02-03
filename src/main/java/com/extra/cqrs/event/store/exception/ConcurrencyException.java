package com.extra.cqrs.event.store.exception;

import com.extra.cqrs.domain.DomainEvent;

public class ConcurrencyException extends Exception {

    public ConcurrencyException(final DomainEvent domainMessage) {
        super(String.format("Concurrency exception for aggregate id: \"%s\".", domainMessage.getId()));
    }

    public ConcurrencyException(final DomainEvent domainMessage, final Throwable cause) {
        super(String.format("Concurrency exception for aggregate id: \"%s\".", domainMessage.getId()), cause);
    }
}
