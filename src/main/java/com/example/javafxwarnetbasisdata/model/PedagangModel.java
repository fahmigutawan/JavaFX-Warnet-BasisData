package com.example.javafxwarnetbasisdata.model;

import javafx.beans.property.SimpleStringProperty;

public class PedagangModel {
    public SimpleStringProperty pedagang_id;
    public SimpleStringProperty stand_nama;
    public SimpleStringProperty stand_number;

    public PedagangModel(SimpleStringProperty pedagang_id, SimpleStringProperty stand_nama, SimpleStringProperty stand_number) {
        this.pedagang_id = pedagang_id;
        this.stand_nama = stand_nama;
        this.stand_number = stand_number;
    }
}
