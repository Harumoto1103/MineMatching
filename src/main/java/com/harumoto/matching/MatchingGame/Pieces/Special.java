package com.harumoto.matching.MatchingGame.Pieces;

import com.harumoto.matching.MatchingGame.Coordinate;
import com.harumoto.matching.MatchingGame.GameCanvas;

public class Special extends Piece {

    public Special(String strRepr, GameCanvas canvas, Coordinate coordinate) {
        super(strRepr, PieceTypes.SPECIAL, canvas, coordinate);
    }
}
