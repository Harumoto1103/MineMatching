package com.harumoto.matching.MatchingGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.harumoto.matching.MatchingGame.Pieces.Piece;

/**
 * The Backup class is used to store and restore the state of the game.
 */
public class Backup {
    // Stores satisfied conditions as a map
    Map<String, Integer> satisfied = new HashMap<>();
    // Logger to keep track of certain events or actions
    ArrayList<Integer> logger = new ArrayList<>();
    // Keeps track of the number of times something was logged
    Integer loggedTimes = 0;

    // The game canvas (2D array of pieces) and its size
    private Piece[][] canvas;
    private Integer canvasSize;

    // Stores the state of a "hammer" in the game
    Integer hammer;

    /**
     * Backs up the hammer state.
     * 
     * @param hammer the hammer state to backup
     */
    public void backupHammer(Integer hammer) {
        this.hammer = hammer;
    }

    /**
     * Backs up the satisfied array.
     * 
     * @param satisfied the satisfied conditions to backup
     */
    public void backupSatisfiedArray(Map<String, Integer> satisfied) {
        this.satisfied.putAll(satisfied);
    }

    /**
     * Backs up the logger and the number of logged times.
     * 
     * @param logger the logger data to backup
     * @param loggedTimes the number of times logged
     */
    public void backupLogger(ArrayList<Integer> logger, Integer loggedTimes) {
        this.logger.addAll(logger);
        this.loggedTimes = loggedTimes;
    }

    /**
     * Backs up the game canvas.
     * 
     * @param canvas the game canvas to backup
     * @param canvasSize the size of the game canvas
     */
    public void backupCanvas(Piece[][] canvas, Integer canvasSize) {
        this.canvas = new Piece[canvasSize][canvasSize];
        for (int i = 0; i < canvasSize; i++) {
            for (int j = 0; j < canvasSize; j++) {
                this.canvas[i][j] = canvas[i][j];
            }
        }
        this.canvasSize = canvasSize;
    }

    /**
     * Restores the satisfied array to the given destination map.
     * 
     * @param destination the map to restore the satisfied conditions into
     */
    public void restoreSatisfiedArray(Map<String, Integer> destination) {
        destination.clear();
        destination.putAll(this.satisfied);
    }

    /**
     * Returns the backed-up satisfied array.
     * 
     * @return the satisfied conditions map
     */
    public Map<String, Integer> getSatisfiedArray() {
        return this.satisfied;
    }

    /**
     * Returns the backed-up logger.
     * 
     * @return the logger data
     */
    public ArrayList<Integer> getLogger() {
        return this.logger;
    }

    /**
     * Restores the logger to the given destination list.
     * 
     * @param destination the list to restore the logger data into
     */
    public void restoreLogger(ArrayList<Integer> destination) {
        destination.clear();
        destination.addAll(this.logger);
    }

    /**
     * Restores the game canvas to the given destination 2D array.
     * 
     * @param destination the 2D array to restore the canvas into
     */
    public void restoreCanvas(Piece[][] destination) {
        for (int i = 0; i < canvasSize; i++) {
            for (int j = 0; j < canvasSize; j++) {
                destination[i][j] = this.canvas[i][j];
            }
        }
    }

    /**
     * Returns the number of logged times.
     * 
     * @return the number of logged times
     */
    public Integer getLoggedTimes() {
        return this.loggedTimes;
    }

    /**
     * Returns the backed-up hammer state.
     * 
     * @return the hammer state
     */
    public Integer getHammer() {
        return this.hammer;
    }
}
