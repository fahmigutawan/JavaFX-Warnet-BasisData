package com.example.javafxwarnetbasisdata.controller;

import com.example.javafxwarnetbasisdata.listener.KomputerModelListener;
import com.example.javafxwarnetbasisdata.listener.OrderModelListener;
import com.example.javafxwarnetbasisdata.listener.ResponseListener;
import com.example.javafxwarnetbasisdata.model.KomputerModel;
import com.example.javafxwarnetbasisdata.model.OrderModel;
import com.example.javafxwarnetbasisdata.repository.Repository;
import com.example.javafxwarnetbasisdata.util.CustomException;
import com.example.javafxwarnetbasisdata.util.CustomScene;
import com.example.javafxwarnetbasisdata.util.TemporaryMemory;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class KomputerDetailController implements Initializable {
    public Button kembali_btn;
    public Label komputer_id_label;
    public Label komputer_status_label;
    public Label komputer_harga_label;
    public Button akhiri_pesanan_btn;
    public Label komputer_kategori_label;
    private Double komputer_harga_tmp = .0;
    public Button pesan_btn;
    private OrderModel orderModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getKomputer();
        kembali_btn.setOnMouseClicked(mouseEvent -> {
            Stage stage = (Stage) kembali_btn.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafxwarnetbasisdata/home-user-view.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene scene = new CustomScene(root);
            stage.setScene(scene);
        });
        pesan_btn.setOnMouseClicked(mouseEvent -> {
            Repository.createKomputerTransaction(
                    TemporaryMemory.pickedComputerId,
                    TemporaryMemory.savedCustomerId,
                    komputer_harga_tmp,
                    new ResponseListener() {
                        @Override
                        public void onSuccess(Object o) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Komputer berhasil dipesan");
                            alert.show();
                            refresh();
                        }

                        @Override
                        public void onFailed(CustomException e) {
                            Alert alert = new Alert(Alert.AlertType.ERROR, e.customMessage);
                            alert.show();
                        }
                    }
            );
        });
        akhiri_pesanan_btn.setOnMouseClicked(mouseEvent -> {
            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Anda yakin ingin mengakhiri sesi?"
            );
            Optional<ButtonType> result = alert.showAndWait();
            ButtonType button = result.orElse(ButtonType.CANCEL);

            if (button == ButtonType.OK) {
                Repository.endKomputerTransaction(
                        TemporaryMemory.savedCustomerId,
                        orderModel.order_id(),
                        new ResponseListener() {
                            @Override
                            public void onSuccess(Object o) {
                                refresh();
                                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Sesi berhasil diakhiri");
                                alert.show();
                            }

                            @Override
                            public void onFailed(CustomException e) {
                                Alert alert = new Alert(Alert.AlertType.ERROR, e.customMessage);
                                alert.show();
                            }
                        }
                );
            }
        });
    }

    void refresh() {
        getKomputer();
    }

    void getKomputer() {
        Repository.getKomputerById(
                TemporaryMemory.pickedComputerId,
                new KomputerModelListener() {
                    @Override
                    public void onSuccess(KomputerModel komputer) {
                        switch (komputer.status().getValue()) {
                            case "ready" -> {
                                pesan_btn.setDisable(false);
                                akhiri_pesanan_btn.setVisible(false);
                                komputer_status_label.setTextFill(Color.WHITE);
                            }
                            case "not ready" -> {
                                pesan_btn.setDisable(true);
                                komputer_status_label.setTextFill(Color.MEDIUMVIOLETRED);

                                Repository.getOrderDetailByKomputerId(
                                        komputer.komputer_id().getValue(),
                                        new OrderModelListener() {
                                            @Override
                                            public void onSuccess(OrderModel order) {
                                                orderModel = order;
                                                if(order.customer_id().equals(TemporaryMemory.savedCustomerId)){
                                                    akhiri_pesanan_btn.setVisible(true);
                                                }else{
                                                    akhiri_pesanan_btn.setVisible(false);
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

                        if (komputer_status_label.getText().toLowerCase(Locale.ROOT).equals("not ready")) {
                            komputer_status_label.setTextFill(Color.MEDIUMVIOLETRED);
                        }
                        komputer_kategori_label.setText(komputer.kategori_word().getValue());
                        komputer_harga_tmp = komputer.harga_perjam().getValue();
                        komputer_id_label.setText(komputer.komputer_id().getValue());
                        komputer_status_label.setText(komputer.status().getValue());
                        komputer_harga_label.setText(String.format("Rp %.0f", komputer.harga_perjam().getValue()));
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
