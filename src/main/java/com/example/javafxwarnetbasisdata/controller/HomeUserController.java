package com.example.javafxwarnetbasisdata.controller;

import com.example.javafxwarnetbasisdata.listener.ListOfFoodListener;
import com.example.javafxwarnetbasisdata.listener.ListOfKomputerListener;
import com.example.javafxwarnetbasisdata.listener.ResponseListener;
import com.example.javafxwarnetbasisdata.listener.UserModelListener;
import com.example.javafxwarnetbasisdata.model.FoodModel;
import com.example.javafxwarnetbasisdata.model.KomputerModel;
import com.example.javafxwarnetbasisdata.model.UserModel;
import com.example.javafxwarnetbasisdata.repository.Repository;
import com.example.javafxwarnetbasisdata.util.CustomException;
import com.example.javafxwarnetbasisdata.util.CustomScene;
import com.example.javafxwarnetbasisdata.util.TemporaryMemory;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class HomeUserController implements Initializable {
    public Label home_user_username;
    public HBox home_user_komputer_list;
    public Label komputer_orderlist_btn;
    public Label editprofile_btn;
    public Label keluar_btn;
    public ScrollPane komputer_scrollpane;
    public ScrollPane makanan_scrollpane;
    public VBox home_user_makanan_list;
    public Label makanan_orderlist_btn;
    public Label makanan_keranjang_btn;
    public TableView<FoodModel> keranjang_table;
    public TableColumn<FoodModel, String> keranjang_id_column;
    public TableColumn<FoodModel, String> keranjang_namamkn_column;
    public TableColumn<FoodModel, String> keranjang_idstand_column;
    public Button keranjang_order_btn;
    public Button keranjang_delete_btn;
    public Button keranjang_clear_btn;


    private ObservableList<FoodModel> keranjangItem = TemporaryMemory.keranjang;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getUser();
        if(keranjangItem.isEmpty()){
            getAllMakanan();
        }else{
            getAllMakananByPedagangId(keranjangItem.get(0).pedagang_id());
        }
        getAllKomputer();

        keranjang_table.setItems(keranjangItem);
        keranjang_idstand_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().pedagang_id()));
        keranjang_id_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().makanan_id()));
        keranjang_namamkn_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().nama()));

        keranjang_table.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            keranjang_delete_btn.setDisable(false);
        });

        komputer_orderlist_btn.setOnMouseClicked(mouseEvent -> {
            Stage stage = (Stage) komputer_orderlist_btn.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafxwarnetbasisdata/komputer-orderlist-view.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene scene = new CustomScene(root);
            stage.setScene(scene);
        });
        editprofile_btn.setOnMouseClicked(mouseEvent -> {

        });
        keluar_btn.setOnMouseClicked(mouseEvent -> {
            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Anda yakin ingin keluar?"
            );
            Optional<ButtonType> result = alert.showAndWait();
            ButtonType button = result.orElse(ButtonType.CANCEL);

            if (button == ButtonType.OK) {
                Stage stage = (Stage) keluar_btn.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafxwarnetbasisdata/onboard-view.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scene scene = new CustomScene(root);
                stage.setScene(scene);
            }
        });
        keranjang_order_btn.setOnMouseClicked(mouseEvent -> {
            order();
            if(keranjangItem.isEmpty()) keranjang_order_btn.setDisable(true);
        });
        keranjang_delete_btn.setOnMouseClicked(mouseEvent -> {
            removeFromKeranjang(keranjang_table.getSelectionModel().getSelectedItem());
            if(keranjangItem.isEmpty()) {
                keranjang_order_btn.setDisable(true);
                keranjang_delete_btn.setDisable(true);
            }
        });
        keranjang_clear_btn.setOnMouseClicked(mouseEvent -> {
            clearFromKeranjang();
            keranjang_order_btn.setDisable(true);
            keranjang_delete_btn.setDisable(true);
        });
    }

    void getAllMakanan() {
        Repository.getListMakanan(new ListOfFoodListener() {
            @Override
            public void onSuccess(List<FoodModel> listOfFood) {
                home_user_makanan_list.getChildren().clear();
                for (FoodModel item : listOfFood) {
                    // Hbox to be container for Vboxes bellow
                    HBox hbox = new HBox();
                    hbox.setPadding(new Insets(16));
                    hbox.setStyle("-fx-background-color: #D9D9D9;");

                    // Hbox for left content (Image, then text on its right)
                    HBox leftContentHbox = new HBox();
                    leftContentHbox.setSpacing(8);
                    leftContentHbox.setPadding(new Insets(8));

                    // Image for inside left content
                    ImageView img = new ImageView();
                    img.setFitHeight(80);
                    img.setFitWidth(80);
                    img.setImage(new Image(getClass().getResourceAsStream("/com/example/javafxwarnetbasisdata/image/img_food.jpg")));

                    // VBox for inside left content (food name, price, etc)
                    VBox leftContentTextVbox = new VBox();
                    HBox.setHgrow(leftContentTextVbox, Priority.ALWAYS);
                    leftContentTextVbox.setAlignment(Pos.CENTER_LEFT);

                    // VBox for right button (Lihat detail)
                    VBox rightContentVbox = new VBox();
                    HBox.setHgrow(rightContentVbox, Priority.ALWAYS);
                    rightContentVbox.setSpacing(8);
                    rightContentVbox.setAlignment(Pos.CENTER_RIGHT);

                    /**LEFT CONTENT*/
                    //Nama Makanan
                    Label foodName = new Label();
                    foodName.setText(item.nama());
                    foodName.setFont(new Font(24));

                    //Harga
                    Label harga = new Label();
                    harga.setText(String.format("Rp %.0f", item.harga()));
                    harga.setFont(new Font(20));

                    //Kategori
                    Label kategori = new Label();
                    kategori.setText(item.kategori_word());
                    kategori.setFont(new Font(14));
                    kategori.setTextFill(Color.DARKGREEN);

                    leftContentTextVbox.getChildren().add(foodName);
                    leftContentTextVbox.getChildren().add(harga);
                    leftContentTextVbox.getChildren().add(kategori);

                    /**RIGHT CONTENT*/
                    // Button
                    Button tambahKeranjang = new Button();
                    tambahKeranjang.setPrefWidth(200);
                    tambahKeranjang.setText("Tambah ke keranjang");
                    tambahKeranjang.setFont(new Font(18));
                    tambahKeranjang.setOnMouseClicked(mouseEvent -> {
                        insertToKeranjang(item);
                    });

                    // Add left item to HBOX
                    leftContentHbox.getChildren().add(img);
                    leftContentHbox.getChildren().add(leftContentTextVbox);

                    // Add right item to VBOX
                    rightContentVbox.getChildren().add(tambahKeranjang);

                    // Add all parent to Root HBOX
                    hbox.getChildren().add(leftContentHbox);
                    hbox.getChildren().add(rightContentVbox);

                    // Add Root HBOX to lsit
                    home_user_makanan_list.getChildren().add(hbox);
                }
            }

            @Override
            public void onFailed(CustomException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.customMessage);
                alert.show();
            }
        });
    }

    void getAllMakananByPedagangId(String pedagang_id) {
        Repository.getListMakananByPedagangId(pedagang_id, new ListOfFoodListener() {
            @Override
            public void onSuccess(List<FoodModel> listOfFood) {
                home_user_makanan_list.getChildren().clear();

                for (FoodModel item : listOfFood) {
                    // Hbox to be container for Vboxes bellow
                    HBox hbox = new HBox();
                    hbox.setPadding(new Insets(16));
                    hbox.setStyle("-fx-background-color: #D9D9D9;");

                    // Hbox for left content (Image, then text on its right)
                    HBox leftContentHbox = new HBox();
                    leftContentHbox.setSpacing(8);
                    leftContentHbox.setPadding(new Insets(8));

                    // Image for inside left content
                    ImageView img = new ImageView();
                    img.setFitHeight(80);
                    img.setFitWidth(80);
                    img.setImage(new Image(getClass().getResourceAsStream("/com/example/javafxwarnetbasisdata/image/img_food.jpg")));

                    // VBox for inside left content (food name, price, etc)
                    VBox leftContentTextVbox = new VBox();
                    HBox.setHgrow(leftContentTextVbox, Priority.ALWAYS);
                    leftContentTextVbox.setAlignment(Pos.CENTER_LEFT);

                    // VBox for right button (Lihat detail)
                    VBox rightContentVbox = new VBox();
                    HBox.setHgrow(rightContentVbox, Priority.ALWAYS);
                    rightContentVbox.setSpacing(8);
                    rightContentVbox.setAlignment(Pos.CENTER_RIGHT);

                    /**LEFT CONTENT*/
                    //Nama Makanan
                    Label foodName = new Label();
                    foodName.setText(item.nama());
                    foodName.setFont(new Font(24));

                    //Harga
                    Label harga = new Label();
                    harga.setText(String.format("Rp %.0f", item.harga()));
                    harga.setFont(new Font(20));

                    //Kategori
                    Label kategori = new Label();
                    kategori.setText(item.kategori_word());
                    kategori.setFont(new Font(14));
                    kategori.setTextFill(Color.DARKGREEN);

                    leftContentTextVbox.getChildren().add(foodName);
                    leftContentTextVbox.getChildren().add(harga);
                    leftContentTextVbox.getChildren().add(kategori);

                    /**RIGHT CONTENT*/
                    // Button
                    Button tambahKeranjang = new Button();
                    tambahKeranjang.setPrefWidth(200);
                    tambahKeranjang.setText("Tambah ke keranjang");
                    tambahKeranjang.setFont(new Font(18));
                    tambahKeranjang.setOnMouseClicked(mouseEvent -> {
                        insertToKeranjang(item);
                        tambahKeranjang.setDisable(true);
                    });
                    if (keranjangItem.size() == 1) {
                        if (Objects.equals(keranjangItem.get(0).makanan_id(), item.makanan_id())) {
                            tambahKeranjang.setDisable(true);
                        }
                    }

                    // Add left item to HBOX
                    leftContentHbox.getChildren().add(img);
                    leftContentHbox.getChildren().add(leftContentTextVbox);

                    // Add right item to VBOX
                    rightContentVbox.getChildren().add(tambahKeranjang);

                    // Add all parent to Root HBOX
                    hbox.getChildren().add(leftContentHbox);
                    hbox.getChildren().add(rightContentVbox);

                    // Add Root HBOX to lsit
                    home_user_makanan_list.getChildren().add(hbox);
                }
            }

            @Override
            public void onFailed(CustomException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.customMessage);
                alert.show();
            }
        });
    }

    void getAllKomputer() {
        Repository.getListOfKomputer(new ListOfKomputerListener() {
            @Override
            public void onSuccess(ArrayList<KomputerModel> listOfKomputer) {
                for (KomputerModel item : listOfKomputer) {

                    // Very first layer (background)
                    VBox firstLayer = new VBox();
                    firstLayer.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, new CornerRadii(8), Insets.EMPTY)));
                    firstLayer.setOnMouseEntered(mouseEvent -> {
                        firstLayer.setBackground(new Background(new BackgroundFill(Color.LIGHTCYAN, new CornerRadii(8), Insets.EMPTY)));
                    });
                    firstLayer.setOnMouseExited(mouseEvent -> {
                        firstLayer.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, new CornerRadii(8), Insets.EMPTY)));
                    });
                    firstLayer.setOnMousePressed(mouseEvent -> {
                        firstLayer.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, new CornerRadii(8), Insets.EMPTY)));
                    });
                    firstLayer.setOnMouseReleased(mouseEvent -> {
                        firstLayer.setBackground(new Background(new BackgroundFill(Color.LIGHTCYAN, new CornerRadii(8), Insets.EMPTY)));

                    });
                    firstLayer.setOnMouseClicked(mouseEvent -> {
                        TemporaryMemory.pickedComputerId = item.komputer_id().getValue();
                        Stage stage = (Stage) firstLayer.getScene().getWindow();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafxwarnetbasisdata/komputer-detail-view.fxml"));
                        Parent root = null;
                        try {
                            root = loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Scene scene = new CustomScene(root);
                        stage.setScene(scene);
                    });
                    HBox.setHgrow(firstLayer, Priority.NEVER);
                    firstLayer.setPadding(new Insets(8));

                    // Vbox of content
                    VBox vbox = new VBox();
                    vbox.setSpacing(8);
                    vbox.setPadding(new Insets(8));
                    HBox.setHgrow(vbox, Priority.NEVER);

                    // Image
                    ImageView img = new ImageView();
                    img.setPickOnBounds(true);
                    img.setFitWidth(80);
                    img.setFitHeight(80);
                    switch (item.status().getValue()) {
                        case "ready" -> {
                            img.setImage(new Image(getClass().getResourceAsStream("/com/example/javafxwarnetbasisdata/image/ic_computer_available.png")));
                        }

                        case "not ready" -> {
                            img.setImage(new Image(getClass().getResourceAsStream("/com/example/javafxwarnetbasisdata/image/ic_computer_unavailable.png")));
                        }
                    }

                    // text of ready/not ready
                    Label availabelText = new Label();
                    availabelText.setFont(new Font(18));
                    switch (item.status().getValue()) {
                        case "ready" -> {
                            availabelText.setText("Ready");
                            availabelText.setTextFill(Color.MEDIUMBLUE);
                        }

                        case "not ready" -> {
                            availabelText.setText("Not Ready");
                            availabelText.setTextFill(Color.MEDIUMVIOLETRED);
                        }
                    }

                    // text of PC id
                    Label pcid = new Label();
                    pcid.setFont(new Font(18));
                    pcid.setText(item.komputer_id().getValue());

                    // text of price
                    Label price = new Label();
                    price.setFont(new Font(18));
                    price.setText(String.format("Rp %s", item.harga_perjam().getValue().toString()));

                    // Vbox of text
                    VBox vboxOfText = new VBox();
                    vboxOfText.getChildren().add(availabelText);
                    vboxOfText.getChildren().add(pcid);
                    vboxOfText.getChildren().add(price);

                    // Add component to vbox
                    vbox.getChildren().add(img);
                    vbox.getChildren().add(vboxOfText);


                    // Add component to firstLayer
                    firstLayer.getChildren().add(vbox);

                    // Add firstLayer to home_user_komputer_list
                    home_user_komputer_list.getChildren().add(firstLayer);
                }

                komputer_scrollpane.setStyle("-fx-fit-to-height: true;");
            }

            @Override
            public void onFailed(CustomException e) {

            }
        });
    }

    void getUser() {
        Repository.getUserByUserId(TemporaryMemory.savedCustomerId, new UserModelListener() {
            @Override
            public void onSuccess(UserModel o) {
                home_user_username.setText(String.format("Selamat Datang, %s", o.name()));
            }

            @Override
            public void onFailed(CustomException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.customMessage);
                alert.show();
            }
        });
    }

    void order(){
        Repository.createOrderMakanan(
                TemporaryMemory.savedCustomerId,
                keranjangItem.stream().toList(),
                new ResponseListener() {
                    @Override
                    public void onSuccess(Object o) {
                        clearFromKeranjang();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Order berhasil dibuat");
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

    void insertToKeranjang(FoodModel food) {
        keranjangItem.add(food);
        keranjang_order_btn.setDisable(false);
        if (keranjangItem.size() == 1) getAllMakananByPedagangId(food.pedagang_id());
    }

    void removeFromKeranjang(FoodModel food) {
        keranjangItem.remove(food);

        if (keranjangItem.isEmpty()) {
            getAllMakanan();
        }
    }

    void clearFromKeranjang() {
        keranjangItem.clear();
        getAllMakanan();
    }
}
