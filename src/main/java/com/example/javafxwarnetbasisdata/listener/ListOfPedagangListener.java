package com.example.javafxwarnetbasisdata.listener;

import com.example.javafxwarnetbasisdata.model.EmployeeModel;
import com.example.javafxwarnetbasisdata.model.PedagangModel;
import com.example.javafxwarnetbasisdata.util.CustomException;

import java.util.ArrayList;

public interface ListOfPedagangListener {
    void onSuccess(ArrayList<PedagangModel> listOfPedagang);

    void onFailed(CustomException e);
}
