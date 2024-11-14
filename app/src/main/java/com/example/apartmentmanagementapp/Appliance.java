package com.example.apartmentmanagementapp;

import androidx.annotation.NonNull;

public class Appliance {
    private final int id;
    private String name;
    private String type;

    public Appliance(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    @NonNull
    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}
