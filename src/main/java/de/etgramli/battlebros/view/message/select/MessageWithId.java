package de.etgramli.battlebros.view.message.select;

import org.springframework.lang.NonNull;

import java.util.UUID;

/**
 * Message with an (UU)ID, so that different messages from the same type can be differentiated.
 */
public abstract class MessageWithId {

    private final UUID id;

    protected MessageWithId() {
        id = UUID.randomUUID();
    }

    protected MessageWithId(@NonNull final String id) {
        this.id = UUID.fromString(id);
    }

    public String getId() {
        return id.toString();
    }
}
