package com.example.javafxwarnetbasisdata.listener;

import com.example.javafxwarnetbasisdata.model.MakananOfOrderMakananModel;
import com.example.javafxwarnetbasisdata.util.CustomException;

import java.util.List;

public interface ListOfMakananOfOrderMakananListener {
    void onSuccess(List<MakananOfOrderMakananModel> orders);

    void onFailed(CustomException e);
}
