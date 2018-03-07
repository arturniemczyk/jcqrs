package com.extra.cqrs.event.handling;

import com.extra.cqrs.domain.DomainEvent;
import com.extra.cqrs.domain.DomainEventStream;
import com.extra.cqrs.fixtures.event.bus.TestEvent;
import com.extra.cqrs.fixtures.event.bus.TestListener;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class InMemoryEventBusTest {

    @Test
    public void publishDomainMessageStream() {
        final TestListener listener = spy(new TestListener());
        final InMemoryEventBus bus = new InMemoryEventBus();
        bus.register(listener);
        final TestEvent event = new TestEvent();
        bus.publish(createDomainMessageStream(List.of(event)));
        verify(listener, times(1)).onTestEvent(event);
    }

    private DomainEventStream createDomainMessageStream(final List<Object> events) {
        int version = 0;
        final List<DomainEvent> messages = new ArrayList<>();
        final String id = "f906c4fa-117e-4c84-a9d3-e22b6c519be3";
        for (final Object event: events) {
            messages.add(new DomainEvent(id, version, event, LocalDateTime.now()));
            version++;
        }

        return new DomainEventStream(messages);
    }

}
