package de.xielong.ultimatetictactoe.data;

import java.io.Serializable;

import lombok.Data;

@Data
public class Field implements CanBeOwned, Serializable {

    private final int   index;

    private final Board board;

    private Player      ownedBy;

}
