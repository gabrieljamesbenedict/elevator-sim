package com.gjbmloslos.elevatorsim;

import com.gjbmloslos.elevatorsim.entities.Elevator;
import com.gjbmloslos.elevatorsim.entities.Person;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        Person person = new Person(1,"Student",1,5);
        Elevator elevator = new Elevator(1,1,true);
        int maxFloor = 10;
        int maxCapacity = 10;
        double speed = 20;
        Elevator.setSpeed(speed);
        Elevator.setMaxFloor(maxFloor);
        Elevator.setCapacity(maxCapacity);
    }

    public void callElevator(Elevator elevator,Person person){
        if(elevator.getFloor()==person.getCurrFloor()){
            elevator.pickup(person);
            if(person.getCurrFloor()>person.getDestination()){
                elevator.setGoingUp(false);
            }else{
                elevator.setGoingUp(true);
            }
            elevator.move();
        }
        else if(elevator.getFloor()>person.getCurrFloor()){
            elevator.setGoingUp(false);
        }else{
            elevator.setGoingUp(true);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}