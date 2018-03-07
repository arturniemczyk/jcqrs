package com.extra.cqrs;

import com.extra.cqrs.domain.DomainEvent;
import com.extra.cqrs.domain.DomainEventStream;
import com.extra.cqrs.event.handling.InMemoryEventBus;
import com.extra.cqrs.event.store.InMemoryEventStore;
import com.extra.cqrs.event.store.exception.ConcurrencyException;
import com.extra.cqrs.event.store.exception.EventStreamNotFound;
import com.extra.cqrs.fixtures.aggregate.root.AggregateRootChangeEvent;
import com.extra.cqrs.fixtures.aggregate.root.TestAggregateRoot;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AggregateRootRepositoryTest {

    private InMemoryEventStore store;

    private AggregateRootRepository repository;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        store = new InMemoryEventStore();
        repository = new AggregateRootRepository(store, new InMemoryEventBus());
    }

    @Test
    public void saveAggregate() throws ConcurrencyException, EventStreamNotFound {

        final TestAggregateRoot aggregateRoot = new TestAggregateRoot();

        aggregateRoot.create("f906c4fa-117e-4c84-a9d3-e22b6c519aaa");
        aggregateRoot.change();
        aggregateRoot.change();

        repository.save(aggregateRoot);

        final DomainEventStream stream = store.load("f906c4fa-117e-4c84-a9d3-e22b6c519aaa");

        assertThat(stream.count()).isEqualTo(3);
    }

    @Test
    public void saveAggregateWithoutAggregateIdThrowsException() throws ConcurrencyException {

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Aggregate root is missing it's id (\" com.extra.cqrs.fixtures.aggregate.root.TestAggregateRoot\")");

        final TestAggregateRoot aggregateRoot = new TestAggregateRoot();

        repository.save(aggregateRoot);

    }

    @Test
    public void findAggregateById() throws ConcurrencyException {

        final String id = "f906c4fa-117e-4c84-a9d3-e22b6c519aaa";

        store.save(new DomainEventStream(List.of(
                createDomainMessage(id, 0, new AggregateRootChangeEvent()),
                createDomainMessage(id, 1, new AggregateRootChangeEvent()),
                createDomainMessage(id, 2, new AggregateRootChangeEvent()),
                createDomainMessage(id, 3, new AggregateRootChangeEvent())
        )));

        final TestAggregateRoot aggregateRoot = repository.find(id, TestAggregateRoot.class);

        assertThat(aggregateRoot.getVersion()).isEqualTo(4);

    }

    @Test
    public void repositoryReturnsNullIfNoAggregateIsFound() {

        final String id = "f906c4fa-117e-4c84-a9d3-e22b6c519aaa";

        assertThat(repository.find(id, TestAggregateRoot.class)).isNull();

    }

    private DomainEvent createDomainMessage(final String id, final int version, final Object event) {
        return new DomainEvent(id, version, event, LocalDateTime.now());
    }
}
