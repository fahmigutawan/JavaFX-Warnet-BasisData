package com.example.javafxwarnetbasisdata.controller;

import com.example.javafxwarnetbasisdata.listener.*;
import com.example.javafxwarnetbasisdata.model.CustomerModel;
import com.example.javafxwarnetbasisdata.model.EmployeeModel;
import com.example.javafxwarnetbasisdata.model.KomputerModel;
import com.example.javafxwarnetbasisdata.model.PedagangModel;
import com.example.javafxwarnetbasisdata.repository.Repository;
import com.example.javafxwarnetbasisdata.util.CustomException;
import com.example.javafxwarnetbasisdata.util.CustomScene;
import com.example.javafxwarnetbasisdata.util.HomeAdminState;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class HomeAdminController implements Initializable {
    public TableView<EmployeeModel> employee_table;
    public TableColumn<EmployeeModel, String> employee_id;
    public TableColumn<EmployeeModel, Double> employee_salary;
    public TableColumn<EmployeeModel, String> employee_name;
    public TableColumn<EmployeeModel, String> employee_phone_number;
    public TableColumn<EmployeeModel, String> employee_street;
    public TableColumn<EmployeeModel, String> employee_postcode;
    public TableColumn<EmployeeModel, String> employee_city;
    public TableColumn<EmployeeModel, String> employee_province;

    public TableView<PedagangModel> pedagang_table;
    public TableColumn<PedagangModel, String> pedagang_id;
    public TableColumn<PedagangModel, String> pedagang_standname;
    public TableColumn<PedagangModel, String> pedagang_standnumber;

    public TableView<CustomerModel> customer_table;
    public TableColumn<CustomerModel, String> customer_id_column;
    public TableColumn<CustomerModel, String> customer_name_column;
    public TableColumn<CustomerModel, String> customer_profile_pic_column;
    public TableColumn<CustomerModel, Double> customer_balance_column;


    public TableView<KomputerModel> komputer_table;
    public TableColumn<KomputerModel, String> komputer_id_column;
    public TableColumn<KomputerModel, String> komputer_kategoriid_column;
    public TableColumn<KomputerModel, Double> komputer_hargaperjam_column;
    public TableColumn<KomputerModel, String> komputer_status_column;

    public StackPane field_stackpane;
    public StackPane tabel_stackpane;

    public TextField idField;
    public TextField salaryAccField;
    public TextField salaryField;
    public TextField nameField;
    public TextField no_telpField;
    public TextField jalanField;
    public TextField kodePosField;
    public TextField kotaField;
    public TextField provinsiField;

    public TextField pedagang_id_field;
    public TextField pedagang_name_field;
    public TextField pedagang_number_field;

    public TextField customer_id_field;
    public TextField customer_balance_acc_field;
    public TextField customer_profile_pic_field;
    public TextField customer_nama_field;

    public TextField komputer_id_field;
    public TextField komputer_kategoriid_field;
    public TextField komputer_harga_perjam_field;
    public TextField komputer_Status_field;

    public TextField search_field;

    public Button insertButton;
    public Button updateButton;
    public Button deleteButton;
    public Button clearButton;
    public Button pegawai_btn;
    public Button pedagang_btn;
    public Button customer_btn;
    public Button komputer_btn;
    public Button keluar_btn;

    public VBox container_field_pedagang;
    public VBox container_field_pegawai;
    public VBox container_field_customer;
    public VBox container_field_komputer;

    private HomeAdminState state = HomeAdminState.Pegawai;

    private ObservableList<EmployeeModel> employeeModels = FXCollections.observableArrayList();
    private ObservableList<PedagangModel> pedagangModels = FXCollections.observableArrayList();
    private ObservableList<CustomerModel> customerModels = FXCollections.observableArrayList();
    private ObservableList<KomputerModel> komputerModels = FXCollections.observableArrayList();

    public void init() {
        Repository.getListOfEmployee(new ListOfEmployeeListener() {
            @Override
            public void onSuccess(ArrayList<EmployeeModel> listOfEmployee) {
                employeeModels.addAll(listOfEmployee);
            }

            @Override
            public void onFailed(CustomException e) {

            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tabel_stackpane.getChildren().clear();
        tabel_stackpane.getChildren().add(employee_table);

        field_stackpane.getChildren().clear();
        field_stackpane.getChildren().add(container_field_pegawai);

        employee_id.setCellValueFactory(cellData -> cellData.getValue().pegawai_id);
        employee_salary.setCellValueFactory(cellData -> cellData.getValue().salary.asObject());
        employee_name.setCellValueFactory(cellData -> cellData.getValue().nama);
        employee_phone_number.setCellValueFactory(cellData -> cellData.getValue().no_telp);
        employee_street.setCellValueFactory(cellData -> cellData.getValue().jalan);
        employee_postcode.setCellValueFactory(cellData -> cellData.getValue().kode_pos);
        employee_city.setCellValueFactory(cellData -> cellData.getValue().kota);
        employee_province.setCellValueFactory(cellData -> cellData.getValue().provinsi);

        pedagang_id.setCellValueFactory(cellData -> cellData.getValue().pedagang_id);
        pedagang_standname.setCellValueFactory(cellData -> cellData.getValue().stand_nama);
        pedagang_standnumber.setCellValueFactory(cellData -> cellData.getValue().stand_number);

        customer_id_column.setCellValueFactory(cellData -> cellData.getValue().customer_id);
        customer_name_column.setCellValueFactory(cellData -> cellData.getValue().nama);
        customer_profile_pic_column.setCellValueFactory(cellData -> cellData.getValue().profile_pic);
        customer_balance_column.setCellValueFactory(cellData -> cellData.getValue().balance_acc.asObject());

        komputer_id_column.setCellValueFactory(cellData -> cellData.getValue().komputer_id());
        komputer_kategoriid_column.setCellValueFactory(cellData -> cellData.getValue().kategori_id());
        komputer_hargaperjam_column.setCellValueFactory(cellData -> cellData.getValue().harga_perjam().asObject());
        komputer_status_column.setCellValueFactory(cellData -> cellData.getValue().status());

        customer_table.setItems(customerModels);
        pedagang_table.setItems(pedagangModels);
        employee_table.setItems(employeeModels);
        komputer_table.setItems(komputerModels);

        employee_table.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            idField.setText(employee_table.getSelectionModel().getSelectedItem().pegawai_id.getValue());
            salaryField.setText(employee_table.getSelectionModel().getSelectedItem().salary.getValue().toString());
            nameField.setText(employee_table.getSelectionModel().getSelectedItem().nama.getValue());
            no_telpField.setText(employee_table.getSelectionModel().getSelectedItem().no_telp.getValue());
            jalanField.setText(employee_table.getSelectionModel().getSelectedItem().jalan.getValue());
            kodePosField.setText(employee_table.getSelectionModel().getSelectedItem().kode_pos.getValue());
            kotaField.setText(employee_table.getSelectionModel().getSelectedItem().kota.getValue());
            provinsiField.setText(employee_table.getSelectionModel().getSelectedItem().provinsi.getValue());
        });
        pedagang_table.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            pedagang_id_field.setText(pedagang_table.getSelectionModel().getSelectedItem().pedagang_id.getValue());
            pedagang_name_field.setText(pedagang_table.getSelectionModel().getSelectedItem().stand_nama.getValue());
            pedagang_number_field.setText(pedagang_table.getSelectionModel().getSelectedItem().stand_number.getValue());
        });
        customer_table.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            customer_id_field.setText(customer_table.getSelectionModel().getSelectedItem().customer_id.getValue());
            customer_nama_field.setText(customer_table.getSelectionModel().getSelectedItem().nama.getValue());
            customer_profile_pic_field.setText(customer_table.getSelectionModel().getSelectedItem().profile_pic.getValue());
            customer_balance_acc_field.setText(customer_table.getSelectionModel().getSelectedItem().balance_acc.getValue().toString());
        });
        komputer_table.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            komputer_id_field.setText(komputer_table.getSelectionModel().getSelectedItem().komputer_id().getValue());
            komputer_kategoriid_field.setText(komputer_table.getSelectionModel().getSelectedItem().kategori_id().getValue());
            komputer_harga_perjam_field.setText(komputer_table.getSelectionModel().getSelectedItem().harga_perjam().getValue().toString());
            komputer_Status_field.setText(komputer_table.getSelectionModel().getSelectedItem().status().getValue());
        });

        pegawai_btn.setOnMouseClicked(mouseEvent -> {
            if (state != HomeAdminState.Pegawai) {
                state = HomeAdminState.Pegawai;
                changeTable();
            }
        });
        pedagang_btn.setOnMouseClicked(mouseEvent -> {
            if (state != HomeAdminState.Pedagang) {
                state = HomeAdminState.Pedagang;
                changeTable();
            }
        });
        customer_btn.setOnMouseClicked(mouseEvent -> {
            if (state != HomeAdminState.Customer) {
                state = HomeAdminState.Customer;
                changeTable();
            }
        });
        komputer_btn.setOnMouseClicked(mouseEvent -> {
            if (state != HomeAdminState.Komputer) {
                state = HomeAdminState.Komputer;
                changeTable();
            }
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

        insertButton.setOnMouseClicked(mouseEvent -> {
            insert();
        });
        updateButton.setOnMouseClicked(mouseEvent -> {
            update();
        });
        deleteButton.setOnMouseClicked(mouseEvent -> {
            delete();
        });
        clearButton.setOnMouseClicked(mouseEvent -> {
            clear();
        });

        search_field.textProperty().addListener((observableValue, s, t1) -> {
            if(observableValue.getValue().isEmpty()){
                refresh();
            }else{
                search(observableValue.getValue());
            }
        });
    }

    boolean isAllFilled() {
        if (state == HomeAdminState.Pegawai) {
            if (nameField.getText().isEmpty()) return false;
            if (salaryField.getText().isEmpty()) return false;
            if (no_telpField.getText().isEmpty()) return false;
            if (jalanField.getText().isEmpty()) return false;
            if (kodePosField.getText().isEmpty()) return false;
            if (kotaField.getText().isEmpty()) return false;
            return (!provinsiField.getText().isEmpty());
        }

        if (state == HomeAdminState.Pedagang) {
            if (pedagang_name_field.getText().isEmpty()) return false;
            return (!pedagang_number_field.getText().isEmpty());
        }

        if (state == HomeAdminState.Customer) {
            if (customer_nama_field.getText().isEmpty()) return false;
            return (!customer_profile_pic_field.getText().isEmpty());
        }

        if (state == HomeAdminState.Komputer) {
            if (komputer_kategoriid_field.getText().isEmpty()) return false;
            return (!komputer_harga_perjam_field.getText().isEmpty());
        }

        return true;
    }

    void clear() {
        if (state == HomeAdminState.Pegawai) {
            idField.setText("");
            salaryField.setText("");
            nameField.setText("");
            no_telpField.setText("");
            jalanField.setText("");
            kodePosField.setText("");
            kotaField.setText("");
            provinsiField.setText("");
            return;
        }
        if (state == HomeAdminState.Pedagang) {
            pedagang_id_field.clear();
            pedagang_name_field.clear();
            pedagang_number_field.clear();
            return;
        }
        if (state == HomeAdminState.Customer) {
            customer_id_field.clear();
            customer_nama_field.clear();
            customer_profile_pic_field.clear();
            customer_balance_acc_field.clear();
            return;
        }
        if (state == HomeAdminState.Komputer) {
            komputer_id_field.clear();
            komputer_kategoriid_field.clear();
            komputer_harga_perjam_field.clear();
            komputer_Status_field.clear();
            return;
        }
    }

    void insert() {
        if (state == HomeAdminState.Pegawai) {
            if (!idField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Harap klik \"CLEAR\" terlebih dahulu");
                alert.show();
            } else if (!isAllFilled()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Harap isi semua field");
                alert.show();
            } else {
                Repository.insertNewPegawai(
                        nameField.getText(),
                        Double.parseDouble(salaryField.getText()),
                        no_telpField.getText(),
                        jalanField.getText(),
                        kodePosField.getText(),
                        kotaField.getText(),
                        provinsiField.getText(),
                        new ResponseListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Pegawai berhasil ditambahkan");
                                alert.show();
                                refresh();
                                clear();
                            }

                            @Override
                            public void onFailed(CustomException e) {
                                Alert alert = new Alert(Alert.AlertType.ERROR, e.customMessage);
                                alert.show();
                            }
                        }
                );
            }
            return;
        }
        if (state == HomeAdminState.Pedagang) {
            if (!pedagang_id_field.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Harap klik \"CLEAR\" terlebih dahulu");
                alert.show();
            } else if (!isAllFilled()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Harap isi semua field");
                alert.show();
            } else {
                Repository.insertNewPedagang(
                        pedagang_name_field.getText(),
                        pedagang_number_field.getText(),
                        new ResponseListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Pedagang berhasil ditambahkan");
                                alert.show();
                                refresh();
                                clear();
                            }

                            @Override
                            public void onFailed(CustomException e) {
                                Alert alert = new Alert(Alert.AlertType.ERROR, e.customMessage);
                                alert.show();
                            }
                        }
                );
            }
            return;
        }
//        if (state == HomeAdminState.Customer) {
//            if (!customer_id_field.getText().isEmpty()) {
//                Alert alert = new Alert(Alert.AlertType.WARNING, "Harap klik \"CLEAR\" terlebih dahulu");
//                alert.show();
//            } if (!isAllFilled()) {
//                Alert alert = new Alert(Alert.AlertType.WARNING, "Harap isi semua field");
//                alert.show();
//            } else {
//
//            }
//            return;
//        }
        if (state == HomeAdminState.Komputer) {
            if (!komputer_id_field.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Harap klik \"CLEAR\" terlebih dahulu");
                alert.show();
            } else if (!isAllFilled()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Harap isi semua field");
                alert.show();
            } else {
                Repository.insertNewKomputer(
                        komputer_kategoriid_field.getText(),
                        Double.parseDouble(komputer_harga_perjam_field.getText()),
                        new ResponseListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Komputer berhasil ditambahkan");
                                alert.show();
                                refresh();
                                clear();
                            }

                            @Override
                            public void onFailed(CustomException e) {
                                Alert alert = new Alert(Alert.AlertType.ERROR, e.customMessage);
                                alert.show();
                            }
                        }
                );
            }
            return;
        }
    }

    void update() {
        if (state == HomeAdminState.Pegawai) {
            if (idField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Harap pilih pegawai terlebih dahulu");
                alert.show();
            } else {
                Repository.updatePegawai(
                        idField.getText(),
                        nameField.getText(),
                        Double.parseDouble(salaryField.getText()),
                        no_telpField.getText(),
                        jalanField.getText(),
                        kodePosField.getText(),
                        kotaField.getText(),
                        provinsiField.getText(),
                        new ResponseListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Data berhasil di update");
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
            }
            return;
        }
        if (state == HomeAdminState.Pedagang) {
            if (pedagang_id_field.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Harap pilih pedagang terlebih dahulu");
                alert.show();
            } else {
                Repository.updatePedagangById(
                        pedagang_id_field.getText(),
                        pedagang_name_field.getText(),
                        pedagang_number_field.getText(),
                        new ResponseListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Data berhasil di update");
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
            }
            return;
        }
        if (state == HomeAdminState.Customer) {
            if (customer_id_field.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Harap pilih customer terlebih dahulu");
                alert.show();
            } else {
                Repository.updateCustomerById(
                        customer_id_field.getText(),
                        customer_nama_field.getText(),
                        customer_profile_pic_field.getText(),
                        new ResponseListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Data berhasil di update");
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
            }
            return;
        }
        if (state == HomeAdminState.Komputer) {
            if (komputer_id_field.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Harap pilih customer terlebih dahulu");
                alert.show();
            } else {
                Repository.updateKomputerById(
                        komputer_id_field.getText(),
                        komputer_kategoriid_field.getText(),
                        Double.parseDouble(komputer_harga_perjam_field.getText()),
                        new ResponseListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Data berhasil di update");
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
            }
            return;
        }
    }

    void delete() {
        if (state == HomeAdminState.Pegawai) {
            if (!nameField.getText().isEmpty() &&
                    !idField.getText().isEmpty()) {
                Alert alert = new Alert(
                        Alert.AlertType.CONFIRMATION,
                        String.format("Anda yakin ingin menghapus %s (%s)",
                                nameField.getText(),
                                idField.getText()
                        )
                );
                Optional<ButtonType> result = alert.showAndWait();
                ButtonType button = result.orElse(ButtonType.CANCEL);

                if (button == ButtonType.OK) {
                    Repository.deletePegawaiById(
                            employee_table.getSelectionModel().getSelectedItem().pegawai_id.getValue(),
                            new ResponseListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    refresh();
                                    clear();
                                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION, "Data berhasil dihapus");
                                    alert1.show();
                                }

                                @Override
                                public void onFailed(CustomException e) {
                                    Alert alert1 = new Alert(Alert.AlertType.ERROR, e.customMessage);
                                    alert1.show();
                                }
                            }
                    );
                }
            }
            return;
        }
        if (state == HomeAdminState.Pedagang) {
            if (!pedagang_id_field.getText().isEmpty() &&
                    !pedagang_name_field.getText().isEmpty()) {
                Alert alert = new Alert(
                        Alert.AlertType.CONFIRMATION,
                        String.format("Anda yakin ingin menghapus %s (%s)",
                                pedagang_name_field.getText(),
                                pedagang_id_field.getText()
                        )
                );
                Optional<ButtonType> result = alert.showAndWait();
                ButtonType button = result.orElse(ButtonType.CANCEL);

                if (button == ButtonType.OK) {
                    Repository.deletePedagangById(
                            pedagang_table.getSelectionModel().getSelectedItem().pedagang_id.getValue(),
                            new ResponseListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    refresh();
                                    clear();
                                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION, "Data berhasil dihapus");
                                    alert1.show();
                                }

                                @Override
                                public void onFailed(CustomException e) {
                                    Alert alert1 = new Alert(Alert.AlertType.ERROR, e.customMessage);
                                    alert1.show();
                                }
                            }
                    );
                }
            }
            return;
        }
//        if (state == HomeAdminState.Customer) {
//            return;
//        }
        if (state == HomeAdminState.Komputer) {
            if (!komputer_id_field.getText().isEmpty()) {
                Alert alert = new Alert(
                        Alert.AlertType.CONFIRMATION,
                        String.format("Anda yakin ingin menghapus %s",
                                komputer_id_field.getText()
                        )
                );
                Optional<ButtonType> result = alert.showAndWait();
                ButtonType button = result.orElse(ButtonType.CANCEL);

                if (button == ButtonType.OK) {
                    Repository.deleteKomputerById(
                            komputer_table.getSelectionModel().getSelectedItem().komputer_id().getValue(),
                            new ResponseListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    refresh();
                                    clear();
                                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION, "Data berhasil dihapus");
                                    alert1.show();
                                }

                                @Override
                                public void onFailed(CustomException e) {
                                    Alert alert1 = new Alert(Alert.AlertType.ERROR, e.customMessage);
                                    alert1.show();
                                }
                            }
                    );
                }
            }
            return;
        }
    }

    void search(String keyword) {
        if (state == HomeAdminState.Pegawai) {

        }
        if (state == HomeAdminState.Pedagang) {

        }
        if (state == HomeAdminState.Customer) {

        }
        if (state == HomeAdminState.Komputer) {

        }
    }

    void refresh() {
        if (state == HomeAdminState.Pegawai) {
            employeeModels.clear();
            Repository.getListOfEmployee(new ListOfEmployeeListener() {
                @Override
                public void onSuccess(ArrayList<EmployeeModel> listOfEmployee) {
                    employeeModels.addAll(listOfEmployee);
                }

                @Override
                public void onFailed(CustomException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, e.customMessage);
                    alert.show();
                }
            });
            return;
        }
        if (state == HomeAdminState.Pedagang) {
            pedagangModels.clear();
            Repository.getListOfPedagang(
                    new ListOfPedagangListener() {
                        @Override
                        public void onSuccess(ArrayList<PedagangModel> listOfPedagang) {
                            System.out.println(listOfPedagang);
                            pedagangModels.addAll(listOfPedagang);
                        }

                        @Override
                        public void onFailed(CustomException e) {
                            Alert alert = new Alert(Alert.AlertType.ERROR, e.customMessage);
                            alert.show();
                        }
                    }
            );
            return;
        }
        if (state == HomeAdminState.Customer) {
            customerModels.clear();
            Repository.getListOfCustomer(new ListOfCustomerListener() {
                @Override
                public void onSuccess(ArrayList<CustomerModel> listOfCustomer) {
                    customerModels.addAll(listOfCustomer);
                }

                @Override
                public void onFailed(CustomException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, e.customMessage);
                    alert.show();
                }
            });
            return;
        }
        if (state == HomeAdminState.Komputer) {
            komputerModels.clear();
            Repository.getListOfKomputer(
                    new ListOfKomputerListener() {
                        @Override
                        public void onSuccess(ArrayList<KomputerModel> listOfKomputer) {
                            komputerModels.addAll(listOfKomputer);
                        }

                        @Override
                        public void onFailed(CustomException e) {
                            Alert alert = new Alert(Alert.AlertType.ERROR, e.customMessage);
                            alert.show();
                        }
                    }
            );
            return;
        }
    }

    void changeTable() {
        if (state == HomeAdminState.Customer) {
            insertButton.setDisable(true);
            deleteButton.setDisable(true);
        } else {
            insertButton.setDisable(false);
            deleteButton.setDisable(false);
        }

        if (state == HomeAdminState.Pegawai) {
            if (employeeModels.isEmpty()) {
                refresh();
            }

            field_stackpane.getChildren().clear();
            field_stackpane.getChildren().add(container_field_pegawai);

            tabel_stackpane.getChildren().clear();
            tabel_stackpane.getChildren().add(employee_table);
            return;
        }
        if (state == HomeAdminState.Pedagang) {
            if (pedagangModels.isEmpty()) {
                refresh();
            }

            field_stackpane.getChildren().clear();
            field_stackpane.getChildren().add(container_field_pedagang);

            tabel_stackpane.getChildren().clear();
            tabel_stackpane.getChildren().add(pedagang_table);
            return;
        }
        if (state == HomeAdminState.Customer) {
            if (customerModels.isEmpty()) {
                refresh();
            }

            field_stackpane.getChildren().clear();
            field_stackpane.getChildren().add(container_field_customer);

            tabel_stackpane.getChildren().clear();
            tabel_stackpane.getChildren().add(customer_table);
            return;
        }
        if (state == HomeAdminState.Komputer) {
            if (komputerModels.isEmpty()) {
                refresh();
            }

            field_stackpane.getChildren().clear();
            field_stackpane.getChildren().add(container_field_komputer);

            tabel_stackpane.getChildren().clear();
            tabel_stackpane.getChildren().add(komputer_table);
        }
    }
}