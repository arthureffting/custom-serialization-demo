package com.example.globalserialization.demo.controllers;


import com.example.globalserialization.demo.domain.Car;
import com.example.globalserialization.demo.domain.Manufacturer;
import com.example.globalserialization.demo.domain.Model;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @GetMapping(value = "/car")
    public ResponseEntity<Car> getMeMyCar() {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId("koyota-identifier");
        manufacturer.setName("Koyota");
        Model model = new Model();
        model.setId("torolla-identifier");
        model.setName("Torolla");
        Car car = new Car();
        car.setId("car-identifier");
        car.setManufacturer(manufacturer);
        car.setModel(model);
        return ResponseEntity.ok(car);
    }

}
