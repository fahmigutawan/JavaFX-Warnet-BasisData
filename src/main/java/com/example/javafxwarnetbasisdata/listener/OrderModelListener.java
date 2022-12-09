package com.example.javafxwarnetbasisdata.listener;

import com.example.javafxwarnetbasisdata.model.KomputerModel;
import com.example.javafxwarnetbasisdata.model.OrderModel;
import com.example.javafxwarnetbasisdata.util.CustomException;

public interface OrderModelListener {
    void onSuccess(OrderModel order);

    void onFailed(CustomException e);
}
