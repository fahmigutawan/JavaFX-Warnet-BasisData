package com.example.javafxwarnetbasisdata.repository;

import com.example.javafxwarnetbasisdata.listener.*;
import com.example.javafxwarnetbasisdata.model.*;
import com.example.javafxwarnetbasisdata.util.CustomException;
import com.example.javafxwarnetbasisdata.util.DbUrl;
import com.example.javafxwarnetbasisdata.util.TemporaryMemory;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

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
                        TemporaryMemory.savedAdminId = result.getString("pegawai_id");
                        try {
                            listener.onSuccess(null);
                        } catch (Exception e) {
                            listener.onFailed(new CustomException("Terjadi kesalahan. Coba lagi nanti"));
                        }
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
    ) {
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
                        TemporaryMemory.savedCustomerId = result.getString("customer_id");
                        try {
                            listener.onSuccess(null);
                        } catch (Exception e) {
                            listener.onFailed(new CustomException("Terjadi kesalahan. Coba lagi nanti"));
                        }
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
            String nama,
            String password,
            ResponseListener listener
    ) {
        try (Connection conn = connection()) {
            // Get user with same name, to check if name has been used
            String userWithUsernameCount = "SELECT COUNT(*) as count " +
                    "FROM customer " +
                    "WHERE username=?";
            PreparedStatement userWithUsernameCountStatement = conn.prepareStatement(userWithUsernameCount);
            userWithUsernameCountStatement.setString(1, username);
            ResultSet userWithUsernameCountResult = userWithUsernameCountStatement.executeQuery();
            if (userWithUsernameCountResult.next()) {
                if (userWithUsernameCountResult.getInt("count") > 0) {
                    listener.onFailed(new CustomException("Username telah digunakan. Buat name lain"));
                    return;
                }
            }

            // If all checked, then register below here
            String registerUser = "begin transaction " +
                    " declare @last_id  numeric(18,0) " +
                    " set @last_id = IDENT_CURRENT('dbo.customer') " +
                    " " +
                    " insert into customer_information ([customer_id], [nama], [profile_pic], [balance_acc]) " +
                    " values( " +
                    "  ('USER'+cast((@last_id + 1) as varchar(18))), " +
                    "  ?, " +
                    "  'https://i.pinimg.com/736x/fe/91/43/fe9143350d8d892b41d2344dbf086cbd.jpg', " +
                    "  0 " +
                    " ) " +
                    " INSERT INTO customer (customer_id, name, password) " +
                    " values(('USER'+cast((@last_id + 1) as varchar(18))), ?,?) " +
                    "commit transaction";
            PreparedStatement registerUserStatement = conn.prepareStatement(registerUser);
            registerUserStatement.setString(1, nama);
            registerUserStatement.setString(2, username);
            registerUserStatement.setString(3, password);
            registerUserStatement.executeUpdate();
            try {
                listener.onSuccess(null);
            } catch (Exception e) {
                listener.onFailed(new CustomException("Terjadi kesalahan. Coba lagi nanti"));
            }
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // Get user account by user_id
    public static void getUserByUserId(
            String user_id,
            UserModelListener listener
    ) {
        try (Connection conn = connection()) {
            String command = "SELECT customer_id, nama " +
                    "FROM customer_information " +
                    "WHERE customer_id=?";
            PreparedStatement statement = conn.prepareStatement(command);
            statement.setString(1, user_id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                listener.onSuccess(
                        new UserModel(
                                result.getString("customer_id"),
                                result.getString("nama")
                        )
                );
            } else {
                listener.onFailed(new CustomException("Error saat mengambil data user"));
            }
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // Get employee
    public static void getListOfEmployee(
            ListOfEmployeeListener listener
    ) {
        try (Connection conn = connection()) {
            String employeeListQuery = "select * from pegawai_information";
            Statement employeeStatement = conn.createStatement();
            ResultSet employeeResult = employeeStatement.executeQuery(employeeListQuery);
            ArrayList<EmployeeModel> listOfEmployee = new ArrayList<>();
            while (employeeResult.next()) {
                listOfEmployee.add(
                        new EmployeeModel(
                                new SimpleStringProperty(employeeResult.getString("pegawai_id")),
                                new SimpleStringProperty(employeeResult.getString("profile_pic")),
                                new SimpleStringProperty(""),
                                new SimpleDoubleProperty(employeeResult.getDouble("salary")),
                                new SimpleStringProperty(employeeResult.getString("nama")),
                                new SimpleStringProperty(employeeResult.getString("no_telp")),
                                new SimpleStringProperty(employeeResult.getString("jalan")),
                                new SimpleStringProperty(employeeResult.getString("kode_pos")),
                                new SimpleStringProperty(employeeResult.getString("kota")),
                                new SimpleStringProperty(employeeResult.getString("provinsi"))
                        )
                );
            }
            listener.onSuccess(listOfEmployee);
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // Insert new pegawai
    public static void insertNewPegawai(
            String nama,
            Double salary,
            String notelp,
            String jalan,
            String kodepos,
            String kota,
            String provinsi,
            ResponseListener listener
    ) {
        try (Connection conn = connection()) {
            String query = "begin transaction " +
                    "declare @last_id_pegawaiinfo  numeric(18,0) " +
                    "set @last_id_pegawaiinfo = IDENT_CURRENT('dbo.pegawai_information') " +
                    "insert into pegawai_information ([pegawai_id], [profile_pic], [salary_acc_number], [salary], [nama], [no_telp], [jalan], [kode_pos], [kota], [provinsi]) " +
                    "VALUES ( " +
                    "('PGW'+cast((@last_id_pegawaiinfo + 1) as varchar(18))), " +
                    "'https://www.oberlo.com/media/1603970279-pexels-photo-3.jpg?fit=max&fm=jpg&w=1824',  " +
                    "NULL, " +
                    "?, " +
                    "?,  " +
                    "?,  " +
                    "?,  " +
                    "?,  " +
                    "?,  " +
                    "?) " +
                    "commit transaction";

            PreparedStatement statement = conn.prepareStatement(query);
            statement.setDouble(1, salary);
            statement.setString(2, nama);
            statement.setString(3, notelp);
            statement.setString(4, jalan);
            statement.setString(5, kodepos);
            statement.setString(6, kota);
            statement.setString(7, provinsi);
            statement.executeUpdate();

            listener.onSuccess(null);
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // Update pegawai
    public static void updatePegawai(
            String pegawai_id,
            String nama,
            Double salary,
            String notelp,
            String jalan,
            String kodepos,
            String kota,
            String provinsi,
            ResponseListener listener
    ) {
        try (Connection conn = connection()) {
            String query = "update pegawai_information " +
                    "set  " +
                    "salary = ?, " +
                    "nama = ?, " +
                    "no_telp = ?, " +
                    "jalan = ?, " +
                    "kode_pos = ?, " +
                    "kota = ?, " +
                    "provinsi = ? " +
                    "where pegawai_id  = ?";

            PreparedStatement statement = conn.prepareStatement(query);
            statement.setDouble(1, salary);
            statement.setString(2, nama);
            statement.setString(3, notelp);
            statement.setString(4, jalan);
            statement.setString(5, kodepos);
            statement.setString(6, kota);
            statement.setString(7, provinsi);
            statement.setString(8, pegawai_id);
            statement.executeUpdate();

            listener.onSuccess(null);
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // Delete pegawai by pegawai ID
    public static void deletePegawaiById(
            String pegawai_id,
            ResponseListener listener
    ) {
        try (Connection conn = connection()) {
            String query = "delete from " +
                    "pegawai_information " +
                    "where pegawai_id=?";

            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, pegawai_id);
            statement.executeUpdate();

            listener.onSuccess(null);
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // Search pegawai
    public static void searchPegawai(
            String keyword,
            ListOfEmployeeListener listener
    ) {
        ArrayList<EmployeeModel> result = new ArrayList<>();
        try (Connection conn = connection()) {
            String query = "begin transaction " +
                    "declare @search_pegawai varchar(128) " +
                    "set @search_pegawai = ? " +
                    "select * " +
                    "from pegawai_information " +
                    "where " +
                    "pegawai_id like '%'+@search_pegawai+'%' or " +
                    "salary_acc_number like '%'+@search_pegawai+'%' or " +
                    "salary like '%'+@search_pegawai+'%' or " +
                    "nama like '%'+@search_pegawai+'%' or " +
                    "no_telp like '%'+@search_pegawai+'%' or " +
                    "jalan like '%'+@search_pegawai+'%' or " +
                    "kode_pos like '%'+@search_pegawai+'%' or " +
                    "kota like '%'+@search_pegawai+'%' or " +
                    "provinsi like '%'+@search_pegawai+'%' " +
                    "commit transaction";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, keyword);
            ResultSet res = statement.executeQuery();

            while(res.next()){
                result.add(
                        new EmployeeModel(
                                new SimpleStringProperty(res.getString("pegawai_id")),
                                new SimpleStringProperty(res.getString("profile_pic")),
                                new SimpleStringProperty(""),
                                new SimpleDoubleProperty(res.getDouble("salary")),
                                new SimpleStringProperty(res.getString("nama")),
                                new SimpleStringProperty(res.getString("no_telp")),
                                new SimpleStringProperty(res.getString("jalan")),
                                new SimpleStringProperty(res.getString("kode_pos")),
                                new SimpleStringProperty(res.getString("kota")),
                                new SimpleStringProperty(res.getString("provinsi"))
                        )
                );
            }
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // Get list of pedagang
    public static void getListOfPedagang(
            ListOfPedagangListener listener
    ) {
        ArrayList<PedagangModel> result = new ArrayList<>();

        try (Connection conn = connection()) {
            String query = "select * " +
                    "from pedagang_information";
            Statement statement = conn.createStatement();
            ResultSet res = statement.executeQuery(query);

            while (res.next()) {
                result.add(
                        new PedagangModel(
                                new SimpleStringProperty(res.getString("pedagang_id")),
                                new SimpleStringProperty(res.getString("stand_name")),
                                new SimpleStringProperty(res.getString("stand_number"))
                        )
                );
            }

            listener.onSuccess(result);

        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // Search pedagang
    public static void searchPedagang(
            String keyword,
            ListOfPedagangListener listener
    ){
        ArrayList<PedagangModel> result = new ArrayList<>();

        try(Connection conn = connection()){
            String query = "begin transaction " +
                    "declare @search_pedagang varchar(128) " +
                    "set @search_pedagang = ? " +
                    "select * from  " +
                    "pedagang_information " +
                    "where " +
                    "pedagang_id like '%'+@search_pedagang+'%' or " +
                    "stand_name like '%'+@search_pedagang+'%' " +
                    "commit transaction";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, keyword);
            ResultSet res = statement.executeQuery();

            while(res.next()){
                result.add(
                        new PedagangModel(
                                new SimpleStringProperty(res.getString("pedagang_id")),
                                new SimpleStringProperty(res.getString("stand_name")),
                                new SimpleStringProperty(res.getString("stand_number"))
                        )
                );
            }

            listener.onSuccess(result);
        }catch (SQLException e){
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // insert new pedagang
    public static void insertNewPedagang(
            String nama_stand,
            String nomor_stand,
            ResponseListener listener
    ) {
        try (Connection conn = connection()) {
            // Check if stand number has been used
            String checkQuery = "select count(*) as count " +
                    "from pedagang_information " +
                    "where stand_number=?";
            PreparedStatement checkStatement = conn.prepareStatement(checkQuery);
            checkStatement.setString(1, nomor_stand);
            ResultSet checkRes = checkStatement.executeQuery();

            if (checkRes.next()) {
                if (checkRes.getInt("count") > 0) {
                    listener.onFailed(new CustomException("Nomor stand telah digunakan, pilih yang lain"));
                    return;
                }
            }

            String query = "begin transaction " +
                    "declare @last_id_pedaganginfo  numeric(18,0) " +
                    "set @last_id_pedaganginfo = IDENT_CURRENT('dbo.pedagang_information') " +
                    "insert into pedagang_information ([pedagang_id], [stand_name], [stand_number]) " +
                    "values( " +
                    "('PDG'+cast((@last_id_pedaganginfo + 1) as varchar(18))), " +
                    "?, " +
                    "? " +
                    ") " +
                    "commit transaction";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, nama_stand);
            statement.setString(2, nomor_stand);
            statement.executeUpdate();

            listener.onSuccess(null);
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // update pedagang
    public static void updatePedagangById(
            String pedagang_id,
            String nama_stand,
            String nomor_stand,
            ResponseListener listener
    ) {
        try (Connection conn = connection()) {
            // Check if stand number has been used
            String standQuery = "select stand_number " +
                    "from pedagang_information " +
                    "where pedagang_id=?";
            PreparedStatement standStatement = conn.prepareStatement(standQuery);
            standStatement.setString(1, pedagang_id);
            ResultSet standRes = standStatement.executeQuery();

            String checkQuery = "select count(*) as count " +
                    "from pedagang_information " +
                    "where stand_number=?";
            PreparedStatement checkStatement = conn.prepareStatement(checkQuery);
            checkStatement.setString(1, nomor_stand);
            ResultSet checkRes = checkStatement.executeQuery();

            if (checkRes.next()) {
                if (checkRes.getInt("count") > 0) {
                    if (standRes.next()) {
                        if (!standRes.getString("stand_number").equals(nomor_stand)) {
                            listener.onFailed(new CustomException("Nomor stand telah digunakan, pilih yang lain"));
                            return;
                        }
                    }
                }
            }

            String query = "update pedagang_information " +
                    "set stand_name=?," +
                    "stand_number=? " +
                    "where pedagang_id=?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, nama_stand);
            statement.setString(2, nomor_stand);
            statement.setString(3, pedagang_id);
            statement.executeUpdate();

            listener.onSuccess(null);
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // delete pedagang by id
    public static void deletePedagangById(
            String pedagang_id,
            ResponseListener listener
    ) {
        try (Connection conn = connection()) {
            String query = "delete from pedagang_information " +
                    "where pedagang_id=?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, pedagang_id);
            statement.executeUpdate();

            listener.onSuccess(null);
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // Get list of customer
    public static void getListOfCustomer(
            ListOfCustomerListener listener
    ) {
        ArrayList<CustomerModel> result = new ArrayList<>();

        try (Connection conn = connection()) {
            String query = "select * " +
                    "from customer_information";
            Statement statement = conn.createStatement();
            ResultSet res = statement.executeQuery(query);

            while (res.next()) {
                result.add(
                        new CustomerModel(
                                new SimpleStringProperty(res.getString("customer_id")),
                                new SimpleStringProperty(res.getString("nama")),
                                new SimpleStringProperty(res.getString("profile_pic")),
                                new SimpleDoubleProperty(res.getDouble("balance_acc"))
                        )
                );
            }

            listener.onSuccess(result);
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // Search customer
    public static void searchCustomer(
            String keyword,
            ListOfCustomerListener listener
    ){
        ArrayList<CustomerModel> result = new ArrayList<>();

        try (Connection conn = connection()) {
            String query = "begin transaction " +
                    "declare @search_customer varchar(128) " +
                    "set @search_customer = ? " +
                    "select * from  " +
                    "customer_information " +
                    "where " +
                    "customer_id like '%'+@search_customer+'%' or " +
                    "nama like '%'+@search_customer+'%' " +
                    "commit transaction";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, keyword);
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                result.add(
                        new CustomerModel(
                                new SimpleStringProperty(res.getString("customer_id")),
                                new SimpleStringProperty(res.getString("nama")),
                                new SimpleStringProperty(res.getString("profile_pic")),
                                new SimpleDoubleProperty(res.getDouble("balance_acc"))
                        )
                );
            }

            listener.onSuccess(result);
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // Update customer by id
    public static void updateCustomerById(
            String customer_id,
            String nama,
            String profile_pic,
            ResponseListener listener
    ) {
        try (Connection conn = connection()) {
            String query = "update customer_information " +
                    "set nama=?," +
                    "profile_pic=? " +
                    "where customer_id=?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, nama);
            statement.setString(2, profile_pic);
            statement.setString(3, customer_id);
            statement.executeUpdate();

            listener.onSuccess(null);
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // Get list of komputer
    public static void getListOfKomputer(
            ListOfKomputerListener listener
    ) {
        ArrayList<KomputerModel> result = new ArrayList<>();

        try (Connection conn = connection()) {
            String query = "select * " +
                    "from komputer";
            Statement statement = conn.createStatement();
            ResultSet res = statement.executeQuery(query);

            while (res.next()) {
                result.add(
                        new KomputerModel(
                                new SimpleStringProperty(res.getString("komputer_id")),
                                new SimpleStringProperty(res.getString("kategori_id")),
                                new SimpleDoubleProperty(res.getDouble("harga_perjam")),
                                new SimpleStringProperty(res.getString("status"))
                        )
                );
            }

            listener.onSuccess(result);
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // search komputer
    public static void searchKomputer(
            String keyword,
            ListOfKomputerListener listener
    ){
        ArrayList<KomputerModel> result = new ArrayList<>();

        try (Connection conn = connection()) {
            String query = "begin transaction " +
                    "declare @search_komputer varchar(128) " +
                    "set @search_komputer = ? " +
                    "select * from komputer " +
                    "where " +
                    "status like '%'+@search_komputer+'%' or " +
                    "komputer_id like '%'+@search_komputer+'%' or " +
                    "harga_perjam like '%'+@search_komputer+'%' " +
                    "commit transaction";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, keyword);
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                result.add(
                        new KomputerModel(
                                new SimpleStringProperty(res.getString("komputer_id")),
                                new SimpleStringProperty(res.getString("kategori_id")),
                                new SimpleDoubleProperty(res.getDouble("harga_perjam")),
                                new SimpleStringProperty(res.getString("status"))
                        )
                );
            }

            listener.onSuccess(result);
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // Insert new komputer
    public static void insertNewKomputer(
            String kategori_id,
            Double harga_perjam,
            ResponseListener listener
    ) {
        try (Connection conn = connection()) {
            String query = "insert into komputer([komputer_id], [kategori_id], [harga_perjam], [status]) " +
                    "values( " +
                    "('PC'+cast((@last_id_komputer + 1) as varchar(18))), " +
                    "?, " +
                    "?, " +
                    "? " +
                    ") " +
                    "commit transaction";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, kategori_id);
            statement.setDouble(2, harga_perjam);
            statement.setString(3, "ready");
            statement.executeUpdate();

            listener.onSuccess(null);
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // Update komputer by id
    public static void updateKomputerById(
            String komputer_id,
            String kategori_id,
            Double harga_perjam,
            ResponseListener listener
    ) {
        try (Connection conn = connection()) {
            String query = "update komputer " +
                    "set kategori_id=?," +
                    "harga_perjam=? " +
                    "where komputer_id=?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, kategori_id);
            statement.setDouble(2, harga_perjam);
            statement.setString(3, komputer_id);
            statement.executeUpdate();

            listener.onSuccess(null);
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // Delete komputer by id
    public static void deleteKomputerById(
            String komputer_id,
            ResponseListener listener
    ) {
        try (Connection conn = connection()) {
            String query = "delete from komputer " +
                    "where komputer_id=?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, komputer_id);
            statement.executeUpdate();

            listener.onSuccess(null);
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }
}