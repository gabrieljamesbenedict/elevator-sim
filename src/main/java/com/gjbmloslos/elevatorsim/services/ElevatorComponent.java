package com.gjbmloslos.elevatorsim.services;

import com.gjbmloslos.elevatorsim.entities.Elevator;
import com.gjbmloslos.elevatorsim.entities.Person;

public interface ElevatorComponent {

    public void moveUp();
    public void moveDown();
    public void pickUp(Person person);
    public void dropOff(Person person);

}
