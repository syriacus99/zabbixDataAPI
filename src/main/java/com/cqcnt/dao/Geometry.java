package com.cqcnt.dao;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Geometry {
    private String type = "LineString";
    private List<List<Double>> coordinates;
    public Geometry(){
        coordinates = new ArrayList<>();
    }
    public Geometry(List<Double> from,List<Double> to){
        coordinates = new ArrayList<>();
        coordinates.add(to);
        coordinates.add(from);
    }
}
