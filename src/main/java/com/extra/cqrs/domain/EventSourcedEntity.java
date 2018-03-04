package com.extra.cqrs.domain;

public interface EventSourcedEntity {

    void apply(Object event);

}
