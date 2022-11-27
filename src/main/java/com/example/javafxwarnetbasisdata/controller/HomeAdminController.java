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

    public TableColumn employee_id;
    public TableColumn employee_salary_acc;
    public TableColumn employee_salary;
    public TableColumn employee_name;
    public TableColumn employee_phone_number;
    public TableColumn employee_address;

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
        employee_id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        employee_salary_acc.setCellValueFactory(new PropertyValueFactory<>("SalaryAcc"));
        employee_salary.setCellValueFactory(new PropertyValueFactory<>("Salary"));
        employee_name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        employee_phone_number.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));
        employee_address.setCellValueFactory(new PropertyValueFactory<>("Address"));

        employee_data.setItems(employeeModels);
    }
}