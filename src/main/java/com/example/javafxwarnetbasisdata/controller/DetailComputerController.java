package com.example.javafxwarnetbasisdata.controller;

import com.example.javafxwarnetbasisdata.util.TemporaryMemory;
import javafx.scene.control.Label;

public class DetailComputerController {
    public Label test_label;

    public void init(){
        test_label.setText("SELECTED COMPUTER ID -> " + TemporaryMemory.selectedComputerId);
    }
}
