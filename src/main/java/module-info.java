module com.example.easvtickets {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires javafx.swing;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;
    requires java.naming;
    requires bcrypt;
    requires com.google.zxing.javase;
    requires com.google.zxing;

    opens com.example.easvtickets to javafx.fxml;
    exports com.example.easvtickets;
    opens com.example.easvtickets.BE to javafx.base;
    //exports com.example.easvtickets.GUI;
    //opens com.example.easvtickets.GUI to javafx.fxml;
    exports com.example.easvtickets.GUI.Controller;
    opens com.example.easvtickets.GUI.Controller to javafx.fxml;

}