package com.example.javafxwarnetbasisdata.controller;

import com.example.javafxwarnetbasisdata.listener.UserModelListener;
import com.example.javafxwarnetbasisdata.model.UserModel;
import com.example.javafxwarnetbasisdata.repository.Repository;
import com.example.javafxwarnetbasisdata.util.CustomException;
import com.example.javafxwarnetbasisdata.util.TemporaryMemory;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class HomeUserController {
    public void init(){
        Repository.getUserByUserId(
                TemporaryMemory.savedUserId,
                new UserModelListener() {
                    @Override
                    public void onSuccess(UserModel o) {
                        home_user_username.setText(String.format("Selamat Datang, %s",o.username()));
                    }

                    @Override
                    public void onFailed(CustomException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, e.customMessage);
                        alert.show();
                    }
                }
        );


    }

    public Label home_user_username;
    public VBox home_user_komputer_list;
}
