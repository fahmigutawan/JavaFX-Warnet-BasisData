package com.example.javafxwarnetbasisdata.controller;

import com.example.javafxwarnetbasisdata.listener.AvailableComputerListener;
import com.example.javafxwarnetbasisdata.listener.UserModelListener;
import com.example.javafxwarnetbasisdata.model.ComputerModel;
import com.example.javafxwarnetbasisdata.model.UserModel;
import com.example.javafxwarnetbasisdata.repository.Repository;
import com.example.javafxwarnetbasisdata.util.CustomException;
import com.example.javafxwarnetbasisdata.util.CustomScene;
import com.example.javafxwarnetbasisdata.util.TemporaryMemory;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

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
                        for (ComputerModel item : availableComputers) {
                            // We use rootFrame because maybe later we'll add image on the left side
                            HBox rootFrame = new HBox();
                            VBox textFrame = new VBox();
                            rootFrame.getChildren().add(textFrame);

                            // Set onClick event
                            rootFrame.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                    TemporaryMemory.pickedComputerId = item.komputer_id();
                                    try{
                                        Stage stage = (Stage) rootFrame.getScene().getWindow();
                                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafxwarnetbasisdata/detail-computer-view.fxml"));
                                        Parent root = loader.load();
                                        DetailComputerController controller = loader.getController();
                                        controller.init();
                                        Scene scene = new CustomScene(root);
                                        stage.setScene(scene);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                        Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
                                        alert.show();
                                    }
                                }
                            });

                            // set Border
                            textFrame.setBorder(new Border(
                                    new BorderStroke(
                                            Color.BLACK,
                                            BorderStrokeStyle.SOLID,
                                            CornerRadii.EMPTY,
                                            BorderWidths.DEFAULT
                                    )
                            ));

                            // set Padding
                            rootFrame.setPadding(new Insets(10f));
                            textFrame.setPadding(new Insets(10f));

                            // text Tersedia/Tidak tersedia
                            Label availableStatus = new Label();
                            availableStatus.setMinSize(80, 0);
                            availableStatus.setText("Tersedia");
                            availableStatus.setTextFill(Color.BLACK);

                            // Nama PC
                            Label idPcLabel = new Label();
                            idPcLabel.setMinSize(80, 0);
                            idPcLabel.setText(item.komputer_id());
                            idPcLabel.setTextFill(Color.BLACK);

                            // Kategori
                            Label kategoriLabel = new Label();
                            kategoriLabel.setMinSize(80, 0);
                            kategoriLabel.setText(item.spek_kategori());
                            kategoriLabel.setTextFill(Color.BLACK);

                            // Harga
                            Label hargaLabel = new Label();
                            hargaLabel.setMinSize(80, 0);
                            hargaLabel.setText(String.format("Rp %d/JAM", item.harga_per_jam()));
                            hargaLabel.setTextFill(Color.BLUE);

                            // Add content to textFrame
                            textFrame.getChildren().add(availableStatus);
                            textFrame.getChildren().add(idPcLabel);
                            textFrame.getChildren().add(kategoriLabel);
                            textFrame.getChildren().add(hargaLabel);

                            // Least, add rootFrame to HBox
                            home_user_komputer_list.getChildren().add(rootFrame);
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
