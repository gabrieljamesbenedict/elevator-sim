package com.gjbmloslos.elevatorsim.entities;

import com.gjbmloslos.elevatorsim.components.StatisticsComponent;
import com.gjbmloslos.elevatorsim.constants.Direction;
import com.gjbmloslos.elevatorsim.constants.PersonRole;
import javafx.scene.layout.Pane;

public class Person extends StatisticsComponent {

    private int id;
    private PersonRole role;
    private int currentFloor;
    private int destination;
    private Direction direction;

    private Pane pane;

    public Person(int id, PersonRole role, int currentFloor, int destination) {
        this.id = id;
        this.role = role;
        this.currentFloor = currentFloor;
        this.destination = destination;
        this.direction = currentFloor < destination? Direction.UP : Direction.DOWN;
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

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Pane getPane() {
        return pane;
    }

    public void setPane(Pane pane) {
        this.pane = pane;
    }
}
