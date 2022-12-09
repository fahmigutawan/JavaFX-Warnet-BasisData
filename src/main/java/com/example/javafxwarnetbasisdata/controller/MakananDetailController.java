package com.example.javafxwarnetbasisdata.controller;

import com.example.javafxwarnetbasisdata.listener.FoodModelListener;
import com.example.javafxwarnetbasisdata.listener.ResponseListener;
import com.example.javafxwarnetbasisdata.model.FoodModel;
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
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MakananDetailController implements Initializable {
    public Label nama_makanan_label;
    public Label makanan_kategori_label;
    public Label makanan_harga_label;
    public Label makanan_stok_label;
    public Button pesan_btn;
    public Button tambah_cart_btn;
    public Button kembali_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getMakanan();

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
    }

    void getMakanan(){
        Repository.getMakananById(
                TemporaryMemory.pickerMakananId,
                new FoodModelListener() {
                    @Override
                    public void onSuccess(FoodModel food) {
                        nama_makanan_label.setText(food.nama());
                        makanan_kategori_label.setText(food.kategori_word());
                        makanan_harga_label.setText(String.format("Rp %.0f", food.harga()));
                        makanan_stok_label.setText(String.format("Stok: %d", food.stok()));

                        tambah_cart_btn.setOnMouseClicked(mouseEvent -> {
                            Repository.insertMakananToKeranjang(
                                    TemporaryMemory.savedCustomerId,
                                    food.pedagang_id(),
                                    food.makanan_id(),
                                    new ResponseListener() {
                                        @Override
                                        public void onSuccess(Object o) {
                                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Makanan berhasil ditambahkan ke keranjang");
                                            alert.show();
                                        }

                                        @Override
                                        public void onFailed(CustomException e) {
                                            Alert alert = new Alert(Alert.AlertType.ERROR, e.customMessage);
                                            alert.show();
                                        }
                                    }
                            );
                        });
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
