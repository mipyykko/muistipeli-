/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui.javafx;

import javafx.geometry.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author pyykkomi
 */
public class TulosIkkuna extends GridPane {

    private Stage primaryStage;
    private GridPane sisalto;
    
    public TulosIkkuna(Stage primaryStage) {
        super();
        this.primaryStage = primaryStage;

        sisalto = new GridPane();
        setAlignment(Pos.CENTER);
        setPadding(new Insets(15));
        setHgap(16);
        setVgap(8);
        setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null)));

    }
}
