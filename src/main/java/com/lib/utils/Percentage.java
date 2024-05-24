package com.lib.utils;

public class Percentage {

    /**
     * This algorithm calculates the percentage of completion between two values.
     * @param limited If true, the maximum value will be 100.
     * @param part The value already completed.
     * @param max The maximum the value can reach.
     * @return A percentage
     */
    public static double getPercentage(boolean limited, double part, double max){
        double percentage = Math.round((part*100)/max);
        if(percentage >= 100.0 && limited){
            return 100.0;
        }
        return percentage;
    }

    /**
     * This algorithm calculates the percentage of completion between two or more values.
     * @param limited If true, the maximum value will be 100.
     * @param percentageData The percentage object with {@param part The value already completed}, and {@param max The maximum the value can reach}.
     * @return A percentage
     */
    public static double getPercentage(boolean limited, PercentageData... percentageData){
        double percentage = 0;

        for (int i = 0; i < percentageData.length; i++) {

            double percentageLoop = Math.round((percentageData[i].part*100)/percentageData[i].max);

            if(percentageLoop >= 100.0 && limited){
                percentage += 100.0;
            }else{
                percentage += percentageLoop;
            }

        }

        percentage = percentage / percentageData.length;

        return percentage;
    }

    public static class PercentageData{

        private double part;
        private double max;

        public PercentageData(double part, double max){
            this.part = part;
            this.max = max;
        }

        public double getPart() {
            return part;
        }

        public double getMax() {
            return max;
        }
    }
}
