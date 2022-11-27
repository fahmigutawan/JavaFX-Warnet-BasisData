package com.example.javafxwarnetbasisdata.listener;

import com.example.javafxwarnetbasisdata.model.EmployeeModel;
import com.example.javafxwarnetbasisdata.util.CustomException;

import java.util.ArrayList;

public interface ListOfEmployeeListener {
    void onSuccess(ArrayList<EmployeeModel> listOfEmployee);

    void onFailed(CustomException e);
}
