package com.harumoto.matching.MatchingGame.Pieces;

import com.harumoto.matching.MatchingGame.Coordinate;
import com.harumoto.matching.MatchingGame.GameCanvas;

public class Null extends Piece {
    public Null(GameCanvas canvas, Coordinate coordinate) {
        super(" ", PieceTypes.NULL, canvas, coordinate);
    }
}
