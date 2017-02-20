/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli;

import com.mipyykko.muistipeli.logiikka.Peli;
import com.mipyykko.muistipeli.malli.Kuva;
import com.mipyykko.muistipeli.malli.Tausta;
import com.mipyykko.muistipeli.malli.enums.Korttityyppi;
import com.mipyykko.muistipeli.ui.javafx.JavaFXUI;
import com.mipyykko.muistipeli.ui.javafx.JavaFXInit;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * JavaFX-käyttöliittymän käynnistävä pääohjelma.
 * 
 * @author pyykkomi
 */
public class JavaFXMain extends Application {

    /**
     * Käyttöliittymän ajava pääohjelma.
     * 
     * @param primaryStage JavaFX:n luoma Stage.
     * @throws Exception -
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        JavaFXUI ui = new JavaFXUI(/*peli*/);
        ui.setStage(primaryStage);
        ui.nayta();
    }
    
}
