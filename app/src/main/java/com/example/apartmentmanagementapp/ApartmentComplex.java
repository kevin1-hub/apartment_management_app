package com.example.apartmentmanagementapp;
// ApartmentComplex.java
public class ApartmentComplex {
    private String name;
    private int floors;
    private int parkingSlots;
    private boolean securityPersonnel;
    private boolean cctv;

    // No-argument constructor
    public ApartmentComplex() {
    }

    // Constructor with parameters (for convenience if needed)
    public ApartmentComplex(String name, int floors, int parkingSlots, boolean securityPersonnel, boolean cctv) {
        this.name = name;
        this.floors = floors;
        this.parkingSlots = parkingSlots;
        this.securityPersonnel = securityPersonnel;
        this.cctv = cctv;
    }

    // Getters and setters for each property
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

    public int getParkingSlots() {
        return parkingSlots;
    }

    public void setParkingSlots(int parkingSlots) {
        this.parkingSlots = parkingSlots;
    }

    public boolean isSecurityPersonnel() {
        return securityPersonnel;
    }

    public void setSecurityPersonnel(boolean securityPersonnel) {
        this.securityPersonnel = securityPersonnel;
    }

    public boolean isCCTV() {
        return cctv;
    }

    public void setCCTV(boolean cctv) {
        this.cctv = cctv;
    }
}
