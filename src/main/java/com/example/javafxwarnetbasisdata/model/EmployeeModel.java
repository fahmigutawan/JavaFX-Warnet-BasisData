package com.example.javafxwarnetbasisdata.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class EmployeeModel {
    public SimpleStringProperty pegawai_id;
    public SimpleStringProperty profile_pic;
    public SimpleStringProperty salary_acc;
    public SimpleDoubleProperty salary;
    public SimpleStringProperty nama;
    public SimpleStringProperty no_telp;
    public SimpleStringProperty jalan;
    public SimpleStringProperty kode_pos;
    public SimpleStringProperty kota;
    public SimpleStringProperty provinsi;

    public EmployeeModel(
            SimpleStringProperty pegawai_id,
            SimpleStringProperty profile_pic,
            SimpleStringProperty salary_acc,
            SimpleDoubleProperty salary,
            SimpleStringProperty nama,
            SimpleStringProperty no_telp,
            SimpleStringProperty jalan,
            SimpleStringProperty kode_pos,
            SimpleStringProperty kota,
            SimpleStringProperty provinsi
    ) {
        this.pegawai_id = pegawai_id;
        this.profile_pic = profile_pic;
        this.salary_acc = salary_acc;
        this.salary = salary;
        this.nama = nama;
        this.no_telp = no_telp;
        this.jalan = jalan;
        this.kode_pos = kode_pos;
        this.kota = kota;
        this.provinsi = provinsi;
    }
}
