package com.harumoto.matching.MatchingGame;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.harumoto.matching.MatchingGame.Pieces.*;

/**
 * The GameCanvas class represents the game board consisting of pieces arranged
 * in a grid.
 */
public class GameCanvas {
    private Piece[][] canvas; // The 2D array representing the game board
    private Integer canvasSize; // The size of the game board

    /**
     * Constructs a GameCanvas with the specified size.
     * 
     * @param canvasSize the size of the game board
     */
    public GameCanvas(Integer canvasSize) {
        this.canvasSize = canvasSize;
        this.canvas = new Piece[this.canvasSize][this.canvasSize];
    }

    /**
     * Returns the 2D array representing the game board.
     * 
     * @return the game board array
     */
    public Piece[][] getCanvasArray() {
        return this.canvas;
    }

    /**
     * Sets the game board array from the specified source array.
     * 
     * @param source the source array to copy from
     */
    public void setCanvasArray(Piece[][] source) {
        for (int i = 0; i < this.canvasSize; i++) {
            for (int j = 0; j < this.canvasSize; j++) { // Corrected comparison from '>' to '<'
                this.canvas[i][j] = source[i][j];
            }
        }
    }

    /**
     * Returns the piece at the specified coordinate.
     * 
     * @param coordinate the coordinate of the piece
     * @return the piece at the specified coordinate
     */
    public Piece getPiece(Coordinate coordinate) {
        Integer x = coordinate.x();
        Integer y = coordinate.y();
        return this.canvas[x][y];
    }

    /**
     * Returns the type of piece at the specified coordinate.
     * 
     * @param coordinate the coordinate of the piece
     * @return the type of the piece at the specified coordinate
     */
    public PieceTypes getPieceType(Coordinate coordinate) {
        return this.getPiece(coordinate).type();
    }

    /**
     * Places a piece at the specified coordinate.
     * 
     * @param piece      the piece to place
     * @param coordinate the coordinate to place the piece at
     */
    public void placePiece(Piece piece, Coordinate coordinate) {
        Integer x = coordinate.x();
        Integer y = coordinate.y();
        this.canvas[x][y] = piece;
    }

    /**
     * Replaces the piece at the specified coordinate with a Null piece.
     * 
     * @param coordinate the coordinate to nullify
     */
    public void nullize(Coordinate coordinate) {
        this.placePiece(new Null(this, coordinate), coordinate);
    }

    /**
     * Swaps the pieces at the specified coordinates.
     * 
     * @param orig the first coordinate
     * @param dest the second coordinate
     */
    public void swap(Coordinate orig, Coordinate dest) {
        Piece origPiece = this.getPiece(orig);
        Piece destPiece = this.getPiece(dest);
        this.placePiece(origPiece, dest);
        this.placePiece(destPiece, orig);
    }

    /**
     * Returns the size of the game board.
     * 
     * @return the size of the game board
     */
    public Integer size() {
        return this.canvasSize;
    }

    /**
     * Applies gravity to the pieces on the game board, making them fall down if
     * there are empty
     * spaces (NULL pieces) below them. The method iterates multiple times to ensure
     * all pieces are
     * settled.
     * 
     * @return the number of iterations gravity was applied
     */
    public int gravitate() {
        int iterTimes = 0; // Counter for the number of iterations where pieces were moved

        for (int p = 0; p < this.size(); p++) {
            int movements = 0; // Counter for movements in the current iteration
            for (int i = 0; i < this.size(); i++) {
                int k = 4;

                // Find the first NULL piece from the bottom
                while (k >= 1 && !this.getPieceType(new Coordinate(k, i)).equals(PieceTypes.NULL))
                    k--;
                int l = k;

                // Find the first non-NULL piece above the found NULL piece
                while (l >= 1 && this.getPieceType(new Coordinate(l, i)).equals(PieceTypes.NULL))
                    l--;

                // Swap the found non-NULL piece with the found NULL piece if conditions are met
                if (l != k && l < k
                        && !this.getPieceType(new Coordinate(l, i)).equals(PieceTypes.NULL)
                        && this.getPieceType(new Coordinate(k, i)).equals(PieceTypes.NULL)
                        && !this.getPieceType(new Coordinate(l, i)).equals(PieceTypes.WALL)) {
                    Piece tmpPiece = this.canvas[k][i];
                    this.canvas[k][i] = this.canvas[l][i];
                    this.canvas[l][i] = tmpPiece;

                    movements++;
                }

            }

            if (movements != 0)
                iterTimes++; // Increment the counter for iterations where pieces were moved
        }

        return iterTimes; // Return the total number of iterations where pieces were moved
    }

    /**
     * Prints the current state of the game board to the console.
     */
    public void printCanvas() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(
                        String.format("%s ", this.getPiece(new Coordinate(i, j)).toString()));
            }
            System.err.println();
        }
    }

    /**
     * Fills all NULL pieces on the canvas with new randomly generated pieces. The
     * new pieces can be
     * Ordinary, Eliminator, or Special pieces.
     */
    public void suffuse() {

        for (Integer i = 0; i < this.canvasSize; i++) {
            for (Integer j = 0; j < this.canvasSize; j++) {
                Coordinate inLoopCoordinate = new Coordinate(i, j);

                // Check if the current piece is NULL
                if (this.getPieceType(inLoopCoordinate).equals(PieceTypes.NULL)) {
                    Random random = new Random();
                    Integer randPieceType = random.nextInt(10);
                    Piece newPiece;

                    // 90% chance to create an Ordinary piece
                    if (0 <= randPieceType && randPieceType <= 8) {
                        Integer randPiece = random.nextInt(5);
                        newPiece = new Ordinary(Piece.ORDINARIES[randPiece], this, inLoopCoordinate);
                    } else {
                        // 10% chance to create an Eliminator or Special piece
                        Integer randPiece = random.nextInt(7);
                        if (0 <= randPiece && randPiece <= 4) {
                            // 5/7 chance to create an Eliminator piece
                            newPiece = new Eliminator(Piece.ELIMINATORS[randPiece],
                                    Piece.ORDINARIES[randPiece], this, inLoopCoordinate);
                        } else {
                            // 2/7 chance to create a Special piece
                            newPiece = new Special(Piece.SPECIALS[randPiece - 5], this,
                                    inLoopCoordinate);
                        }
                    }

                    this.placePiece(newPiece, inLoopCoordinate);
                }
            }
        }
    }

    /**
     * Returns the set of coordinates that can be cleared starting from the
     * specified coordinate.
     * 
     * @param coordinate the starting coordinate
     * @return the set of coordinates that can be cleared
     */
    public Set<Coordinate> getClearables(Coordinate coordinate) {
        Piece currentPiece = this.getPiece(coordinate);

        // If the piece is a WALL, return an empty set as it can't be cleared
        if (currentPiece.type().equals(PieceTypes.WALL))
            return new HashSet<Coordinate>();

        Set<Coordinate> HorizontalClearables = new HashSet<>();
        Set<Coordinate> VerticalClearables = new HashSet<>();
        Integer x = coordinate.x();
        Integer y = coordinate.y();
        String currentStrRepr = currentPiece.toString(); // String representation of the current
                                                         // piece

        // Check horizontally to the right
        for (Integer i = y; i < this.size(); i++) {
            Coordinate inLoopCoordinate = new Coordinate(x, i);
            Piece inLoopPiece = this.getPiece(inLoopCoordinate);
            // Stop if a piece with a different string representation is found
            if (!inLoopPiece.toString().equals(currentStrRepr))
                break;
            HorizontalClearables.add(inLoopCoordinate);
        }

        // Check horizontally to the left
        for (Integer i = y - 1; i >= 0; i--) {
            Coordinate inLoopCoordinate = new Coordinate(x, i);
            Piece inLoopPiece = this.getPiece(inLoopCoordinate);
            // Stop if a piece with a different string representation is found
            if (!inLoopPiece.toString().equals(currentStrRepr))
                break;
            HorizontalClearables.add(inLoopCoordinate);
        }

        // Clear the set if fewer than 3 consecutive pieces are found
        if (HorizontalClearables.size() < 3)
            HorizontalClearables.clear();

        // Check vertically downward
        for (Integer i = x; i < this.size(); i++) {
            Coordinate inLoopCoordinate = new Coordinate(i, y);
            Piece inLoopPiece = this.getPiece(inLoopCoordinate);
            // Stop if a piece with a different string representation is found
            if (!inLoopPiece.toString().equals(currentStrRepr))
                break;
            VerticalClearables.add(inLoopCoordinate);
        }

        // Check vertically upward
        for (Integer i = x - 1; i >= 0; i--) {
            Coordinate inLoopCoordinate = new Coordinate(i, y);
            Piece inLoopPiece = this.getPiece(inLoopCoordinate);
            // Stop if a piece with a different string representation is found
            if (!inLoopPiece.toString().equals(currentStrRepr))
                break;
            VerticalClearables.add(inLoopCoordinate);
        }

        // Clear the set if fewer than 3 consecutive pieces are found
        if (VerticalClearables.size() < 3)
            VerticalClearables.clear();

        // Combine horizontal and vertical clearable coordinates into one set
        Set<Coordinate> combined = new HashSet<>();
        combined.addAll(VerticalClearables);
        combined.addAll(HorizontalClearables);

        return combined;
    }

}
