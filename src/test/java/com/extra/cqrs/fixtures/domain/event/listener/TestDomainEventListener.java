package com.extra.cqrs.fixtures.domain.event.listener;

import com.extra.cqrs.domain.DomainEventListener;

public class TestDomainEventListener extends DomainEventListener {

    public void onTestEvent(final TestEvent event) {
        //Silent please
    }

}
