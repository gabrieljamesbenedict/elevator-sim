package com.gjbmloslos.elevatorsim.manager;

import com.gjbmloslos.elevatorsim.constants.PersonRole;
import com.gjbmloslos.elevatorsim.constants.SimulationConfig;
import com.gjbmloslos.elevatorsim.entities.Person;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class PersonSpawnManager {

    Random r;

    AtomicInteger personIdCounter = new AtomicInteger(0);
    ArrayList<Person> personList;

    public PersonSpawnManager () {
        r = new Random();
        this.personList = new ArrayList<>();
    }

    public Person spawn () {

        float facultyChance = SimulationConfig.FACULTY_CHANCE;

        int maxFloor = SimulationConfig.BUILDING_MAX_FLOOR;
        int id = personIdCounter.incrementAndGet();
        PersonRole role = r.nextFloat() < facultyChance ? PersonRole.FACULTY : PersonRole.STUDENT;
        int currentFloor = r.nextInt(maxFloor);
        int destination = r.nextInt(maxFloor);
        while (currentFloor == destination) {
            destination = r.nextInt(maxFloor);
        }

        Person person = new Person(id, role, currentFloor, destination);
        personList.add(person);

        return person;
    }

}
