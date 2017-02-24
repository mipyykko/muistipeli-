/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli;

import com.mipyykko.muistipeli.ui.javafx.JavaFXUI;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX-käyttöliittymän käynnistävä pääohjelma.
 *
 * @author pyykkomi
 */
public class JavaFXMain extends Application {

    private static Stage primaryStage;
    
    /**
     * Käyttöliittymän ajava pääohjelma.
     *
     * @param primaryStage JavaFX:n luoma Stage.
     * @throws Exception Exception.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        JavaFXUI ui = new JavaFXUI(/*peli*/);
        this.primaryStage = primaryStage;
        ui.setStage(primaryStage);
        ui.nayta();
    }

    public static Stage getStage() {
        return primaryStage;
    }
}
