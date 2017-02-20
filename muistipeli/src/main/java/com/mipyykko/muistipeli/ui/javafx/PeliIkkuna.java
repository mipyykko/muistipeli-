/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui.javafx;

import com.mipyykko.muistipeli.logiikka.Peli;
import com.mipyykko.muistipeli.malli.enums.Pelitila;
import java.awt.Point;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Peli-ikkuna joka sisältää ruudukon ja yläreunan statusbarin.
 * 
 * @author pyykkomi
 */
public class PeliIkkuna extends BorderPane {

    private StatusHBox status;
    private Ruudukko ruudukko;
    private Peli peli;
    private Point[] siirto, edellinenSiirto;
    private int animX = 0, animKerrat = 0;
    private Timeline naytaTimeline;

    /**
     * Konstruktori.
     * 
     * @param peli Saa parametrina peli-objektin.
     */
    public PeliIkkuna(Peli peli) {
        super();
        this.peli = peli;
        setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null)));

        status = new StatusHBox();
        setTop(status);

        ruudukko = new Ruudukko(this);
        ruudukko.setOnMouseClicked((MouseEvent event) -> klikattuRuutua(event));
        setCenter(ruudukko);

        Ruudukko.setHalignment(ruudukko, HPos.CENTER);
        
        this.siirto = new Point[2];
        this.edellinenSiirto = new Point[2];
        naytaKaikki();
    }

    public void setPeli(Peli peli) {
        this.peli = peli;
    }
    
    public Ruudukko getRuudukko() {
        return ruudukko;
    }
    
    /**
     * Käynnistää animaation, joka näyttää kortit pystyrivi kerrallaan.
     */
    public void naytaKaikki() {
        if (peli == null || peli.getPelilauta() == null) {
            return;
        }
        peli.setTila(Pelitila.ANIM_KAYNNISSA);
        naytaTimeline = new Timeline();
        naytaTimeline.getKeyFrames().add(new KeyFrame(
            Duration.millis(100), (ActionEvent event) -> kaannaPystyRivi(event)));
        naytaTimeline.setCycleCount(peli.getPelilauta().getLeveys());
        naytaTimeline.setCycleCount(Animation.INDEFINITE);
        naytaTimeline.play();
    }
    
    private void kaannaPystyRivi(ActionEvent event) {
        for (int y = 0; y < peli.getPelilauta().getKorkeus(); y++) {
            ruudukko.kaannaKortti(new Point(animX, y));
        }
        animX++;
        if (animX >= peli.getPelilauta().getLeveys()) {
            if (animKerrat < 1) {
                animKerrat++;
                naytaTimeline.stop();
                /* ensimmäisen kääntökerran jälkeen odotetaan
                jonka jälkeen käännetään uudelleen, sen jälkeen voidaan asettaa
                pelitila
                */
                // TODO: magic numbers
                PauseTransition p = new PauseTransition(Duration.seconds(1));
                p.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        naytaKaikki();
                    }
                });
                p.play();
                animX = 0;
            } else {
                naytaTimeline.stop();
                naytaTimeline = null;
                peli.setTila(Pelitila.ODOTTAA_SIIRTOA);
                //naytaTimeline.stop();
            }
        }
    }
    
    private void odotaEnnenParinKaantoa(int seconds) {
        peli.setTila(Pelitila.ANIM_KAYNNISSA);
        PauseTransition pause = new PauseTransition(Duration.seconds(seconds));
        pause.setOnFinished(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent t) {
                for (Point p : edellinenSiirto) {
                    ruudukko.kaannaKortti(p);
                }
                peli.setTila(Pelitila.ODOTTAA_SIIRTOA);
            }
        });
        pause.play();
    }
    
    private void paivitaScore() {
        status.setScore("Siirrot: " + peli.getSiirrotLkm() + " Parit: " + peli.getParitLkm());
    }

    private void hoidaSiirto(Point p) {
        if (peli.getTila() != Pelitila.ANIM_KAYNNISSA) {
            return;
        }
        ruudukko.kaannaKortti(p);
        if (siirto[0] != null) {
            siirto[1] = p;
            if (!peli.tarkistaPari(siirto)) {
                // sekunnin tauko korttien kääntämisen jälkeen
                odotaEnnenParinKaantoa(1);
            }
            paivitaScore();
            edellinenSiirto = siirto;
            siirto = new Point[2];
        } else {
            siirto[0] = p;
            peli.setTila(Pelitila.ODOTTAA_SIIRTOA);
        }
    }
    
    private void klikattuRuutua(MouseEvent event) {

        if (peli == null || peli.getTila() == Pelitila.ANIM_KAYNNISSA) {
            return;
        }
        Node n = (Node) event.getTarget();
        if (n == null || !(n instanceof GridPane) && !(n instanceof ImageView)) {
            return;
        }
        Integer x = Ruudukko.getColumnIndex(n), y = Ruudukko.getRowIndex(n);
        if (x == null || y == null) {
            return;
        }
        Point p = new Point(x, y);

        if (peli.getTila() != Pelitila.ODOTTAA_SIIRTOA
                || peli.getPelilauta().getKortti(p).getKaannetty()) {
            return;
        }

        peli.setTila(Pelitila.ANIM_KAYNNISSA);
        hoidaSiirto(p);
        if (peli.getTila() == Pelitila.PELI_LOPPU) {
            System.out.println("lölz"); //TODO
        }
    }

}
