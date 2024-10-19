package com.example.uberapp.UberApp.utils;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import com.example.uberapp.UberApp.dto.PointDto;

public class GeometryUtil {

    // convert pointdto to point
    public static Point createPoint(PointDto PointDto){
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Coordinate coordinate = new Coordinate(PointDto.getCoordinates()[0],PointDto.getCoordinates()[1]);
        return geometryFactory.createPoint(coordinate);
    }
}
