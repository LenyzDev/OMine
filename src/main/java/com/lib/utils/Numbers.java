package com.lib.utils;

public class Numbers {

    public static boolean isDouble(String string) {
        try {
            double value = Double.parseDouble(string);
            if(value > 0){
                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isInt(String string) {
        try {
            double value = Double.parseDouble(string);
            if(string.contains(".")){
                return false;
            }
            if(value > 0){
                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
