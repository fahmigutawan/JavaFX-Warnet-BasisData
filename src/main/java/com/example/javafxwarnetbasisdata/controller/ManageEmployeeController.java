package com.example.javafxwarnetbasisdata.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import com.example.javafxwarnetbasisdata.util.DbUrl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ManageEmployeeController {

    @FXML
    private TextField idField;

    @FXML
    private TextField salaryAccField;

    @FXML
    private TextField salaryField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField no_telpField;

    @FXML
    private TextField jalanField;

    @FXML
    private TextField kotaField;

    @FXML
    private TextField provinsiField;

    @FXML
    private TextField kodePosField;

    @FXML
    private Button insertButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private javafx.scene.control.TableView<pegawai> TableView;

    @FXML
    private TableColumn<pegawai, String> idColumn;

    @FXML
    private TableColumn<pegawai, String> salaryAccColumn;

    @FXML
    private TableColumn<pegawai, Integer> salaryColumn;

    @FXML
    private TableColumn<pegawai, String> nameColumn;

    @FXML
    private TableColumn<pegawai, String> no_telpColumn;

    @FXML
    private TableColumn<pegawai, String> jalanColumn;

    @FXML
    private TableColumn<pegawai, String> kotaColumn;

    @FXML
    private TableColumn<pegawai, String> provinsiColumn;

    @FXML
    private TableColumn<pegawai, String> kodeposColumn;

    @FXML
    private void insertButton() {
        String query = "insert into pegawai values("+idField.getText()+",'"+salaryAccField.getText()+"','"+ salaryField.getText()+"',"+ nameField.getText()+","+ no_telpField.getText()+ jalanField.getText()+ kotaField.getText()+ provinsiField.getText()+ kodePosField.getText()+")";
        executeQuery(query);
        showPegawai();
    }


    @FXML
    private void updateButton() {
        String query = "UPDATE pegawai SET salary_acc_number='"+salaryAccField.getText()+"',gaji='"+ salaryField.getText()+"',nama="+ nameField.getText()+",noTelp="+ no_telpField.getText()+",jalan="+ jalanField.getText()+",kota="+kotaField.getText()+",provinsi="+ provinsiField.getText()+",kode_pos="+ kodePosField.getText()+" WHERE pegawai_id="+idField.getText()+"";
        executeQuery(query);
        showPegawai();
    }

    @FXML
    private void deleteButton() {
        String query = "DELETE FROM pegawai WHERE pegawai_id="+idField.getText()+"";
        executeQuery(query);
        showPegawai();
    }
    //mulai kebawah ini gatau cara konekinnya banh heheh>~<

    public void executeQuery(String query) {
        Connection conn = getConnection();
        Statement st;
        try {
            st = conn.createStatement();
            st.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showPegawai();
    }

    private static Connection connection() throws SQLException {
        return DriverManager.getConnection(DbUrl.dbUrl);
    }

    public ObservableList<pegawai> getPegawaiList(){
        ObservableList<pegawai> pegawaiList = FXCollections.observableArrayList();
        Connection connection = getConnection();
        String query = "SELECT * FROM pegawai ";
        Statement st;
        ResultSet rs;

        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            pegawai pegawaiOL;
            while(rs.next()) {
                pegawaiOL = new pegawai(rs.getString("pegawai_id"),rs.getString("salary_acc_number"),rs.getInt("gaji"),rs.getString("nama"),rs.geString("noTelp"),rs.getString("jalan"),rs.getString("kota"), rs.getString("provinsi"), rs.getString("kode_pos"));
                pegawaiList.add(pegawaiOL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pegawaiList;
    }

    // I had to change ArrayList to ObservableList I didn't find another option to do this but this works :)
    public void showPegawai() {
        ObservableList<pegawai> list = getPegawaiList();

        idColumn.setCellValueFactory(new PropertyValueFactory<pegawai,String>("pegawai_id"));
        salaryAccColumn.setCellValueFactory(new PropertyValueFactory<pegawai,String>("salary_acc_number"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<pegawai,Integer>("gaji"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<pegawai,Integer>("nama"));
        no_telpColumn.setCellValueFactory(new PropertyValueFactory<pegawai,Integer>("noTelp"));
        jalanColumn.setCellValueFactory(new PropertyValueFactory<pegawai,Integer>("jalan"));
        kotaColumn.setCellValueFactory(new PropertyValueFactory<pegawai,Integer>("kota"));
        provinsiColumn.setCellValueFactory(new PropertyValueFactory<pegawai,Integer>("provinsi"));
        kodeposColumn.setCellValueFactory(new PropertyValueFactory<pegawai,Integer>("kode_pos"));


        TableView.setItems(list);
    }

}





