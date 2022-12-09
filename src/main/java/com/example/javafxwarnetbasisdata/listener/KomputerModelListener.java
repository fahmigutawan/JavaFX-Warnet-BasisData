package com.example.javafxwarnetbasisdata.listener;

import com.example.javafxwarnetbasisdata.model.KomputerModel;
import com.example.javafxwarnetbasisdata.util.CustomException;

import java.util.ArrayList;

public interface KomputerModelListener {
    void onSuccess(KomputerModel komputer);

    void onFailed(CustomException e);
}
