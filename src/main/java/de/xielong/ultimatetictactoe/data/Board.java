package de.xielong.ultimatetictactoe.data;

import java.io.Serializable;

import lombok.Data;

@Data
public class Board implements Serializable {

    private final int index;

    /**
     * Will be <code>null</code> if no winner is decided, yet.
     */
    private Player    winner;

    private boolean   allowed = true;

    private Field[]   fields  = new Field[Game.NUMBER_OF_FIELDS];

    public Board(int index) {
        this.index = index;

        for (int i = 0; i < fields.length; i++) {
            fields[i] = new Field(i);
        }
    }

    public boolean isEnabled() {
        return allowed && (winner == null);
    }

    public boolean isWon() {
        return winner != null;
    }
}
