package de.xielong.ultimatetictactoe.data;

import java.io.Serializable;

import lombok.Data;
import de.xielong.ultimatetictactoe.GameRules;

@Data
public class Game implements Serializable {

    private OwnedState state     = OwnedState.OPEN;

    private Player     whoseTurn = Player.ONE;

    private Board[]    boards    = new Board[GameRules.NUMBER_OF_FIELDS];

    public Game() {
        for (int i = 0; i < boards.length; i++) {
            boards[i] = new Board(i);
        }
    }
}
