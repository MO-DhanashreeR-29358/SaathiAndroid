package com.mosl.tabapp.model;

import java.io.Serializable;

/**
 * Created by raju on 3/9/2017.
 */

public class CalendarModel implements Serializable {
    private String name;
    private String time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public CalendarModel(String name, String time) {

        this.name = name;
        this.time = time;
    }
}
