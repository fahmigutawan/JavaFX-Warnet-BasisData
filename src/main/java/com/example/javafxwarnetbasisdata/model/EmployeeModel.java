package com.example.javafxwarnetbasisdata.model;

public record EmployeeModel(
        String id,
        String salaryAcc,
        int salary,
        String name,
        String noTelp,
        String street,
        String province,
        String postCode
) {
}
