/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui.javafx;

import com.mipyykko.muistipeli.JavaFXMain;
import com.mipyykko.muistipeli.logiikka.Peli;
import com.mipyykko.muistipeli.malli.enums.Korttityyppi;
import javafx.event.ActionEvent;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 *
 * @author pyykkomi
 */
public class TulosIkkuna extends GridPane {

    private Stage primaryStage;
    private GridPane sisalto;
    private Peli peli;
    
    public TulosIkkuna(Peli peli) {
        super();
        this.primaryStage = JavaFXMain.getStage();
        this.peli = peli;
        
        sisalto = new GridPane();
        setAlignment(Pos.CENTER);
        setPadding(new Insets(15));
        setHgap(16);
        setVgap(8);
        setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null)));

        Text otsikko = new Text("Jee! Voitit!");
        otsikko.setFont(Font.loadFont(getClass().getClassLoader().getResourceAsStream("fontit/GoodDog.otf"), 64));
        otsikko.setTextAlignment(TextAlignment.CENTER);
        
        Text tulos = new Text("Käytit " + peli.getSiirrotLkm() + " siirtoa.");
        tulos.setTextAlignment(TextAlignment.CENTER);
        
        Button uusipeli = new Button("Uusi peli");
        uusipeli.setOnAction((ActionEvent ae) -> {
        try {
            peli.uusiPeli();
            PeliIkkuna peliIkkuna = new PeliIkkuna(peli);
            primaryStage.setScene(new Scene(peliIkkuna, this.getScene().getWidth(),
                    this.getScene().getHeight()));
        } catch (Exception e) {
            System.out.println("Pelin luominen epäonnistui! " + e.getMessage()); // debug
        }            
        });
        
        sisalto.add(otsikko, 1, 0);
        sisalto.add(new HBox(), 1, 1);
        sisalto.add(tulos, 1, 2);
        sisalto.add(uusipeli, 1, 3);
        
        setHalignment(otsikko, HPos.CENTER);
        setHalignment(tulos, HPos.CENTER);
        setHalignment(uusipeli, HPos.CENTER);
        
        add(sisalto, 1, 1);
    }
}
