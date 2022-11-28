package com.example.javafxwarnetbasisdata.listener;

import com.example.javafxwarnetbasisdata.model.EmployeeModel;
import com.example.javafxwarnetbasisdata.util.CustomException;

import java.util.ArrayList;

public interface EmployeeModelListener {
    void onSuccess(EmployeeModel employee);

    void onFailed(CustomException e);
}
