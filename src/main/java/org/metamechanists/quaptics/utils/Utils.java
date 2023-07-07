package org.metamechanists.quaptics.utils;

public class Utils {
    public static double roundTo2dp(final double value) {
        return Math.round(value*Math.pow(10, 2)) / Math.pow(10, 2);
    }
}
