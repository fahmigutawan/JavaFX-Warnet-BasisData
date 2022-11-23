package com.example.javafxwarnetbasisdata.repository;

import com.example.javafxwarnetbasisdata.util.CustomException;
import com.example.javafxwarnetbasisdata.util.DbUrl;
import com.example.javafxwarnetbasisdata.util.ResponseListener;

import java.sql.*;
import java.util.ArrayList;

public class Repository {
    private static Connection connection() throws SQLException {
        return DriverManager.getConnection(DbUrl.dbUrl);
    }

    public static void login(
            String adminUsername,
            String adminPassword,
            ResponseListener listener
    ) {
        try (Connection conn = connection()) {
            String command = "select * from admin where username=(?)";
            PreparedStatement statement = conn.prepareStatement(command);

            statement.setString(0, adminUsername);

            ResultSet result = statement.executeQuery();

            if (result.getString("username").contains(adminUsername)) {
                if (result.getString("password").equals(adminPassword)) {
                    // Success
                    listener.onSuccess(null);
                } else {
                    // Wrong password
                    listener.onFailed(new CustomException("Password salah. Coba lagi nanti"));
                }
            } else {
                // No user found
                listener.onFailed(new CustomException("User tidak ditemukan. Silahkan registrasi terlebih dahulu"));
            }
        } catch (SQLException e) {
            listener.onFailed(e);
        }
    }
}
