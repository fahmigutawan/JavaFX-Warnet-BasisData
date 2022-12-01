package com.example.javafxwarnetbasisdata.controller;

import com.example.javafxwarnetbasisdata.listener.ListOfCustomerListener;
import com.example.javafxwarnetbasisdata.listener.ListOfKomputerListener;
import com.example.javafxwarnetbasisdata.listener.UserModelListener;
import com.example.javafxwarnetbasisdata.model.CustomerModel;
import com.example.javafxwarnetbasisdata.model.KomputerModel;
import com.example.javafxwarnetbasisdata.model.UserModel;
import com.example.javafxwarnetbasisdata.repository.Repository;
import com.example.javafxwarnetbasisdata.util.CustomException;
import com.example.javafxwarnetbasisdata.util.TemporaryMemory;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.ArrayList;

public class HomeUserController {
    public Label home_user_username;
    public HBox home_user_komputer_list;

    public void init() {
        Repository.getUserByUserId(
                TemporaryMemory.savedCustomerId,
                new UserModelListener() {
                    @Override
                    public void onSuccess(UserModel o) {
                        home_user_username.setText(String.format("Selamat Datang, %s", o.name()));
                    }

                    @Override
                    public void onFailed(CustomException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, e.customMessage);
                        alert.show();
                    }
                }
        );

        Repository.getListOfKomputer(new ListOfKomputerListener() {
            @Override
            public void onSuccess(ArrayList<KomputerModel> listOfKomputer) {

            }

            @Override
            public void onFailed(CustomException e) {

            }
        });
    }
}
