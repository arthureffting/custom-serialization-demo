package com.example.globalserialization.demo.domain;

import com.example.globalserialization.demo.services.MyService;
import com.example.globalserialization.demo.annotations.Identified;
import com.example.globalserialization.demo.annotations.Named;
import com.example.globalserialization.demo.annotations.ServiceDependent;

public class Car extends IdentifiedResource {

    @Identified
    private Model model;

    @Named
    private Manufacturer manufacturer;

    @ServiceDependent(service = MyService.class,
            method = "getCarPosition")
    private Position position;

    //...

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

}
