package com.gjbmloslos.elevatorsim.entities;

import javafx.scene.layout.HBox;

import java.util.ArrayDeque;
import java.util.Queue;

public class Floor {

    private int id;
    private HBox hbox;
    private Queue<Person> personQueue;

    public Floor (int id, HBox hbox) {
        this.id = id;
        this.hbox = hbox;
        personQueue = new ArrayDeque<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HBox getHbox() {
        return hbox;
    }

    public void setHbox(HBox hbox) {
        this.hbox = hbox;
    }

    public Queue<Person> getPersonQueue() {
        return personQueue;
    }

    public void setPersonQueue(Queue<Person> personQueue) {
        this.personQueue = personQueue;
    }
}
