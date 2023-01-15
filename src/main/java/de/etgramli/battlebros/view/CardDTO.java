package de.etgramli.battlebros.view;

/**
 * Contains data to be transferred to the client.
 * Infos important to the client:
 * - Name
 * - value
 * - Element
 * - Effect text
 */
public final class CardDTO {
    private final int id;

    public CardDTO(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (CardDTO) obj;
        return this.id == that.getId();
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "CardDTO[" + "id=" + id + ']';
    }
}
