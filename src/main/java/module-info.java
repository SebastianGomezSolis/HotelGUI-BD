module com.sistema.hotelguibd {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.zaxxer.hikari;
    requires java.logging;
    requires javafx.graphics;


    opens com.sistema.hotelguibd to javafx.fxml;

    exports com.sistema.hotelguibd.modelo;
    opens com.sistema.hotelguibd.modelo to javafx.fxml;

    exports com.sistema.hotelguibd.controller;
    opens com.sistema.hotelguibd.controller to javafx.fxml;

    exports com.sistema.hotelguibd.datos;
    opens com.sistema.hotelguibd.datos to javafx.fxml;

    exports com.sistema.hotelguibd;

}