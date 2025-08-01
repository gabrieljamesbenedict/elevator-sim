package com.gjbmloslos.elevatorsim.manager;

import com.gjbmloslos.elevatorsim.components.StatisticsComponent;

import java.util.ArrayList;

public class StatisticsManager {

    ArrayList<StatisticsComponent> stats;

    int personSpawnedAmount;
    int personServedAmount;
    int personCurrentAmount;
    double aveWaitingTime;
    double maxWaitingTime;

    public StatisticsManager () {
        stats = new ArrayList<>();
        personServedAmount = 0;
        personCurrentAmount = 0;
        personSpawnedAmount = 0;
        aveWaitingTime = 0;
        maxWaitingTime = 0;
    }

    public void addStatisticsComponent (StatisticsComponent s) {
        stats.add(s);
    }

    public void removeStatisticsComponent (StatisticsComponent s) {
        s.setInactive();
    }


    public void getAverageWaitingTime() {
        int sumWaitingTime = 0;
        for (StatisticsComponent s : stats) {
            if (s.isActive()) s.addWaitingTime();
            sumWaitingTime += s.getWaitingTime();
            if (maxWaitingTime < s.getWaitingTime()) {
                maxWaitingTime = s.getWaitingTime();
            }
        }
        aveWaitingTime = (double) sumWaitingTime / stats.size();
    }

    public void trackSpawnedPerson () {
        personSpawnedAmount++;
        personCurrentAmount++;
    }

    public void trackServedPerson () {
        personCurrentAmount--;
        personServedAmount++;
    }

    public int getPersonSpawnedAmount() {
        return personSpawnedAmount;
    }

    public int getPersonServedAmount() {
        return personServedAmount;
    }

    public int getPersonCurrentAmount() {
        return personCurrentAmount;
    }

    public double getAveWaitingTime() {
        return aveWaitingTime;
    }

    public double getMaxWaitingTime() {
        return maxWaitingTime;
    }
}
