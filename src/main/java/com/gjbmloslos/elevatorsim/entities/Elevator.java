package com.gjbmloslos.elevatorsim.entities;

import com.gjbmloslos.elevatorsim.services.ElevatorComponent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

public class Elevator implements ElevatorComponent {

    private int capacity;
    private int currentFloor;

    private ArrayList<Person> personList;

    public Elevator(int capacity, int currentFloor) {
        this.capacity = capacity;
        this.currentFloor = currentFloor;
        this.personList = new ArrayList<>();
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public ArrayList<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(ArrayList<Person> personList) {
        this.personList = personList;
    }

    @Override
    public void moveUp() {
        currentFloor++;
    }

    @Override
    public void moveDown() {
        currentFloor--;
    }

    @Override
    public void pickUp(Person person) {
        personList.add(person);
    }

    @Override
    public void dropOff(Person person) {
        personList.remove(person);
    }

}
