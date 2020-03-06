package com.example.globalserialization.demo.domain;


import com.example.globalserialization.demo.serialization.MyCustomSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = MyCustomSerializer.class)
public class IdentifiedResource {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
