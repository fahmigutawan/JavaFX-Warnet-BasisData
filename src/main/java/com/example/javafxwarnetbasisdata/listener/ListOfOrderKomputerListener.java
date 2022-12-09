package com.example.javafxwarnetbasisdata.listener;

import com.example.javafxwarnetbasisdata.model.OrderKomputerModel;
import com.example.javafxwarnetbasisdata.model.OrderModel;
import com.example.javafxwarnetbasisdata.util.CustomException;

import java.util.List;

public interface ListOfOrderKomputerListener {
    void onSuccess(List<OrderKomputerModel> orders);

    void onFailed(CustomException e);
}
