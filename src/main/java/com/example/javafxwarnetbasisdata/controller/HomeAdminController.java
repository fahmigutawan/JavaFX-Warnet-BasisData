package com.example.javafxwarnetbasisdata.controller;

import com.example.javafxwarnetbasisdata.listener.EmployeeModelListener;
import com.example.javafxwarnetbasisdata.listener.ListOfEmployeeListener;
import com.example.javafxwarnetbasisdata.model.EmployeeModel;
import com.example.javafxwarnetbasisdata.repository.Repository;
import com.example.javafxwarnetbasisdata.util.CustomException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HomeAdminController implements Initializable {
    public TableView<EmployeeModel> employee_data;

    public TableColumn<EmployeeModel, String> employee_id;
    public TableColumn<EmployeeModel, String> employee_salary_acc;
    public TableColumn<EmployeeModel, Integer> employee_salary;
    public TableColumn<EmployeeModel, String> employee_name;
    public TableColumn<EmployeeModel, String> employee_phone_number;
    public TableColumn<EmployeeModel, String> employee_address;
    public TextField idField;
    public TextField salaryAccField;
    public TextField salaryField;
    public TextField nameField;
    public TextField no_telpField;
    public TextField jalanField;
    public TextField kotaField;
    public TextField provinsiField;
    public TextField kodePosField;
    public Button insertButton;
    public Button updateButton;
    public Button deleteButton;

    private ObservableList<EmployeeModel> employeeModels = FXCollections.observableArrayList( );
    public void init(){
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
        employee_id.setCellValueFactory(cellData -> cellData.getValue().id);
        employee_salary_acc.setCellValueFactory(cellData -> cellData.getValue().salaryAcc);
        employee_salary.setCellValueFactory(cellData -> cellData.getValue().salary.asObject());
        employee_name.setCellValueFactory(cellData -> cellData.getValue().name);
        employee_phone_number.setCellValueFactory(cellData -> cellData.getValue().phoneNumber);
        employee_address.setCellValueFactory(cellData -> cellData.getValue().address);

        employee_data.setItems(employeeModels);
        employee_data.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String id = employee_data.getSelectionModel().getSelectedItem().id.get();
                Repository.getEmployeById(
                        id,
                        new EmployeeModelListener() {
                            @Override
                            public void onSuccess(EmployeeModel employee) {
                                idField.setText(employee.id.get());
                                salaryAccField.setText(employee.salaryAcc.get());
                                salaryField.setText(String.valueOf(employee.salary.get()));
                                nameField.setText(employee.name.get());
                                no_telpField.setText(employee.phoneNumber.get());
                                jalanField.setText(employee.jalan);
                                kotaField.setText("Belum Ada");
                                provinsiField.setText(employee.provinsi);
                                kodePosField.setText(employee.kode_pos);
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
}
