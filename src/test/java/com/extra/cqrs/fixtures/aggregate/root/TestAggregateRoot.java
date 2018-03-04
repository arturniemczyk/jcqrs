package com.extra.cqrs.fixtures.aggregate.root;

import com.extra.cqrs.domain.AggregateRoot;

public class TestAggregateRoot extends AggregateRoot {
    @Override
    public String getAggregateRootId() {
        return null;
    }

    public void onAggregateRootTestEvent1(final AggregateRootTestEvent1 event) {
        //Silent please
    }
}
