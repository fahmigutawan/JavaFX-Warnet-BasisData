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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
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
                            // Image
                            ImageView img = new ImageView();
                            img.setImage(new Image(getClass().getResourceAsStream("/com/example/javafxwarnetbasisdata/image/ic_computer_available.png")));
                            img.setFitWidth(30);
                            img.setFitHeight(30);

                            // Btn
                            Button btn = new Button();

                            // We use rootFrame because maybe later we'll add image on the left side
                            VBox rootFrame = new VBox();
                            VBox contentFrame = new VBox();
                            VBox textFrame = new VBox();
                            rootFrame.getChildren().add(contentFrame);
                            contentFrame.getChildren().add(img);
                            contentFrame.getChildren().add(textFrame);
                            contentFrame.getChildren().add(btn);

                            // set Border
                            contentFrame.setBorder(new Border(
                                    new BorderStroke(
                                            Color.BLACK,
                                            BorderStrokeStyle.SOLID,
                                            CornerRadii.EMPTY,
                                            BorderWidths.DEFAULT
                                    )
                            ));

                            // set spacing
                            contentFrame.setSpacing(8);

                            // set Padding
                            rootFrame.setPadding(new Insets(10f));
                            contentFrame.setPadding(new Insets(10f));
//                            textFrame.setPadding(new Insets(10f));

                            // text Tersedia/Tidak tersedia
                            Label availableStatus = new Label();
                            availableStatus.setMinSize(80, 0);
                            availableStatus.setText("Tersedia");
                            availableStatus.setStyle("-fx-font-weight: bold");
                            availableStatus.setTextFill(Color.BLACK);

                            // Nama PC
                            Label idPcLabel = new Label();
                            idPcLabel.setMinSize(80, 0);
                            idPcLabel.setText(item.komputer_id());
                            idPcLabel.setTextFill(Color.BLACK);

                            // Kategori
                            Label kategoriLabel = new Label();
                            kategoriLabel.setMinSize(100, 0);
                            kategoriLabel.setText(item.spek_kategori());
                            kategoriLabel.setTextFill(Color.BLACK);

                            // Harga
                            Label hargaLabel = new Label();
                            hargaLabel.setMinSize(100, 0);
                            hargaLabel.setText(String.format("Rp %d/JAM", item.harga_per_jam()));
                            hargaLabel.setTextFill(Color.BLUE);

                            // Detail btn
                            btn.setText("Lihat Detail >");
                            btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
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
