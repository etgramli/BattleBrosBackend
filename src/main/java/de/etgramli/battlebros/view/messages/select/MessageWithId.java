package de.etgramli.battlebros.view.messages.select;

import org.springframework.lang.NonNull;

import java.util.UUID;


public abstract class MessageWithId {

    private final UUID id;

    protected MessageWithId() {
        id = UUID.randomUUID();
    }

    protected MessageWithId(@NonNull String id) {
        this.id = UUID.fromString(id);
    }

    public String getId() {
        return id.toString();
    }
}
