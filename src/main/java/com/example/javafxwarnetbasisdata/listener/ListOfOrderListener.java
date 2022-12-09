package com.example.javafxwarnetbasisdata.listener;

import com.example.javafxwarnetbasisdata.model.OrderModel;
import com.example.javafxwarnetbasisdata.util.CustomException;

import java.util.List;

public interface ListOfOrderListener {
    void onSuccess(List<OrderModel> orders);

    void onFailed(CustomException e);
}
