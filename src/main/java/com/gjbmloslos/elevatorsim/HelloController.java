package com.gjbmloslos.elevatorsim;

import com.gjbmloslos.elevatorsim.constants.ElevatorStats;
import com.gjbmloslos.elevatorsim.constants.SimulationConfig;
import com.gjbmloslos.elevatorsim.entities.Elevator;
import com.gjbmloslos.elevatorsim.entities.Person;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class HelloController {

    AtomicInteger tickNum;
    ArrayList<ArrayList<Person>> floorList;
    Pane[][] elevatorPanes;
    Elevator[] elevators;

    @FXML
    VBox building;

    @FXML
    Label tickCounter;

    @FXML
    public void initialize () {

        tickNum = new AtomicInteger(0);

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

        floorList = new ArrayList<>();
        elevatorPanes = new Pane[SimulationConfig.BUILDING_MAX_FLOOR][elevatorAmount];

        for (int i = 0; i < floorsAmount; i++) {
            HBox floor = new HBox();
            int height = 75;
            floor.setMinHeight(height);
            floor.setPrefHeight(height);
            floor.setMaxHeight(height);
            floor.prefWidth(600);
            BackgroundFill fill = new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY);
            floor.setBackground(new Background(fill));

            HBox innerBox = new HBox();
            innerBox.setMinSize(boxWidth, height);
            innerBox.setPrefSize(boxWidth, height);
            innerBox.setMaxSize(boxWidth, height);
            //BackgroundFill innerFill = new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY);
            //innerBox.setBackground(new Background(innerFill));
            innerBox.setSpacing(5);
            for (int j = 0; j < elevatorAmount; j++) {
                Pane pane = new Pane();
                pane.setMinHeight(elevatorSize);
                pane.setPrefHeight(elevatorSize);
                pane.setMaxHeight(elevatorSize);
                pane.setMinWidth(elevatorSize);
                pane.setPrefWidth(elevatorSize);
                pane.setMaxWidth(elevatorSize);
                BackgroundFill elevFill = new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY);
                pane.setBackground(new Background(elevFill));
                elevatorPanes[floorsAmount-i-1][j] = pane;
                innerBox.getChildren().add(pane);
            }
            floor.getChildren().add(innerBox);

            // Add the floor to the VBox and the floorList
            building.getChildren().add(floor);
            floorList.add(new ArrayList<Person>());
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
            pane.setMinHeight(elevatorSize);
            pane.setPrefHeight(elevatorSize);
            pane.setMaxHeight(elevatorSize);
            pane.setMinWidth(elevatorSize);
            pane.setPrefWidth(elevatorSize);
            pane.setMaxWidth(elevatorSize);
            BackgroundFill elevFill = new BackgroundFill(Color.LIGHTSALMON, CornerRadii.EMPTY, Insets.EMPTY);
            pane.setBackground(new Background(elevFill));
            elevatorPanes[0][i] = pane;
            elevators[i] = new Elevator(elevatorCapacity, startingFloor);
        }

        for (int i = facultyElevators; i < elevatorAmount; i++) {
            Pane pane = elevatorPanes[0][i];
            pane.setMinHeight(elevatorSize);
            pane.setPrefHeight(elevatorSize);
            pane.setMaxHeight(elevatorSize);
            pane.setMinWidth(elevatorSize);
            pane.setPrefWidth(elevatorSize);
            pane.setMaxWidth(elevatorSize);
            BackgroundFill elevFill = new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY);
            pane.setBackground(new Background(elevFill));
            elevatorPanes[0][i] = pane;
            elevators[i] = new Elevator(elevatorCapacity, startingFloor);
        }

    }

    private void startSimulation () {

        int facultyElevators = SimulationConfig.BUILDING_FACULTY_ELEVATOR_AMOUNT;
        int studentElevators = SimulationConfig.BUILDING_STUDENT_ELEVATOR_AMOUNT;
        int elevatorAmount = facultyElevators + studentElevators;
        int maxFloor = SimulationConfig.BUILDING_MAX_FLOOR;
        int speed = ElevatorStats.ELEVATOR_SPEED;

        Runnable tick = () -> {
            int currentTick = tickNum.incrementAndGet();
            Platform.runLater(()-> tickCounter.setText("Ticks: " + currentTick));

            for (int i = 0; i < elevatorAmount; i++) {
                if (currentTick % speed == 0) {
                    updateElevatorPosition(elevators, elevatorPanes, i, Color.GRAY);
                    elevators[i].step(maxFloor);
                    System.out.println("Elevator at floor " + elevators[i].getCurrentFloor() +
                            ", direction: " + elevators[i].getDirection());
                }
            }

            for (int i = 0; i < elevatorAmount; i++) {
                updateElevatorPosition(elevators, elevatorPanes, i, i < facultyElevators? Color.LIGHTSALMON:Color.LIGHTBLUE);
            }
        };

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(tick, 0, 100, TimeUnit.MILLISECONDS);
    }

    private void updateElevatorPosition(Elevator[] elevators, Pane[][] elevatorPanes, int elevatorIndex, Color color) {
        int elevatorFloor = elevators[elevatorIndex].getCurrentFloor();
        Pane pane = elevatorPanes[elevatorFloor][elevatorIndex];

        BackgroundFill elevFill = new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY);
        pane.setBackground(new Background(elevFill));
    }


}