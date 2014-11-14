package de.xielong.ultimatetictactoe.data;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;
import de.xielong.ultimatetictactoe.GameRules;

@Data
@ToString(exclude = { "fields" })
public class Board implements CanBeOwned, Serializable {

    private final int index;

    /**
     * Will be <code>null</code> if no winner is decided, yet.
     */
    private Player    ownedBy;

    private boolean   allowed = true;

    private Field[]   fields  = new Field[GameRules.NUMBER_OF_FIELDS];

    public Board(int index) {
        this.index = index;

        for (int i = 0; i < fields.length; i++) {
            fields[i] = new Field(i, this);
        }
    }

    public boolean isEnabled() {
        return allowed && (ownedBy == null);
    }

    public boolean isWon() {
        return ownedBy != null;
    }
}
