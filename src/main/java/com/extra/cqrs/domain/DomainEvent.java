package com.extra.cqrs.domain;

import java.time.LocalDateTime;

public class DomainEvent {

    private final String id;

    private final int version;

    private final Object payload;

    private final LocalDateTime createdAt;

    public DomainEvent(final String id, final int version, final Object payload, final LocalDateTime createdAt) {
        this.id = id;
        this.version = version;
        this.payload = payload;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public Object getPayload() {
        return payload;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getType() {
        return payload.getClass().getName();
    }
}
