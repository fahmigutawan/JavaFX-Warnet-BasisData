package com.example.javafxwarnetbasisdata.util;

import java.sql.SQLException;

public interface ResponseListener {
    void onSuccess(Object o);
    void onFailed(SQLException e);
}
