package com.extra.cqrs.domain;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DomainEventListener {

    protected DomainEventListener() {
        //Cannot initialize directly
    }

    public void handle(final DomainEvent domainEvent) {
        final Object event = domainEvent.getPayload();
        final Method method = getHandleMethod(event);
        if (method != null) {
            try {
                method.invoke(this, event);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Method getHandleMethod(final Object event) {

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
