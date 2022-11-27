package com.example.javafxwarnetbasisdata.listener;

import com.example.javafxwarnetbasisdata.model.ComputerModel;
import com.example.javafxwarnetbasisdata.util.CustomException;

import java.util.ArrayList;

public interface AvailableComputerListener {
    void onSuccess(ArrayList<ComputerModel> availableComputers, ArrayList<ComputerModel> unavailableComputers);

    void onFailed(CustomException e);
}
