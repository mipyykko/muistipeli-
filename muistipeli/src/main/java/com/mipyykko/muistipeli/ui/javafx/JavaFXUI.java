/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui.javafx;

import com.mipyykko.muistipeli.logiikka.*;
import com.mipyykko.muistipeli.malli.enums.*;
import com.mipyykko.muistipeli.ui.UI;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * JavaFX-käyttöliittymä.
 *
 * @author pyykkomi
 */
public class JavaFXUI implements UI {

    private Peli peli;
    private int ikkunaleveys, ikkunakorkeus;
    private Stage primaryStage;
    private Group root;
    private StackPane paaIkkuna;
    private Scene scene;
    private boolean firstrun;

    /**
     * JavaFXUI:n konstruktori.
     *
     */
    public JavaFXUI() {
        this.peli = null;
        this.ikkunaleveys = 600; // magic numbers!
        this.ikkunakorkeus = 600;
        this.firstrun = true;

    }

    public void setStage(Stage stage) {
        this.primaryStage = stage;
    }

    @Override
    public void setPeli(Peli peli) {
        this.peli = peli;
    }

    /**
     * Luo peli-ikkunan ja näyttää sen.
     */
    @Override
    public void nayta() /*throws Exception*/ {
        if (firstrun) {
            firstrun = false;
            IkkunaController ic = new IkkunaController(this);
            ic.lataaIkkuna(JavaFXIkkuna.VALIKKO);
            ic.lataaIkkuna(JavaFXIkkuna.PELI);
            ic.lataaIkkuna(JavaFXIkkuna.TULOS);
            
            ic.asetaIkkuna(JavaFXIkkuna.VALIKKO);
           
            // todo: tähän tarkistus jos levyllä tosiaan on toinen css jolloin käytetään sitä
            String css = getClass().getResource("/styles/muistipeli.css").toExternalForm();
            ic.getStylesheets().add(css);
            root = new Group();
            root.getChildren().add(ic);
            
            if (peli != null) {
                peli.setTila(Pelitila.VALIKKO);
            }

            ic.prefWidthProperty().bind(primaryStage.widthProperty());
            ic.prefHeightProperty().bind(primaryStage.heightProperty());
            Font.loadFont(getClass().getResourceAsStream("/fontit/GoodDog.otf"), 24);

            scene = new Scene(root, ikkunaleveys, ikkunakorkeus);

            primaryStage.setTitle("Muistipeliö");
            primaryStage.setScene(scene);
            primaryStage.sizeToScene();
            primaryStage.initStyle(StageStyle.UTILITY);
            primaryStage.show();
            primaryStage.toFront();
        }
    }

    @Override
    public void sulje() {
        primaryStage.close();
    }

}
