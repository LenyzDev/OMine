package com.lib.utils;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Formatter {

    private static final List<String> suffixes = Arrays.asList("", "K", "M", "B", "T", "Q", "QQ", "S", "SP", "O", "N", "D");

    public static String compressTime(Long time){
        long minutos = TimeUnit.MILLISECONDS.toMinutes(time);
        long segundos =  TimeUnit.MILLISECONDS.toSeconds(time) - (minutos * 60);
        return String.format("%02d:%02d", minutos, segundos);
    }

    public static String formatTime(Long time) {
        if (time >= 3600000L) {
            String duration = String.format("%d hora",
                    TimeUnit.MILLISECONDS.toHours(time)
            );
            if (TimeUnit.MILLISECONDS.toHours(time) > 1) {
                duration += "s";
            }

            if (time - 3600000L > 60000L) {
                if (TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.MILLISECONDS.toHours(time) * 60 > 0) {
                    duration += String.format(" e %d minutos",
                            TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.MILLISECONDS.toHours(time) * 60
                    );
                } else {
                    if (TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MILLISECONDS.toHours(time) * 3600 > 0) {
                        duration += String.format(" e %d segundos",
                                TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MILLISECONDS.toHours(time) * 3600
                        );
                    }
                }
            } else {
                if (time - 3600000L > 0L) {
                    duration += String.format(" e %d segundos",
                            TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MILLISECONDS.toHours(time) * 3600
                    );
                }
            }

            return duration;
        } else {
            if (time >= 60000L) {
                String duration = String.format("%d minuto",
                        TimeUnit.MILLISECONDS.toMinutes(time)
                );
                if (TimeUnit.MILLISECONDS.toMinutes(time) > 1) {
                    duration += "s";
                }

                if (time - 60000L > 0L) {
                    if (TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)) > 0) {
                        duration += String.format(" e %d segundos",
                                TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))
                        );
                    }
                }
                return duration;
            } else {
                return (time / 1000) + " segundos";
            }
        }
    }

    public static String formatDecimals(Double value, int decimalsQuantity){
        return String.format(Locale.GERMAN, "%,."+decimalsQuantity+"f", value);
    }
    public static String formatDecimals(Integer value, int decimalsQuantity){
        return String.format(Locale.GERMAN, "%,."+decimalsQuantity+"f", Double.parseDouble(String.valueOf(value)));
    }
    public static String formatDecimals(Double value, int decimalsQuantity, boolean limited){
        if(limited && value > 999999999){
            return formatNumber(value);
        }
        return String.format(Locale.GERMAN, "%,."+decimalsQuantity+"f", Double.parseDouble(String.valueOf(value)));
    }
    public static String formatDecimals(Integer value, int decimalsQuantity, boolean limited){
        if(limited && value > 999999999){
            return formatNumber(value);
        }
        return String.format(Locale.GERMAN, "%,."+decimalsQuantity+"f", Double.parseDouble(String.valueOf(value)));
    }

    private static String formatNumber(double value) {
        int index = 0;

        double tmp;
        while ((tmp = value / 1000) >= 1) {
            value = tmp;
            ++index;
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(value) + suffixes.get(index);
    }
}
