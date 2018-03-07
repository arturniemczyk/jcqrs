package com.extra.cqrs.fixtures.aggregate.root;

public class AggregateRootCreateEvent {

    private final String id;

    public AggregateRootCreateEvent(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
