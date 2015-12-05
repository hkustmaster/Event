package com.hkust.android.event.model;

/**
 * Created by hozdanny on 15/12/5.
 */
public class Location {
    private String type;
    private double[] coordinates;

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
