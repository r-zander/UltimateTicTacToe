package de.xielong.ultimatetictactoe.data;

import java.io.Serializable;

import lombok.Data;

@Data
public class Game implements Serializable {

    public static final int NUMBER_OF_FIELDS = 9;

    private Player          whoseTurn        = Player.ONE;

    private Board[]         boards           = new Board[NUMBER_OF_FIELDS];

    public Game() {
        for (int i = 0; i < boards.length; i++) {
            boards[i] = new Board(i);
        }
    }
}
