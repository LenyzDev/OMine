package com.lib.utils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TimeFormatter {

    private String and;
    private String second;
    private String minute;
    private String hour;

    public TimeFormatter(String and, String second, String minute, String hour){
        this.and = and;
        this.second = second;
        this.minute = minute;
        this.hour = hour;
    }

    public String compressTime(Long time){
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
        long seconds =  TimeUnit.MILLISECONDS.toSeconds(time) - (minutes * 60);
        return String.format("%02d:%02d", minutes, seconds);
    }

    public String formatTime(int seconds) {
        long time = seconds * 1000L;
        if (time >= 3600000L) {
            String duration = String.format("%d "+hour,
                    TimeUnit.MILLISECONDS.toHours(time)
            );
            if (TimeUnit.MILLISECONDS.toHours(time) > 1) {
                duration += "s";
            }

            if (time - 3600000L > 60000L) {
                if (TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.MILLISECONDS.toHours(time) * 60 > 0) {
                    duration += String.format(" "+and+" %d "+minute+"s",
                            TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.MILLISECONDS.toHours(time) * 60
                    );
                } else {
                    if (TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MILLISECONDS.toHours(time) * 3600 > 0) {
                        duration += String.format(" "+and+" %d "+second+"s",
                                TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MILLISECONDS.toHours(time) * 3600
                        );
                    }
                }
            } else {
                if (time - 3600000L > 0L) {
                    duration += String.format(" "+and+" %d "+second+"s",
                            TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MILLISECONDS.toHours(time) * 3600
                    );
                }
            }

            return duration;
        } else {
            if (time >= 60000L) {
                String duration = String.format("%d "+minute,
                        TimeUnit.MILLISECONDS.toMinutes(time)
                );
                if (TimeUnit.MILLISECONDS.toMinutes(time) > 1) {
                    duration += "s";
                }

                if (time - 60000L > 0L) {
                    if (TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)) > 0) {
                        duration += String.format(" "+and+" %d "+second+"s",
                                TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))
                        );
                    }
                }
                return duration;
            } else {
                return (time / 1000) + " "+second+"s";
            }
        }
    }

    public String formatTime(Long time) {
        if (time >= 3600000L) {
            String duration = String.format("%d "+hour,
                    TimeUnit.MILLISECONDS.toHours(time)
            );
            if (TimeUnit.MILLISECONDS.toHours(time) > 1) {
                duration += "s";
            }

            if (time - 3600000L > 60000L) {
                if (TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.MILLISECONDS.toHours(time) * 60 > 0) {
                    duration += String.format(" "+and+" %d "+minute+"s",
                            TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.MILLISECONDS.toHours(time) * 60
                    );
                } else {
                    if (TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MILLISECONDS.toHours(time) * 3600 > 0) {
                        duration += String.format(" "+and+" %d "+second+"s",
                                TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MILLISECONDS.toHours(time) * 3600
                        );
                    }
                }
            } else {
                if (time - 3600000L > 0L) {
                    duration += String.format(" "+and+" %d "+second+"s",
                            TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MILLISECONDS.toHours(time) * 3600
                    );
                }
            }

            return duration;
        } else {
            if (time >= 60000L) {
                String duration = String.format("%d "+minute,
                        TimeUnit.MILLISECONDS.toMinutes(time)
                );
                if (TimeUnit.MILLISECONDS.toMinutes(time) > 1) {
                    duration += "s";
                }

                if (time - 60000L > 0L) {
                    if (TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)) > 0) {
                        duration += String.format(" "+and+" %d "+second+"s",
                                TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))
                        );
                    }
                }
                return duration;
            } else {
                return (time / 1000) + " "+second+"s";
            }
        }
    }
}
