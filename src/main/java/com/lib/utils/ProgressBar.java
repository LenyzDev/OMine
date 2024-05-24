package com.lib.utils;

import com.google.common.base.Strings;
import org.bukkit.ChatColor;

public final class ProgressBar {

    /**
     * Generate a new progress bar.
     * This method use also his partner: {@see ProgressBar#getProgressBar(int, int, char, ChatColor, ChatColor);}.
     * @param totalBars The amount of bars in the string
     * @param completedPercentage The completed percentage of the bars amount
     * @param symbol The single bar symbol
     * @param incompleteColor The incomplete color zone of the bar
     * @param completeColor The complete color zone of the bar
     * @return The bar string
     */
    public static String getProgressBar(int totalBars, short completedPercentage, String symbol, ChatColor incompleteColor, ChatColor completeColor) {
        int current = percentage(totalBars, completedPercentage);
        return getProgressBar(totalBars, current, symbol, incompleteColor, completeColor);
    }

    /**
     * Generate a new progress bar
     * @param totalBars The amount of bars in the string
     * @param completeAmount The completed amount of the bars
     * @param symbol The single bar symbol
     * @param incompleteColor The incomplete color zone of the bar
     * @param completeColor The complete color zone of the bar
     * @return The bar string
     */
    public static String getProgressBar(int totalBars, int completeAmount, String symbol, ChatColor incompleteColor, ChatColor completeColor) {
        return completeColor+ Strings.repeat(symbol, completeAmount)+incompleteColor+ Strings.repeat(symbol, totalBars-completeAmount);
    }

    /**
     * Generate a new progress bar
     * @param totalBars The amount of bars in the string
     * @param symbol The single bar symbol
     * @param incompleteColor The incomplete color zone of the bar
     * @param completeColor The complete color zone of the bar
     * @return The bar string
     */
    public static String getProgressBar(int totalBars, short completedPercentage, String symbol, String incompleteColor, String completeColor) {
        int current = percentage(totalBars, completedPercentage);
        return completeColor+ Strings.repeat(symbol, current)+incompleteColor+ Strings.repeat(symbol, totalBars-current );
    }


    /**
     * This algorithm calculates the part of the total with respect to the percentage.
     * @param max The total part
     * @param percentage The percentage part respect {@param max}.
     * @return A total amount part
     */
    public static int percentage(int max, short percentage) {
        return Math.round((max*percentage)/100);
    }/**
     * This algorithm calculates the percentage of a part of the total.
     * @param max The total part
     * @param part The part that will become a percentage
     * @return The percentage that represent the part of the total.
     */
    public static int percentage(int max, int part) {
        return Math.round((part*100)/max);
    }
}
