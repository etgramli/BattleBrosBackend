package de.etgramli.battlebros.view;

import java.security.Principal;
import java.util.Objects;

public final class StompPrincipal implements Principal {
    private final String name;

    StompPrincipal(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (StompPrincipal) obj;
        return Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "StompPrincipal[" +
                "name=" + name + ']';
    }

    @Override
    public String getName() {
        return name;
    }
}
