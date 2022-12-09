package com.example.javafxwarnetbasisdata.util;

import com.example.javafxwarnetbasisdata.model.FoodModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TemporaryMemory {
    public static String savedCustomerId = "";
    public static String savedAdminId = "";
    public static String pickedComputerId = "";
    public static String pickerMakananId = "";

    public static ObservableList<FoodModel> keranjang = FXCollections.observableArrayList();
}
