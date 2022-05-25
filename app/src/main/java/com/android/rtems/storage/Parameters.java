package com.android.rtems.storage;

public class Parameters {
    private int temperature;
    private int pressure;
    private int humidity;
    private int air_quality;

    public Parameters(int temperature, int pressure, int humidity, int air_quality) {
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.air_quality = air_quality;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getAir_quality() {
        return air_quality;
    }

    public void setAir_quality(int air_quality) {
        this.air_quality = air_quality;
    }
}
