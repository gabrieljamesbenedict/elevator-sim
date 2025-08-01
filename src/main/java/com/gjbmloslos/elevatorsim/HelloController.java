package com.gjbmloslos.elevatorsim;

import com.gjbmloslos.elevatorsim.constants.Direction;
import com.gjbmloslos.elevatorsim.constants.ElevatorStats;
import com.gjbmloslos.elevatorsim.constants.PersonRole;
import com.gjbmloslos.elevatorsim.constants.SimulationConfig;
import com.gjbmloslos.elevatorsim.entities.Elevator;
import com.gjbmloslos.elevatorsim.entities.Floor;
import com.gjbmloslos.elevatorsim.entities.Person;
import com.gjbmloslos.elevatorsim.manager.PersonSpawnManager;
import com.gjbmloslos.elevatorsim.manager.StatisticsManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class HelloController {

    PersonSpawnManager spawnManager;
    StatisticsManager statisticsManager;

    Floor[] floors;
    Elevator[] elevators;
    Pane[][] elevatorPanes;

    @FXML
    VBox building;

    @FXML
    Label tickCounter;

    @FXML
    Label meanWaitingTime;

    @FXML
    Label maxWaitingTime;

    @FXML
    Label totalSpawned;

    @FXML
    Label totalServed;

    @FXML
    Label currentAmount;

    @FXML
    public void initialize () {

        generateFloors();
        generateElevators();

        spawnManager = new PersonSpawnManager();
        statisticsManager = new StatisticsManager();

        startSimulation();

        System.out.println("HELLO WORLD");
    }

    private void generateFloors () {

        int floorsAmount = SimulationConfig.BUILDING_MAX_FLOOR;
        int elevatorAmount = SimulationConfig.BUILDING_FACULTY_ELEVATOR_AMOUNT + SimulationConfig.BUILDING_STUDENT_ELEVATOR_AMOUNT;
        int elevatorSize = 75;
        int boxWidth = elevatorAmount * elevatorSize;

        elevatorPanes = new Pane[SimulationConfig.BUILDING_MAX_FLOOR][elevatorAmount];
        floors = new Floor[floorsAmount];

        for (int i = 0; i < floorsAmount; i++) {
            HBox floorHBox = new HBox();
            int height = 75;
            floorHBox.setMinHeight(height);
            floorHBox.setPrefHeight(height);
            floorHBox.setMaxHeight(height);
            floorHBox.prefWidth(600);
            BackgroundFill fill = new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY);
            floorHBox.setBackground(new Background(fill));
            floorHBox.setAlignment(Pos.CENTER_LEFT);
            floorHBox.setSpacing(10);

            HBox innerBox = new HBox();
            innerBox.setMinSize(boxWidth, height);
            innerBox.setPrefSize(boxWidth, height);
            innerBox.setMaxSize(boxWidth, height);
            innerBox.setSpacing(5);
            for (int j = 0; j < elevatorAmount; j++) {
                Pane pane = new Pane();
                setPaneDimension(pane, elevatorSize);
                BackgroundFill elevFill = new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY);
                pane.setBackground(new Background(elevFill));
                elevatorPanes[floorsAmount-i-1][j] = pane;
                innerBox.getChildren().add(pane);
            }
            floorHBox.getChildren().add(innerBox);

            // Add the floor to the VBox and the floorList
            building.getChildren().add(floorHBox);
            floors[floorsAmount-i-1] = new Floor(i, floorHBox);
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

        System.out.println("Starting simulation");

        AtomicInteger tickNum = new AtomicInteger(0);
        AtomicInteger spawnCooldown = new AtomicInteger(0);

        List<Elevator> idleElevators = new LinkedList<>();
        List<Elevator> busyElevators = new LinkedList<>();

        for (Elevator e : elevators) {
            idleElevators.add(e);
        }

        final int spawnDelay = SimulationConfig.PERSON_SPAWN_DELAY;
        int facultyElevators = SimulationConfig.BUILDING_FACULTY_ELEVATOR_AMOUNT;
        int studentElevators = SimulationConfig.BUILDING_STUDENT_ELEVATOR_AMOUNT;
        int elevatorAmount = facultyElevators + studentElevators;
        int maxFloor = SimulationConfig.BUILDING_MAX_FLOOR;
        int speed = ElevatorStats.ELEVATOR_SPEED;
        int totalCapacity = ElevatorStats.ELEVATOR_MAX_CAPACITY;

        Runnable tick = () -> {
            try {
                int currentTick = tickNum.incrementAndGet();
                Platform.runLater(()-> {
                    tickCounter.setText(String.format("Ticks: %5d", currentTick));
                    meanWaitingTime.setText(String.format("Mean Waiting Time: %5.2f", statisticsManager.getAveWaitingTime()));
                    maxWaitingTime.setText(String.format("Max Waiting Time: %5.2f", statisticsManager.getMaxWaitingTime()));
                    totalSpawned.setText(String.format("Total Spawned: %5d", statisticsManager.getPersonSpawnedAmount()));
                    totalServed.setText(String.format("Total Served: %5d", statisticsManager.getPersonServedAmount()));
                    currentAmount.setText(String.format("Current Amount: %5d", statisticsManager.getPersonCurrentAmount()));
                });


                // Main Elevator Logic
                for (Elevator e : elevators) {
                    Floor currentFloor = floors[e.getCurrentFloor()];
                    Queue<Person> queue = currentFloor.getPersonQueue();
                    int currentLoad;
                    int maxLoad = e.getCapacity();
                    int dest = e.getCurrentFloor();

                    // Drop off Logic
                    List<Person> droppingOff = e.getPersonList()
                            .stream()
                            .filter(p -> p.getDestination() == e.getCurrentFloor())
                            .toList();
                    if (!droppingOff.isEmpty()) {
                        droppingOff.forEach(p -> {
                            e.dropOff(p);
                            statisticsManager.trackServedPerson();
                            System.out.println("Elevator " + e.getId() + " dropped off Person " + p.getId() + ", Capacity:" + e.getPersonList().size() +"/"+e.getCapacity());
                        });
                    }
                    currentLoad = e.getPersonList().size();

                    // Idling Logic (Generic for all roles)
                    if (e.getBusy() && e.getPersonList().isEmpty()) {
                        boolean hasWaitingSameRole = Arrays.stream(floors)
                                .anyMatch(f -> f.getPersonQueue().stream()
                                        .anyMatch(p -> p.getRole() == e.getRole()));

                        if (!hasWaitingSameRole) {
                            e.setBusy(false);
                            busyElevators.remove(e);
                            idleElevators.add(e);
                            System.out.println("Elevator " + e.getId() + " is now idle.");
                        }
                    }


                    if (e.getRole() == PersonRole.FACULTY) {
                        // FACULTY ELEVATORS


                        // Pickup Logic
                        int elevatorFloor = e.getCurrentFloor();
                        if (!queue.isEmpty() && currentLoad < maxLoad) {
                            if (queue.peek().getRole() == PersonRole.FACULTY) {
                                Person peeked = queue.peek();

                                boolean goingSameDirection = e.getDirection() == peeked.getDirection();
                                boolean atElevatorTerminal = peeked.getCurrentFloor() == maxFloor-1 || peeked.getCurrentFloor() == 0;
                                boolean changeDirection = e.getDirection()==Direction.UP? !hasWaitingAbove(e.getCurrentFloor(), floors)&&peeked.getDirection()==Direction.DOWN : !hasWaitingBelow(e.getCurrentFloor(), floors)&&peeked.getDirection()==Direction.UP;
                                boolean hasPersonGoingThere = e.getPersonList().stream().anyMatch(p -> p.getDirection() == e.getDirection());
                                if (goingSameDirection || atElevatorTerminal || (changeDirection && !hasPersonGoingThere)) {
                                    Person person = queue.poll();
                                    e.pickUp(person);
                                    currentLoad++;
                                    statisticsManager.removeStatisticsComponent(person);
                                    Platform.runLater(() -> currentFloor.getHbox().getChildren().remove(person.getPane()));
                                    System.out.println("Elevator " + e.getId() + " picked up Person " + person.getId() + ", Capacity:" + currentLoad + "/" + e.getCapacity());
                                    if (changeDirection) e.setDirection(e.getDirection() == Direction.UP? Direction.DOWN : Direction.UP);
                                }

                            } else if (queue.stream().anyMatch(p ->
                                    p.getRole() == PersonRole.FACULTY
                            )) {

                                Iterator<Person> it = queue.iterator();
                                while (it.hasNext()) {
                                    Person person = it.next();
                                    boolean isCorrectRole = person.getRole() == PersonRole.FACULTY;
                                    boolean goingSameDirection = e.getDirection() == person.getDirection();
                                    boolean atElevatorTerminal = person.getCurrentFloor() == maxFloor-1 || person.getCurrentFloor() == 0;
                                    boolean changeDirection = e.getDirection()==Direction.UP? !hasWaitingAbove(e.getCurrentFloor(), floors)&&person.getDirection()==Direction.DOWN : !hasWaitingBelow(e.getCurrentFloor(), floors)&&person.getDirection()==Direction.UP;
                                    boolean hasPersonGoingThere = e.getPersonList().stream().anyMatch(p -> p.getDirection() == e.getDirection());
                                    if (isCorrectRole && (goingSameDirection || atElevatorTerminal || (changeDirection && !hasPersonGoingThere))) {
                                        it.remove();
                                        e.pickUp(person);
                                        currentLoad++;
                                        statisticsManager.removeStatisticsComponent(person);
                                        Platform.runLater(() -> currentFloor.getHbox().getChildren().remove(person.getPane()));
                                        System.out.println("Elevator " + e.getId() + " picked up Person " + person.getId() + ", Capacity:" + currentLoad + "/" + e.getCapacity());
                                        if (changeDirection) e.setDirection(e.getDirection() == Direction.UP? Direction.DOWN : Direction.UP);
                                    }
                                }

                            }
                        }


                        // Move Logic
                        if (currentTick % speed == 0 && (!e.getPersonList().isEmpty() || e.getBusy())) {
                            updateElevatorPosition(elevators, elevatorPanes, e.getId(), Color.GRAY);
                            e.step(maxFloor);
                            Color c = e.getRole() == PersonRole.FACULTY ? Color.LIGHTSALMON : Color.LIGHTBLUE;
                            updateElevatorPosition(elevators, elevatorPanes, e.getId(), c);
                            //System.out.println("Elevator " + e.getId() + " moved to floor: " + e.getCurrentFloor() + ", direction: " + e.getDirection());
                        }


                    } else {
                        // STUDENT ELEVATORS


                        // Pickup Logic
                        int elevatorFloor = e.getCurrentFloor();
                        if (!queue.isEmpty() && currentLoad < maxLoad) {
                            if (queue.peek().getRole() == PersonRole.STUDENT) {
                                Person peeked = queue.peek();

                                boolean goingSameDirection = e.getDirection() == peeked.getDirection();
                                boolean atElevatorTerminal = peeked.getCurrentFloor() == maxFloor-1 || peeked.getCurrentFloor() == 0;
                                boolean changeDirection = e.getDirection()==Direction.UP? !hasWaitingAbove(e.getCurrentFloor(), floors)&&peeked.getDirection()==Direction.DOWN : !hasWaitingBelow(e.getCurrentFloor(), floors)&&peeked.getDirection()==Direction.UP;
                                boolean hasPersonGoingThere = e.getPersonList().stream().anyMatch(p -> p.getDirection() == e.getDirection());
                                if (goingSameDirection || atElevatorTerminal || (changeDirection && !hasPersonGoingThere)) {
                                    Person person = queue.poll();
                                    e.pickUp(person);
                                    currentLoad++;
                                    statisticsManager.removeStatisticsComponent(person);
                                    Platform.runLater(() -> currentFloor.getHbox().getChildren().remove(person.getPane()));
                                    System.out.println("Elevator " + e.getId() + " picked up Person " + person.getId() + ", Capacity:" + currentLoad + "/" + e.getCapacity());
                                    if (changeDirection) e.setDirection(e.getDirection() == Direction.UP? Direction.DOWN : Direction.UP);
                                }

                            } else if (queue.stream().anyMatch(p ->
                                    p.getRole() == PersonRole.STUDENT
                            )) {

                                Iterator<Person> it = queue.iterator();
                                while (it.hasNext()) {
                                    Person person = it.next();
                                    boolean isCorrectRole = person.getRole() == PersonRole.STUDENT;
                                    boolean goingSameDirection = e.getDirection() == person.getDirection();
                                    boolean atElevatorTerminal = person.getCurrentFloor() == maxFloor-1 || person.getCurrentFloor() == 0;
                                    boolean changeDirection = e.getDirection()==Direction.UP? !hasWaitingAbove(e.getCurrentFloor(), floors)&&person.getDirection()==Direction.DOWN : !hasWaitingBelow(e.getCurrentFloor(), floors)&&person.getDirection()==Direction.UP;
                                    boolean hasPersonGoingThere = e.getPersonList().stream().anyMatch(p -> p.getDirection() == e.getDirection());
                                    if (isCorrectRole && (goingSameDirection || atElevatorTerminal || (changeDirection && !hasPersonGoingThere))) {
                                        it.remove();
                                        e.pickUp(person);
                                        currentLoad++;
                                        statisticsManager.removeStatisticsComponent(person);
                                        Platform.runLater(() -> currentFloor.getHbox().getChildren().remove(person.getPane()));
                                        System.out.println("Elevator " + e.getId() + " picked up Person " + person.getId() + ", Capacity:" + currentLoad + "/" + e.getCapacity());
                                        if (changeDirection) e.setDirection(e.getDirection() == Direction.UP? Direction.DOWN : Direction.UP);
                                    }
                                }

                            }
                        }


                        // Move Logic
                        if (currentTick % speed == 0 && (!e.getPersonList().isEmpty() || e.getBusy())) {
                            updateElevatorPosition(elevators, elevatorPanes, e.getId(), Color.GRAY);
                            e.step(maxFloor);
                            Color c = e.getRole() == PersonRole.FACULTY ? Color.LIGHTSALMON : Color.LIGHTBLUE;
                            updateElevatorPosition(elevators, elevatorPanes, e.getId(), c);
                            //System.out.println("Elevator " + e.getId() + " moved to floor: " + e.getCurrentFloor() + ", direction: " + e.getDirection());
                        }

                    }


                }


                // Main Customer Spawn Logic
                int cooldown = spawnCooldown.decrementAndGet();
                if (cooldown <= 0) {
                    Person person = spawnManager.spawn();
                    Pane pane = new Pane();
                    setPaneDimension(pane, 30);
                    updatePaneColor(pane, person.getRole() == PersonRole.FACULTY? Color.PINK:Color.CADETBLUE );
                    person.setPane(pane);
                    Floor currentFloor = floors[person.getCurrentFloor()];
                    currentFloor.getPersonQueue().add(person);
                    Platform.runLater(() -> currentFloor.getHbox().getChildren().add(pane));

                    spawnCooldown.set(spawnDelay);
                    statisticsManager.addStatisticsComponent(person);
                    statisticsManager.trackSpawnedPerson();
                    System.out.println("Spawned Person " + person.getId() + " [" +person.getRole() + "] "
                            + " at floor:" + person.getCurrentFloor() + " with destination:" + person.getDestination());

                    List<Elevator> idleStudentElevators = idleElevators.stream()
                            .filter(e -> e.getRole() == person.getRole())
                            .toList();
                    if (!idleStudentElevators.isEmpty()) {
                        Elevator assigned = idleStudentElevators.getFirst();
                        idleElevators.remove(assigned);
                        busyElevators.add(assigned);
                        assigned.setBusy(true);
                        System.out.println("Elevator " + assigned.getId() + " assigned to Person " + person.getId());
                    }
                }

                statisticsManager.getAverageWaitingTime();

            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(tick, 3000, 50, TimeUnit.MILLISECONDS);
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

    private boolean hasWaitingAbove(int currentFloor, Floor[] floors) {
        for (int i = currentFloor + 1; i < floors.length; i++) {
            Queue<Person> queue = floors[i].getPersonQueue();
            if (queue.stream().anyMatch(p -> p.getRole() == PersonRole.STUDENT)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasWaitingBelow(int currentFloor, Floor[] floors) {
        for (int i = currentFloor - 1; i >= 0; i--) {
            Queue<Person> queue = floors[i].getPersonQueue();
            if (queue.stream().anyMatch(p -> p.getRole() == PersonRole.STUDENT)) {
                return true;
            }
        }
        return false;
    }


}