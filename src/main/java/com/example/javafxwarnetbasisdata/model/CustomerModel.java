package com.example.javafxwarnetbasisdata.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class CustomerModel {
    public CustomerModel(SimpleStringProperty customer_id, SimpleStringProperty nama, SimpleStringProperty profile_pic, SimpleDoubleProperty balance_acc) {
        this.customer_id = customer_id;
        this.nama = nama;
        this.profile_pic = profile_pic;
        this.balance_acc = balance_acc;
    }

    public SimpleStringProperty customer_id;
    public SimpleStringProperty nama;
    public SimpleStringProperty profile_pic;
    public SimpleDoubleProperty balance_acc;
}
