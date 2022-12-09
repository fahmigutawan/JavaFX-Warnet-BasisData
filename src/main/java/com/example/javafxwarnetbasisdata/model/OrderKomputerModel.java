package com.example.javafxwarnetbasisdata.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public record OrderKomputerModel(
        SimpleStringProperty order_id,
        SimpleStringProperty customer_id,
        SimpleStringProperty komputer_id,
        SimpleDoubleProperty harga,
        SimpleStringProperty status
) { }
