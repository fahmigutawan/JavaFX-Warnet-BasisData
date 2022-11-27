package com.example.javafxwarnetbasisdata.controller;

import com.example.javafxwarnetbasisdata.listener.ListOfEmployeeListener;
import com.example.javafxwarnetbasisdata.model.EmployeeModel;
import com.example.javafxwarnetbasisdata.repository.Repository;
import com.example.javafxwarnetbasisdata.util.CustomException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

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
        employee_id.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        employee_salary_acc.setCellValueFactory(cellData -> cellData.getValue().salaryAccProperty());
        employee_salary.setCellValueFactory(cellData -> cellData.getValue().salaryProperty().asObject());
        employee_name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        employee_phone_number.setCellValueFactory(cellData -> cellData.getValue().phoneNumberProperty());
        employee_address.setCellValueFactory(cellData -> cellData.getValue().addressProperty());

        employee_data.setItems(employeeModels);
    }
}
