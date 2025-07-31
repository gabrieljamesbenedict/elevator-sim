package com.gjbmloslos.elevatorsim.entities;

public class Person {

    private int id;
    private String role;
    private int currentFloor;
    private int destination;

    public Person(int id, String role, int currentFloor, int destination) {
        this.id = id;
        this.role = role;
        this.currentFloor = currentFloor;
        this.destination = destination;
    }

    enum PersonRole {
        STUDENT,
        FACULTY
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }
}
