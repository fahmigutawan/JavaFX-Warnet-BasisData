package com.example.javafxwarnetbasisdata;

import com.example.javafxwarnetbasisdata.util.CustomScene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MyApplication extends Application {
    @Override
    public void stop() throws Exception {
        super.stop();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MyApplication.class.getResource("onboard-view.fxml"));
        Scene scene = new CustomScene(fxmlLoader.load());
        stage.setTitle("Warnet Sukamaju");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}