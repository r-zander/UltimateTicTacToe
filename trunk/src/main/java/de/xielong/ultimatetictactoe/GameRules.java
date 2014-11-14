package de.xielong.ultimatetictactoe;

import de.xielong.ultimatetictactoe.data.Board;
import de.xielong.ultimatetictactoe.data.CanBeOwned;
import de.xielong.ultimatetictactoe.data.Field;
import de.xielong.ultimatetictactoe.data.Game;
import de.xielong.ultimatetictactoe.data.OwnedState;
import de.xielong.ultimatetictactoe.data.Player;

public class GameRules {

    private GameRules() {}

    public static final int NUMBER_OF_ROWS_AND_COLS = 3;

    public static final int NUMBER_OF_FIELDS        = NUMBER_OF_ROWS_AND_COLS * NUMBER_OF_ROWS_AND_COLS;

    public static void makeMove(Game currentGame, Field currentField) {
        Player currentPlayer = currentGame.getWhoseTurn();

        markCurrentField(currentField, currentPlayer);

        int currentFieldIndex = currentField.getIndex();
        /*
         * Check current Board for winner.
         */
        OwnedState ownedState = checkForWinner(currentField, currentField.getBoard().getFields(), currentPlayer);

        if (ownedState != OwnedState.OPEN) {
            if (ownedState == OwnedState.TIED) {
                currentField.getBoard().setOwnedBy(Player.TIED);
            } else {
                currentField.getBoard().setOwnedBy(currentPlayer);
            }

            /*
             * Check game for winner.
             */
            currentGame.setState(checkForWinner(currentField.getBoard(), currentGame.getBoards(), currentPlayer));
        }

        /*
         * Solange das Spiel offen ist, kommt jetzt der nächste Spieler dran. Andernfalls ist das Spiel hier vorbei.
         */
        if (currentGame.getState() == OwnedState.OPEN) {
            disOrEnableBoards(currentGame, currentFieldIndex);
            turnToNextPlayer(currentGame);
        } else {
            disableAllBoards(currentGame);
        }
    }

    public static void disableAllBoards(Game currentGame) {
        for (Board board : currentGame.getBoards()) {
            board.setAllowed(false);
        }
    }

    public static void disOrEnableBoards(Game currentGame, int currentFieldIndex) {
        /*
         * Alle Boards, ausser dem, mit dem Field-Index deaktivieren.
         */
        boolean enableAll = currentGame.getBoards()[currentFieldIndex].isWon();
        for (Board board : currentGame.getBoards()) {
            board.setAllowed(enableAll || board.getIndex() == currentFieldIndex);
        }
    }

    public static void markCurrentField(Field currentField, Player currentPlayer) {
        currentField.setOwnedBy(currentPlayer);
    }

    public static OwnedState checkForWinner(CanBeOwned canBeOwned, CanBeOwned[] otherCanBeOwneds, Player currentPlayer) {
        int currentIndex = canBeOwned.getIndex();

        int x = get2DX(currentIndex);
        int y = get2DY(currentIndex);

        int col = 0;
        int row = 0;
        int dia1 = 0;
        int dia2 = 0;

        for (int i = 0; i < NUMBER_OF_ROWS_AND_COLS; i++) {
            if (otherCanBeOwneds[get2DIndex(x, i)].getOwnedBy() == currentPlayer) {
                col++;
                if (col == NUMBER_OF_ROWS_AND_COLS) {
                    return OwnedState.WON;
                }
            }
            if (otherCanBeOwneds[get2DIndex(i, y)].getOwnedBy() == currentPlayer) {
                row++;
                if (row == NUMBER_OF_ROWS_AND_COLS) {
                    return OwnedState.WON;
                }
            }
            if (otherCanBeOwneds[get2DIndex(i, i)].getOwnedBy() == currentPlayer) {
                dia1++;
                if (dia1 == NUMBER_OF_ROWS_AND_COLS) {
                    return OwnedState.WON;
                }
            }
            if (otherCanBeOwneds[get2DIndex(i, NUMBER_OF_ROWS_AND_COLS - i - 1)].getOwnedBy() == currentPlayer) {
                dia2++;
                if (dia2 == NUMBER_OF_ROWS_AND_COLS) {
                    return OwnedState.WON;
                }
            }
        }
        for (CanBeOwned canBeOwned2 : otherCanBeOwneds) {
            if (canBeOwned2.getOwnedBy() == null) {
                return OwnedState.OPEN;
            }
        }
        return OwnedState.TIED;
    }

    public static void turnToNextPlayer(Game currentGame) {
        switch (currentGame.getWhoseTurn()) {
            case ONE:
                currentGame.setWhoseTurn(Player.TWO);
                break;
            case TWO:
                currentGame.setWhoseTurn(Player.ONE);
                break;
            default:
                break;
        }
    }

    protected static int get2DIndex(int x, int y) {
        return y * NUMBER_OF_ROWS_AND_COLS + x;
    }

    protected static int get2DX(int index) {
        return index % NUMBER_OF_ROWS_AND_COLS;
    }

    protected static int get2DY(int index) {
        return index / NUMBER_OF_ROWS_AND_COLS;
    }

}
