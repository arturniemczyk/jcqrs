package com.extra.cqrs.fixtures.aggregate.root;

import com.extra.cqrs.domain.AggregateRoot;

public class TestAggregateRoot extends AggregateRoot {

    private String aggregateId;

    @Override
    public String getAggregateRootId() {
        return aggregateId;
    }

    public void change() {
        apply(new AggregateRootChangeEvent());
    }

    public void create(final String id) {
        apply(new AggregateRootCreateEvent(id));
    }


    public void onAggregateRootChangeEvent(final AggregateRootChangeEvent event) {
        //Silent please
    }

    private void onAggregateRootCreateEvent(final AggregateRootCreateEvent event) {
        aggregateId = event.getId();
    }
}
