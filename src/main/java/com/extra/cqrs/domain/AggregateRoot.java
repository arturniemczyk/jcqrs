package com.extra.cqrs.domain;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AggregateRoot implements EventSourcedEntity {

    private int version;

    private final List<DomainEvent> changes = new ArrayList<>();

    private boolean isHydrated;

    @Override
    public void apply(final Object event) {

        final Method method = getApplyMethod(event);

        if (method != null) {

            try {
                method.invoke(this, event);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }

        }

        changes.add(new DomainEvent(getAggregateRootId(), version, event, LocalDateTime.now()));

        version++;
    }

    public abstract String getAggregateRootId();

    public void loadFromStream(final DomainEventStream stream) {

        if (isHydrated) {
            throw new RuntimeException("AggregateRoot already contains some data and cannot be hydrated again.");
        }

        for (final DomainEvent event: stream) {

            final int expectedVersion = version;

            if (event.getVersion() != expectedVersion) {
               throw new RuntimeException("Imported event version number was different than expected.");
            }

            apply(event.getPayload());

        }

        markChangesAsImported();
    }

    public void markChangesAsCommitted() {

        changes.clear();

    }

    public DomainEventStream getUncommittedChanges() {
        return new DomainEventStream(new ArrayList<>(changes));
    }

    public int getVersion() {
        return version;
    }

    private void markChangesAsImported() {

        markChangesAsCommitted();
        isHydrated = true;

    }

    private Method getApplyMethod(final Object event) {

        final String methodName = event.getClass().getSimpleName();

        try {
            final Method m = this.getClass().getDeclaredMethod("on" + methodName, event.getClass());
            m.setAccessible(true);
            return m;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
