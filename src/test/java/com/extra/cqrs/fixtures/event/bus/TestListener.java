package com.extra.cqrs.fixtures.event.bus;

import com.extra.cqrs.domain.DomainEventListener;

public class TestListener extends DomainEventListener {

    public void onTestEvent(final TestEvent event) {
        //Silent please
    }

}
