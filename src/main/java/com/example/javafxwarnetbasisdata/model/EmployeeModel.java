package com.example.javafxwarnetbasisdata.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class EmployeeModel {
    private SimpleStringProperty id;

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getSalaryAcc() {
        return salaryAcc.get();
    }

    public SimpleStringProperty salaryAccProperty() {
        return salaryAcc;
    }

    public void setSalaryAcc(String salaryAcc) {
        this.salaryAcc.set(salaryAcc);
    }

    public int getSalary() {
        return salary.get();
    }

    public SimpleIntegerProperty salaryProperty() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary.set(salary);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public SimpleStringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public String getAddress() {
        return address.get();
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    private SimpleStringProperty salaryAcc;
    private SimpleIntegerProperty salary;
    private SimpleStringProperty name;
    private SimpleStringProperty phoneNumber;
    private SimpleStringProperty address;

    public EmployeeModel(
            SimpleStringProperty id,
            SimpleStringProperty salaryAcc,
            SimpleIntegerProperty salary,
            SimpleStringProperty name,
            SimpleStringProperty phoneNumber,
            SimpleStringProperty address
    ) {
        this.id = id;
        this.salaryAcc = salaryAcc;
        this.salary = salary;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}
