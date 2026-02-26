package com.harumoto.matching.MatchingGame.Judge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Condition {
    Map<String, Integer> conditions = new HashMap<>();
    Map<String, Integer> satisfied = new HashMap<>();

    /* Logger variable, append the total cleared amount per clear. */
    ArrayList<Integer> logger = new ArrayList<>();
    /* Required clear time with condition. */
    Integer logTimesRq = 0;
    /* Logged times. */
    Integer loggedTimes = 0;
    /* The condition: amount of pieces cleared every time. */
    Integer loggerCondition = 0;

    /**
     * Sets satisfied condition map, cloning from an existing map.
     * 
     * @param source The map for cloning.
     */
    public void setSatisfiedArray(Map<String, Integer> source) {
        this.satisfied.clear();
        this.satisfied.putAll(source);
    }

    /**
     * Sets the logger array list, cloning from an existing array list.
     * 
     * @param source The array list for cloning.
     */
    public void setLogger(ArrayList<Integer> source) {
        this.logger.clear();
        this.logger.addAll(source);
    }

    /**
     * Sets the loggedTimes variable.
     * 
     * @param loggedTimes
     */
    public void setLoggedTimes(Integer loggedTimes) {
        this.loggedTimes = loggedTimes;
    }

    /**
     * Returns the map for satisfied amounts.
     * 
     * @return the map for satisfied amounts.
     */
    public Map<String, Integer> getSatisfiedArray() {
        return satisfied;
    }

    /**
     * Returns the logged times.
     * 
     * @return An integer, indicating the logged times.
     */
    public Integer getLoggedTimes() {
        return loggedTimes;
    }

    /**
     * Returns the logger array.
     * 
     * @return An array list, the logger.
     */
    public ArrayList<Integer> getLogger() {
        return this.logger;
    }

    /**
     * Returns the condition for a certain piece.
     * 
     * @param condition The string representation of a certain piece or condition.
     * @return An integer, the condition.
     */
    public Integer getCondition(String condition) {
        if (condition.equals("times"))
            return this.logTimesRq;
        return this.conditions.get(condition);
    }

    /**
     * Returns the satisfied amount of a certain piece.
     * 
     * @param condition The string representation of a certain piece or condition.
     * @return An integer, the satisfied amount.
     */
    public Integer getSatisfied(String condition) {
        if (condition.equals("times")) {
            Integer counter = 0;

            for (Integer i = 0; i < this.logger.size(); i++) {
                Integer dAmount = i == 0 ? this.logger.get(0) : this.logger.get(i) - this.logger.get(i - 1);

                if (dAmount >= this.loggerCondition)
                    counter++;
            }

            return counter;
        }
        return this.satisfied.get(condition);
    }

    /**
     * Initializer: Appends a condition.
     * 
     * @param condition The string representation of a piece or condition.
     * @param value     The condition value, an integer.
     */
    public void init(String condition, Integer value) {
        this.conditions.put(condition, value);
        this.satisfied.put(condition, 0);
    }

    public void newSatisfied(String condition, Integer value) {
        System.out.println(String.format("Condition: %s, Value: %d", condition, value));
        this.satisfied.put(condition, this.satisfied.get(condition) + value);
    }

    public void printLogger() {
        System.out.println("[Logger]");
        for (int i = 0; i < logger.size(); i++) {
            System.out.print(String.format("%d, ", logger.get(i)));
        }
    }

    public Boolean isSatisfied(String condition) {
        if (this.conditions.get(condition) <= this.satisfied.get(condition))
            return true;
        return false;
    }

    public Boolean isAllSatisfied() {
        for (String condition : this.conditions.keySet())
            if (this.conditions.get(condition) > this.satisfied.get(condition))
                return false;
        return true;
    }

    public void log() {
        System.out.println(String.format("Logged: %d", this.satisfied.get("overall")));
        this.logger.add(this.satisfied.get("overall"));
        this.loggedTimes++;
    }

    public void setLogTimesRq(Integer times) {
        this.logTimesRq = times;
    }

    public void setLogAmountRq(Integer amount) {
        this.loggerCondition = amount;
    }

    public Boolean logSatisfied() {
        Integer counter = 0;
        for (Integer i = 0; i < this.logger.size(); i++) {
            Integer dAmount = i == 0 ? this.logger.get(0) : this.logger.get(i) - this.logger.get(i - 1);
            if (dAmount >= this.loggerCondition)
                counter++;
        }
        System.out.println(String.format("Counter: %d, logTimesRq: %d", counter, logTimesRq));
        if (counter < this.logTimesRq)
            return false;
        return true;
    }

    private String toGUIForm(String condition) {
        if (condition.equals("A"))
            return "Diamond";
        else if (condition.equals("B"))
            return "Emerald";
        else if (condition.equals("C"))
            return "Eye of Ender";
        else if (condition.equals("D"))
            return "Redstone";
        else if (condition.equals("E"))
            return "Lazurite";
        else
            return "";
    }

    public String toString() {
        String status = "";
        for (String condition : this.conditions.keySet()) {
            if (this.conditions.get(condition) != 0 && !condition.equals("overall")) {
                status += String.format("%d/%d of %s has been eliminated.\n",
                        this.satisfied.get(condition), this.conditions.get(condition),
                        String.format("%s(%s)", condition, this.toGUIForm(condition)));
            }
        }
        if (this.conditions.get("overall") != 0) {
            status += String.format("%d/%d of elements has been eliminated.\n",
                    this.satisfied.get("overall"), this.conditions.get("overall"));
        }
        if (this.logTimesRq != 0 && this.loggerCondition != 0) {
            Integer counter = 0;
            for (Integer i = 0; i < this.logger.size(); i++) {
                Integer dAmount = i == 0 ? this.logger.get(0) : this.logger.get(i) - this.logger.get(i - 1);
                if (dAmount >= this.loggerCondition)
                    counter++;
            }
            status += String.format("%d/%d of clear operation meets the requirement.\n", counter,
                    this.logTimesRq);
        }
        return status;
    }

    Integer getDLog() {
        return logger.size() > 1 ? logger.get(logger.size() - 1) - logger.get(logger.size() - 2)
                : logger.get(0);
    }
}
