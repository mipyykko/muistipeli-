/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.util;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author pyykkomi
 */
public class TestApplication extends Application {

    private Stage primaryStage;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("testi");
        GridPane root = new GridPane();
        Group k = new Group();
        root.getChildren().add(k);
        primaryStage.setScene(new Scene(root, 100, 100));
        primaryStage.show();
    }

    public void sulje() {
        primaryStage.close();
    }
}
