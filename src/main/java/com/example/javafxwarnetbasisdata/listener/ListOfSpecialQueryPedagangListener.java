package com.example.javafxwarnetbasisdata.listener;

import com.example.javafxwarnetbasisdata.model.SpecialQueryMakananModel;
import com.example.javafxwarnetbasisdata.model.SpecialQueryPedagangModel;
import com.example.javafxwarnetbasisdata.util.CustomException;

import java.util.List;

public interface ListOfSpecialQueryPedagangListener {
    void onSuccess(List<SpecialQueryPedagangModel> pedagangs);

    void onFailed(CustomException e);
}
