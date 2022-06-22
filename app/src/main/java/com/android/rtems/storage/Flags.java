package com.android.rtems.storage;

//Class to store JSON object to java object
public class Flags{
    private int sleep;
    private int terminate;

    public int getSleep() {
        return sleep;
    }

    public void setSleep(int sleep) {
        this.sleep = sleep;
    }

    public int getTerminate() {
        return terminate;
    }

    public void setTerminate(int terminate) {
        this.terminate = terminate;
    }

    @Override
    public String toString() {
        return "Flags{" +
                "sleep=" + sleep +
                ", terminate=" + terminate +
                '}';
    }
}

