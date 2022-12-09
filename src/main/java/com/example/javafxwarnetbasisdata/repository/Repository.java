package com.example.javafxwarnetbasisdata.repository;

import com.example.javafxwarnetbasisdata.listener.*;
import com.example.javafxwarnetbasisdata.model.*;
import com.example.javafxwarnetbasisdata.util.CustomException;
import com.example.javafxwarnetbasisdata.util.DbUrl;
import com.example.javafxwarnetbasisdata.util.TemporaryMemory;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
//import net.sf.jasperreports.engine.*;
//import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Repository {
    private static Connection connection() throws SQLException {
        return DriverManager.getConnection(DbUrl.dbUrl);
    }

    // Jasper create report
//    public static void createReport(HashMap map, InputStream is) {
//        try (Connection conn = connection()) {
////            JasperReport jr = JasperCompileManager.compileReport(is);
////            JasperViewer.viewReport(jp, false);
//            JasperPrint jp = JasperFillManager.fillReport("report/sample.jasper", null, conn);
//            JasperCompileManager.compileReportToFile("report/sample.jrxml", "report/report.jasper");
//            JasperViewer jv = new JasperViewer(jp, false);
//            jv.setVisible(true);
//        } catch (JRException | SQLException e) {
//            e.printStackTrace();
//        }
//    }

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
                    " INSERT INTO customer (customer_id, username, password) " +
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

            while (res.next()) {
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

            listener.onSuccess(result);
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
    ) {
        ArrayList<PedagangModel> result = new ArrayList<>();

        try (Connection conn = connection()) {
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
    ) {
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
            String query = "select pc.*, kat.kategori_word " +
                    "from komputer pc " +
                    "join kategori_komputer kat " +
                    "on kat.kategori_id = pc.kategori_id";
            Statement statement = conn.createStatement();
            ResultSet res = statement.executeQuery(query);

            while (res.next()) {
                result.add(
                        new KomputerModel(
                                new SimpleStringProperty(res.getString("komputer_id")),
                                new SimpleStringProperty(res.getString("kategori_id")),
                                new SimpleDoubleProperty(res.getDouble("harga_perjam")),
                                new SimpleStringProperty(res.getString("status")),
                                new SimpleStringProperty(res.getString("kategori_word"))
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
    ) {
        ArrayList<KomputerModel> result = new ArrayList<>();

        try (Connection conn = connection()) {
            String query = "begin transaction " +
                    "declare @search_komputer varchar(128) " +
                    "set @search_komputer = ? " +
                    "select pc.*, kat.kategori_word from komputer pc " +
                    "join kategori_komputer kat " +
                    "on kat.kategori_id = pc.kategori_id " +
                    "where " +
                    "pc.status like '%'+@search_komputer+'%' or " +
                    "pc.komputer_id like '%'+@search_komputer+'%' or " +
                    "kat.kategori_word like '%'+@search_komputer+'%' or " +
                    "pc.harga_perjam like '%'+@search_komputer+'%' " +
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
                                new SimpleStringProperty(res.getString("status")),
                                new SimpleStringProperty(res.getString("kategori_word"))
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
            String query = "begin transaction " +
                    "declare @last_id_komputer  numeric(18,0) " +
                    "set @last_id_komputer = IDENT_CURRENT('dbo.komputer') " +
                    "insert into komputer([komputer_id], [kategori_id], [harga_perjam], [status]) " +
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

    // Get komputer by id
    public static void getKomputerById(
            String komputer_id,
            KomputerModelListener listener
    ) {
        try (Connection conn = connection()) {
            String query = "select pc.*, kat.kategori_word " +
                    "from komputer pc " +
                    "join kategori_komputer kat " +
                    "on kat.kategori_id = pc.kategori_id " +
                    "where pc.komputer_id=?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, komputer_id);
            ResultSet res = statement.executeQuery();

            if (res.next()) {
                listener.onSuccess(
                        new KomputerModel(
                                new SimpleStringProperty(res.getString("komputer_id")),
                                new SimpleStringProperty(res.getString("kategori_id")),
                                new SimpleDoubleProperty(res.getDouble("harga_perjam")),
                                new SimpleStringProperty(res.getString("status")),
                                new SimpleStringProperty(res.getString("kategori_word"))
                        )
                );
                return;
            }

            listener.onFailed(new CustomException("Komputer tidak ditemukan"));
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // Create komputer transaction
    public static void createKomputerTransaction(
            String komputer_id,
            String customer_id,
            Double harga,
            ResponseListener listener) {
        try (Connection conn = connection()) {
            String query = "begin try " +
                    "begin transaction " +
                    "if((select status from komputer where komputer_id=?) = 'not ready') " +
                    "rollback transaction " +
                    "else  " +
                    "update komputer set status='not ready' where komputer_id=? " +
                    "" +
                    "declare @count int " +
                    "set @count = IDENT_CURRENT('dbo.order_komputer') " +
                    "" +
                    "insert into order_komputer values( " +
                    "('ORDER_PC'+cast((@count + 1) as varchar(18))), " +
                    "?, " +
                    "?, " +
                    "?, " +
                    "'1' " +
                    ") " +
                    "commit transaction " +
                    "end try " +
                    "begin catch " +
                    "rollback transaction " +
                    "end catch ";

            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, komputer_id);
            statement.setString(2, komputer_id);
            statement.setString(3, customer_id);
            statement.setString(4, komputer_id);
            statement.setString(5, harga.toString());
            statement.executeUpdate();

            listener.onSuccess(null);
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // End komputer transaction
    public static void endKomputerTransaction(
            String customer_id,
            String order_id,
            ResponseListener listener
    ) {
        try (Connection conn = connection()) {
            String query = "begin try " +
                    "begin transaction " +
                    "declare @komputer_id varchar(5) " +
                    "if(select customer_id from order_komputer where order_id = '?') != ? " +
                    "rollback transaction " +
                    "set @komputer_id = (" +
                    "select pc.komputer_id from order_komputer ord join komputer pc on pc.komputer_id = ord.komputer_id where ord.order_id = ? " +
                    ")" +
                    "update komputer set status = 'ready' where komputer_id = @komputer_id " +
                    "update order_komputer set status = '3' where order_id = ? " +
                    "commit transaction " +
                    "end try " +
                    "begin catch " +
                    "rollback transaction " +
                    "end catch ";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, customer_id);
            statement.setString(2, order_id);
            statement.setString(3, order_id);

            statement.executeUpdate();

            listener.onSuccess(null);
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // Get komputer transaction by komputer id
    public static void getOrderDetailByKomputerId(
            String komputer_id,
            OrderModelListener listener
    ) {
        try (Connection conn = connection()) {
            String query = "begin transaction " +
                    "select * " +
                    "from order_komputer " +
                    "where komputer_id =? and status = '1' " +
                    "commit transaction";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, komputer_id);
            ResultSet res = statement.executeQuery();

            if (res.next()) {
                listener.onSuccess(
                        new OrderModel(
                                res.getString("order_id"),
                                res.getString("customer_id"),
                                res.getString("komputer_id"),
                                res.getDouble("harga"),
                                res.getString("status")
                        )
                );
                return;
            }

            listener.onFailed(new CustomException("Order tidak ditemukan"));
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // Get list order by customer_id
    public static void getOrderListByCustomerId(
            String customer_id,
            ListOfOrderListener listener
    ) {
        ArrayList<OrderModel> result = new ArrayList<>();

        try (Connection conn = connection()) {
            String query = "select * " +
                    "from order_komputer " +
                    "where customer_id = ? " +
                    "order by id desc";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, customer_id);
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                result.add(
                        new OrderModel(
                                res.getString("order_id"),
                                res.getString("customer_id"),
                                res.getString("komputer_id"),
                                res.getDouble("harga"),
                                res.getString("status")
                        )
                );
            }

            listener.onSuccess(result);
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // Get list makanan
    public static void getListMakanan(
            ListOfFoodListener listener
    ) {
        ArrayList<FoodModel> result = new ArrayList<>();
        try (Connection conn = connection()) {
            String query = "select m.*, kat.kategori_word from makanan m " +
                    "join kategori_makanan kat " +
                    "on kat.kategori_id = m.kategori_id";
            Statement statement = conn.createStatement();
            ResultSet res = statement.executeQuery(query);

            while (res.next()) {
                result.add(
                        new FoodModel(
                                res.getString("makanan_id"),
                                res.getString("pedagang_id"),
                                res.getDouble("harga"),
                                res.getInt("stok"),
                                res.getString("nama"),
                                res.getString("kategori_id"),
                                res.getString("kategori_word")
                        )
                );
            }

            listener.onSuccess(result);
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // Get list makanan by pedagang_id
    public static void getListMakananByPedagangId(
            String pedagang_id,
            ListOfFoodListener listener
    ) {
        ArrayList<FoodModel> result = new ArrayList<>();
        try (Connection conn = connection()) {
            String query = "select m.*, kat.kategori_word from makanan m " +
                    "join kategori_makanan kat " +
                    "on kat.kategori_id = m.kategori_id " +
                    "where m.pedagang_id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, pedagang_id);
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                result.add(
                        new FoodModel(
                                res.getString("makanan_id"),
                                res.getString("pedagang_id"),
                                res.getDouble("harga"),
                                res.getInt("stok"),
                                res.getString("nama"),
                                res.getString("kategori_id"),
                                res.getString("kategori_word")
                        )
                );
            }

            listener.onSuccess(result);
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // Get makanan
    public static void getMakananById(
            String makanan_id,
            FoodModelListener listener
    ) {
        try (Connection conn = connection()) {
            String query = "select m.*, kat.kategori_word from makanan m " +
                    "join kategori_makanan kat " +
                    "on kat.kategori_id = m.kategori_id " +
                    "where m.makanan_id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, makanan_id);
            ResultSet res = statement.executeQuery();

            if (res.next()) {
                listener.onSuccess(
                        new FoodModel(
                                res.getString("makanan_id"),
                                res.getString("pedagang_id"),
                                res.getDouble("harga"),
                                res.getInt("stok"),
                                res.getString("nama"),
                                res.getString("kategori_id"),
                                res.getString("kategori_word")
                        )
                );
                return;
            }

            listener.onFailed(new CustomException("Makanan tidak ditemukan"));
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // Get all order komputer
    public static void getOrderKomputerList(
            ListOfOrderKomputerListener listener
    ) {
        ArrayList<OrderKomputerModel> arr = new ArrayList<>();

        try (Connection conn = connection()) {
            String query = "select * " +
                    "from order_komputer";
            Statement statement = conn.createStatement();
            ResultSet res = statement.executeQuery(query);

            while (res.next()) {
                arr.add(
                        new OrderKomputerModel(
                                new SimpleStringProperty(res.getString("order_id")),
                                new SimpleStringProperty(res.getString("customer_id")),
                                new SimpleStringProperty(res.getString("komputer_id")),
                                new SimpleDoubleProperty(res.getDouble("harga")),
                                new SimpleStringProperty(res.getString("status"))
                        )
                );
            }

            listener.onSuccess(arr);
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // Get all order makanan
    public static void getOrderMakananList(
            ListOfOrderMakananListener listener
    ) {
        ArrayList<OrderMakananModel> arr = new ArrayList<>();

        try (Connection conn = connection()) {
            String query = "select * from order_makanan";
            Statement statement = conn.createStatement();
            ResultSet res = statement.executeQuery(query);

            while (res.next()) {
                arr.add(
                        new OrderMakananModel(
                                res.getString("order_id"),
                                res.getString("customer_id"),
                                res.getString("pedagang_id"),
                                res.getDouble("total_harga"),
                                res.getString("status")
                        )
                );
            }

            listener.onSuccess(arr);
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // Get makanan of order makanan
    public static void getMakananOfOrderMakananList(
            String order_id,
            ListOfMakananOfOrderMakananListener listener
    ) {
        ArrayList<MakananOfOrderMakananModel> arr = new ArrayList<>();

        try (Connection conn = connection()) {
            String query = "select * from makanan_of_order_makanan " +
                    "where order_id=?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, order_id);
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                arr.add(
                        new MakananOfOrderMakananModel(
                                res.getString("order_id"),
                                res.getString("makanan_id")
                        )
                );
            }

            listener.onSuccess(arr);
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // Insert makanan to keranjang
    public static void insertMakananToKeranjang(
            String customer_id,
            String pedagang_id,
            String makanan_id,
            ResponseListener listener
    ) {
        try (Connection conn = connection()) {
            String query = "insert into keranjang_makanan " +
                    "values (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, customer_id);
            statement.setString(2, pedagang_id);
            statement.setString(3, makanan_id);
            statement.executeUpdate();

            listener.onSuccess(null);
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // create order makanan
    public static void createOrderMakanan(
            String customer_id,
            List<FoodModel> arr,
            ResponseListener listener
    ) {
        try (Connection conn = connection()) {
            //get current id
            String currIdQuery = "select IDENT_CURRENT('dbo.order_makanan') as current_identity";
            Statement currIdStatement = conn.createStatement();
            ResultSet res = currIdStatement.executeQuery(currIdQuery);
            int identity = 0;
            if (res.next()) {
                identity = res.getInt("current_identity") + 1;
            }

            //define order_id
            String order_id = String.format("ORD-MKN-%d", identity);

            //create order_makanan
            Double harga = 0.0;
            String pedagang_id = "";
            for (FoodModel item : arr) {
                harga += item.harga();
                pedagang_id = item.pedagang_id();
            }
            String orderQuery = "insert into order_makanan " +
                    "values(?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(orderQuery);
            statement.setString(1, order_id);
            statement.setString(2, customer_id);
            statement.setString(3, pedagang_id);
            statement.setDouble(4, harga);
            statement.setString(5, "1");
            statement.executeUpdate();

            //insert makanan to makanan_of_order_makanan
            for (FoodModel item : arr) {
                String makanan_of_orderQuery = "insert into makanan_of_order_makanan " +
                        "values(?,?)";
                PreparedStatement makanan_of_orderStatement = conn.prepareStatement(makanan_of_orderQuery);
                makanan_of_orderStatement.setString(1, order_id);
                makanan_of_orderStatement.setString(2, item.makanan_id());
                makanan_of_orderStatement.executeUpdate();
            }

            listener.onSuccess(null);
        } catch (SQLException e) {
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // get top 5 makanan by terjual
    public static void getTop5MakananTerjual(
            ListOfSpecialQueryMakananListener listener
    ){
        ArrayList<SpecialQueryMakananModel> arr = new ArrayList<>();

        try(Connection conn = connection()){
            String query = "select top 5 " +
                    "mkn_info.makanan_id, " +
                    "mkn_info.pedagang_id, " +
                    "mkn_info.nama, " +
                    "count(mkn_info.makanan_id) as terjual  " +
                    "from makanan_of_order_makanan mkn " +
                    "join makanan mkn_info on mkn_info.makanan_id = mkn.makanan_id " +
                    "group by mkn_info.makanan_id, mkn_info.pedagang_id, mkn_info.nama";
            Statement statement = conn.createStatement();
            ResultSet res = statement.executeQuery(query);

            while(res.next()){
                arr.add(
                        new SpecialQueryMakananModel(
                                res.getString("makanan_id"),
                                res.getString("pedagang_id"),
                                res.getString("nama"),
                                res.getInt("terjual")
                        )
                );
            }

            listener.onSuccess(arr);
        }catch (SQLException e){
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }

    // get top 5 pedagang by penjualan
    public static void getTop5PedagangByPenjualan(
            ListOfSpecialQueryPedagangListener listener
    ){
        ArrayList<SpecialQueryPedagangModel> arr = new ArrayList<>();

        try(Connection conn = connection()){
            String query = "select top 5 " +
                    "pdg.pedagang_id, " +
                    "pdg.stand_name, " +
                    "count(pdg.pedagang_id) as penjualan " +
                    "from pedagang_information pdg " +
                    "join order_makanan ord on ord.pedagang_id = pdg.pedagang_id " +
                    "join makanan_of_order_makanan mkn on mkn.order_id = ord.order_id " +
                    "group by pdg.pedagang_id, pdg.stand_name";
            Statement statement = conn.createStatement();
            ResultSet res = statement.executeQuery(query);

            while(res.next()){
                arr.add(
                        new SpecialQueryPedagangModel(
                                res.getString("pedagang_id"),
                                res.getString("stand_name"),
                                res.getInt("penjualan")
                        )
                );
            }

            listener.onSuccess(arr);
        }catch (SQLException e){
            listener.onFailed(new CustomException(e.getMessage()));
        }
    }
}