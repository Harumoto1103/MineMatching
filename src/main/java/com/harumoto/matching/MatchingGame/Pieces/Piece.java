package com.harumoto.matching.MatchingGame.Pieces;

import com.harumoto.matching.MatchingGame.Coordinate;
import com.harumoto.matching.MatchingGame.GameCanvas;

public abstract class Piece {
    protected String strRepr;
    private PieceTypes peiceType;
    protected GameCanvas canvas;
    protected Coordinate coordinate;

    public static final String[] ORDINARIES = { "A", "B", "C", "D", "E" };
    public static final String[] SPECIALS = { "+", "|" };
    public static final String[] ELIMINATORS = { "a", "b", "c", "d", "e" };

    public Piece(String strRepr, PieceTypes pieceType, GameCanvas canvas, Coordinate coordinate) {
        this.strRepr = strRepr;
        this.peiceType = pieceType;
        this.canvas = canvas;
        this.coordinate = coordinate;
    }

    public Coordinate getCoordinate() {
        return this.coordinate;
    }

    public PieceTypes type() {
        return this.peiceType;
    }

    public String toString() {
        return this.strRepr;
    }

    public static PieceTypes toType(String strRepr) {
        for (String type : ORDINARIES) {
            if (strRepr.equals(type))
                return PieceTypes.ORDINARY;
        }
        for (String type : ELIMINATORS) {
            if (strRepr.equals(type))
                return PieceTypes.ELIMINATOR;
        }
        for (String type : SPECIALS) {
            if (strRepr.equals(type))
                return PieceTypes.SPECIAL;
        }
        return PieceTypes.NULL;
    }

    public static Piece toPiece(String strRepr, GameCanvas canvas, Coordinate coordinate) {
        if (strRepr.equals("A"))
            return (new Ordinary("A", canvas, coordinate));
        else if (strRepr.equals("B"))
            return (new Ordinary("B", canvas, coordinate));
        else if (strRepr.equals("C"))
            return (new Ordinary("C", canvas, coordinate));
        else if (strRepr.equals("D"))
            return (new Ordinary("D", canvas, coordinate));
        else if (strRepr.equals("E"))
            return (new Ordinary("E", canvas, coordinate));
        else if (strRepr.equals("a"))
            return (new Eliminator("a", "A", canvas, coordinate));
        else if (strRepr.equals("b"))
            return (new Eliminator("b", "B", canvas, coordinate));
        else if (strRepr.equals("c"))
            return (new Eliminator("c", "C", canvas, coordinate));
        else if (strRepr.equals("d"))
            return (new Eliminator("d", "D", canvas, coordinate));
        else if (strRepr.equals("e"))
            return (new Eliminator("e", "E", canvas, coordinate));
        else if (strRepr.equals("+"))
            return (new Special("+", canvas, coordinate));
        else if (strRepr.equals("|"))
            return (new Special("|", canvas, coordinate));
        else if (strRepr.equals("#"))
            return (new Wall("#", canvas, coordinate));
        else
            return (new Null(canvas, coordinate));
    }

}
