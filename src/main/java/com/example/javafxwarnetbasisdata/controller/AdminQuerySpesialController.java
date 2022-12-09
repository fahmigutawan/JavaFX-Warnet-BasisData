package com.example.javafxwarnetbasisdata.controller;

import com.example.javafxwarnetbasisdata.listener.ListOfSpecialQueryMakananListener;
import com.example.javafxwarnetbasisdata.listener.ListOfSpecialQueryPedagangListener;
import com.example.javafxwarnetbasisdata.model.SpecialQueryMakananModel;
import com.example.javafxwarnetbasisdata.model.SpecialQueryPedagangModel;
import com.example.javafxwarnetbasisdata.repository.Repository;
import com.example.javafxwarnetbasisdata.util.CustomException;
import com.example.javafxwarnetbasisdata.util.CustomScene;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminQuerySpesialController implements Initializable {
    public Button top5makanan_btn;
    public Button top5pedagang_btn;

    public TableView<SpecialQueryMakananModel> makanan_table;
    public TableColumn<SpecialQueryMakananModel, String> makananid_column;
    public TableColumn<SpecialQueryMakananModel, String> makanan_pedagangid_column;
    public TableColumn<SpecialQueryMakananModel, String> makanan_nama_column;
    public TableColumn<SpecialQueryMakananModel, Integer> makanan_terjual_column;

    public TableView<SpecialQueryPedagangModel> pedagang_table;
    public TableColumn<SpecialQueryPedagangModel, String> pedagang_id_column;
    public TableColumn<SpecialQueryPedagangModel, String> pedagang_nama_column;
    public TableColumn<SpecialQueryPedagangModel, Integer> pedagang_pejualan_column;
    public Button kembali_btn;

    ObservableList<SpecialQueryMakananModel> makananModels = FXCollections.observableArrayList();
    ObservableList<SpecialQueryPedagangModel> pedagangModels = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Repository.getTop5MakananTerjual(
                new ListOfSpecialQueryMakananListener() {
                    @Override
                    public void onSuccess(List<SpecialQueryMakananModel> makanans) {
                        makananModels.addAll(makanans);
                    }

                    @Override
                    public void onFailed(CustomException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, e.customMessage);
                        alert.show();
                    }
                }
        );

        Repository.getTop5PedagangByPenjualan(
                new ListOfSpecialQueryPedagangListener() {
                    @Override
                    public void onSuccess(List<SpecialQueryPedagangModel> pedagangs) {
                        pedagangModels.addAll(pedagangs);
                    }

                    @Override
                    public void onFailed(CustomException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, e.customMessage);
                        alert.show();
                    }
                }
        );

        kembali_btn.setOnMouseClicked(mouseEvent -> {
            Stage stage = (Stage) kembali_btn.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafxwarnetbasisdata/home-admin-view.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene scene = new CustomScene(root);
            stage.setScene(scene);
        });

        top5makanan_btn.setOnMouseClicked(mouseEvent -> {
            pedagang_table.setVisible(false);
            makanan_table.setVisible(true);
        });

        top5pedagang_btn.setOnMouseClicked(mouseEvent -> {
            makanan_table.setVisible(false);
            pedagang_table.setVisible(true);
        });

        pedagang_table.setItems(pedagangModels);
        pedagang_id_column.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().pedagang_id()));
        pedagang_nama_column.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().nama()));
        pedagang_pejualan_column.setCellValueFactory(celldata -> new SimpleIntegerProperty(celldata.getValue().penjualan()).asObject());

        makanan_table.setItems(makananModels);
        makananid_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().makanan_id()));
        makanan_pedagangid_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().pedagang_id()));
        makanan_nama_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().nama()));
        makanan_terjual_column.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().terjual()).asObject());
    }
}
