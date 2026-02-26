package com.harumoto.matching.MatchingGame.Judge;

public class Judge {
    // Instance of the Condition class to track and manage game conditions.
    private Condition condition;

    /**
     * Constructor to initialize the Judge with a new Condition and set the
     * "overall" condition to
     * 0.
     */
    public Judge() {
        this.condition = new Condition();
        this.condition.init("overall", 0);
    }

    public void printLogger() {
        this.condition.printLogger();
    }

    /**
     * Getter method to retrieve the Condition instance.
     * 
     * @return The Condition instance.
     */
    public Condition getCondition() {
        return this.condition;
    }

    /**
     * Method to initialize the overall sum condition with a specified value.
     * 
     * @param value The value to set for the "overall" condition.
     */
    public void initSumCondition(Integer value) {
        this.condition.init("overall", value);
    }

    /**
     * Method to initialize a specific condition with a specified value.
     * 
     * @param condition The name of the condition to initialize.
     * @param value     The value to set for the specified condition.
     */
    public void init(String condition, Integer value) {
        this.condition.init(condition, value);
    }

    /**
     * Method to check if a specific condition is satisfied.
     * 
     * @param condition The name of the condition to check.
     * @return True if the condition is satisfied, false otherwise.
     */
    public Boolean isSatisfied(String condition) {
        return this.condition.isSatisfied(condition);
    }

    /**
     * Method to check if the logging condition is satisfied.
     * 
     * @return True if the logging condition is satisfied, false otherwise.
     */
    public Boolean logSatisfied() {
        return this.condition.logSatisfied();
    }

    /**
     * Method to check if all conditions and the logging condition are satisfied.
     * 
     * @return True if all conditions are satisfied, false otherwise.
     */
    public Boolean isAllSatisfied() {
        return this.condition.isAllSatisfied() && this.logSatisfied();
    }

    /**
     * Method to update the satisfied amount for a specific condition and the
     * "overall" condition.
     * 
     * @param condition The name of the condition to update.
     * @param value     The value to add to the satisfied amount.
     */
    public void newSatisfied(String condition, Integer value) {
        this.condition.newSatisfied(condition, value);
        this.condition.newSatisfied("overall", value);
    }

    /**
     * Method to increment the satisfied amount for a specific condition and the
     * "overall" condition
     * by 1.
     * 
     * @param condition The name of the condition to increment.
     */
    public void addSatisfied(String condition) {
        this.condition.newSatisfied(condition, 1);
        this.condition.newSatisfied("overall", 1);
    }

    /**
     * Method to log the current state of the overall condition.
     */
    public void log() {
        this.condition.log();
    }

    /**
     * Method to set the required number of times a specific condition must be
     * logged.
     * 
     * @param times The number of times a specific condition must be logged.
     */
    public void setLogTimesRq(Integer times) {
        this.condition.setLogTimesRq(times);
    }

    /**
     * Method to set the required amount for a specific logging condition.
     * 
     * @param amount The amount required for the logging condition.
     */
    public void setLogAmountRq(Integer amount) {
        this.condition.setLogAmountRq(amount);
    }

    /**
     * Method to return the string representation of the current condition status.
     * 
     * @return The string representation of the current condition status.
     */
    public String toString() {
        return this.condition.toString();
    }

    /**
     * Method to get the difference between the last two logged values.
     * 
     * @return The difference between the last two logged values.
     */
    public Integer getDLog() {
        return condition.getDLog();
    }
}
