/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui.javafx;

import com.mipyykko.muistipeli.logiikka.Peli;
import com.mipyykko.muistipeli.malli.enums.Pelitila;
import java.awt.Point;
import javafx.animation.*;
import javafx.event.*;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
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
    private Timeline korttiAnimTimeline;

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

        ruudukko = new Ruudukko(this, peli);
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

    private FadeTransition ft(Point p, int a, int b) {
        FadeTransition ft = new FadeTransition(Duration.millis(100), ruudukko.getIv(p));
        ft.setFromValue(a);
        ft.setToValue(b);
        return ft;
    }
    
    private void animoiPari(Point[] siirto) {
        FadeTransition[] ft = null;
        for (int i = 0; i < siirto.length; i++) {
            final Point s = siirto[i];
            ft[i] = ft(siirto[i], 0, 1);
            final FadeTransition ft2 = ft(s, 1, 0);
            ft[i].setOnFinished((ActionEvent ae) -> ft2.play());
            ft[i].play();
        }
    }
    
    

    private void paivitaScore() {
        status.setScore("Siirrot: " + peli.getSiirrotLkm() + " Parit: " + peli.getParitLkm());
    }

    private void hoidaSiirto(Point p) {
        if (peli.getTila() != Pelitila.ANIM_KAYNNISSA) {
            return;
        }
        if (siirto[0] != null && siirto[0] != p) {
            ruudukko.kaannaKortti(p);
            siirto[1] = p;
            if (!peli.tarkistaPari(siirto)) {
                // sekunnin tauko korttien kääntämisen jälkeen
                peli.setTila(Pelitila.ANIM_KAYNNISSA);
                odotaEnnenParinKaantoa(1);
            } else {
                //animoiPari(siirto);
            }
            paivitaScore();
            edellinenSiirto = siirto;
            siirto = new Point[2];
        } else {
            ruudukko.kaannaKortti(p);
            siirto[0] = p;
            peli.setTila(Pelitila.ODOTTAA_SIIRTOA);
        }
    }

    private void klikattuRuutua(MouseEvent event) {

        if (peli == null || peli.getTila() != Pelitila.ODOTTAA_SIIRTOA) {
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

//        if (peli.getTila() != Pelitila.ODOTTAA_SIIRTOA) {
//            return;
//        }
        if (peli.getPelilauta().getKortti(p).getKaannetty()) {
            return;
        }

        peli.setTila(Pelitila.ANIM_KAYNNISSA);
        hoidaSiirto(p);
        if (peli.getTila() == Pelitila.PELI_LOPPU) {
            System.out.println("lölz"); //TODO
        }
    }

}
