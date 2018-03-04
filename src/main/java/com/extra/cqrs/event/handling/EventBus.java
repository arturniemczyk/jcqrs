package com.extra.cqrs.event.handling;

import com.extra.cqrs.domain.DomainEventStream;

public interface EventBus {

    void publish(DomainEventStream stream);

}
