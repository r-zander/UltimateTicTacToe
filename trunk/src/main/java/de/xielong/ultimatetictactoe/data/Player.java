package de.xielong.ultimatetictactoe.data;

import lombok.Getter;

public enum Player {

    /**
     * Player 1
     */
    ONE("player1"),

    /**
     * Player 2
     */
    TWO("player2"),

    /**
     * Assigned to any board that's neither won by 1 or 2.
     */
    TIED("tied");

    @Getter
    private String cssClass;

    private Player(String cssClass) {
        this.cssClass = cssClass;
    }

}
