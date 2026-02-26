package com.harumoto.matching.MatchingGame.Pieces;

import com.harumoto.matching.MatchingGame.Coordinate;
import com.harumoto.matching.MatchingGame.GameCanvas;

public class Ordinary extends Piece {
    public Ordinary(String strRepr, GameCanvas canvas, Coordinate coordinate) {
        super(strRepr, PieceTypes.ORDINARY, canvas, coordinate);
    }

}
