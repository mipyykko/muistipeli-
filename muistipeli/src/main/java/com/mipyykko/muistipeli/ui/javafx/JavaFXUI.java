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
import javafx.stage.Stage;

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
    private PeliIkkuna peliIkkuna;
    private ValikkoIkkuna valikkoIkkuna;
    private StatusHBox status;
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
     * Näytä pelilauta.
     */
    @Override
    public void nayta() /*throws Exception*/ {
        if (firstrun) {
            firstrun = false;
            //try {
            // TODO: koko ikkuna stackpane, johon lisätään ja josta poistetaan tasoja
            IkkunaController ic = new IkkunaController();
            ic.lataaIkkuna(JavaFXIkkuna.VALIKKO);
            ic.lataaIkkuna(JavaFXIkkuna.PELI);
            ic.lataaIkkuna(JavaFXIkkuna.TULOS);
            
            ic.asetaIkkuna(JavaFXIkkuna.VALIKKO);
           
            root = new Group();
            root.getChildren().add(ic);
//            valikkoIkkuna = new ValikkoIkkuna();
            
            if (peli != null) {
                peli.setTila(Pelitila.VALIKKO);
            }

            ic.prefWidthProperty().bind(primaryStage.widthProperty());
            ic.prefHeightProperty().bind(primaryStage.heightProperty());
            scene = new Scene(root/*valikkoIkkuna*/, ikkunaleveys, ikkunakorkeus);
            scene.setFill(Color.YELLOW);

            primaryStage.setTitle("Muistipeliö");
            primaryStage.setScene(scene);
            primaryStage.sizeToScene();
            primaryStage.show();
            //} catch (Exception e) {
            //    throw new Exception("UI:n alustus ei onnistunut: " + e.getMessage());
            //}
        }
    }

    private void sulje() {
        primaryStage.close();
    }

}
