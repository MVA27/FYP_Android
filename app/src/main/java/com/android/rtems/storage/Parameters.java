package com.android.rtems.storage;

public class Parameters {
    private float temperature;
    private float pressure;
    private float humidity;
    private float air_quality;

    public Parameters(){}
    public Parameters(float temperature, float pressure, float humidity, float air_quality) {
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.air_quality = air_quality;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getAir_quality() {
        return air_quality;
    }

    public void setAir_quality(float air_quality) {
        this.air_quality = air_quality;
    }
}
