package com.gjbmloslos.elevatorsim;

import com.gjbmloslos.elevatorsim.constants.SimulationConfig;
import com.gjbmloslos.elevatorsim.entities.Person;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class HelloController {

    ArrayList<ArrayList<Person>> floorList;
    Pane[][] elevators;

    @FXML
    VBox building;

    @FXML
    public void initialize () {

        generateFloors();
        generateElevators();

        System.out.println("HELLO WORLD");
    }

    private void generateFloors () {

        int floorsAmount = SimulationConfig.BUILDING_MAX_FLOOR;
        int elevatorAmount = SimulationConfig.BUILDING_FACULTY_ELEVATOR_AMOUNT + SimulationConfig.BUILDING_STUDENT_ELEVATOR_AMOUNT;
        int elevatorSize = 75;
        int boxWidth = elevatorAmount * elevatorSize;

        floorList = new ArrayList<>();
        elevators = new Pane[SimulationConfig.BUILDING_MAX_FLOOR][elevatorAmount];

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
                elevators[floorsAmount-i-1][j] = pane;
                innerBox.getChildren().add(pane);
            }
            floor.getChildren().add(innerBox);

            // Add the floor to the VBox and the floorList
            building.getChildren().add(floor);
            floorList.add(new ArrayList<Person>());
        }

    }

    private void generateElevators () {

        int elevatorAmount = SimulationConfig.BUILDING_FACULTY_ELEVATOR_AMOUNT + SimulationConfig.BUILDING_STUDENT_ELEVATOR_AMOUNT;
        int elevatorSize = 75;
        int facultyElevators = SimulationConfig.BUILDING_FACULTY_ELEVATOR_AMOUNT;
        int studentElevators = SimulationConfig.BUILDING_STUDENT_ELEVATOR_AMOUNT;

        for (int i = 0; i < facultyElevators; i++) {
            Pane pane = elevators[0][i];
            pane.setMinHeight(elevatorSize);
            pane.setPrefHeight(elevatorSize);
            pane.setMaxHeight(elevatorSize);
            pane.setMinWidth(elevatorSize);
            pane.setPrefWidth(elevatorSize);
            pane.setMaxWidth(elevatorSize);
            BackgroundFill elevFill = new BackgroundFill(Color.LIGHTSALMON, CornerRadii.EMPTY, Insets.EMPTY);
            pane.setBackground(new Background(elevFill));
            elevators[0][i] = pane;
        }

        for (int i = facultyElevators; i < elevatorAmount; i++) {
            Pane pane = elevators[0][i];
            pane.setMinHeight(elevatorSize);
            pane.setPrefHeight(elevatorSize);
            pane.setMaxHeight(elevatorSize);
            pane.setMinWidth(elevatorSize);
            pane.setPrefWidth(elevatorSize);
            pane.setMaxWidth(elevatorSize);
            BackgroundFill elevFill = new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY);
            pane.setBackground(new Background(elevFill));
            elevators[0][i] = pane;
        }

    }

}