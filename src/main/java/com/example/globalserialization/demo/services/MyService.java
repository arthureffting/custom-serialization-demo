package com.example.globalserialization.demo.services;


import com.example.globalserialization.demo.domain.Car;
import com.example.globalserialization.demo.domain.Position;
import org.springframework.stereotype.Service;

@Service
public class MyService {

    public Position getCarPosition(Car car) {
        Position position = new Position();
        // Do your stuff
        return position;
    }

}
