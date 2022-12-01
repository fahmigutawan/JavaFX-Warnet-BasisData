package com.example.javafxwarnetbasisdata.listener;

import com.example.javafxwarnetbasisdata.model.CustomerModel;
import com.example.javafxwarnetbasisdata.model.PedagangModel;
import com.example.javafxwarnetbasisdata.util.CustomException;

import java.util.ArrayList;

public interface ListOfCustomerListener {
    void onSuccess(ArrayList<CustomerModel> listOfCustomer);

    void onFailed(CustomException e);
}
