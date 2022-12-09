module com.example.javafxwarnetbasisdata {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires jasperreports;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    opens com.example.javafxwarnetbasisdata to javafx.fxml;
    exports com.example.javafxwarnetbasisdata;
    exports com.example.javafxwarnetbasisdata.controller;
    opens com.example.javafxwarnetbasisdata.controller to javafx.fxml;
}