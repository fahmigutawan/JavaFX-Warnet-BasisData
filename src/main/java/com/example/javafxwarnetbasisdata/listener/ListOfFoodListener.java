package com.example.javafxwarnetbasisdata.listener;

import com.example.javafxwarnetbasisdata.model.FoodModel;
import com.example.javafxwarnetbasisdata.model.KomputerModel;
import com.example.javafxwarnetbasisdata.util.CustomException;

import java.util.List;

public interface ListOfFoodListener {
    void onSuccess(List<FoodModel> listOfFood);

    void onFailed(CustomException e);
}
