package com.gjbmloslos.elevatorsim.components;

public class StatisticsComponent {

    int waitingTime = 0;
    boolean active = true;

    public void addWaitingTime () {
        waitingTime++;
    }

    public int getWaitingTime () {
        return waitingTime;
    }

    public void setInactive() {
        active = false;
    }

    public boolean isActive () {
        return active;
    }

}
