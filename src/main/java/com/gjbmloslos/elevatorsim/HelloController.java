package com.gjbmloslos.elevatorsim;

import com.gjbmloslos.elevatorsim.constants.ElevatorStats;
import com.gjbmloslos.elevatorsim.constants.PersonRole;
import com.gjbmloslos.elevatorsim.constants.SimulationConfig;
import com.gjbmloslos.elevatorsim.entities.Elevator;
import com.gjbmloslos.elevatorsim.entities.Person;
import com.gjbmloslos.elevatorsim.manager.PersonSpawnManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class HelloController {

    PersonSpawnManager spawnManager;

    HBox[] floors;
    Pane[][] elevatorPanes;
    Elevator[] elevators;

    @FXML
    VBox building;

    @FXML
    Label tickCounter;

    @FXML
    public void initialize () {

        spawnManager = new PersonSpawnManager();

        generateFloors();
        generateElevators();
        startSimulation();

        System.out.println("HELLO WORLD");
    }

    private void generateFloors () {

        int floorsAmount = SimulationConfig.BUILDING_MAX_FLOOR;
        int elevatorAmount = SimulationConfig.BUILDING_FACULTY_ELEVATOR_AMOUNT + SimulationConfig.BUILDING_STUDENT_ELEVATOR_AMOUNT;
        int elevatorSize = 75;
        int boxWidth = elevatorAmount * elevatorSize;

        elevatorPanes = new Pane[SimulationConfig.BUILDING_MAX_FLOOR][elevatorAmount];
        floors = new HBox[floorsAmount];

        for (int i = 0; i < floorsAmount; i++) {
            HBox floor = new HBox();
            int height = 75;
            floor.setMinHeight(height);
            floor.setPrefHeight(height);
            floor.setMaxHeight(height);
            floor.prefWidth(600);
            BackgroundFill fill = new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY);
            floor.setBackground(new Background(fill));
            floor.setAlignment(Pos.CENTER_LEFT);
            floor.setSpacing(10);

            HBox innerBox = new HBox();
            innerBox.setMinSize(boxWidth, height);
            innerBox.setPrefSize(boxWidth, height);
            innerBox.setMaxSize(boxWidth, height);
            //BackgroundFill innerFill = new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY);
            //innerBox.setBackground(new Background(innerFill));
            innerBox.setSpacing(5);
            for (int j = 0; j < elevatorAmount; j++) {
                Pane pane = new Pane();
                setPaneDimension(pane, elevatorSize);
                BackgroundFill elevFill = new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY);
                pane.setBackground(new Background(elevFill));
                elevatorPanes[floorsAmount-i-1][j] = pane;
                innerBox.getChildren().add(pane);
            }
            floor.getChildren().add(innerBox);

            // Add the floor to the VBox and the floorList
            building.getChildren().add(floor);
            floors[i] = floor;
        }

    }

    private void generateElevators () {

        int elevatorSize = 75;
        int facultyElevators = SimulationConfig.BUILDING_FACULTY_ELEVATOR_AMOUNT;
        int studentElevators = SimulationConfig.BUILDING_STUDENT_ELEVATOR_AMOUNT;
        int elevatorAmount = facultyElevators + studentElevators;
        int elevatorCapacity = ElevatorStats.ELEVATOR_MAX_CAPACITY;
        int startingFloor = 0;

        elevators = new Elevator[elevatorAmount];

        for (int i = 0; i < facultyElevators; i++) {
            Pane pane = elevatorPanes[0][i];
            setPaneDimension(pane, elevatorSize);
            BackgroundFill elevFill = new BackgroundFill(Color.LIGHTSALMON, CornerRadii.EMPTY, Insets.EMPTY);
            pane.setBackground(new Background(elevFill));
            elevators[i] = new Elevator(i, PersonRole.FACULTY, elevatorCapacity, startingFloor);
        }

        for (int i = facultyElevators; i < elevatorAmount; i++) {
            Pane pane = elevatorPanes[0][i];
            setPaneDimension(pane, elevatorSize);
            BackgroundFill elevFill = new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY);
            pane.setBackground(new Background(elevFill));
            elevators[i] = new Elevator(i, PersonRole.STUDENT, elevatorCapacity, startingFloor);
        }

    }

    private void startSimulation () {

        AtomicInteger tickNum = new AtomicInteger(0);
        AtomicInteger spawnCooldown = new AtomicInteger(0);

        final int spawnDelay = SimulationConfig.PERSON_SPAWN_DELAY;
        int facultyElevators = SimulationConfig.BUILDING_FACULTY_ELEVATOR_AMOUNT;
        int studentElevators = SimulationConfig.BUILDING_STUDENT_ELEVATOR_AMOUNT;
        int elevatorAmount = facultyElevators + studentElevators;
        int maxFloor = SimulationConfig.BUILDING_MAX_FLOOR;
        int speed = ElevatorStats.ELEVATOR_SPEED;

        Runnable tick = () -> {
            int currentTick = tickNum.incrementAndGet();
            Platform.runLater(()-> tickCounter.setText("Ticks: " + currentTick));

            for (Elevator e : elevators) {
                if (currentTick % speed == 0) {
                    updateElevatorPosition(elevators, elevatorPanes, e.getId(), Color.GRAY);
                    e.step(maxFloor);
                    System.out.println("Elevator " + e.getId() + " moved to floor:" + e.getCurrentFloor() +
                            ", direction: " + e.getDirection());
                }


                updateElevatorPosition(elevators, elevatorPanes, e.getId(), e.getRole() == PersonRole.FACULTY? Color.LIGHTSALMON:Color.LIGHTBLUE);
            }

            int cooldown = spawnCooldown.decrementAndGet();
            if (cooldown <= 0) {
                Person person = spawnManager.spawn();
                Pane pane = new Pane();
                setPaneDimension(pane, 30);
                updatePaneColor(pane, person.getRole() == PersonRole.FACULTY? Color.PINK:Color.CADETBLUE );
                HBox currentFloor = floors[person.getCurrentFloor()];
                Platform.runLater(() -> currentFloor.getChildren().add(pane));

                spawnCooldown.set(spawnDelay);
                System.out.println("Spawned Person " + person.getId() + " [" +person.getRole() + "] "
                        + " at floor:" + person.getCurrentFloor() + " with destination:" + person.getDestination());
            }

        };

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(tick, 0, 200, TimeUnit.MILLISECONDS);
    }

    private void updateElevatorPosition(Elevator[] elevators, Pane[][] elevatorPanes, int elevatorIndex, Color color) {
        int elevatorFloor = elevators[elevatorIndex].getCurrentFloor();
        Pane pane = elevatorPanes[elevatorFloor][elevatorIndex];

        updatePaneColor(pane, color);
    }

    private void updatePaneColor (Pane pane, Color color) {
        BackgroundFill elevFill = new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY);
        pane.setBackground(new Background(elevFill));
    }

    private void setPaneDimension (Pane pane, int dimension) {
        pane.setMinHeight(dimension);
        pane.setPrefHeight(dimension);
        pane.setMaxHeight(dimension);
        pane.setMinWidth(dimension);
        pane.setPrefWidth(dimension);
        pane.setMaxWidth(dimension);
    }

}