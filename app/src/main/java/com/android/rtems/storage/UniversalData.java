package com.android.rtems.storage;

/**
 * This class holds full data from Parameters table from server
 * It is child class of Parameters table
 */
public class UniversalData extends Parameters{
    private int day;
    private int month;
    private int year;
    private int hours;
    private int minutes;
    private int seconds;

    public UniversalData(float temperature, float pressure, float humidity, float air_quality, int day, int month, int year, int hours, int minutes, int seconds) {
        super(temperature, pressure, humidity, air_quality);
        this.day = day;
        this.month = month;
        this.year = year;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    @Override
    public String toString() {
        return "UniversalData{" +
                "day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", hours=" + hours +
                ", minutes=" + minutes +
                ", seconds=" + seconds +
                '}';
    }
}
