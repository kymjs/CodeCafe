package com.kymjs.model;

/**
 * @author kymjs (http://www.kymjs.com/) on 11/27/15.
 */
public class Event {
    private String action;
    private Object object;
    public int arg;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public <T extends Object> T getObject() {
        return (T) object;
    }

    public void setObject(Object obj) {
        this.object = obj;
    }
}
