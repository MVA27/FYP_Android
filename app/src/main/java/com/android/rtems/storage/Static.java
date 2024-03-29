package com.android.rtems.storage;

/**
 * All the static variables that may be required by various classes are encapsulated in a single class
 */
public class Static {

    public static UniversalData[] universalData;

    public static Parameters parameters;

    /**
     * Threshold values that are fetched from various activities are placed in this variable
     */
    public static Parameters threshold;

    public static Flags flags;

    /**
     * Time(in sec)
     * Default: time is kept to 10 seconds
     * Type: is double as integers resulted in 0 during division
     */
    public static double refreshTime = 10.0D;

    public static User user;
}
