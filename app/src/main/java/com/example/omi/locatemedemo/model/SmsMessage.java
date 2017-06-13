package com.example.omi.locatemedemo.model;

import java.io.Serializable;

/**
 * Created by omi on 6/12/2017.
 */

public class SmsMessage implements Serializable{
    private String number;
    private String body;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
