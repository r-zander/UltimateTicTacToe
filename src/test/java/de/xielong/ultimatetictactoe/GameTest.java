package de.xielong.ultimatetictactoe;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GameTest {

    @Test
    public void testXYCalculation() {

        assertXY(0, 0, 0);
        assertXY(1, 1, 0);
        assertXY(2, 2, 0);
        assertXY(3, 0, 1);
        assertXY(4, 1, 1);
        assertXY(5, 2, 1);
        assertXY(6, 0, 2);
        assertXY(7, 1, 2);
        assertXY(8, 2, 2);
    }

    private void assertXY(int currentFieldIndex, int expectedX, int expectedY) {
        assertEquals(expectedX, GameRules.get2DX(currentFieldIndex));
        assertEquals(expectedY, GameRules.get2DY(currentFieldIndex));
    }

}
