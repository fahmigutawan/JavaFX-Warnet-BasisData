package com.example.javafxwarnetbasisdata.listener;

import com.example.javafxwarnetbasisdata.model.KomputerModel;
import com.example.javafxwarnetbasisdata.model.PedagangModel;
import com.example.javafxwarnetbasisdata.util.CustomException;

import java.util.ArrayList;

public interface ListOfKomputerListener {
    void onSuccess(ArrayList<KomputerModel> listOfKomputer);

    void onFailed(CustomException e);
}
