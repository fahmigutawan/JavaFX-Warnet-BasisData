package com.example.javafxwarnetbasisdata.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class EmployeeModel {
    public SimpleStringProperty id;
    public SimpleStringProperty salaryAcc;
    public SimpleIntegerProperty salary;
    public SimpleStringProperty name;
    public SimpleStringProperty phoneNumber;
    public SimpleStringProperty address;
    public String jalan;
    public String provinsi;
    public String kode_pos;

    public EmployeeModel(
            SimpleStringProperty id,
            SimpleStringProperty salaryAcc,
            SimpleIntegerProperty salary,
            SimpleStringProperty name,
            SimpleStringProperty phoneNumber,
            SimpleStringProperty address,
            String jalan,
            String provinsi,
            String kode_pos
    ) {
        this.id = id;
        this.salaryAcc = salaryAcc;
        this.salary = salary;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.jalan = jalan;
        this.provinsi = provinsi;
        this.kode_pos = kode_pos;
    }
}
