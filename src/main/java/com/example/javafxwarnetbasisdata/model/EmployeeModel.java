package com.example.javafxwarnetbasisdata.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public record EmployeeModel(
        SimpleStringProperty id,
        SimpleStringProperty salaryAcc,
        SimpleIntegerProperty salary,
        SimpleStringProperty name,
        SimpleStringProperty phoneNumber,
        SimpleStringProperty address
) {
}
