package com.example.javafxwarnetbasisdata.listener;

import com.example.javafxwarnetbasisdata.model.UserModel;
import com.example.javafxwarnetbasisdata.util.CustomException;

public interface UserModelListener {
    void onSuccess(UserModel o);
    void onFailed(CustomException e);
}
