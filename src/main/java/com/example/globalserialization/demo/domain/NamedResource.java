package com.example.globalserialization.demo.domain;

public class NamedResource extends IdentifiedResource {

    private String name;

    //...

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
