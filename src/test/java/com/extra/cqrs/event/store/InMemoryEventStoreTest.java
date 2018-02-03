package com.extra.cqrs.event.store;

import com.extra.cqrs.domain.DomainEvent;
import com.extra.cqrs.domain.DomainEventStream;
import com.extra.cqrs.event.store.exception.ConcurrencyException;
import com.extra.cqrs.event.store.exception.EventStreamNotFound;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryEventStoreTest {


    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testSaveEventStream() throws ConcurrencyException, EventStreamNotFound {

        final DomainEventStream stream = new DomainEventStream(List.of(
            createDomainMessage("f906c4fa-117e-4c84-a9d3-e22b6c519be3", 0),
            createDomainMessage("f906c4fa-117e-4c84-a9d3-e22b6c519be3", 1),
            createDomainMessage("f906c4fa-117e-4c84-a9d3-e22b6c519be3", 2)
        ));

        final EventStore store = new InMemoryEventStore();
        store.save(stream);

        assertThat(store.load("f906c4fa-117e-4c84-a9d3-e22b6c519be3")).hasSize(3);
        assertThat(store.load("f906c4fa-117e-4c84-a9d3-e22b6c519be3")).hasSameElementsAs(stream);
    }

    @Test
    public void testSaveEventsWithTheSameVersionThrowsConcurrencyException() throws ConcurrencyException {

        expectedException.expect(ConcurrencyException.class);
        expectedException.expectMessage("Concurrency exception for aggregate id: \"f906c4fa-117e-4c84-a9d3-e22b6c519be3\"");

        final EventStore store = new InMemoryEventStore();
        final DomainEventStream stream = new DomainEventStream(List.of(
                createDomainMessage("f906c4fa-117e-4c84-a9d3-e22b6c519be3", 0),
                createDomainMessage("f906c4fa-117e-4c84-a9d3-e22b6c519be3", 0)
        ));

        store.save(stream);
    }

    @Test
    public void testLoadStreamForNonExistingIdThrowsException() throws EventStreamNotFound {

        expectedException.expect(EventStreamNotFound.class);
        expectedException.expectMessage("EventStream not found: \"f906c4fa-117e-4c84-a9d3-e22b6c519be3\"");

        final EventStore store = new InMemoryEventStore();
        store.load("f906c4fa-117e-4c84-a9d3-e22b6c519be3");
    }

    private DomainEvent createDomainMessage(final String id, final int version) {
        return new DomainEvent(
            id,
            version,
            new Object(),
            LocalDateTime.parse("2016-01-02T15:25:31", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'H:m:s"))
        );
    }

}
