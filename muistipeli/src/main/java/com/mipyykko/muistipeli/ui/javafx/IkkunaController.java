/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui.javafx;

import com.mipyykko.muistipeli.logiikka.Peli;
import com.mipyykko.muistipeli.malli.enums.JavaFXIkkuna;
import com.mipyykko.muistipeli.ui.UI;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.GaussianBlur;
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
    private JavaFXIkkuna curId;
    private UI ui;
    
    public IkkunaController(UI ui) {
        ikkunat = new HashMap<>();
        controllers = new HashMap<>();
        this.ui = ui;
        this.peli = null;
        this.curTransition = null;
        this.curId = null;
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
    
    public UI getUI() {
        return ui;
    }

    public boolean lataaIkkuna(JavaFXIkkuna id) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + id.tiedosto()));
            Parent ikkuna = (Parent) loader.load();
            ControlledRuutu controller = (ControlledRuutu) loader.getController();
            controller.asetaParent(this);
            lisaaIkkuna(id, ikkuna);
            controllers.put(id, controller);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean asetaIkkuna(JavaFXIkkuna id) {
        if (ikkunat.get(id) != null) {
            if (!getChildren().isEmpty()) {
                if (curId == JavaFXIkkuna.TULOS && id == JavaFXIkkuna.PELI) {
                    TranslateTransition curTransition = new TranslateTransition(new Duration(350), ikkunat.get(curId));
                    curTransition.setToX(this.getScene().getWidth());
                    curTransition.setOnFinished(e -> ikkunat.get(id).toFront());
                    GaussianBlur gb = new GaussianBlur();
                    Animation b = new Timeline(
                            new KeyFrame(Duration.ZERO, new KeyValue(gb.radiusProperty(), 10d)),
                            new KeyFrame(Duration.seconds(0.5), new KeyValue(gb.radiusProperty(), 0d)));
                    b.setCycleCount(1);
                    ikkunat.get(id).setEffect(gb);
                    b.play();
                    curTransition.play();
                } else {
                    ikkunat.get(id).setTranslateX(-this.getScene().getWidth());
                    TranslateTransition curTransition = new TranslateTransition(new Duration(350), ikkunat.get(id));
                    curTransition.setToX(0);

                    if (!getChildren().contains(ikkunat.get(id))) {
                        getChildren().add(0, ikkunat.get(id));
                    }

                    ikkunat.get(id).toFront();
                    ikkunat.get(id).setEffect(null);

                    if (id == JavaFXIkkuna.TULOS) {
                        GaussianBlur gb = new GaussianBlur();
                        Animation b = new Timeline(
                                new KeyFrame(Duration.ZERO, new KeyValue(gb.radiusProperty(), 0d)),
                                new KeyFrame(Duration.seconds(0.5), new KeyValue(gb.radiusProperty(), 10d)));
                        b.setCycleCount(1);
                        ikkunat.get(curId).setEffect(gb);
                        b.play();
                        TulosController t = (TulosController) controllers.get(id);
                        t.asetaPeli(peli);
                    }
                    //curTransition.setOnFinished(e -> getChildren().remove(0));
                    curTransition.play();
                    // vaihda kahden ikkunan välillä
                }
            } else {
                getChildren().add(ikkunat.get(id));
            }
            curId = id;
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
