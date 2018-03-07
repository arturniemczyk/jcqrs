package com.extra.cqrs.domain;

import com.extra.cqrs.fixtures.aggregate.root.AggregateRootChangeEvent;
import com.extra.cqrs.fixtures.aggregate.root.TestAggregateRoot;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.spy;

public class AggregateRootTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void newInstanceHasVersionSetToZero() {
        final TestAggregateRoot aggregateRoot = new TestAggregateRoot();
        assertThat(aggregateRoot.getVersion()).isEqualTo(0);
    }

    @Test
    public void testApplyingAnEventIncrementsVersion() {
        final TestAggregateRoot aggregateRoot = new TestAggregateRoot();
        aggregateRoot.apply(new AggregateRootChangeEvent());
        aggregateRoot.apply(new AggregateRootChangeEvent());
        aggregateRoot.apply(new AggregateRootChangeEvent());
        assertThat(aggregateRoot.getVersion()).isEqualTo(3);
    }

    @Test
    public void markChangesAsCommitted() {
        final TestAggregateRoot aggregateRoot = new TestAggregateRoot();
        aggregateRoot.apply(new AggregateRootChangeEvent());
        aggregateRoot.markChangesAsCommitted();
        assertThat(aggregateRoot.getUncommittedChanges()).isEmpty();
    }

    @Test
    public void loadingEventsFromStreamOnHydratedAggregateThrowsException() {

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("AggregateRoot already contains some data and cannot be hydrated again.");

        final TestAggregateRoot aggregateRoot = new TestAggregateRoot();
        aggregateRoot.loadFromStream(createDomainMessageStream(List.of(
                new AggregateRootChangeEvent(),
                new AggregateRootChangeEvent(),
                new AggregateRootChangeEvent()
        ), 0));

        aggregateRoot.loadFromStream(createDomainMessageStream(List.of(
                new AggregateRootChangeEvent()
        ), 3));
    }

    @Test
    public void loadingEventsFromStreamInWrongOrderThrowsException() {

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Imported event version number was different than expected.");

        final TestAggregateRoot aggregateRoot = new TestAggregateRoot();
        aggregateRoot.loadFromStream(createDomainMessageStream(List.of(
                new AggregateRootChangeEvent(),
                new AggregateRootChangeEvent(),
                new AggregateRootChangeEvent()
        ), 2));
    }

    @Test
    public void methodIsCalledAfterApplyingEvent() {
        final TestAggregateRoot aggregateRoot = spy(TestAggregateRoot.class);
        final AggregateRootChangeEvent event = new AggregateRootChangeEvent();
        aggregateRoot.apply(event);
        verify(aggregateRoot, times(1)).onAggregateRootChangeEvent(event);
    }

    private DomainEventStream createDomainMessageStream(final List<Object> events, final int version) {

        int currentVersion = version;

        final String aggregateId = "f906c4fa-117e-4c84-a9d3-e22b6c519be3";
        final List<DomainEvent> messages = new ArrayList<>();
        for (final Object event: events) {
            messages.add(new DomainEvent(aggregateId, currentVersion, event, LocalDateTime.now()));
            currentVersion++;
        }

        return new DomainEventStream(messages);
    }

}
