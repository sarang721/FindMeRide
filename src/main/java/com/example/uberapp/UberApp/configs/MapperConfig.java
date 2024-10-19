package com.example.uberapp.UberApp.configs;


import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.uberapp.UberApp.dto.PointDto;
import com.example.uberapp.UberApp.utils.GeometryUtil;

@Configuration
public class MapperConfig{
    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(PointDto.class, Point.class).setConverter(context -> {
            PointDto pointdto = context.getSource();
            return GeometryUtil.createPoint(pointdto);
        });

        modelMapper.typeMap(Point.class, PointDto.class).setConverter(context -> {
            Point point = context.getSource();
            double coordinates[] = {
                point.getX(),
                point.getY()
            };

            return new PointDto(coordinates);
        });

        return modelMapper;
    }
}