package com.example.uberapp.UberApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointDto {

    private double[] coordinates;
    private String type = "Point";

    public PointDto(double[] coordinates){
        this.coordinates = coordinates;
    }
}
