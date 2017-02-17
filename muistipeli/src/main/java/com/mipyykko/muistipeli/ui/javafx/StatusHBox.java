/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui.javafx;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Ikkunan ylälaidan loota jossa esim. siirrot ja pisteet.
 * 
 * @author pyykkomi
 */
public class StatusHBox extends HBox {
    
    private Text score;
    
    /**
     * Konstruktori.
     */
    public StatusHBox() {
        super();
        setPadding(new Insets(15, 15, 15, 15));
        setSpacing(10);
        setStyle("-fx-background-color: #800000");
        score = new Text("Siirrot: 0 Parit: 0");
        score.setFont(Font.loadFont(getClass().getClassLoader().getResourceAsStream("fontit/GoodDog.otf"), 24));
        score.setFill(Color.YELLOW);
        getChildren().add(score);
    }
    
    public void setScore(String txt) { // TODO jotain
        score.setText(txt);
    }
}
