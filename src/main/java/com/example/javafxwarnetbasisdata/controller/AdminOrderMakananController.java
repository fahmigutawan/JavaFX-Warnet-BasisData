package com.example.javafxwarnetbasisdata.controller;

import com.example.javafxwarnetbasisdata.listener.ListOfMakananOfOrderMakananListener;
import com.example.javafxwarnetbasisdata.listener.ListOfOrderMakananListener;
import com.example.javafxwarnetbasisdata.model.MakananOfOrderMakananModel;
import com.example.javafxwarnetbasisdata.model.OrderMakananModel;
import com.example.javafxwarnetbasisdata.repository.Repository;
import com.example.javafxwarnetbasisdata.util.CustomException;
import com.example.javafxwarnetbasisdata.util.CustomScene;
import javafx.beans.property.SimpleDoubleProperty;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminOrderMakananController implements Initializable {

    public Button kembali_btn;
    public TableView<OrderMakananModel> ordermakanan_table;
    public TableColumn<OrderMakananModel, String> ordermakanan_orderid_column;
    public TableColumn<OrderMakananModel, String> ordermakanan_customerid_column;
    public TableColumn<OrderMakananModel, String> ordermakanan_pedagangid_column;
    public TableColumn<OrderMakananModel, Double> ordermakanan_totalharga_column;
    public TableColumn<OrderMakananModel, String> ordermakanan_status_column;

    public TableView<MakananOfOrderMakananModel> makanan_of_ordermakanan_table;
    public TableColumn<MakananOfOrderMakananModel, String> makanan_of_ordermakanan_orderid_column;
    public TableColumn<MakananOfOrderMakananModel, String> makanan_of_ordermakanan_makananid_column;

    private ObservableList<OrderMakananModel> orderMakananModels = FXCollections.observableArrayList();
    private ObservableList<MakananOfOrderMakananModel> makananOfOrderMakananModelModels = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Repository.getOrderMakananList(new ListOfOrderMakananListener() {
            @Override
            public void onSuccess(List<OrderMakananModel> orders) {
                orderMakananModels.addAll(orders);
            }

            @Override
            public void onFailed(CustomException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.customMessage);
                alert.show();
            }
        });

        ordermakanan_orderid_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().order_id()));
        ordermakanan_customerid_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().customer_id()));
        ordermakanan_pedagangid_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().pedagang_id()));
        ordermakanan_totalharga_column.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().total_harga()).asObject());
        ordermakanan_status_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().status()));
        ordermakanan_table.setItems(orderMakananModels);

        ordermakanan_table.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            Repository.getMakananOfOrderMakananList(
                    ordermakanan_table.getSelectionModel().getSelectedItem().order_id(),
                    new ListOfMakananOfOrderMakananListener() {
                        @Override
                        public void onSuccess(List<MakananOfOrderMakananModel> orders) {
                            makananOfOrderMakananModelModels.clear();
                            makananOfOrderMakananModelModels.addAll(orders);
                        }

                        @Override
                        public void onFailed(CustomException e) {
                            Alert alert = new Alert(Alert.AlertType.ERROR, e.customMessage);
                            alert.show();
                        }
                    }
            );
        });

        makanan_of_ordermakanan_orderid_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().order_id()));
        makanan_of_ordermakanan_makananid_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().makanan_id()));
        makanan_of_ordermakanan_table.setItems(makananOfOrderMakananModelModels);

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
    }
}
