package com.gjbmloslos.elevatorsim.entities;

import com.gjbmloslos.elevatorsim.components.ElevatorComponent;

import java.util.ArrayList;

public class Elevator implements ElevatorComponent {

    private int capacity;
    private int currentFloor;
    private Direction direction;
    private ArrayList<Person> personList;

    public Elevator(int capacity, int currentFloor) {
        this.capacity = capacity;
        this.currentFloor = currentFloor;
        this.direction = Direction.UP;
        this.personList = new ArrayList<>();
    }

    public enum Direction {
        UP,
        DOWN
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

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public ArrayList<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(ArrayList<Person> personList) {
        this.personList = personList;
    }

    @Override
    public void step (int maxFloor) {
        if (direction == Direction.UP) {
            if (currentFloor < maxFloor - 1) {
                moveUp();
            } else {
                direction = Direction.DOWN;
                moveDown();
            }
        } else {
            if (currentFloor > 0) {
                moveDown();
            } else {
                direction = Direction.UP;
                moveUp();
            }
        }
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
