module com.gjbmloslos.elevatorsim {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens com.gjbmloslos.elevatorsim to javafx.fxml;
    exports com.gjbmloslos.elevatorsim;
}