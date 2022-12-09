package com.example.javafxwarnetbasisdata.listener;

import com.example.javafxwarnetbasisdata.model.MakananOfOrderMakananModel;
import com.example.javafxwarnetbasisdata.model.SpecialQueryMakananModel;
import com.example.javafxwarnetbasisdata.util.CustomException;

import java.util.List;

public interface ListOfSpecialQueryMakananListener {
    void onSuccess(List<SpecialQueryMakananModel> makanans);

    void onFailed(CustomException e);
}
