package com.example.javafxwarnetbasisdata.listener;

import com.example.javafxwarnetbasisdata.model.OrderKomputerModel;
import com.example.javafxwarnetbasisdata.model.OrderMakananModel;
import com.example.javafxwarnetbasisdata.util.CustomException;

import java.util.List;

public interface ListOfOrderMakananListener {
    void onSuccess(List<OrderMakananModel> orders);

    void onFailed(CustomException e);
}
