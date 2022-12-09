package com.example.javafxwarnetbasisdata.controller;

import com.example.javafxwarnetbasisdata.listener.ListOfOrderKomputerListener;
import com.example.javafxwarnetbasisdata.model.OrderKomputerModel;
import com.example.javafxwarnetbasisdata.repository.Repository;
import com.example.javafxwarnetbasisdata.util.CustomException;
import com.example.javafxwarnetbasisdata.util.CustomScene;
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

public class AdminOrderKomputerController implements Initializable {
    public TableView<OrderKomputerModel> orderkomputer_table;

    public TableColumn<OrderKomputerModel, String> orderkomputer_orderid_column;
    public TableColumn<OrderKomputerModel, String> orderkomputer_customerid_column;
    public TableColumn<OrderKomputerModel, String> orderkomputer_komputerid_column;
    public TableColumn<OrderKomputerModel, Double> orderkomputer_harga_column;
    public TableColumn<OrderKomputerModel, String> orderkomputer_status_column;

    public Button kembali_btn;

    private ObservableList<OrderKomputerModel> orderKomputerModels = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getOrderKomputerList();

        orderkomputer_orderid_column.setCellValueFactory(cellData -> cellData.getValue().order_id());
        orderkomputer_customerid_column.setCellValueFactory(cellData -> cellData.getValue().customer_id());
        orderkomputer_komputerid_column.setCellValueFactory(cellData -> cellData.getValue().komputer_id());
        orderkomputer_harga_column.setCellValueFactory(cellData -> cellData.getValue().harga().asObject());
        orderkomputer_status_column.setCellValueFactory(cellData -> cellData.getValue().status());

        orderkomputer_table.setItems(orderKomputerModels);

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

    void getOrderKomputerList() {
        Repository.getOrderKomputerList(
                new ListOfOrderKomputerListener() {
                    @Override
                    public void onSuccess(List<OrderKomputerModel> orders) {
                        orderKomputerModels.addAll(orders);
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
