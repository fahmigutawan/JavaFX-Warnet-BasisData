package com.example.javafxwarnetbasisdata.controller;

import com.example.javafxwarnetbasisdata.repository.Repository;
import com.example.javafxwarnetbasisdata.util.CustomException;
import com.example.javafxwarnetbasisdata.util.CustomScene;
import com.example.javafxwarnetbasisdata.listener.ResponseListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterUserController {
    public TextField user_register_username;
    public PasswordField user_register_password;
    public Button user_register_back_btn;
    public Button user_regsiter_register_btn;
    public TextField user_register_name;

    public void onBackClicked() throws IOException {
        Stage stage = (Stage) user_register_back_btn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/javafxwarnetbasisdata/login-user-view.fxml"));
        Scene scene = new CustomScene(root);
        stage.setScene(scene);
    }

    public void onRegisterClicked() {
        Repository.registerUser(
                user_register_username.getText(),
                user_register_name.getText(),
                user_register_password.getText(),
                new ResponseListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Akun berhasil dibuat. Silahkan masuk");
                        alert.show();
                    }

                    @Override
                    public void onFailed(CustomException e) {
                        Alert alert = new Alert(Alert.AlertType.WARNING, e.customMessage);
                        alert.show();
                    }
                }
        );
    }
}
