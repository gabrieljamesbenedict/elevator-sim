package com.gjbmloslos.elevatorsim.components;

import com.gjbmloslos.elevatorsim.entities.Person;

public interface ElevatorComponent {

    public void step(int maxFloor);
    public void moveUp();
    public void moveDown();
    public void pickUp(Person person);
    public void dropOff(Person person);

}
