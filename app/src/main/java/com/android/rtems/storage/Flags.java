package com.android.rtems.storage;

import com.google.gson.annotations.SerializedName;

//Class to store JSON object to java object
public class Flags{
    private int sleep;
    private int terminate;

    @SerializedName("sms_service")
    private int smsService;

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

    public int getSmsService() {
        return smsService;
    }

    public void setSmsService(int smsService) {
        this.smsService = smsService;
    }

    @Override
    public String toString() {
        return "Flags{" +
                "sleep=" + sleep +
                ", terminate=" + terminate +
                '}';
    }
}

