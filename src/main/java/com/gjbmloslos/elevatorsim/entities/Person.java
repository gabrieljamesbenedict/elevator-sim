package com.gjbmloslos.elevatorsim.entities;

public class Person {
    private int id;
    private String role;
    private int currFloor;
    private int destination;

    public Person(int id, String role, int currFloor, int destination) {
        this.id = id;
        this.role = role;
        this.currFloor = currFloor;
        this.destination = destination;
    }


    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public int getCurrFloor() {
        return currFloor;
    }

    public void setCurrFloor(int currFloor) {
        this.currFloor = currFloor;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
