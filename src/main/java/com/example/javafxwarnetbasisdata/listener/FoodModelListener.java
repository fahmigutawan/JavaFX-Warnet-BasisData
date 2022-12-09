package com.example.javafxwarnetbasisdata.listener;

import com.example.javafxwarnetbasisdata.model.FoodModel;
import com.example.javafxwarnetbasisdata.model.KomputerModel;
import com.example.javafxwarnetbasisdata.util.CustomException;

public interface FoodModelListener {
    void onSuccess(FoodModel food);

    void onFailed(CustomException e);
}
