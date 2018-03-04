package com.extra.cqrs.domain;

import com.extra.cqrs.fixtures.domain.event.listener.TestEvent;
import com.extra.cqrs.fixtures.domain.event.listener.TestDomainEventListener;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

public class DomainEventListenerTest {

    @Test
    public void handleDomainEvent() {
        final TestDomainEventListener listener = spy(TestDomainEventListener.class);
        final TestEvent event = new TestEvent();
        final DomainEvent domainEvent = new DomainEvent("123", 0, event, LocalDateTime.now());
        listener.handle(domainEvent);
        verify(listener, times(1)).onTestEvent(event);
    }

    @Test
    public void ignoreUnhandledDomainEvent() {
        final TestDomainEventListener listener = spy(TestDomainEventListener.class);
        final DomainEvent domainEvent = new DomainEvent("123", 0, new Object(), LocalDateTime.now());
        listener.handle(domainEvent);
        verify(listener, never()).onTestEvent(any());

    }
}
