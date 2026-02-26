package com.harumoto.matching.MatchingGame.Pieces;

import com.harumoto.matching.MatchingGame.Coordinate;
import com.harumoto.matching.MatchingGame.GameCanvas;

public class Eliminator extends Piece {

    private String target;

    public Eliminator(String strRepr, String target, GameCanvas canvas, Coordinate coordinate) {
        super(strRepr, PieceTypes.ELIMINATOR, canvas, coordinate);
        this.target = target;
    }

    public String target() {
        return this.target;
    }

}
