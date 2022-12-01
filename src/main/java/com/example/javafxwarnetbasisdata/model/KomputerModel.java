package com.example.javafxwarnetbasisdata.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public record KomputerModel(
        SimpleStringProperty komputer_id,
        SimpleStringProperty kategori_id,
        SimpleDoubleProperty harga_perjam,
        SimpleStringProperty status
) { }
