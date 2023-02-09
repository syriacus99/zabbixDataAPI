package com.cqcnt.dao;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Geometry {
    private String type = "LineString";
    private List<List<Double>> coordinates;

    public Geometry(List<Double> from,List<Double> to){
        this.coordinates.set(0,from);
        this.coordinates.set(1,to);
    }
}
