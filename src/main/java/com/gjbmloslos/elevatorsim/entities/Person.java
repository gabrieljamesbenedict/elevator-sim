package com.gjbmloslos.elevatorsim.entities;

import com.gjbmloslos.elevatorsim.constants.PersonRole;

public class Person {

    private int id;
    private PersonRole role;
    private int currentFloor;
    private int destination;

    public Person(int id, PersonRole role, int currentFloor, int destination) {
        this.id = id;
        this.role = role;
        this.currentFloor = currentFloor;
        this.destination = destination;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PersonRole getRole() {
        return role;
    }

    public void setRole(PersonRole role) {
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
