package com.example.javafxwarnetbasisdata.util;

import java.sql.SQLException;

public class CustomException extends SQLException {

    public CustomException(String message){
        customMessage = message;
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }

    public String customMessage;
}
