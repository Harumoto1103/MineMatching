package com.harumoto.matching.MatchingGame.Pieces;

import com.harumoto.matching.MatchingGame.Coordinate;
import com.harumoto.matching.MatchingGame.GameCanvas;

public class Wall extends Piece {

    public Wall(String strRepr, GameCanvas canvas, Coordinate coordinate) {
        super(strRepr, PieceTypes.WALL, canvas, coordinate);
    }
}
