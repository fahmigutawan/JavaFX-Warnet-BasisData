package com.example.javafxwarnetbasisdata.listener;

import com.example.javafxwarnetbasisdata.util.CustomException;

import java.io.IOException;
import java.sql.SQLException;

public interface ResponseListener {
    void onSuccess(Object o);
    void onFailed(CustomException e);
}
