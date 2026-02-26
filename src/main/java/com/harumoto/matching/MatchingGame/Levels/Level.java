package com.harumoto.matching.MatchingGame.Levels;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

import com.harumoto.matching.MatchingGame.Coordinate;
import com.harumoto.matching.MatchingGame.GameCanvas;
import com.harumoto.matching.MatchingGame.Judge.Judge;
import com.harumoto.matching.MatchingGame.Pieces.Eliminator;
import com.harumoto.matching.MatchingGame.Pieces.Piece;
import com.harumoto.matching.MatchingGame.Pieces.PieceTypes;

public class Level {
    public GameCanvas levelCanvas;
    String presetPath;
    private Integer canvasSize;
    public Judge judge;

    /**
     * Returns the instance of Judge class in use.
     * 
     * @return The instance of Judge class in use.
     */
    public Judge getJudge() {
        return this.judge;
    }

    /**
     * Returns the file path of this level.
     * 
     * @return A string, the file path of this level.
     */
    public String getPath() {
        return this.presetPath;
    }

    /**
     * Returns the instance of GameCanvas in use.
     * 
     * @return The instance of GameCanvas in use.
     */
    public GameCanvas getCanvas() {
        return levelCanvas;
    }

    /**
     * Clears pieces on the game canvas starting from the specified coordinate.
     * 
     * @param coordinate the starting coordinate
     * @return true if any pieces were cleared, false otherwise
     */
    public boolean clear(Coordinate coordinate) {

        Piece currentPiece = this.levelCanvas.getPiece(coordinate);

        if (currentPiece.type().equals(PieceTypes.ORDINARY)) {

            Set<Coordinate> clearables = this.levelCanvas.getClearables(coordinate);
            // If there are no clearable coordinates, return false
            if (clearables.isEmpty())
                return false;
            // Clear each piece in the set of clearable coordinates
            for (Coordinate clearable : clearables) {
                this.judge.addSatisfied(this.levelCanvas.getPiece(clearable).toString());
                this.levelCanvas.nullize(clearable);
            }
            // Clear the original piece as well
            this.judge.addSatisfied(currentPiece.toString());
            this.levelCanvas.nullize(coordinate);

            // Check if the piece is of type ELIMINATOR
        } else if (currentPiece.type().equals(PieceTypes.ELIMINATOR)) {

            String target = ((Eliminator) currentPiece).target();
            // Iterate through the entire canvas to find and clear target pieces
            for (int i = 0; i < this.canvasSize; i++) {
                for (int j = 0; j < this.canvasSize; j++) {
                    Coordinate inLoopCoordinate = new Coordinate(i, j);
                    if (this.levelCanvas.getPiece(inLoopCoordinate).toString().equals(target)) {
                        this.judge.addSatisfied(
                                this.levelCanvas.getPiece(inLoopCoordinate).toString());
                        this.levelCanvas.nullize(inLoopCoordinate);
                    }
                }
            }
            this.levelCanvas.nullize(coordinate);

        } else if (currentPiece.type().equals(PieceTypes.SPECIAL)) {

            if (currentPiece.toString().equals("+")) {
                int x = coordinate.x();
                int y = coordinate.y();
                // Clear a 3x3 area centered on the special piece
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (0 <= i + x && i + x < this.levelCanvas.size() && 0 <= j + y
                                && j + y < this.levelCanvas.size()
                                && !this.levelCanvas.getPiece(new Coordinate(i + x, j + y))
                                        .toString().equals("#")) {
                            this.judge.addSatisfied(this.levelCanvas
                                    .getPiece(new Coordinate(i + x, j + y)).toString());
                            this.levelCanvas.nullize(new Coordinate(i + x, j + y));
                        }
                    }
                }
                return true;

            } else if (currentPiece.toString().equals("|")) {
                // Clear all pieces in the column of the special piece
                for (int i = 0; i < this.levelCanvas.size(); i++) {
                    Coordinate inLoopCoordinate = new Coordinate(i, coordinate.y());
                    if (this.levelCanvas.getPiece(inLoopCoordinate).toString().equals("#"))
                        continue;
                    this.judge.addSatisfied(this.levelCanvas.getPiece(inLoopCoordinate).toString());
                    this.levelCanvas.nullize(inLoopCoordinate);
                }
            }
        } else {
            return false; // Return false if the piece type does not match any expected types
        }
        return true; // Return true if pieces were successfully cleared
    }

    /**
     * Constructor to initialize the Level object using a preset configuration file.
     * 
     * @param presetPath the path to the preset configuration file
     * @throws Exception if an error occurs while reading the file or parsing the
     *                   configuration
     */
    public Level(String presetPath) throws Exception {
        this.presetPath = presetPath;
        // Read the entire preset file and split it into lines
        String[] presetLines = Files.readString(Paths.get(presetPath)).split("\n");
        // Split the first line to get the overall configuration
        String[] overallConfig = presetLines[0].split(",");

        this.judge = new Judge(); // Initialize the Judge object

        // Parse and set the canvas size from the overall configuration
        this.canvasSize = Integer.parseInt(overallConfig[0]);
        this.levelCanvas = new GameCanvas(this.canvasSize); // Initialize the GameCanvas object with
                                                            // the canvas size

        // Initialize the Judge conditions based on the overall configuration
        this.judge.initSumCondition(Integer.parseInt(overallConfig[1]));
        this.judge.setLogTimesRq(Integer.parseInt(overallConfig[2]));
        this.judge.setLogAmountRq(Integer.parseInt(overallConfig[3]));

        // Split the second line to get the piece conditions
        String[] pieceConditions = presetLines[1].split(",");

        // Initialize the ordinary pieces' conditions in the Judge
        for (Integer i = 0; i < Piece.ORDINARIES.length; i++)
            this.judge.init(Piece.ORDINARIES[i], Integer.parseInt(pieceConditions[i]));

        // Initialize the special and eliminator pieces' conditions in the Judge to 0
        for (Integer i = 0; i < Piece.SPECIALS.length; i++)
            this.judge.init(Piece.SPECIALS[i], 0);
        for (Integer i = 0; i < Piece.ELIMINATORS.length; i++)
            this.judge.init(Piece.ELIMINATORS[i], 0);

        // Load the board configuration from the preset file
        for (Integer i = 0; i < this.canvasSize; i++) {
            for (Integer j = 0; j < this.canvasSize; j++) {
                Coordinate inLoopCoordinate = new Coordinate(i, j); // Create a coordinate for the
                                                                    // current cell
                // Place the piece on the canvas based on the preset configuration
                this.levelCanvas.placePiece(Piece.toPiece((presetLines[2 + i].split(","))[j],
                        levelCanvas, inLoopCoordinate), inLoopCoordinate);
            }
        }
    }

}
