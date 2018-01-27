package com.extra.cqrs.domain;

import java.util.Date;

public class DomainEvent {

    private final String id;

    private final int version;

    private final Object payload;

    private final Date createdAt;

    public DomainEvent(final String id, final int version, final Object payload, final Date createdAt) {
        this.id = id;
        this.version = version;
        this.payload = payload;
        this.createdAt = new Date(createdAt.getTime());
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

    public Date getCreatedAt() {
        return new Date(createdAt.getTime());
    }

    public String getType() {
        return payload.getClass().getName();
    }
}
