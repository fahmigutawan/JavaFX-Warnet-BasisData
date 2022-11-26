package com.example.javafxwarnetbasisdata.controller;

import com.example.javafxwarnetbasisdata.repository.Repository;
import com.example.javafxwarnetbasisdata.util.CustomException;
import com.example.javafxwarnetbasisdata.util.CustomScene;
import com.example.javafxwarnetbasisdata.listener.ResponseListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginAdminController {
    @FXML
    public TextField admin_login_username;
    @FXML
    public PasswordField admin_login_password;
    @FXML
    public Button admin_login_back_btn;

    @FXML
    public Button login_admin_btn;

    @FXML
    public void onBackClicked() throws IOException {
        Stage stage = (Stage) admin_login_back_btn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/javafxwarnetbasisdata/onboard-view.fxml"));
        Scene scene = new CustomScene(root);
        stage.setScene(scene);
    }

    @FXML
    public void onLoginClicked() throws IOException{
        Repository.loginAdmin(
                admin_login_username.getText(),
                admin_login_password.getText(),
                new ResponseListener() {
                    @Override
                    public void onSuccess(Object o) throws IOException {
                        onLoginAdminClicked();
                    }

                    @Override
                    public void onFailed(CustomException e) {
                        Alert alert = new Alert(Alert.AlertType.WARNING, e.customMessage);
                        alert.show();
                    }
                }
        );
    }
    public void onLoginAdminClicked() throws IOException {
        Stage stage = (Stage) login_admin_btn.getScene().getWindow();
        Parent loginAdminRoot = FXMLLoader.load(getClass().getResource("/com/example/javafxwarnetbasisdata/home-admin-view.fxml"));
        Scene scene = new CustomScene(loginAdminRoot);
        stage.setScene(scene);
        stage.show();
    }
}
