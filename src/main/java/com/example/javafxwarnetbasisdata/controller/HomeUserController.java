package com.example.javafxwarnetbasisdata.controller;

import com.example.javafxwarnetbasisdata.listener.AvailableComputerListener;
import com.example.javafxwarnetbasisdata.listener.UserModelListener;
import com.example.javafxwarnetbasisdata.model.ComputerModel;
import com.example.javafxwarnetbasisdata.model.UserModel;
import com.example.javafxwarnetbasisdata.repository.Repository;
import com.example.javafxwarnetbasisdata.util.CustomException;
import com.example.javafxwarnetbasisdata.util.TemporaryMemory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class HomeUserController {
    public Label home_user_username;
    public HBox home_user_komputer_list;

    public void init() {
        Repository.getUserByUserId(
                TemporaryMemory.savedUserId,
                new UserModelListener() {
                    @Override
                    public void onSuccess(UserModel o) {
                        home_user_username.setText(String.format("Selamat Datang, %s", o.username()));
                    }

                    @Override
                    public void onFailed(CustomException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, e.customMessage);
                        alert.show();
                    }
                }
        );

        Repository.getAvailableComputers(
                new AvailableComputerListener() {
                    @Override
                    public void onSuccess(
                            ArrayList<ComputerModel> availableComputers,
                            ArrayList<ComputerModel> unavailableComputers
                    ) {
                        for (ComputerModel item : unavailableComputers) {

                        }
                    }

                    @Override
                    public void onFailed(CustomException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, e.customMessage);
                        alert.show();
                    }
                }
        );
    }
}
