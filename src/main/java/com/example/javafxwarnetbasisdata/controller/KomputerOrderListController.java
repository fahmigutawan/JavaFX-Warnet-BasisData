package com.example.javafxwarnetbasisdata.controller;

import com.example.javafxwarnetbasisdata.MyApplication;
import com.example.javafxwarnetbasisdata.listener.ListOfOrderListener;
import com.example.javafxwarnetbasisdata.listener.ResponseListener;
import com.example.javafxwarnetbasisdata.model.OrderModel;
import com.example.javafxwarnetbasisdata.repository.Repository;
import com.example.javafxwarnetbasisdata.util.CustomException;
import com.example.javafxwarnetbasisdata.util.CustomScene;
import com.example.javafxwarnetbasisdata.util.TemporaryMemory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class KomputerOrderListController implements Initializable {
    public Button kembali_btn;
    public VBox container_order_list;
    public ScrollPane scrollpane_for_container;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getOrderList();

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

    void refresh() {
        container_order_list.getChildren().clear();
        getOrderList();
    }

    void getOrderList() {
        Repository.getOrderListByCustomerId(
                TemporaryMemory.savedCustomerId,
                new ListOfOrderListener() {
                    @Override
                    public void onSuccess(List<OrderModel> orders) {
                        for (OrderModel item : orders) {
                            // Very first layer
                            Pane firstLayer = new Pane();
                            firstLayer.setPadding(new Insets(8, 0, 8, 0));

                            // Hbox to be container for Vboxes bellow
                            HBox hbox = new HBox();
                            hbox.setPadding(new Insets(16));
                            hbox.setStyle("-fx-background-color: #D9D9D9;");

                            // VBox for left text (PC Name, price, etc)
                            VBox leftContentVbox = new VBox();
                            HBox.setHgrow(leftContentVbox, Priority.ALWAYS);
                            leftContentVbox.setAlignment(Pos.CENTER_LEFT);

                            // VBox for right button (Cetak & akhiri)
                            VBox rightContentVbox = new VBox();
                            HBox.setHgrow(rightContentVbox, Priority.ALWAYS);
                            rightContentVbox.setSpacing(8);
                            rightContentVbox.setAlignment(Pos.CENTER_RIGHT);

                            /**Left Content*/
                            //Pc Name
                            Label pcName = new Label();
                            pcName.setText(item.komputer_id());
                            pcName.setFont(new Font(24));

                            //Harga
                            Label harga = new Label();
                            harga.setText(String.format("Rp %.0f", item.harga()));
                            harga.setFont(new Font(20));

                            // Status
                            Label status = new Label();
                            status.setFont(new Font(20));
                            switch (item.status()) {
                                case "1" -> {
                                    status.setText("Masih Berjalan");
                                    status.setTextFill(Color.BLUE);
                                }

                                case "3" -> {
                                    status.setText("Sudah berakhir");
                                    status.setTextFill(Color.RED);
                                }
                            }

                            // Insert name & harga to VBox
                            leftContentVbox.getChildren().add(pcName);
                            leftContentVbox.getChildren().add(harga);
                            leftContentVbox.getChildren().add(status);

                            /**Right content*/
                            // Button cetak
                            Button cetakBtn = new Button();
                            cetakBtn.setPrefWidth(200);
                            cetakBtn.setText("Cetak");
                            cetakBtn.setFont(new Font(18));
                            rightContentVbox.getChildren().add(cetakBtn);
                            cetakBtn.setOnMouseClicked(mouseEvent -> {

                                Repository.createReport(
                                        new HashMap(),
                                        getClass().getResourceAsStream("/report/sample.jrxml"));
                            });

                            // Button akhiri
                            Button akhiriBtn = new Button();
                            akhiriBtn.setPrefWidth(200);
                            akhiriBtn.setText("Akhiri");
                            akhiriBtn.setFont(new Font(18));
                            akhiriBtn.setOnMouseClicked(mouseEvent -> {
                                Alert alert = new Alert(
                                        Alert.AlertType.CONFIRMATION,
                                        "Anda yakin ingin mengakhiri sesi?"
                                );
                                Optional<ButtonType> result = alert.showAndWait();
                                ButtonType button = result.orElse(ButtonType.CANCEL);

                                if (button == ButtonType.OK) {
                                    Repository.endKomputerTransaction(
                                            item.customer_id(),
                                            item.order_id(),
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
                            switch (item.status()) {
                                case "1" -> {
                                    akhiriBtn.setDisable(false);
                                }

                                case "3" -> {
                                    akhiriBtn.setDisable(true);
                                }
                            }
                            rightContentVbox.getChildren().add(akhiriBtn);

                            /**Add all component to Pane*/
                            hbox.getChildren().add(leftContentVbox);
                            hbox.getChildren().add(rightContentVbox);
                            firstLayer.getChildren().add(hbox);

                            container_order_list.getChildren().add(hbox);
                        }
                    }

                    @Override
                    public void onFailed(CustomException e) {

                    }
                }
        );
    }
}
