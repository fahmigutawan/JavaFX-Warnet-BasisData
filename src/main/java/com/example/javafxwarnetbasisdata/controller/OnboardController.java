package com.example.javafxwarnetbasisdata.controller;

import com.example.javafxwarnetbasisdata.util.CustomScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class OnboardController{
    @FXML
    public Button onboard_loginuser_btn;
    @FXML
    public Button onboard_loginadmin_btn;
    @FXML
    public Button onboard_exit_btn;

    @FXML
    public void onboardLoginAdminClicked() throws IOException {
        Stage stage = (Stage) onboard_loginadmin_btn.getScene().getWindow();
        Parent loginAdminRoot = FXMLLoader.load(getClass().getResource("/com/example/javafxwarnetbasisdata/login-admin-view.fxml"));
        Scene scene = new CustomScene(loginAdminRoot);
        stage.setScene(scene);
        stage.show();
    }
}
