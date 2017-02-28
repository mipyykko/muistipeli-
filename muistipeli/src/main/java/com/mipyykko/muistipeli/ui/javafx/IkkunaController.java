/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui.javafx;

import com.mipyykko.muistipeli.logiikka.Peli;
import com.mipyykko.muistipeli.malli.enums.JavaFXIkkuna;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 *
 * @author pyykkomi
 */
public class IkkunaController extends StackPane {

    private Map<JavaFXIkkuna, Node> ikkunat;
    private Map<JavaFXIkkuna, ControlledRuutu> controllers;
    private Peli peli;
    private TranslateTransition curTransition;
    
    public IkkunaController() {
        ikkunat = new HashMap<>();
        controllers = new HashMap<>();
        this.peli = null;
        this.curTransition = null;
    }

    public void setPeli(Peli peli) {
        this.peli = peli;
    }

    public Peli getPeli() {
        return peli;
    }

    public void lisaaIkkuna(JavaFXIkkuna id, Node ikkuna) {
        ikkunat.put(id, ikkuna);
    }

    public ControlledRuutu getController(JavaFXIkkuna id) {
        return controllers.get(id);
    }
    
    public TranslateTransition getCurTransition() {
        return curTransition;
    }
    
    public void setCurTransition(TranslateTransition t) {
        curTransition = t;
    }
    
    public boolean lataaIkkuna(JavaFXIkkuna id) {
        try {
            System.out.println(id.toString());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + id.tiedosto()));
            System.out.println(getClass().getResource("/fxml/" + id.tiedosto()));
            Parent ikkuna = (Parent) loader.load();
            ControlledRuutu controller = (ControlledRuutu) loader.getController();
            controller.asetaParent(this);
            lisaaIkkuna(id, ikkuna);
            controllers.put(id, controller);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("daa:" + e.getCause() + " " + e.getMessage());
            return false;
        }
    }

    public boolean asetaIkkuna(JavaFXIkkuna id) {
        if (ikkunat.get(id) != null) {
            if (!getChildren().isEmpty()) {
                ikkunat.get(id).setTranslateX(-this.getScene().getWidth());
                TranslateTransition curTransition = new TranslateTransition(new Duration(350), ikkunat.get(id));
                curTransition.setToX(0);
                
                //getChildren().remove(0);

                getChildren().add(/*0, */ikkunat.get(id));
               
                if (id == JavaFXIkkuna.PELI) {
                    PeliController p = (PeliController) controllers.get(id);
                    System.out.println("ic");
                    //p.alustaRuudukko();
                } else if (id == JavaFXIkkuna.TULOS) {
                    TulosController t = (TulosController) controllers.get(id);
                    t.asetaPeli(peli);
                }
                curTransition.setOnFinished(e -> getChildren().remove(0));
                curTransition.play();
                // vaihda kahden ikkunan välillä
            } else {
                getChildren().add(ikkunat.get(id));
            }
            return true;
        } else {
            // virhe
            return false;
        }
    }

    public boolean poistaIkkuna(JavaFXIkkuna id) {
        if (ikkunat.remove(id) == null) {
            // ei ollut 
            return false;
        }
        return true;
    }
}
