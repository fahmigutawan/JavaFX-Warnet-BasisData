package com.example.javafxwarnetbasisdata.repository;

import com.example.javafxwarnetbasisdata.util.CustomException;
import com.example.javafxwarnetbasisdata.util.DbUrl;
import com.example.javafxwarnetbasisdata.util.ResponseListener;
import com.example.javafxwarnetbasisdata.util.TemporaryMemory;

import java.sql.*;

public class Repository {
    private static Connection connection() throws SQLException {
        return DriverManager.getConnection(DbUrl.dbUrl);
    }

    // Login to admin account
    public static void loginAdmin(
            String adminUsername,
            String adminPassword,
            ResponseListener listener
    ) {
        try (Connection conn = connection()) {
            // Get the count
            String usernameCount = "SELECT COUNT(*) as count " +
                    "FROM admin " +
                    "WHERE username=?";
            PreparedStatement usernameCountStatement = conn.prepareStatement(usernameCount);
            usernameCountStatement.setString(1, adminUsername);
            ResultSet resultCount = usernameCountStatement.executeQuery();

            // Get the data
            String command = "SELECT * " +
                    "FROM admin " +
                    "WHERE username=?";
            PreparedStatement statement = conn.prepareStatement(command);
            statement.setString(1, adminUsername);
            ResultSet result = statement.executeQuery();

            // Operation
            if (resultCount.next() && result.next()) {
                if (resultCount.getInt("count") > 0) {
                    if (result.getString("password").equals(adminPassword)) {
                        // Success
                        TemporaryMemory.savedAdminId = result.getString("admin_id");
                        listener.onSuccess(null);
                    } else {
                        // Wrong password
                        listener.onFailed(new CustomException("Password salah. Coba lagi nanti"));
                    }
                } else {
                    // No user found
                    listener.onFailed(new CustomException("User tidak ditemukan. Silahkan registrasi terlebih dahulu"));
                }
            } else {
                // No user found
                listener.onFailed(new CustomException("User tidak ditemukan. Silahkan registrasi terlebih dahulu"));
            }
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // Login to user account
    public static void loginUser(
            String username,
            String password,
            ResponseListener listener
    ){
        try (Connection conn = connection()) {
            // Get the count
            String usernameCount = "SELECT COUNT(*) as count " +
                    "FROM customer " +
                    "WHERE username=?";
            PreparedStatement usernameCountStatement = conn.prepareStatement(usernameCount);
            usernameCountStatement.setString(1, username);
            ResultSet resultCount = usernameCountStatement.executeQuery();

            // Get the data
            String command = "SELECT * " +
                    "FROM customer " +
                    "WHERE username=?";
            PreparedStatement statement = conn.prepareStatement(command);
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();

            // Operation
            if (resultCount.next() && result.next()) {
                if (resultCount.getInt("count") > 0) {
                    if (result.getString("password").equals(password)) {
                        // Success
                        TemporaryMemory.savedUserId = result.getString("user_id");
                        listener.onSuccess(null);
                    } else {
                        // Wrong password
                        listener.onFailed(new CustomException("Password salah. Coba lagi nanti"));
                    }
                } else {
                    // No user found
                    listener.onFailed(new CustomException("User tidak ditemukan. Silahkan registrasi terlebih dahulu"));
                }
            } else {
                // No user found
                listener.onFailed(new CustomException("User tidak ditemukan. Silahkan registrasi terlebih dahulu"));
            }
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // Register new account for user
    public static void registerUser(
            String username,
            String password,
            ResponseListener listener
    ){
        try(Connection conn = connection()){
            // Get user count
            String userCount = "SELECT COUNT(*) as count " +
                    "FROM customer";
            PreparedStatement userCountStatement = conn.prepareStatement(userCount);
            ResultSet userCountResult = userCountStatement.executeQuery();
            int userCountInt = 0;
            if(userCountResult.next()){
                userCountInt = userCountResult.getInt("count");
            }

            // Get user with same username, to check if username has been used
            String userWithUsernameCount = "SELECT COUNT(*) as count " +
                    "FROM customer " +
                    "WHERE username=?";
            PreparedStatement userWithUsernameCountStatement = conn.prepareStatement(userWithUsernameCount);
            userWithUsernameCountStatement.setString(1,username);
            ResultSet userWithUsernameCountResult = userWithUsernameCountStatement.executeQuery();
            if(userWithUsernameCountResult.next()){
                if(userWithUsernameCountResult.getInt("count") > 0){
                    listener.onFailed(new CustomException("Username telah digunakan. Buat username lain"));
                    return;
                }
            }

            // If all checked, then register below here
            String registerUser = "INSERT INTO customer (user_id, username, password) " +
                    "VALUES(?, ?, ?)";
            PreparedStatement registerUserStatement = conn.prepareStatement(registerUser);
            registerUserStatement.setString(1, String.format("user-%d", userCountInt));
            registerUserStatement.setString(2, username);
            registerUserStatement.setString(3, password);
            registerUserStatement.executeUpdate();

            TemporaryMemory.savedUserId = String.format("user-%d", userCountInt);
            listener.onSuccess(null);
        }catch (SQLException e){
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }
}
