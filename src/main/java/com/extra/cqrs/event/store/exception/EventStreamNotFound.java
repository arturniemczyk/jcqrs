package com.extra.cqrs.event.store.exception;

public class EventStreamNotFound extends Exception {

    public EventStreamNotFound(final String id) {
        super(String.format("EventStream not found: \"%s\"", id));
    }
}
