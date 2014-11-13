package de.xielong.ultimatetictactoe.data;

import java.io.Serializable;

import lombok.Data;

@Data
public class Field implements Serializable {

    private final int index;

    private Board     board;

    private Player    ownedBy;

}
