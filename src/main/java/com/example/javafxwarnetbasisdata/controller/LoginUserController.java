package com.example.javafxwarnetbasisdata.controller;

import com.example.javafxwarnetbasisdata.util.CustomScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginUserController {
    public TextField user_login_username;
    public PasswordField user_login_password;
    public Button user_login_back_btn;

    public void onBackClicked() throws IOException {
        Stage stage = (Stage) user_login_back_btn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/javafxwarnetbasisdata/onboard-view.fxml"));
        Scene scene = new CustomScene(root);
        stage.setScene(scene);
    }
}
